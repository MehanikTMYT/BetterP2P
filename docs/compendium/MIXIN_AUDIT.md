# Адресный Mixin Audit

Аудит исходников, без runtime-конфликтного прогона. Пути от /root/home/forks на NL. Изменения mixins в этом этапе не выполнялись: горячие участки ещё не измерены.

| Участок и доказательство | Решение | Почему / проверка |
|---|---|---|
| AE-Compendium/src/main/java/ru/mehanik/aecompendium/client/MaterialListOrderButton.java:24, onInit(ScreenEvent.Init.Post) | NeoForge Events | Кнопка уже добавляется через корректный клиентский hook; Mixin не нужен. Считать widgets после повторного init/resize, не регистрировать subscriber повторно |
| AE-Compendium/src/main/java/ru/mehanik/aecompendium/AeCompendium.java:23 | Java API + Events | Payload registrar("1"), сохранить protocol/ID. Общий helper допустим без нового wire format |
| ae-stack/compendium-sources/PolymorphicEnergistics/src/main/java/gripe/_90/polyeng/mixin/CraftingTermMenuMixin.java:35 | существующий Mixin + Java service | Внутренний CraftingTermMenu, рецепт/результат; сверять descriptor и выбранный recipe на точном AE2 |
| тот же пакет /PatternEncodingTermMenuMixin.java:40,74 | существующий Mixin + Java service | Pattern encoding, setItem HEAD: проверить reentrancy/duplicate recipe selection и шаблон после reload |
| тот же пакет /MEStorageScreenMixin.java:12 | существующий малый Mixin | toggleTerminalStyle RETURN, клиентская ветка; проверить взаимодействие AE2WTLib и терминалов других addons |
| тот же пакет /AEBaseMenuAccessor.java:8 и AbstractRecipesWidgetAccessor.java:7 | accessor Mixin | Доступ к внутренностям; не заменять reflection-обходом |
| EMI-Compendium/.../EmiCompendiumPlugin.java, module/CompendiumModules.java | Java EMI API | Явный initialize/register четырёх модулей; сохранить однократный вызов на EMI reload, не вводить mixin ради поиска рецептов |
| EMI-Compendium/.../recipe/VillagerProfessionRecipe.java:25,100 | Java service + client lifecycle event | static cachedVillager держит level. При другом level объект заменяется, но после disconnect без нового render может удерживаться; подтвердить heap path, добавить explicit cleanup при отдельной реализации |
| colonial-addons/ColonyLink/.../ColonyLinkServerTicker.java:368,975 | Java service | Использует AE2 cached inventory и per-builder caches; сначала JFR стоимость/resolution counts, затем локальная invalidation, не вмешиваться в AE2 storage mixin |
| colonial-addons/ColonyLink/.../BuilderToolHelper.java:566 | Java service | Кэш кандидатов: проверить registry/reload scope и bound; общий diagnostics adapter без объединения data cache |
| colonial-addons/ColonyLink/.../WarehouseLinkTerminalScreen.java:716 | Java API | Уже сравнивает item/components и обновляет materials cache. Проверить reload и выбор другого target; не создавать второй слой mixin |
| colonial-addons/warehouseWorkshop/.../WindowWorkshopModule.java:1832,2154 | Java API/service | ClientRecipeCache используется напрямую; измерить повторные recipe lookups, key должен учитывать reload и components |
| colonial-addons/ColonyPathingEdition/src/main/java/com/arxyt/colonypathingedition/mixins/minecolonies/lumberjack/EntityAIWorkLumberjackMixin.java:147 | существующий @Overwrite требует отдельного обоснования | Высокий conflict риск; не отключать, сравнить оригинальное поведение и заменить малой инъекцией только после regression scenarios |
| тот же корень /plantation/BlockPlantationFieldMixin.java:46; /herder/EntityAIWorkCowboyMixin.java:53,114 | существующие @Overwrite | Не включать GPL в пилот. Target collision map и сравнение с точным MineColonies обязательны |
| Map markers/sync/cache | BLOCKED: исходники Map Compendium не найдены | Не выдумывать target methods. JourneyMap API — первая опция после обнаружения реального компонента |
| Sodium/Lithium/ServerCore/Create/EMI общие hot paths | PLANNED: exact installed JAR audit | Нельзя обещать отсутствие конфликтов без списка версий, configs и runtime |

Для строк с «...» полный Java package ColonyLink: src/main/java/com/colonylink/colonylink; warehouseWorkshop: src/main/java/com/deathfrog/warehouseworkshop/core/client/gui/modules; EMI: src/main/java/ru/mehanik/emicompendium.

polyeng.mixins.json сейчас required=true, defaultRequire=1, compatibilityLevel=JAVA_21; это не доказательство Java21 runtime-требования и не основание менять значение без проверки Mixin 26.1.2. AbstractRecipesWidgetAccessor находится в common mixins списке при клиентском типе-мишени: отдельный dedicated-server classloading тест обязателен.

Для следующего шага собрать matrix target class/method/descriptor → все configs в exact install set, приоритеты, cancellable/redirect/overwrite и expected injection count. Предпочесть WrapOperation/ModifyExpressionValue только после проверки доступной версии MixinExtras; маленькая инъекция не гарантирует совместимости автоматически. Smoke должен включать терминал, шаблоны, автокрафт, EMI reload/навигацию, Domum materials, colony request loops. Не объявлять обычную повторную обработку «дублирующим обработчиком» без доказательства двойной регистрации.
