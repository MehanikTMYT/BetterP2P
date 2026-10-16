#!/usr/bin/env python3
"""Reject Kotlin runtime links, duplicate entries and accidental format/resource changes."""
import collections
import hashlib
import json
import sys
import tomllib
import zipfile
from pathlib import Path

jar = Path(sys.argv[1])
with zipfile.ZipFile(jar) as archive:
    names = archive.namelist()
    duplicates = [name for name, count in collections.Counter(names).items() if count > 1]
    assert not duplicates, f"Duplicate ZIP entries: {duplicates}"
    assert not any(name.endswith((".kt", ".kotlin_module", ".jar")) for name in names), "Kotlin source/module or nested JAR"
    classes = [name for name in names if name.endswith(".class")]
    for name in classes:
        data = archive.read(name)
        assert int.from_bytes(data[6:8], "big") == 69, f"Not Java 25: {name}"
        assert b"kotlin/" not in data and b"kotlinforforge" not in data, f"Kotlin reference: {name}"
    metadata = tomllib.loads(archive.read("META-INF/neoforge.mods.toml").decode())
    assert metadata["modLoader"] == "javafml"
    assert [mod["modId"] for mod in metadata["mods"]] == ["betterp2p"]
    dependencies = metadata["dependencies"]["betterp2p"]
    assert {item["modId"] for item in dependencies} == {"minecraft", "neoforge", "ae2"}
    assert next(item for item in dependencies if item["modId"] == "minecraft")["versionRange"] == "[26.1.2]"
    assert all(item["type"] == "required" for item in dependencies)
    resources = {name: archive.read(name) for name in names if name.startswith(("assets/", "data/")) and not name.endswith("/")}
    if len(sys.argv) > 2:
        with zipfile.ZipFile(sys.argv[2]) as baseline:
            old = {name: baseline.read(name) for name in baseline.namelist() if name.startswith(("assets/", "data/")) and not name.endswith("/")}
        assert resources == old, "Game resources differ from baseline"
    print(json.dumps({"jar": str(jar), "bytes": jar.stat().st_size, "sha256": hashlib.sha256(jar.read_bytes()).hexdigest(), "java25_classes": len(classes), "resources": len(resources), "kotlin_references": 0, "required_mods": [x["modId"] for x in dependencies]}, indent=2))
