---
name: Friend Handoff Day Time
overview: A self-contained prompt for Randall's friend to implement the Day and Time fields for the Add Student feature in Tutor Central, covering model classes, Person integration, commands, parsers, storage, UI, and all tests.
todos:
  - id: friend-pr1
    content: "PR 1: Create Day.java, Time.java, DayTest.java, TimeTest.java model classes"
    status: pending
  - id: friend-pr2
    content: "PR 2: Integrate Day and Time into Person, commands, parsers, storage, UI, and ~20 test files"
    status: pending
isProject: false
---

# Handoff: Implement Day and Time Fields for Add Student Feature

## Copy everything below and send to your friend

---

## Project Context

You are working on **Tutor Central**, an AB3 (AddressBook Level 3) fork for managing private tuition students. The team repo is `AY2526S2-CS2103T-T09-2/tp`.

The Add Student feature spec requires these fields: Name, Subject, Day, Time, Emergency Contact, Attendance, Payment Status. For v1.2, we split the work:

- **Randall (done)**: Subject, EmergencyContact, PaymentStatus model classes + full integration into Person model, commands, storage, UI, and tests. Also removed the old Phone field.
- **You**: Day and Time model classes + full integration (same scope).

Attendance is deferred to a later iteration.

---

## Current State of the Codebase

After merging Randall's PRs, the `Person` model looks like this:

```java
public Person(Name name, Email email, Address address,
              Set<Subject> subjects, EmergencyContact emergencyContact,
              PaymentStatus paymentStatus, Set<Tag> tags)
```

Current CLI prefixes in `CliSyntax.java`: `n/`, `e/`, `a/`, `s/`, `ec/`, `ps/`, `t/`, `r/`

The `add` command format is currently:

```
add n/NAME e/EMAIL a/ADDRESS s/SUBJECT ec/EMERGENCY_CONTACT [ps/PAYMENT_STATUS] [t/TAG]...
```

Your job is to add `Day` and `Time` so the final format becomes:

```
add n/NAME e/EMAIL a/ADDRESS s/SUBJECT d/DAY ti/TIME ec/EMERGENCY_CONTACT [ps/PAYMENT_STATUS] [t/TAG]...
```

---

## What You Need to Implement

### PR 1: Create Day and Time Model Classes with Tests

**Branch**: `feature/add-day-time-model`

#### Create: `src/main/java/seedu/address/model/person/Day.java`

Follow the exact pattern of `Subject.java`. Key details:

- Validation: alphanumeric + spaces, not blank (same regex as Subject: `[\\p{Alnum}][\\p{Alnum} ]`)
- Field name: `public final String dayName`
- Accepts values like "Monday", "Wednesday", etc.
- Implements `toString()`, `equals()`, `hashCode()`
- Include `MESSAGE_CONSTRAINTS`, `VALIDATION_REGEX`, `isValidDay(String)`

Use `Subject.java` as your template -- the structure is identical:

```java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Day {
    public static final String MESSAGE_CONSTRAINTS =
            "Days should only contain alphanumeric characters and spaces, and should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";
    public final String dayName;

    public Day(String day) {
        requireNonNull(day);
        checkArgument(isValidDay(day), MESSAGE_CONSTRAINTS);
        dayName = day;
    }

    public static boolean isValidDay(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    // toString, equals, hashCode -- same pattern as Subject
}
```

#### Create: `src/main/java/seedu/address/model/person/Time.java`

Same pattern, but for lesson times:

- Validation: exactly 4 digits representing 24hr time (regex: `\\d{4}`)
- Field name: `public final String value`
- Accepts values like "1400", "0900"
- `MESSAGE_CONSTRAINTS = "Time should be exactly 4 digits in 24-hour format (e.g. 1400)"`

```java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Time {
    public static final String MESSAGE_CONSTRAINTS =
            "Time should be exactly 4 digits in 24-hour format (e.g. 1400)";
    public static final String VALIDATION_REGEX = "\\d{4}";
    public final String value;

    public Time(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        value = time;
    }

    public static boolean isValidTime(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    // toString, equals, hashCode -- same pattern as EmergencyContact
}
```

#### Create: `src/test/java/seedu/address/model/person/DayTest.java`

Follow `SubjectTest.java` pattern. Test:

- Constructor with null throws NullPointerException
- Constructor with invalid day throws IllegalArgumentException
- `isValidDay()` with blank, special chars (false) and valid days (true)
- `equals()`, `hashCode()`, `toString()`

#### Create: `src/test/java/seedu/address/model/person/TimeTest.java`

Follow `EmergencyContactTest.java` pattern. Test:

- Constructor with null throws NullPointerException
- Constructor with invalid time throws IllegalArgumentException (e.g. "14", "abcd", "12345")
- `isValidTime()` with various inputs
- `equals()`, `hashCode()`, `toString()`

**Commit message**: `Add Day and Time model classes with tests`

---

### PR 2: Integrate Day and Time into Person Model and All Layers

**Branch**: `feature/integrate-day-time-into-person`

This is the big PR. You need to touch every layer. Here is **every file** you must modify, in order:

#### 1. `src/main/java/seedu/address/logic/parser/CliSyntax.java`

Add two new prefix constants. Use `d/` for Day and `ti/` for Time (not `t/` which is already taken by Tag):

```java
public static final Prefix PREFIX_DAY = new Prefix("d/");
public static final Prefix PREFIX_TIME = new Prefix("ti/");
```

#### 2. `src/main/java/seedu/address/model/person/Person.java`

Add two new fields and update everything:

- Add `private final Set<Day> days = new HashSet<>();` and `private final Set<Time> times = new HashSet<>();`
- Update constructor to accept `Set<Day> days` and `Set<Time> times`
- Add to `requireAllNonNull()`
- Add `getDays()` and `getTimes()` getters (return `Collections.unmodifiableSet(...)`)
- Update `equals()`, `hashCode()`, `toString()`

The new constructor signature should be:

```java
public Person(Name name, Email email, Address address,
              Set<Subject> subjects, Set<Day> days, Set<Time> times,
              EmergencyContact emergencyContact,
              PaymentStatus paymentStatus, Set<Tag> tags)
```

#### 3. `src/main/java/seedu/address/logic/parser/ParserUtil.java`

Add 4 new methods (follow the `parseSubject`/`parseSubjects` pattern exactly):

- `parseDay(String day)` -- trims, validates, returns `new Day(trimmedDay)`
- `parseDays(Collection<String> days)` -- iterates and calls `parseDay`
- `parseTime(String time)` -- trims, validates, returns `new Time(trimmedTime)`
- `parseTimes(Collection<String> times)` -- iterates and calls `parseTime`

#### 4. `src/main/java/seedu/address/logic/commands/AddCommand.java`

- Add imports for `PREFIX_DAY` and `PREFIX_TIME`
- Update `MESSAGE_USAGE` to include `[d/DAY]...` and `[ti/TIME]...`
- Update the example command string

#### 5. `src/main/java/seedu/address/logic/parser/AddCommandParser.java`

- Add `PREFIX_DAY` and `PREFIX_TIME` to the `ArgumentTokenizer.tokenize()` call
- Add `PREFIX_DAY` and `PREFIX_TIME` to `verifyNoDuplicatePrefixesFor()` -- actually no, since they are multi-valued (like Subject), they should NOT be in verifyNoDuplicatePrefixes. Just add them to tokenize.
- Parse them: `Set<Day> dayList = ParserUtil.parseDays(argMultimap.getAllValues(PREFIX_DAY));`
- Same for Time
- Update the `new Person(...)` call to include the new sets

#### 6. `src/main/java/seedu/address/logic/commands/EditCommand.java`

- Add `private Set<Day> days;` and `private Set<Time> times;` to `EditPersonDescriptor`
- Add setters and getters (follow the `subjects` pattern exactly)
- Update `isAnyFieldEdited()` to include `days` and `times`
- Update copy constructor
- Update `equals()` and `toString()`
- Update `createEditedPerson()` to handle `days` and `times`

#### 7. `src/main/java/seedu/address/logic/parser/EditCommandParser.java`

- Add `PREFIX_DAY` and `PREFIX_TIME` to `tokenize()`
- Add parsing logic for days and times (follow the subjects pattern)

#### 8. `src/main/java/seedu/address/logic/Messages.java`

Update `format()` to include Days and Times in the output string:

```java
.append("; Days: ");
person.getDays().forEach(builder::append);
builder.append("; Times: ");
person.getTimes().forEach(builder::append);
```

#### 9. `src/main/java/seedu/address/storage/JsonAdaptedPerson.java`

- Add `private final List<String> days = new ArrayList<>();` and `private final List<String> times = new ArrayList<>();`
- Update both constructors (the `@JsonCreator` one and the `Person source` one)
- Update `toModelType()` to validate and convert days and times

#### 10. `src/main/java/seedu/address/ui/PersonCard.java`

- Add `@FXML private FlowPane days;` and `@FXML private FlowPane times;` (or `Label` if you prefer)
- Populate them in the constructor

#### 11. `src/main/resources/view/PersonListCard.fxml`

Add UI elements for days and times:

```xml
<FlowPane fx:id="days" />
<FlowPane fx:id="times" />
```

#### 12. `src/main/java/seedu/address/model/util/SampleDataUtil.java`

- Add a `getDaySet(String... strings)` and `getTimeSet(String... strings)` helper method
- Update all sample persons to include days and times
- Update all `new Person(...)` calls

#### 13. `src/main/java/seedu/address/logic/commands/RemarkCommand.java`

Update the `new Person(...)` call in `execute()` to include `personToEdit.getDays()` and `personToEdit.getTimes()`.

#### Test Files to Modify

1. `**src/test/java/seedu/address/testutil/PersonBuilder.java**`

- Add `days` and `times` fields (default to empty sets)
  - Add `withDays(String... days)` and `withTimes(String... times)` methods
  - Update `build()` to pass them to the Person constructor

1. `**src/test/java/seedu/address/testutil/EditPersonDescriptorBuilder.java**`

- Add `withDays()` and `withTimes()` methods

1. `**src/test/java/seedu/address/logic/commands/CommandTestUtil.java**`

- Add constants: `VALID_DAY_MONDAY = "Monday"`, `VALID_DAY_WEDNESDAY = "Wednesday"`, `VALID_TIME_1400 = "1400"`, `VALID_TIME_0900 = "0900"`
  - Add descriptor constants: `DAY_DESC_MONDAY`, `TIME_DESC_1400`, etc.
  - Add invalid descriptor: `INVALID_DAY_DESC`, `INVALID_TIME_DESC`

1. `**src/test/java/seedu/address/testutil/TypicalPersons.java**`

- Add `.withDays(...)` and `.withTimes(...)` to some of the typical persons

1. `**src/test/java/seedu/address/testutil/PersonUtil.java**`

- Update `getPersonDetails()` to include days and times
  - Update `getEditPersonDescriptorDetails()` to include days and times

1. `**src/test/data/JsonSerializableAddressBookTest/typicalPersonsAddressBook.json**`

- Add `"days": [...]` and `"times": [...]` to each person entry

1. `**src/test/data/JsonSerializableAddressBookTest/duplicatePersonAddressBook.json**`

- Same as above

1. `**src/test/data/JsonSerializableAddressBookTest/invalidPersonAddressBook.json**`

- Same as above

1. `**src/test/data/JsonAddressBookStorageTest/invalidPersonAddressBook.json**`

- Same as above

1. `**src/test/data/JsonAddressBookStorageTest/invalidAndValidPersonAddressBook.json**`

- Same as above

1. `**src/test/java/seedu/address/storage/JsonAdaptedPersonTest.java**`

- Add `VALID_DAYS` and `VALID_TIMES` constants
  - Update all `new JsonAdaptedPerson(...)` constructor calls
  - Add test methods for invalid/null days and times

1. `**src/test/java/seedu/address/model/person/PersonTest.java**`

- Update `equals()` test to include days and times
  - Update `toStringMethod()` expected output

1. `**src/test/java/seedu/address/logic/LogicManagerTest.java**`

- Update the `assertCommandFailureForExceptionFromStorage` method's person construction

1. `**src/test/java/seedu/address/logic/parser/AddCommandParserTest.java**`

- Update test cases to include day/time prefixes

1. `**src/test/java/seedu/address/logic/parser/EditCommandParserTest.java**`

- Add test cases for parsing days and times

1. `**src/test/java/seedu/address/logic/commands/EditCommandTest.java**`

- Update tests for new fields

1. `**src/test/java/seedu/address/logic/commands/EditPersonDescriptorTest.java**`

- Update `equals()` and `toString()` tests

1. `**src/test/java/seedu/address/logic/parser/ParserUtilTest.java**`

- Add tests for `parseDay`, `parseDays`, `parseTime`, `parseTimes`

1. `**src/test/java/seedu/address/logic/commands/RemarkCommandTest.java**`

- Update `new Person(...)` calls to include days and times

1. `**src/test/java/seedu/address/model/person/NameContainsKeywordsPredicateTest.java**`

- If any PersonBuilder calls exist, add the new fields

**Commit message**: `Integrate Day and Time fields into Person model, commands, storage, UI and tests`

---

## How to Build, Test, and Check Style

```bash
./gradlew test                    # Run all unit tests
./gradlew checkstyleMain          # Checkstyle on main code
./gradlew checkstyleTest          # Checkstyle on test code
./gradlew run                     # Run the app to manually verify
```

All three (test + checkstyleMain + checkstyleTest) must pass before pushing.

**Key Checkstyle rules**:

- Max 120 characters per line
- All public methods need Javadoc
- No unused imports
- No wildcard imports

---

## Git Workflow

```bash
# Sync with the team repo
git fetch upstream master

# Create your branch off upstream master (make sure Randall's PR3 is merged first!)
git checkout -b feature/add-day-time-model upstream/master

# ... make changes, then:
git add -A
git commit -m "Add Day and Time model classes with tests"
git push -u origin feature/add-day-time-model

# Create PR on GitHub: your-fork -> AY2526S2-CS2103T-T09-2/tp master
```

For PR 2, create a new branch off upstream master (after PR 1 is merged), or off your PR 1 branch if doing them back-to-back.

---

## Tips

- Always run `./gradlew test checkstyleMain checkstyleTest` before pushing
- If you see `Phone` referenced anywhere, it is dead code from the old AB3 -- ignore it
- The `Remark` class and `RemarkCommand` exist from a tutorial exercise -- just make sure your Person constructor changes are reflected in `RemarkCommand.java` and `RemarkCommandTest.java`
- Use Cursor AI to help -- paste this entire prompt as context and it can guide you through each file change
