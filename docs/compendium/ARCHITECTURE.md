# Independent / Unified — технический план

Статус: проектное решение, не реализованная общая сборка. Minecraft строго 26.1.2, Java 25. В первую очередь проверить BetterP2P; массовое объединение запрещено до первого рабочего пилота.

## Границы

Существующие репозитории остаются источниками истины. Независимые Gradle outputs тематических модулей подключаются distribution через dependency/variant contracts, а не чтение произвольного dirty sibling-directory. Фиксировать commit, patch digest, dependency checksums, Gradle wrapper, JDK vendor/build и NeoForge. AE2/Create/EMI/MineColonies/Mekanism не входят в distribution. BetterP2P и остальные GPL-кандидаты самостоятельны до отдельного успешного лицензионного аудита.

core-api содержит маленькие интерфейсы, а реализация core-runtime добавляется только потребителям, которым она действительно нужна. Остальные модули не получают новую обязательную зависимость. Не перемещать существующие registry IDs, payload IDs, config filenames, data components или NBT keys.

## Independent

Каждый разрешённый модуль выдаёт собственный мод-JAR, tests и непакетированный variant классов/ресурсов с descriptor manifest. Его @Mod и клиентские entrypoints остаются владельцами регистрации. Сборка independent не должна требовать distribution. Использовать build conventions для Java25/reproducibility/dependency verification, а не общую игровую библиотеку только ради Gradle-настроек.

## Unified

distribution объединяет классы и ресурсы разрешённых variants в один плоский JAR, а не архив из тематических JAR. Генератор объединяет [[mods]], dependencies, mixins и accessTransformers; каждый оригинальный modId и @Mod остаётся ровно один раз. Existing AE multi-mod сохраняется. Optional integrations грузятся через отдельные классы после проверки mod presence и стороны.

До упаковки строить manifest «путь → модуль → SHA256». Дубли классов — ошибка, даже одинаковых, пока не назначен один владелец. Дубли ресурсов — ошибка кроме явной политики: service descriptors объединять с дедупликацией строк; JSON translations/tags объединять структурно только при согласованной семантике; конфликты значений — ошибка. LICENSE/NOTICE хранить под META-INF/licenses/<module>/, сохранять required notices. Переименовывать AT paths с обновлением metadata. Mixin configs сохранять отдельными уникальными именами; объединять список metadata, не сливать механически содержимое разных конфигов. Рефмапы/плагины/mixin side разделение сохранять.

Не переносить duplicatesStrategy=EXCLUDE из текущего AE build в новый distribution. jarJar API/внешние библиотеки рассматривать отдельно: пользователь запрещает nested thematic jars, а библиотечную упаковку определяет подтверждённый контракт; итоговый граф не должен дублировать API-классы.

Устанавливать либо independent JARs, либо Unified. Генерировать exact install/remove manifest по хешам; перед запуском обнаруживать дубли modId и давать ясную диагностику.

## Пилот

После BetterP2P gate выбрать EMI-Compendium и маленький MIT-модуль, например ConstructionSticks, только после проверки полных LICENSE/NOTICES и актуальной отдельной сборки. Не извлекать модуль из AE production-установки без теста appliedsticks dependencies. Пилот — отдельный тестовый профиль: 2 modId, оба @Mod обнаружены, registry dump до/после одинаков, переводы доступны, dedicated server/client загружаются, EMI recipe registration однократна. Альтернатива — сначала воспроизводимая перепаковка уже MIT-only тестовых variants, но пустой фиктивный мод не доказывает совместимость реальных функций.

## Общие сервисы

- Event registration: manifest владельцев и idempotence guard на конкретный lifecycle, диагностический счётчик. Не регистрировать повторно auto-subscribers из центрального bootstrap.
- Cache: CacheDiagnostics и LifecycleScope (server/world/client-connection/resource-reload). Каждый владелец сохраняет свою структуру данных; size/hits/misses/evictions, bounded policy и explicit invalidate. World/entity ссылки не помещать в process-global core cache.
- Diagnostics: дешёвые counters, sampled timers, opt-in JFR events; отдельные labels module/operation, без payload contents и пользовательских данных. Heap dump только по диагностической операции.
- Networking: общие validation/thread-dispatch helpers, но прежние payload IDs/codecs/protocol version сохраняются. Клиентский запрос не является серверной авторизацией; проверять distance/ownership/permissions/limits и выполнять на правильном потоке.
- Integration: отдельные adapters; наличие optional mod не должно проверяться в горячем цикле, если достаточно неизменяемого startup capability snapshot. Reload-sensitive состояния нельзя кэшировать навсегда.
- Config: не объединять существующие config files автоматически; core получает только собственные diagnostics settings.

Выносить сервис только при двух доказанных одинаковых потребностях и эквивалентных lifecycle; похожие имена Cache сами по себе недостаточны.

## Критерии проверки

Чистые compile/tests; artifact duplicate/class/metadata/license audit; server без client classes; клиент с optional dependency present/absent; registry/data-component snapshot; загрузка копии старого мира, сохранение и повторная загрузка; client/server protocol roundtrip; один обработчик на каждый ожидаемый lifecycle; отсутствие MixinApplyError, linkage/registration errors. NeoForge .109 и .114 — отдельные результаты, не предположение совместимости.

Performance сравнить A independent → B thematic → C unified на одинаковом мире/config/seed/JDK/heap, с фиксированным warmup и несколькими прогонами: compressed/install bytes, startup, loaded classes, listeners, heap/allocation, MSPT/TPS, FPS, cache sizes после disconnect/unload. Сохранять сырые JFR/spark/GC logs и median/spread. Количество JAR не объясняет улучшение TPS само по себе.
