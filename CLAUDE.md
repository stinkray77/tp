# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./gradlew run                          # Run the application
./gradlew build                        # Build (includes tests and checkstyle)
./gradlew test                         # Run all tests
./gradlew test --tests "seedu.address.model.person.PersonTest"  # Run a single test class
./gradlew test --tests "seedu.address.model.person.PersonTest.testMethodName"  # Run a single test method
./gradlew checkstyleMain               # Lint main sources
./gradlew checkstyleTest               # Lint test sources
./gradlew clean                        # Clean build artifacts
```

## Code Style

Enforced via Checkstyle (`config/checkstyle/checkstyle.xml`):
- Max line length: **120 characters**
- No tabs (spaces only)
- All public methods require Javadoc
- No wildcard imports; import order: STATIC → `java.*` → `javax.*` → `org.*` → `com.*` → project packages
- No consecutive capitals longer than 1 letter in names

## Architecture

This is **Tutor Central**, a student/tuition management app built on AddressBook Level 3 (AB3). It uses Java 17 + JavaFX 17, with JSON persistence via Jackson.

**Four independent layers**, each defined by an interface:

| Layer | Interface | Manager/Impl |
|-------|-----------|--------------|
| UI | `Ui` | `UiManager` → JavaFX components in `ui/` |
| Logic | `Logic` | `LogicManager` → commands + parsers in `logic/` |
| Model | `Model` | `ModelManager` → `AddressBook`, `Person` in `model/` |
| Storage | `Storage` | `StorageManager` → JSON adapters in `storage/` |

**Startup flow** (`MainApp.init()`): Config → Logging → Storage → Model → Logic → UI.

**Command execution flow**: `CommandBox` → `LogicManager.execute()` → `AddressBookParser` dispatches to command-specific parser → `Command.execute(model)` → `CommandResult` → UI update.

### Key model fields on `Person`

`Name`, `Email`, `Address`, `Set<Subject>`, `Set<Day>`, `Set<Time>`, `EmergencyContact`, `PaymentStatus`, `Set<Tag>`

- `Day` — day of week (alphanumeric validation)
- `Time` — 24-hour 4-digit string (e.g., `"1400"`)
- Prefix constants live in `logic/parser/CliSyntax.java`

### Adding a new field (pattern)

1. Create field class in `model/person/` with validation
2. Add to `Person` constructor and getters
3. Add prefix to `CliSyntax`
4. Update `AddCommandParser` / `EditCommandParser` to parse the prefix
5. Add `JsonAdaptedPerson` field + conversion in `storage/`
6. Update FXML and UI card/panel if displayed
7. Add unit tests for the field class and update `PersonBuilder` in test utilities

### Test utilities

- `src/test/java/seedu/address/testutil/PersonBuilder.java` — fluent builder for `Person` in tests
- `src/test/data/` — sample JSON files for integration tests
- `TypicalPersons.java` — pre-built test fixtures
