# Инвентаризация Compendium — 2026-10-13

Аудит выполнен по SSH на NL; Minecraft-файлы на FI не создавались. Это снимок исходников и существующих артефактов, а не подтверждение успешной новой сборки или runtime. Пути ниже относительно /root/home/forks на NL, если явно не указан другой корень. Graphify EMI содержит 64 узла, но старые номера строк и отсутствующие новые методы; факты проверены непосредственно по исходникам. Граф не перестраивался.

## Состояние репозиториев

| Проект | Ветка / HEAD | Незавершённые изменения |
|---|---|---|
| AE-Compendium | main / unborn | 9 untracked записей, включая build; своего commit SHA нет |
| EMI-Compendium | feature/beacons-conduits / 956b46d | 15 записей status |
| ColonialSuite | master / unborn | только .git, кода нет |
| ColonyLink | port/26.1.2 / e965eeb | 98 записей |
| ColonyPathingEdition | port/26.1.2 / ff3e0096 | 104 записи |
| warehouseWorkshop | main / 64a436b | 49 записей |
| greenhousegardener | main / 8442c43 | 232 записи |

Количества — снимок на момент чтения, включая изменения других работ. Чужие изменения не изменялись.

## Модули

«JAR есть» означает обнаруженный build/libs, без вывода о соответствии текущим dirty-исходникам.

| Модуль | Язык | Лицензия / источник | Текущий JAR | Целевой Compendium | Способ интеграции | Риск |
|---|---|---|---|---|---|---|
| AE-Compendium glue | Java, 4 файла | агрегат декларирует MIT/LGPL; отдельного LICENSE нет | ae_compendium-0.1.0+26.1.2.jar | AE | сохранить multi-mod assembly | высокий: происхождение/лицензии, silent duplicates |
| ConstructionSticks | Java | MIT по AE build.gradle | внутри AE JAR | AE | исходники внешнего форка | сохранить constructionstick |
| Applied Construction Sticks | Java | MIT по AE build.gradle | внутри AE JAR | AE | исходники, AE2 API | сохранить appliedsticks |
| BuildingGadgets2-26 | Java | MIT по AE build.gradle | внутри AE JAR | AE | исходники, свой AT | несовпадение AT/resources |
| PolymorphicEnergistics | Java | LGPL-3.0 по metadata | внутри AE JAR | AE | исходники + mixins | внутренние AE2 методы |
| polymorph_plus | Java, multiloader | LGPL-3.0-or-later по metadata | распакованный входной JAR | AE | пока внешняя сборка | metadata/mixins/services |
| AE2WTLib | Java | MIT по AE build.gradle | распакованный JAR; API остаётся nested | AE | внешняя сборка | jarJar metadata/API |
| EMI-Compendium | Java, 13 файлов | MIT LICENSE + notices | emi-compendium-0.1.0-26.1.2-neoforge.jar | EMI | @Mod + @EmiEntrypoint | клиентская загрузка, entity cache |
| BetterP2P | Kotlin → Java в отдельной работе | GPL-3.0 | см. отчёт Java-порта | самостоятельный | AE2 API | не включать автоматически |
| ColonialSuite | нет кода | не установлена | нет | Colonial | пока только план | нельзя считать готовым модулем |
| ColonyLink | Java, 86 файлов | GPL-3.0 LICENSE | colonylink-1.6.9-26.1.2.jar | самостоятельный | API MineColonies/AE2/DO | GPL, сеть, server ticker |
| ColonyPathingEdition | Java, 219 файлов | GPL-3.0 LICENSE | colonypathingedition-26.1.2-1.0.5-ALPHA-13.1.jar | самостоятельный | mixins MineColonies | многочисленные Overwrite |
| MineColonies-Jade-crops | Java, 11 | MIT LICENSE | нет build/libs JAR | Colonial-кандидат | Jade API | порт/сборка не подтверждены |
| jadecolonies | Java, 13 | GPL-3.0 LICENSE | нет | самостоятельный | Jade API | GPL, dirty port |
| mctradepost-retro | Java, 400 | GPL-3.0 LICENSE | mctradepost-1.06.014-1.21.1.jar | самостоятельный | API + mixins | имя старой версии; 722 dirty записи |
| colony4cc | Java, 23 | GPL-3.0 LICENSE | нет | самостоятельный | CC API + mixins | trunk, версия не подтверждена |
| warehouseWorkshop | Java, 42 | GPLv3 gradle.properties; TEMPLATE_LICENSE лишь шаблон | warehouseworkshop-01.01.002.jar | самостоятельный | MineColonies API, сеть | полный текст лицензии/порт |
| greenhousegardener | Java, 107 | GPLv3 gradle.properties; TEMPLATE_LICENSE лишь шаблон | greenhousegardener-01.02.004.jar | самостоятельный | MineColonies API | полный текст лицензии/порт |
| minecolonies-tfc | Java, 222 | GPL-3.0 LICENSE | нет | самостоятельный | compat/replacements mixins | несколько replacement-наборов |
| BuildServerActions | Java, 53 | GPL-3.0 LICENSE | нет | самостоятельный | Java API | version/1.21, порт не доказан |
| MineColonies_Compatibility | Java, 636 | GPL-3.0 LICENSE | нет | самостоятельный | Java API | ветка 1.19.2, вне целевой версии |
| MineColonies-Universal-Resources | Java, 11 | MPL-2.0 LICENSE | нет | Colonial-кандидат после аудита | mixins | обязательства по файлам, порт |
| VolcanicMason | Java, 15 | LGPL-3.0 LICENSE | нет | Colonial-кандидат после аудита | Java API | порт/лицензионная доставка |

Лицензии AE-доноров, отмеченные «по metadata/build», ещё требуют сверки полных upstream LICENSE и состава конкретных JAR. Таблица не является разрешением на распространение объединённого произведения. GPL-компоненты исключены из пилота по требованию пользователя, а не объявлены принципиально несовместимыми с любым объединением.

## Фактическая архитектура

AE build.gradle подключает четыре внешних дерева sourceSets: ConstructionSticks, Applied-Construction-Sticks, PolymorphicEnergistics, BuildingGadgets2-26; zipTree добавляет polymorph_plus и AE2WTLib. metadata сохраняет семь modId: constructionstick, appliedsticks, polymorph_plus, polyeng, buildinggadgets2, ae_compendium, ae2wtlib. AE2WTLib API остаётся nested. duplicatesStrategy=EXCLUDE способен скрывать коллизии. Эту существующую multi-mod схему нельзя заменять одним modId.

AE: Java toolchain 25, ModDev 2.0.141, MC 26.1.2, NeoForge 26.1.2.114, AE2 26.1.11-SNAPSHOT, EMI 1.1.24-semi151a6c88+26.1.2. AE2 runtime обязателен; EMI localRuntime, его API compileOnly. Optional compileOnly Curios/JEI/Sophisticated требуют проверки фактических classloading guards. AeCompendium.java регистрирует payload через registrar("1"); MaterialListOrderButton — client-only event subscriber. Конфиги доноров должны сохранить свои имена.

EMI: Java toolchain/release 25, ModDev 2.0.143, те же MC/NeoForge/EMI. EmiCompendium — @Mod; EmiCompendiumPlugin — @EmiEntrypoint. CompendiumModules явно создаёт четыре модуля (beacon/conduit, professions, effects, breeding). Собственных mixin JSON и сетевого регистратора в src нет. README также перечисляет будущие функции; это не доказательство их реализации. VillagerProfessionRecipe.cachedVillager хранит entity и level; lifecycle-очистку нужно отдельно проверить.

Пользователь предпочитает .109, исходники используют .114. Смена версии без compile/runtime-матрицы не выполнена. Диапазоны ряда metadata разрешают 26.2, хотя целевой release должен быть строго 26.1.2; ужесточить только отдельным проверенным изменением.

## Map и оптимизаторы

Поиск имён каталогов в /root/home и /opt до глубины 5 нашёл AE/EMI Compendium, JourneyMap-API, reference/upstream-watch journeymap, journeymap-teams, lithium, но не Map Compendium или Optimization Compendium. Это ограниченный поиск, не доказательство отсутствия на всех дисках/хостах. /root/home/mod-upstream-watch/repos — reference checkout, не подтверждённая игровая установка. MODPACK_MASTER_LIST_2026-10-03.md:202 перечисляет Sodium/Lithium/FerriteCore/ModernFix/ImmediatelyFast/Entity Culling/More Culling/I'm Fast/Iris/Distant Horizons/spark; состав установленных JAR и исходники конкретных версий этим не доказаны. ServerCore в проверенном inventory не установлен. Их объединение или удаление зависимостей не выполняется.

## Baseline и пределы доказательств

В этом аудите Gradle, клиент и dedicated server не запускались. Наличие build/libs и прежних логов не выдаётся за baseline текущего дерева. Нет сопоставимых JFR/heap/spark результатов, времени старта, loaded-class counts, обработчиков, heap, MSPT/TPS/FPS. Измеримого ускорения пока нет.
