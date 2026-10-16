# Проверка игровых артефактов — 2026-10-13

Дополнение к первичному INVENTORY: исходные деревья не изменялись. ZIP/TOML/class-header анализ выполнен read-only. Snapshot EMI создан в /tmp/emi-compendium-audit-20261013 из текущего dirty дерева (без .git/.gradle/build/run/graphify-out).

| Артефакт | Размер | SHA256 | Классы |
|---|---:|---|---:|
| AE-Compendium/build/libs/ae_compendium-0.1.0+26.1.2.jar | 4695331 | f8d87a0022ffe1da731466c2784ede2240f13d18217a974e045ca0ce1cc37a73 | 476 |
| EMI-Compendium/build/libs/emi-compendium-0.1.0-26.1.2-neoforge.jar | 43065 | 61e237784bf48777e3cd2a291975e93d22fda8796b0fc451ff6ad3b4b3dfb5f8 | 18 |

Все внешние .class этих JAR имеют major=69 (Java25). Дубли ZIP-путей отсутствуют; это не доказывает отсутствие потерь при исходном EXCLUDE.

## AE

Существуют все 5 объявленных mixin configs и оба AT (buildinggadgets2_at.cfg, ae2wtlib_at.cfg). Сохраняются 7 modId. Три service descriptors Polymorph (IClientPlatform, IIntegrationPlatform, IPlatform) присутствуют. JarJar metadata указывает на существующий META-INF/jarjar/de.mari_023.ae2wtlib_api-26.1.1-beta.fleet.jar. Тематические модули распакованы, API пока вложен.

Входные resource paths проверены по source roots и двум JAR-модулям. Найдена реальная коллизия icon.png: PolymorphicEnergistics и AE2WTLib имеют разные байты. EXCLUDE способен молча выбрать один. Коллизии neoforge.mods.toml, pack.mcmeta, MANIFEST.MF и accesstransformer.cfg ожидаемы, но требуют явных правил; текущая сборка заменяет metadata, исключает pack/manifest, переименовывает AT.

По именам license/notice внутри AE найден только корневой LICENSE, содержащий LGPL3. Это недостаточное доказательство полноты уведомлений MIT-доноров; перед распространением собрать полные notices по каждому модулю. Это технический packaging gap, не окончательный юридический вывод.

## EMI

modId emi_compendium, no mixins/services/nested jars. en_us/ru_ru присутствуют. Metadata требует NeoForge [26.1.2.114,) на CLIENT: предпочитаемая пользователем .109 не удовлетворяет этому объявлению. Minecraft строго [26.1.2]; EMI [1.1.24,). Значение side=CLIENT у dependencies не равнозначно клиентскому ограничению @Mod.

LICENSE/NOTICE entries внутри JAR отсутствуют, хотя LICENSE и README third-party notices находятся в репозитории. Пилот должен включать notices в артефакт.

## Build baseline

Среда: OpenJDK 25.0.4.1+1-1-24.04.4-Ubuntu; wrapper Gradle 9.2.1. Offline snapshot build завершился FAILED до компиляции: net.neoforged.moddev 2.0.143 не найден в доступном plugin cache. Лог: /tmp/emi-compendium-audit-20261013-build.log. Это dependency-resolution failure, не compile failure.

Онлайн попытка и её итог фиксируются ниже. AE snapshot build не выполнен: текущая assembly зависит от нескольких dirty sibling trees и готовых JAR; для честного baseline нужен manifest всех их снимков, а не сборка исходников с незафиксированными изменениями.

Dedicated server/client smoke, миры, синхронизация и JFR ещё не выполнялись. Статическая готовность JAR не означает runtime readiness.

## Дополнительный поиск

Только путевые имена в /srv и /root/home/mod-upstream-watch до глубины 5: *compendium* не обнаружены. Никакие конфиги или секреты сервисов не читались. Это не подтверждает отсутствие Map Compendium на других mount points.
