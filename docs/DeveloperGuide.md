---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Tutor Central Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
* Libraries used: [JavaFX](https://openjfx.io/), [Jackson](https://github.com/FasterXML/jackson), [JUnit5](https://github.com/junit-team/junit5)

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="700" />


The `Model` component,

* stores the student list data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

Each `Person` in Tutor Central currently contains:

* `Name`
* `Email`
* `Address`
* `List<LessonSlot>` — each `LessonSlot` bundles a `Subject`, `Day`, and `Time` into a single immutable value object
* `EmergencyContact`
* `PaymentStatus`
* `Remark`
* `Set<Tag>`
* `attendanceRecords` — a `Map<String, Map<String, AttendanceStatus>>` mapping subject name to attendance record key (e.g., `"Monday 1400 - 2026-04-13 Algebra Lesson 2"`) to `AttendanceStatus`, tracking attendance per subject per lesson entry

Derived getters `getSubjects()`, `getDays()`, and `getTimes()` are provided for backward compatibility with predicates used in the `find` command.

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It provides a more focused view of the `AddressBook`'s internal structure, showing how `Person` objects are stored in a `UniquePersonList` and how each `Person` composes its value-object fields.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S2-CS2103T-T09-2/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both student list data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Find by field feature

The extended `find` command supports prefix-based filtering across multiple
student fields. `FindCommandParser` tokenizes the user input with the supported
prefixes `n/`, `s/`, `d/`, `ps/`, and `t/`, then checks whether the command is
using prefixed search or the original name-only search format.

The parser builds a `Predicate<Person>`, which is a yes/no matching rule applied
to each student in the list:

* if the predicate returns `true`, that student remains in the filtered list
* if the predicate returns `false`, that student is excluded

For example, `PaymentStatusMatchesPredicate("Paid")` returns `true` for a
student whose payment status is `Paid`, and `false` otherwise.

When no supported prefix is present, the parser falls back to the command
preamble and creates a `NameContainsKeywordsPredicate`. This allows name-only
searches such as `find alice bob` to continue working without prefixes.

When one or more supported prefixes are present, the parser creates one or more
field-specific predicates:

* `NameContainsKeywordsPredicate`
  Matches when the student's name contains any of the given keywords.
* `SubjectContainsKeywordsPredicate`
  Matches when any of the student's subjects contains any of the given keywords.
* `DayMatchesPredicate`
  Matches when any of the student's lesson days matches one of the provided days.
* `PaymentStatusMatchesPredicate`
  Matches when the student's payment status matches the provided status.
* `TagContainsKeywordsPredicate`
  Matches when any of the student's tags contains any of the given keywords.

If multiple prefixed fields are provided, the parser combines them using
`Predicate.and()` so that all specified conditions must match. If exactly one
prefixed field is provided, the parser returns that single predicate directly
instead of returning a chained predicate.

During execution, `FindCommand#execute()` passes the constructed predicate to
`Model#updateFilteredPersonList()`, which refreshes the displayed student list.

The following sequence diagram shows how a `find` command flows through the
parser, command, and model components.

<puml src="diagrams/FindSequenceDiagram.puml" alt="Sequence diagram for the extended find feature" />

### View student feature

The `view` command displays a student's full details in a popup dialog. `ViewCommandParser` parses
the index from the input string.

During execution, `ViewCommand`:
1. Retrieves the target student from the filtered list and validates the index is in range.
2. Returns a `CommandResult` that includes the `Person` object to view and sets `isShowView()` to `true`.

The `MainWindow` checks `CommandResult#isShowView()` and, if true, calls `PersonViewDialog.show(person)`.

The `PersonViewDialog` is a JavaFX dialog that displays all student fields in a structured `GridPane`
layout: name, emergency contact, email, address, tags, payment status, lesson slots (each showing subject, day, and time), and remark.

<puml src="diagrams/ViewSequenceDiagram.puml" alt="Sequence diagram for the view student feature" />

### Mark payment status feature

The `mark` command updates a student's payment status without requiring the
full `edit` command flow.

`MarkCommandParser` tokenizes the user input with the `ps/` prefix and checks
that the command contains both:

* a non-empty preamble that can be parsed into an index
* exactly one `ps/` value

If the input is valid, the parser creates a `MarkCommand` containing the target
index and the new `PaymentStatus`.

During execution, `MarkCommand` retrieves the target student from the filtered
list and validates that the provided index is in range. It then creates a new
`Person` object with the updated `PaymentStatus` while preserving the student's
name, email, address, lesson slots, emergency contact,
remark, and tags. The updated student replaces the original student in the
model, and the filtered list is refreshed.

<puml src="diagrams/MarkSequenceDiagram.puml" alt="Sequence diagram for the mark payment status feature" />

The following activity diagram summarises the flow when a `mark` command is executed:

<puml src="diagrams/MarkActivityDiagram.puml" alt="Activity diagram for the mark payment status feature" />

### Mark attendance feature

The `markattendance` command records a student's attendance for a specific lesson within a subject.

`MarkAttendanceCommandParser` tokenizes the user input with the `s/`, `d/`, `ti/`, `l/`, and `st/` prefixes and checks
that the command contains:

* a non-empty preamble that can be parsed into an index
* exactly one `s/` value (subject)
* exactly one `d/` value (day)
* exactly one `ti/` value (time)
* exactly one `l/` value (lesson/session label)
* exactly one `st/` value (attendance status)

If the input is valid, the parser creates a `MarkAttendanceCommand` with the target index, subject name,
day, time, lesson label, and `AttendanceStatus`.

During execution, `MarkAttendanceCommand`:
1. Retrieves the target student from the filtered list.
2. Validates that the student has a matching lesson slot for the specified subject, day, and time combination (subject match is case-insensitive).
3. Creates a new `Person` with the updated attendance record using `Person#markAttendance(subject, "Day Time - Lesson", status)`.
4. Replaces the original student in the model.
5. Refreshes the filtered list.

<puml src="diagrams/MarkAttendanceSequenceDiagram.puml" alt="Sequence diagram for the mark attendance feature" />

### List attendance feature

The `listattendance` command displays a student's attendance records, optionally filtered by subject.

`ListAttendanceCommandParser` tokenizes the user input and extracts:

* the index from the preamble
* an optional `s/` value to filter by subject

During execution, `ListAttendanceCommand`:
1. Validates the index is in range.
2. Retrieves the target student from the filtered list.
3. Retrieves the student's `attendanceRecords` map.
4. If a subject filter is provided, filters to show only that subject's records.
5. Formats the results into a human-readable string.
6. Returns a `CommandResult` with the formatted string.

The output format is: `Attendance for [NAME]: [SUBJECT]: [DAY TIME - LESSON]: [STATUS]`

<puml src="diagrams/ListAttendanceSequenceDiagram.puml" alt="Sequence diagram for the list attendance feature" />

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current Tutor Central data state in its history.
* `VersionedAddressBook#undo()` — Restores the previous Tutor Central data state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone Tutor Central data state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial Tutor Central data state, and the `currentStatePointer` pointing to that single saved state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the student list. The `delete` command calls `Model#commitAddressBook()`, causing the modified Tutor Central data state after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted saved state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified Tutor Central data state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the updated Tutor Central data state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous saved state, and restores Tutor Central data to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial Tutor Central state, then there are no previous Tutor Central states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores Tutor Central data to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest Tutor Central state, then there are no undone Tutor Central states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the student list, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all student list states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire student data set.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

**Aspect: Command granularity**

* **Alternative 1 (current choice):** Provide dedicated commands for frequent single-field updates (`mark` for payment status, `remark` for notes).
  * Pros: Shorter, more memorable syntax for the most common operations.
  * Cons: More command classes to maintain.
* **Alternative 2:** Route all field updates through `edit`.
  * Pros: Fewer commands, simpler `AddressBookParser`.
  * Cons: `edit` syntax is verbose for simple status changes (e.g. `edit 1 ps/Paid` vs `mark 1 ps/Paid`).

### \[Proposed\] Data archiving

Archiving would allow tutors to hide inactive students from the main list without permanently deleting them. This feature is not yet implemented.

### \[Proposed\] Monthly payment tracking

The current payment status field (`Paid`, `Due`, `Overdue`) is a single static value per student. In practice, tutors need to track payments on a monthly basis — a student may be `Paid` for March but `Due` for April. A future enhancement would replace the single `PaymentStatus` with a monthly payment record (e.g., `Map<YearMonth, PaymentStatus>`), allowing tutors to:

* Mark payment status per month (e.g., `mark 1 ps/Paid m/2026-04`)
* View payment history over time
* Identify students with overdue payments for specific months
* Generate payment summaries for a given period

### \[Proposed\] Lesson duration

Each `LessonSlot` currently stores only a start time. Adding a `Duration` field (e.g., 60 or 90 minutes) would enable:

* Display of lesson end times in the UI (e.g., "Monday 1400–1530")
* Detection of scheduling conflicts between overlapping lesson slots
* Calculation of total teaching hours per week or month for workload planning


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a freelance private tuition teacher managing a large clientele of students across multiple subjects and levels.
* needs to centrally track lesson schedules, payment statuses, and detailed academic notes.
* needs to track lesson attendance and review attendance history for parent meetings.
* prefers desktop apps over other types.
* can type fast and prefers typing lesson notes and commands during classes.
* prefers typing to mouse interactions to avoid disrupting the flow of a lesson.
* is reasonably comfortable using CLI apps.

**Value proposition**: TutorCentral provides a centralized, keyboard-driven platform for freelance tuition teachers to manage students, track attendance and payment statuses, and maintain organized lesson records significantly faster than navigating multiple GUI-based tools like Excel or Google Calendar.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

Low-priority user stories may include future-facing features that are not implemented in the current version.
Such items are tracked in [Appendix: Planned Enhancements](#appendix-planned-enhancements).

| Priority | As a …​                  | I want to …​                                           | So that I can…​                                              |
|----------|--------------------------|---------------------------------------------------------|--------------------------------------------------------------|
| `* * *`  | new user                 | see usage instructions                                  | refer to instructions when I forget how to use Tutor Central |
| `* * *`  | tutor                    | add a student with schedule and payment details         | maintain complete student records                             |
| `* * *`  | tutor                    | list all students                                       | return to the full student list after searching or filtering |
| `* * *`  | tutor                    | search for students by name                             | find a target student quickly                                 |
| `* * *`  | tutor                    | search students by subject, day, payment status, or tag | quickly find relevant students                                |
| `* * *`  | tutor                    | view a student's full details                           | check information before a lesson                             |
| `* * *`  | tutor                    | delete a student                                        | remove entries that I no longer need                          |
| `*`      | tutor                    | clear all student records                               | reset Tutor Central when starting over or switching datasets  |
| `* *`    | tutor                    | update a student's details                              | correct outdated records when needed                          |
| `* * *`  | tutor                    | mark a student's payment status                         | track who has paid without editing the full student record    |
| `* * *`  | tutor                    | add a free-text remark to a student                     | record lesson notes, progress, or reminders quickly           |
| `* * *`  | tutor                    | mark a student's attendance for a lesson                | keep accurate attendance records without paper                |
| `* * *`  | tutor                    | view a student's attendance history                     | prepare for parent meetings with concrete data                |
| `* * *`  | tutor                    | filter a student's attendance records by subject        | review one subject's attendance quickly                      |
| `* * *`  | tutor                    | filter students by multiple criteria at once            | quickly find relevant students (e.g., Math on Monday)         |
| `* *`    | tutor                    | update a student's attendance after receiving new information | keep attendance records accurate even after the lesson        |
| `* * *`  | tutor                    | have my student records saved automatically             | avoid losing updates between app sessions                     |
| `*`      | tutor                    | identify students who are frequently absent             | follow up with at-risk students and notify their parents      |
| `*`      | tutor                    | export student data                                     | share records with centre managers or parents                 |

### Use cases

(For all use cases below, the **System** is `Tutor Central` and the **Actor** is the `tutor`, unless specified otherwise)

**Use Case 01: Delete a student**

**MSS**

1.  Tutor list students or performs <u>Search for students (UC02).</u>
2.  Tutor requests to delete the target student from the shown results.
3.  Tutor Central deletes the target student.
4.  Tutor Central shows a success message with the deleted student's details.

    Use case ends.

**Extensions**

* 2a. The specified student to delete is invalid.
    * 2a1. Tutor Central shows an error message.

        Use case resumes from step 1.



**Use Case 02: Search for students**

**MSS**

1.  Tutor enters a search command with the desired criteria.
2.  Tutor Central shows a list of students that match the criteria.

    Use case ends.

**Extensions**

* 1a. Tutor Central detects invalid search input.
    * 1a1. Tutor Central shows an error message.

        Use case resumes from step 1.


* 2a. No students match the criteria.
    * 2a1. Tutor Central shows an empty list.

        Use case ends.


**Use Case 03: Add a student**

**MSS**

1.  Tutor requests to add a student by providing the student's details: name, email, address, emergency contact, lesson slots (subject/day/time triplets), payment status, and tags.
2.  Tutor Central validates the input.
3.  Tutor Central records the new student and shows a success message with the added student's details.

    Use case ends.

**Extensions**

* 2a. The provided details are invalid.
    * 2a1. Tutor Central shows an error message.

        Use case resumes from step 1.


* 2b. The number of subjects, days, and times do not match (must be equal triplets).
    * 2b1. Tutor Central shows an error message.

        Use case resumes from step 1.


* 2c. A matching student already exists.
    * 2c1. Tutor Central shows an error message.

        Use case resumes from step 1.


**Use Case 04: Update a student**

**MSS**

1.  Tutor lists students or performs <u>Search for students (UC02).</u>
2.  Tutor requests to update the target student from the search results by providing updated values for one or more fields.
3.  Tutor Central validates the updated values.
4.  Tutor Central updates the student and shows a success message with the updated details.

    Use case ends.

**Extensions**

* 2a. The specified student is invalid.
    * 2a1. Tutor Central shows an error message.

        Use case resumes from step 1.


* 3a. The provided update details are invalid.
    * 3a1. Tutor Central shows an error message.

        Use case resumes from step 1.


* 3b. Tutor updates lesson slots without providing all three of subject, day, and time together.
    * 3b1. Tutor Central shows an error message.

        Use case resumes from step 1.


**Use Case 05: View a student**

**MSS**

1. Tutor lists students or performs <u>Search for students (UC02).</u> 
2. Tutor locates target student and requests to view the student in the currently shown list.
3. Tutor Central shows the full student details in a popup dialog.

    Use case ends.

**Extensions**

* 2a. The specified student index is invalid.
    * 2a1. Tutor Central shows an error message.

        Use case ends.


**Use Case 06: Find students by field**

**MSS**

1.  Tutor lists students or performs <u>Search for students (UC02).</u>
2.  Tutor Central parses the search criteria.
3.  Tutor Central filters the student list using the specified field predicates.
4.  Tutor Central displays the matching students.

    Use case ends.

**Extensions**

* 1a. Tutor provides no criteria to search by.
    * 1a1. Tutor Central shows an error message.

        Use case ends.


* 1b. Tutor provides invalid search input.
    * 1b1. Tutor Central shows an error message.

        Use case ends.


* 3a. No students match the criteria.
    * 3a1. Tutor Central shows `0 persons listed!`.

        Use case ends.


**Use Case 07: Mark payment status**

**MSS**

1. Tutor lists students or performs <u>Search for students (UC02).</u> 
2. Tutor requests to update a student's payment status.
3. Tutor Central locates the target student in the currently shown list.
4. Tutor Central updates the student's payment status.
5. Tutor Central shows a success message confirming the new payment status.

    Use case ends.

**Extensions**

* 2a. The specified student index is invalid.
    * 2a1. Tutor Central shows an error message.

        Use case ends.


* 2b. Tutor provides multiple payment statuses.
    * 2b1. Tutor Central shows an error message.

        Use case ends.


**Use Case 08: Add a remark to a student**

**MSS**

1. Tutor lists students or performs <u>Search for students (UC02).</u>
2. Tutor requests to update a student's remark.
3. Tutor Central locates the target student in the currently shown list.
4. Tutor Central updates the student's remark.
5. Tutor Central shows a success message with the updated student details.

    Use case ends.

**Extensions**

* 1a. The specified student is invalid.
    * 1a1. Tutor Central shows an error message.

        Use case ends.


* 3a. Tutor provides an empty remark.
    * 3a1. Tutor Central removes the student's existing remark.
    * 3a2. Tutor Central shows a success message with the updated student details.

        Use case ends.


**Use Case 09: Mark student attendance**

**MSS**

1.  Tutor lists students or performs <u>Search for students (UC02).</u>
2.  Tutor requests to mark attendance for a student at a specific index, specifying the subject, day, time, and status.
3.  Tutor Central validates the index, subject, day, time, and status.
4.  Tutor Central records the attendance and shows a success message.

    Use case ends.

**Extensions**

* 3a. The specified index is invalid.
    * 3a1. Tutor Central shows an error message.

        Use case resumes from step 2.


* 3b. The specified subject does not match any of the student's subjects.
    * 3b1. Tutor Central shows an error message indicating the subject mismatch.

        Use case resumes from step 2.


* 3c. The attendance status is not one of Present, Absent, or Excused.
    * 3c1. Tutor Central shows an error message with valid status options.

        Use case resumes from step 2.


**Use Case 10: List student attendance**

**MSS**

1. Tutor lists students or performs <u>Search for students (UC02).</u>
2. Tutor requests to list the attendance for a student at a specific index, optionally specifying a subject filter.
3. Tutor Central validates the index, and optionally subject.
4. Tutor Central retrieves and displays the attendance records.

    Use case ends.

**Extensions**

* 3a. The specified index is invalid.
    * 3a1. Tutor Central shows an error message.

        Use case resumes from step 2.


* 4a. No attendance records exist for the student (or for the filtered subject).
    * 4a1. Tutor Central shows a message indicating no records found.

        Use case ends.


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above-average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Changes made via commands must be auto-saved within 1 second of completion.
5.  The student list must scroll smoothly and display at least 200 student records without lag or stutter.
6.  The default view must show essential fields like name and timeslot without horizontal scrolling.
7.  The system must detect and reject duplicate student entries (same name, case-sensitive) within 1 second of the `add` command completing, displaying a clear error message.
8.  Must consume less than 150MB of RAM, and minimal CPU when idle.
9.  Attendance records must be persisted to the JSON data file within 1 second of the `markattendance` command completing.
10. The `listattendance` command should return results within 1 second even for students with 100+ attendance records.
11. All error messages must include the invalid input value and the expected format, so the user can correct the command without consulting the User Guide.


### Glossary

#### User & Data Entities

* **Tutor**: The primary user of Tutor Central, an individual providing tutoring services
* **Student**: A learner whose information is managed in Tutor Central
* **Subject**: An academic subject a student is enrolled in (e.g., Mathematics, English)
* **Lesson Slot**: A scheduled lesson defined by a Subject, Day, and Time triplet (e.g., Mathematics on Monday at 1400)
* **Attendance**: The record of a student's presence at a lesson slot
* **Attendance Status**: The recorded status for attendance: `Present`, `Absent`, or `Excused`
* **Payment Status**: The financial status of a student: `Paid`, `Due`, or `Overdue`
* **Emergency Contact**: An 8-digit Singapore phone number for a student's parent/guardian
* **Remark**: A free-text note attached to a student record
* **Tag**: A single-token label for categorizing students (e.g., `primary`, `exam-prep`, `lower_sec`)
* **Field**: A piece of data within a student record (e.g., Name, Emergency Contact)
* **Time-slot key**: The combined day and time string used to identify an attendance record (e.g., `Monday 1400`)

#### Application Logic & Architecture

* **Model**: The component that holds application data in memory
* **Logic**: The component that parses and executes user commands
* **Storage**: The component that reads and writes data to JSON files
* **Command**: A parsed user instruction ready for execution
* **Predicate**: A filter condition applied to the student list
* **Filtered list**: The currently displayed subset of students, usually produced by commands such as `find`; index-based commands operate on this list
* **JSON**: The file format used for persistent data storage
* **Auto-save**: Automatic saving of changes without needing any extra user action

#### Commands & User Input

* **Prefix**: A short string that marks the beginning of a parameter (e.g., `n/`, `s/`, `ps/`)
* **Index**: The 1-based position number of a student in the displayed list
* **Preamble**: Text before the first prefix in a command string
* **Parameter**: A value provided to a command via a prefix

#### Interface & Environment

* **CLI**: Command Line Interface, the text-based input method
* **GUI**: Graphical User Interface, the visual display
* **JavaFX**: The UI framework used by Tutor Central
* **MarkBind**: The documentation framework used for the project website
* **List View**: The graphical display showing student records as a scrollable list
* **Command Box**: The text box where users type commands
* **Command Output Area**: The display area showing the results of commands entered (successes or failures)
* **Mainstream OS**: Windows, Linux, and macOS

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file.<br>
      Expected: Shows the GUI with a set of sample students. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. Launch via command line

   1. Prerequisites: Java 17 installed. JAR file placed in an empty folder.

   2. Open a terminal. Run `java -version` to confirm Java 17 is active.

   3. Run `java -jar tutorcentral.jar`.<br>
      Expected: Application launches with sample student data. Window opens correctly.

### Adding a student

1. Adding a student with all required fields

   1. Prerequisites: Application is running. Use `list` to view current students.

   2. Test case: `add n/John Doe e/johnd@example.com a/123 Main St s/Mathematics d/Monday ti/1400 ec/91234567 ps/Due`<br>
      Expected: New student added at the end of the list. Success message shows student's name.

   3. Test case: `add n/John Doe e/johnd@example.com a/123 Main St s/Mathematics d/Monday ti/1400 ec/91234567 ps/Due` (same name as existing student)<br>
      Expected: No student added. Error message indicating duplicate student.

   4. Test case: Missing compulsory field, e.g. `add n/John Doe e/johnd@example.com`<br>
      Expected: No student added. Error message showing correct usage format.

   1. Test case: Mismatched subjects/days/times count, e.g. `add n/John Doe e/j@e.com a/addr s/Math s/English d/Monday ti/1400 d/Tuesday ec/91234567 ps/Paid`<br>
      Expected: No student added. Error message stating subjects, days, and times count must all match.

### Editing a student

1. Editing a student's details

   1. Prerequisites: List all students using `list`. At least one student in the list.

   2. Test case: `edit 1 n/Alice Tan`<br>
      Expected: First student's name updated to "Alice Tan". Success message shows updated details.

   3. Test case: `edit 1 s/English d/Wednesday ti/1000`<br>
      Expected: First student's lesson slots replaced with English on Wednesday at 1000. Success message shown.

   4. Test case: `edit 1 s/English d/Wednesday`<br>
      Expected: No edit made. Error message stating subjects, days, and times count must all match.

   5. Test case: `edit 0 n/Test`<br>
      Expected: No edit made. Error message about invalid index.

   6. Test case: `edit 1` (no fields specified)<br>
      Expected: No edit made. Error message stating that at least one field must be provided.

### Viewing a student

1. Viewing a student's full details

   1. Prerequisites: List all students using `list`. At least one student in the list.

   2. Test case: `view 1`<br>
      Expected: A popup dialog shows the first student's full details including name, email, address, lesson slots, emergency contact, payment status, remark, and tags.

   3. Test case: `view 0`<br>
      Expected: No dialog shown. Error message about invalid index.

   4. Test case: `view x` (where x is larger than the list size)<br>
      Expected: No dialog shown. Error message about invalid index.

### Marking payment status

1. Marking payment status with valid input

   1. Prerequisites: List all students using `list`. At least one student in the list.

   2. Test case: `mark 1 ps/Paid`<br>
      Expected: First student's payment status updated to `Paid`. Success message shows name and new status.

   3. Test case: `mark 1 ps/overdue` (lowercase)<br>
      Expected: First student's payment status updated to `Overdue` (auto-capitalised). Success message shown.

   4. Test case: `mark 0 ps/Paid`<br>
      Expected: No change. Error details shown in status message.

   5. Test case: `mark 1 ps/NotAStatus`<br>
      Expected: No change. Error message showing valid payment statuses (Paid, Due, Overdue).

   6. Test case: `mark 1` (missing payment status)<br>
      Expected: No change. Error message showing correct usage format.

### Adding/editing a remark

1. Adding a remark to a student

   1. Prerequisites: List all students using `list`. At least one student in the list.

   2. Test case: `remark 1 r/Needs extra help with algebra.`<br>
      Expected: Remark added to first student. Success message shown.

   3. Test case: `remark 1 r/` (empty remark)<br>
      Expected: Existing remark removed from first student. Success message shown.

### Marking attendance

1. Marking attendance with valid input

   1. Prerequisites: List all students using `list`. First student has a lesson slot for Mathematics on Monday at 1400.

   1. Test case: `markattendance 1 s/Mathematics d/Monday ti/1400 st/Present`<br>
      Expected: Attendance marked. Success message shown.

   1. Test case: `markattendance 1 s/mathematics d/Monday ti/1400 st/Absent` (lowercase subject)<br>
      Expected: Attendance updated (case-insensitive). Success message shown.

   1. Test case: `markattendance 1 s/Mathematics d/Tuesday ti/0900 st/Present` (non-existent slot)<br>
      Expected: Error message — student does not have this lesson slot.

   1. Test case: `markattendance 0 s/Mathematics d/Monday ti/1400 st/Present`<br>
      Expected: Error — invalid index.

### Viewing attendance

1. Viewing attendance records

   1. Prerequisites: Student 1 has attendance records from the marking attendance tests above.

   1. Test case: `listattendance 1`<br>
      Expected: Shows all attendance records grouped by subject, with time-slot keys (e.g., "Monday 1400: Absent").

   1. Test case: `listattendance 1 s/Mathematics`<br>
      Expected: Shows only Mathematics attendance records.

### Finding students

1. Finding students by name

   1. Prerequisites: Sample data loaded. Use `list` to confirm multiple students exist.

   2. Test case: `find n/Alice`<br>
      Expected: Students whose names contain "Alice" are shown. Count shown in result message.

   3. Test case: `find n/NonExistentName`<br>
      Expected: 0 students listed. Result message shows `0 persons listed!`.

2. Finding students by payment status

   1. Test case: `find ps/Due`<br>
      Expected: All students with payment status `Due` are listed.

   2. Test case: `find ps/Paid ps/Overdue`<br>
      Expected: Students with either `Paid` or `Overdue` status are listed.

### Exiting the application

1. Test case: `exit`<br>
   Expected: The application window closes. All data is saved.

### Deleting a person

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `list` command. Multiple students in the list.

   2. Test case: `delete 1`<br>
      Expected: First student is deleted from the list. Details of the deleted student shown in the status message. The full student list is shown again.

   3. Test case: `delete 0`<br>
      Expected: No student is deleted. Error details shown in the status message. The student list remains unchanged.

   4. Test case: `delete +2`<br>
      Expected: No student is deleted. Error message explains that only plain positive integers are accepted.

   5. Test case: `delete 1.0`<br>
      Expected: No student is deleted. Error message explains that only plain positive integers are accepted.

   6. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

2. Deleting a student when only one student is shown (filtered list)

   1. Prerequisites: Use `find n/Alice` to show only one student.

   2. Test case: `delete 1`<br>
      Expected: That student is deleted. Status message shows deleted student's details. The full student list is shown again, and running `list` confirms the student is gone.

   3. Test case: `delete 2`<br>
      Expected: Error message — index out of bounds for the filtered list, even if more students exist in the full list.

### Saving data

1. Dealing with missing data file

   1. Close the application. Navigate to the `data` folder beside the JAR file. Delete `tutorcentral.json`.

   2. Re-launch the application.<br>
      Expected: Application starts with sample data populated from `SampleDataUtil`.

2. Dealing with corrupted data file

   1. Close the application. Open `tutorcentral.json` in a text editor. Delete a required field (e.g. remove the `"name"` field from one entry). Save the file.

   2. Re-launch the application.<br>
      Expected: Application starts with an empty student list. A warning is logged but the application does not crash. The corrupted file is discarded.

3. Data persistence across sessions

   1. Add a new student with `add n/Test Student e/test@example.com a/123 St s/Math ec/91234567 ps/Paid`. Close the application.

   2. Re-launch the application.<br>
      Expected: The student added in the previous session is still present in the list.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Effort**

### Difficulty Level

Tutor Central required significantly more effort than AB3. AB3 manages a flat list of contacts with basic CRUD operations. Tutor Central extends this into a domain-specific tutor management system with:

* **Composite data model** — `Person` was expanded from simple fields (Name, Phone, Email, Address, Tags) to a richer model including `LessonSlot` (a composite value object bundling Subject, Day, and Time), `EmergencyContact`, `PaymentStatus`, `Remark`, and a nested `attendanceRecords` map.
* **Lesson slot refactor** — We initially modelled subjects, days, and times as three independent `Set` fields. This caused data integrity issues (mismatched counts) and made attendance tracking unreliable. We refactored into a unified `List<LessonSlot>` model, requiring changes across the entire stack: model, parser, storage (JSON serialisation), commands, and tests.
* **Attendance tracking** — A two-level nested map (`Map<String, Map<String, AttendanceStatus>>`) keyed by subject and day-time slot was added to each student, with dedicated `markattendance` and `listattendance` commands for recording and viewing attendance history.
* **Dedicated single-field commands** — `mark` (payment status) and `remark` were added as shortcuts to avoid the verbose `edit` syntax for the most common single-field updates.
* **Extended find** — The `find` command was extended from name-only keyword search to support prefix-based multi-field filtering (`n/`, `s/`, `d/`, `ps/`, `t/`) with composable predicates.
* **View dialog** — A `view` command was added to display a student's full details in a popup `PersonViewDialog`, requiring new JavaFX UI work.

### Challenges

* Ensuring the `edit` command preserved existing attendance records when non-slot fields were updated, and correctly pruned stale records when lesson slots were changed.
* Maintaining backward compatibility during the LessonSlot refactor while keeping all existing tests passing.
* JSON serialisation of the nested attendance map with proper validation on deserialisation (subject name validation, day-time key format validation, case-insensitive status parsing).

### Achievements

* 10 use cases fully implemented with comprehensive manual testing coverage.
* All PlantUML diagrams verified against the current codebase.
* Codecov patch coverage maintained above 85% throughout development.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

Team size: 5

1. **Make `find` support OR logic across different fields.** Currently, `find s/Math d/Monday` uses AND logic (students must match both). We plan to add an `--or` flag (e.g., `find --or s/Math d/Monday`) so users can search for students matching any of the specified criteria.

2. **Improve error message when `markattendance` targets a non-existent lesson slot.** Currently the error says the student does not have the specified lesson slot, but does not list the valid slots. We will include the student's actual lesson slots in the error message (e.g., "Valid slots: Mathematics Monday 1400, English Wednesday 1000").

3. **Standardise `listattendance` output when no records exist.** Currently, the message varies slightly depending on whether a subject filter was used. We will unify the output to always say "No attendance records found for [NAME]" optionally followed by "for subject [SUBJECT]".

4. **Make attendance pruning after `edit` more visible to users.** Currently, when a student's lesson slots are changed via `edit`, attendance records for removed slots are automatically pruned so stored attendance stays aligned with the current lesson schedule.

5. **Make `view` dialog update when the underlying student data changes.** Currently, the `PersonViewDialog` shows a snapshot of the student at the time of the `view` command. If the user edits the student while the dialog is open, the dialog is not updated. We will bind the dialog to the model so it reflects live data.

6. **Clarify abbreviated day matching in `find`.** Currently, abbreviated day inputs such as `find d/Mon` are already accepted and normalized to `Monday`. We plan to improve the documentation and examples so this behavior is clearer to users.

7. **Add confirmation prompt for `clear` command.** Currently, `clear` deletes all students immediately with no confirmation. We will add a confirmation step (e.g., requiring `clear --confirm`) to prevent accidental data loss.

8. **Allow `edit` with no changed values to succeed as a no-op.** Currently, running `edit 1` with no fields is rejected with an error ("At least one field to edit must be provided"). A future enhancement could detect when all supplied values are identical to the existing record and treat the command as a silent no-op success, which may feel more natural to users.

9. **Relax name and subject validation to accept common special characters.** Currently, names and subjects only allow alphanumeric characters and spaces. This rejects real-world names containing hyphens (e.g., `Mary-Jane`), apostrophes (e.g., `O'Brien`), periods (e.g., `Dr. Smith`), or slashes (e.g., `s/o Kumar`), and subjects like `A-Math` or `Mother Tongue (Chinese)`. We will update the validation regex for both `Name` and `Subject` to accept hyphens, apostrophes, periods, parentheses, and slashes, while ensuring the slash character does not conflict with command prefix parsing.

10. **Identify students who are frequently absent.** Currently, tutors can view attendance history manually, but Tutor Central does not automatically flag students with repeated absences. We will add a way to identify students who are frequently absent so tutors can follow up with at-risk students and notify their parents.

12. **Export student data.** Currently, student records can only be viewed inside Tutor Central or through the local JSON data file. We will add an export feature so tutors can share selected records with centre managers or parents in a more readable format.

13. **Add lesson end times to lesson slots.** Currently each lesson slot only records a start time (e.g., `ti/1400`). We plan to add an optional end time parameter (e.g., `te/1530`) to `LessonSlot` so tutors can see the full duration of each lesson.

14. **Validate `l/LESSON` as a structured date.** Currently `markattendance` uses a free-text `l/LESSON` session label. Tutors can include dates manually (e.g., `l/2026-04-13 Algebra Lesson 2`), but TutorCentral does not validate, sort, or filter attendance records by date. We plan to add a dedicated `date/DATE` parameter so attendance records can be stored and queried using actual lesson dates, enabling chronological sorting and date-range filtering.
