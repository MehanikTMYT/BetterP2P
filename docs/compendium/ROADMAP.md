# Roadmap

Снимок 2026-10-13. Статус отражает выполненную работу, а не желаемую готовность.

| Этап | Статус | Результат / gate |
|---|---|---|
| SSH NL и обнаружение AE/EMI/ColonialSuite | DONE | фактические директории, dirty состояния и HEAD проверены |
| Инвентаризация colonial-addons | DONE | язык/лицензии/JAR/ветки; runtime не подтверждён |
| Map Compendium, exact QoL installation | BLOCKED | в проверенных корнях не найден; нужен реальный путь/манифест установки |
| Baseline текущих AE/EMI сборок и runtime | PLANNED | наличие старых JAR недостаточно; не запускалось этой задачей аудита |
| BetterP2P Java25 conversion | IN_PROGRESS | отдельная ветка/worktree, статус уточнять по отчёту основного исполнителя |
| Independent/Unified architecture | DONE | ARCHITECTURE.md, реализация distribution ещё не выполнена |
| Source-level Mixin Audit | DONE | MIXIN_AUDIT.md: конкретные targets и допустимые механизмы |
| Полный runtime collision audit | PLANNED | exact versions/install set + client/server smoke |
| MIT-only пилот | PLANNED | после BetterP2P gate, license completeness, независимых baseline |
| Общие cache/diagnostics helpers | PLANNED | только доказанное повторение и lifecycle contracts |
| Unified distribution | PLANNED | после успешного пилота, collision/license checks |
| GPL inclusion | BLOCKED | пользователь требует отдельного успешного лицензионного аудита; Java-порт не блокируется |
| Performance A/B/C | PLANNED | идентичные миры/условия, сырые метрики; улучшения пока не заявлены |

Документационный этап изменяет только docs/compendium/*.md. Зависимости не удалены, исходники/Gradle не изменены, commit/push не выполнялись. Текущие HEAD исходных репозиториев см. INVENTORY.md; единого SHA для грязного multi-repo снимка нет. Следующее действие — завершить и проверить BetterP2P, затем собрать reproducible baseline и малый лицензионно проверенный пилот.
