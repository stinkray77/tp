---
layout: default.md
title: "User Guide"
pageNav: 3
---

# TutorCentral User Guide

TutorCentral is a **desktop app for freelance tutors in Singapore** to manage student information, optimised for use via a **Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). Whether you are tracking payments, recording attendance, or scheduling lessons, TutorCentral keeps everything organised in one place. If you can type fast, TutorCentral can get your student management tasks done faster than traditional GUI apps.

## Key Features

- Manage student profiles (add, edit, delete, view)
- Track payment status (mark as Paid, Due, or Overdue)
- Record and review attendance for lessons
- Search and filter students by name, subject, day, payment status, or tags
- Add remarks and notes per student
- Schedule lessons with day/time tracking

## Using this guide

- **New users:** Start with [Quick Start](#quick-start) for step-by-step setup instructions.
- **Need help with a specific command:** Jump to [Command Summary](#command-summary) for a quick reference.
- **Want deeper understanding:** Explore the [Features](#features) section for detailed explanations of each command.
- **Developers:** Check the [Developer Guide](DeveloperGuide.md) for architecture and implementation details.

## Useful Notations and Glossary

| Term                  | Definition                                                                                                              |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| **GUI**               | Graphical User Interface — the visual display of the application (windows, buttons, etc.)                               |
| **CLI**               | Command Line Interface — a text-based interface where you type commands to interact with the app                        |
| **Command**           | An instruction typed into the command box to perform an action (e.g., `add`, `delete`)                                  |
| **Parameter**         | A value supplied to a command, indicated by a prefix (e.g., `n/John` where `John` is the parameter)                     |
| **Index**             | The number shown beside a student in the displayed list, used to refer to that student in commands                      |
| **Mainstream OS**     | Windows, macOS, and Linux operating systems                                                                             |
| **Tutor**             | The user of TutorCentral — a freelance tutor managing their students                                                    |
| **Student**           | A person being tutored, whose information is stored in TutorCentral                                                     |
| **Emergency Contact** | A valid 8-digit Singapore phone number (starting with 8 or 9) of a person to contact in case of emergency for a student |
| **Attendance**        | A record of whether a student was present, absent, or excused for a lesson                                              |
| **Attendance Status** | One of: `Present`, `Absent`, or `Excused` — indicates a student's attendance for a specific lesson                      |
| **Payment Status**    | One of: `Paid`, `Due`, or `Overdue` — indicates the current fee payment status of a student                             |
| **Subject**           | An academic subject a student is enrolled in (e.g., `Mathematics`, `English`)                                           |
| **Lesson Slot**       | A scheduled tutoring session defined by a subject, day, and time (e.g., Mathematics on Monday at 1400)                  |
| **Tag**               | A short single-token label attached to a student for categorisation (e.g., `priority`, `exam-prep`, `lower_sec`)         |
| **Remark**            | A free-text note attached to a student for any additional information                                                   |

<div style="page-break-after: always;"></div>

<!-- * Table of Contents -->
<page-nav-print />

---

## Notes for Users

- **Launching the app:** Use `java -jar tutorcentral.jar` in a terminal rather than double-clicking the file, as double-clicking may not work on some systems.
- **Folder permissions:** The app needs to create and update files such as `preferences.json` and the `data/` folder. Place the JAR in a normal user-writable folder such as `Documents`, `Desktop`, or a personal project folder. Avoid protected system folders or read-only shared folders.
- **macOS fullscreen:** macOS users running the app in fullscreen mode may experience unexpected behaviour when opening secondary dialogs such as the Help window or the student view dialog. Use windowed mode instead.

---

<div style="page-break-after: always;"></div>

## Quick start

1. Ensure you have Java `17` installed in your Computer. Follow the instructions for your OS:
   - **Windows:** Download and install [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium Temurin 17](https://adoptium.net/temurin/releases/?version=17).
   - **Mac:** Follow the precise installation steps in the [Mac installation guide](https://se-education.org/guides/tutorials/javaInstallationMac.html) on se-education.org. Alternatively, download [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium Temurin 17](https://adoptium.net/temurin/releases/?version=17).
   - **Linux:** Run `sudo apt install openjdk-17-jdk`, or download binaries from [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or [Adoptium Temurin 17](https://adoptium.net/temurin/releases/?version=17).

2. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-T09-2/tp/releases).

3. Copy the file to the folder you want to use as the _home folder_ for your TutorCentral.

4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tutorcentral.jar` command to run the application.<br>
   The initial app state after launch is shown below. Note how the app contains some sample data.<br>
   ![startup](images/startup.png)

5. Type the command in the command box and press Enter to execute it. e.g., typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:
   - `list` : Lists all students.

   - `add n/John Doe e/johnd@example.com a/John street, block 123, #01-01 ec/98765432` : Adds a student named `John Doe` to Tutor Central.

   - `delete 3` : Deletes the 3rd student shown in the current list.

   - `clear` : Deletes all students.

   - `exit` : Exits the app.

6. Refer to the [Features](#features) below for details of each command.

---

<div style="page-break-after: always;"></div>

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

- Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g., in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

- Items in square brackets are optional.<br>
  e.g., `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

- Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g., `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

- Parameters can be in any order.<br>
  e.g., if the command specifies `n/NAME e/EMAIL`, `e/EMAIL n/NAME` is also acceptable.

- Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g., if the command specifies `help 123`, it will be interpreted as `help`.

- If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
  </box>

**Parameter constraints:**

| Prefix | Field             | Rules                                                                    |
| ------ | ----------------- | ------------------------------------------------------------------------ |
| `n/`   | Name              | Alphanumeric characters, spaces, and hyphens, cannot be blank           |
| `e/`   | Email             | Valid email format (e.g., `user@example.com`)                            |
| `a/`   | Address           | Any text, cannot be blank                                                |
| `ec/`  | Emergency Contact | Exactly 8 digits, must start with 8 or 9 (valid Singapore mobile number) |
| `s/`   | Subject           | Alphanumeric characters and spaces; must not be blank; matching is case-insensitive |
| `d/`   | Day               | Full day names (Monday-Sunday) or 3-letter abbreviations (Mon-Sun), case-insensitive |
| `ti/`  | Time              | 4-digit 24-hour format, 0000-2359                                        |
| `ps/`  | Payment Status    | One of: `Paid`, `Due`, `Overdue`; case-insensitive                       |
| `t/`   | Tag               | Starts with an alphanumeric character; may contain alphanumeric characters, hyphens, and underscores; spaces inside tag names are not allowed |
| `r/`   | Remark            | Any text (free-form)                                                     |
| `st/`  | Attendance Status | One of: `Present`, `Absent`, `Excused`; case-insensitive                 |

**Important:** Subjects, days, and times must be specified in matching triplets. Each subject requires a corresponding day and time. If you provide 2 subjects, you must provide exactly 2 days and 2 times.

<div style="page-break-after: always;"></div>

### Viewing help : `help`

Shows a message explaining how to access the help page.

Format: `help`

Example result: A pop-up window will show you a link to this user guide with more detailed instructions.

### Adding a student: `add`

Adds a student to Tutor Central.

Format: `add n/NAME e/EMAIL a/ADDRESS ec/EMERGENCY_CONTACT [s/SUBJECT d/DAY ti/TIME]… [ps/PAYMENT_STATUS] [r/REMARK] [t/TAG]…`

<box type="tip" seamless>

**Tip:** A student can have any number of tags (including 0)
</box>

- You can clear all lesson slots only by providing `s/ d/ ti/` with no values after each prefix, for example `edit 1 s/ d/ ti/`.

Examples:

- `add n/John Tan e/johntan@example.com a/21 Lower Kent Ridge Rd ec/98765432 s/Mathematics d/Monday ti/1400 s/English d/Wednesday ti/1600 ps/Due r/Needs extra help`

If `ps/PAYMENT_STATUS` is omitted, the student's payment status defaults to `Due`.
If `r/REMARK` is omitted, the remark defaults to empty.

Example result after adding a student:
![add result](images/add-result.png)

<div style="page-break-after: always;"></div>

### Listing all students : `list`

Shows a list of all students in Tutor Central.

Format: `list`

<box type="tip" seamless>

**Tip:** Long single-line details such as names, tags, and addresses are shortened with `...` in the student list if they exceed the available display width. The full details are still saved and can be viewed with the `view INDEX` command.

</box>

Example result:
![list result](images/list-result.png)

<div style="page-break-after: always;"></div>

### Editing a student : `edit`

Edits an existing student in Tutor Central.

Format: `edit INDEX [n/NAME] [e/EMAIL] [a/ADDRESS] [ec/EMERGENCY_CONTACT] [s/SUBJECT d/DAY ti/TIME]… [ps/PAYMENT_STATUS] [r/REMARK] [t/TAG]…`

<box type="warning" seamless>

**Caution:** If you edit lesson slots, you must provide all three of subject (`s/`), day (`d/`), and time (`ti/`) together. Editing lesson slots replaces all existing lesson slots.

</box>

- Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
- At least one of the optional fields must be provided.
- Existing values will be updated to the input values.
- When editing tags, the existing tags of the student will be removed i.e. adding of tags is not cumulative.
- You can remove all the student’s tags by typing `t/` without
  specifying any tags after it.
- You can remove all lesson slots by typing `s/ d/ ti/` without specifying any values after the prefixes.

Examples:

- `edit 1 e/johndoe@example.com` Edits the email address of the 1st student to be `johndoe@example.com`.
- `edit 2 n/Betsy Crower t/` Edits the name of the 2nd student to be `Betsy Crower` and clears all existing tags.
- `edit 1 s/ d/ ti/` Clears all lesson slots of the 1st student.

Example result:
![edit result](images/edit-result.png)

<div style="page-break-after: always;"></div>

### Locating students: `find`

Finds students matching the given criteria.

Format: `find [n/NAME_KEYWORDS] [s/SUBJECT] [d/DAY] [ps/PAYMENT_STATUS] [t/TAG]`

* You can still search by name without prefixes using `find KEYWORD [MORE_KEYWORDS]`.
* When using prefixes, multiple criteria use AND logic.
* Repeating the same prefix combines all supplied values for that field.
* Within the same prefix (e.g.`ps/Due ps/Overdue`), values are **OR**-ed.
* For different prefixes (e.g.`ps/Due s/Science`), values are **AND**-ed.
* You can provide multiple keywords after a single prefix, or repeat the same prefix multiple times.
* Name search is case-insensitive and matches full words only.
* Subject and tag searches are case-insensitive and match partial words.
* Day and payment status searches are case-insensitive exact matches.
* Payment status must be one of: `Paid`, `Due`, `Overdue`.

Examples:

- `find John` returns `john` and `John Doe`
- `find s/Mathematics` returns all students taking Mathematics
- `find s/Math s/English` returns students taking Math or English
- `find d/Monday` returns all students with Monday lessons
- `find n/Alice Bob n/Carol` returns students whose names match Alice, Bob, or Carol
- `find ps/Due` returns all students with unpaid fees
- `find s/Math d/Monday` returns students taking Math on Mondays
- `find t/priority` returns students tagged as priority

Example result for name-based search: `find John`
![find by name result](images/find-name-result.png)

Example result for payment-status search: `find ps/Overdue`
![find by payment result](images/find-payment-result.png)

We can use the `list` command to go back to the default layout showing the details of all students.

<div style="page-break-after: always;"></div>

### Viewing a student : `view`

Shows the full details of the student at the given index, in a pop-up window.

Format: `view INDEX`

- The index refers to the index number shown in the displayed student list.
- The index **must be a positive integer** 1, 2, 3, …​

Examples:

- `view 1` shows the full details of the 1st student in the current list.
- `find Alex` followed by `view 1` shows the full details of the 1st student in the filtered results.

Use `view INDEX` if a long name, tag, address, or remark appears shortened with `...` in the student list.

Example result:
![view result](images/view-result.png)

<div style="page-break-after: always;"></div>

### Adding a remark to a student: `remark`

Adds or updates a free-text remark for the student at the given index. Useful for notes like "needs help with algebra" or "prefers morning lessons".

Format: `remark INDEX r/REMARK`

- The index refers to the index number shown in the displayed student list.
- The index **must be a positive integer** 1, 2, 3, ...
- The remark replaces any existing remark for that student.
- To remove a remark, use `remark INDEX r/` with nothing after `r/`.

Examples:

- `remark 1 r/Needs extra help with algebra` adds a remark to the 1st student.
- `remark 2 r/` removes the remark from the 2nd student.

Example result:
![remark result](images/remark-result.png)

<div style="page-break-after: always;"></div>

### Updating payment status: `mark`

Quickly updates the payment status of a student.

Format: `mark INDEX ps/PAYMENT_STATUS`

- The index refers to the index number shown in the displayed student list.
- The index **must be a positive integer** 1, 2, 3, ...
- Payment status must be one of: `Paid`, `Due`, `Overdue`.
- Payment status is case-insensitive, so `paid`, `PAID`, and `Paid` are all accepted.

Examples:

- `mark 1 ps/Overdue` marks the 1st student's payment as Overdue.
- `mark 3 ps/Paid` marks the 3rd student's payment as Paid.

Example result:
![mark result](images/mark-result.png)

<div style="page-break-after: always;"></div>

### Marking attendance: `markattendance`

Records a student's attendance for a specific lesson within a subject. Supports both single and multiple students.

Format: `markattendance INDEXES s/SUBJECT d/DAY ti/TIME l/LESSON st/STATUS`

- The index(es) refer to the index number(s) shown in the displayed student list.
- For single student: Use `INDEX` (e.g., `1`)
- For multiple students: Use comma-separated indices (e.g., `1,2,3` or `1, 2, 3`)
- All indices **must be positive integers** 1, 2, 3, ...
- The student(s) must have a lesson slot matching the specified subject, day, and time combination. Subject matching is case-insensitive.
- `l/LESSON` is a **required** lesson/session label used to distinguish different attendance entries for the same weekly lesson slot. Tutors are encouraged to include the date (e.g., `l/2026-04-13 Algebra Lesson 2`). TutorCentral treats the lesson label as text and does not validate or sort by date.
- Valid attendance statuses are `Present`, `Absent`, and `Excused`.
- If an attendance record already exists for the same subject, time slot, and lesson label, it is updated.
- If no record exists, a new one is created.
- Multiple attendance entries can exist for the same weekly slot if they have different lesson labels.
- When using multiple indices, all students must have the specified subject and lesson slot, otherwise the command will fail for those students.

<box type="warning" seamless>

**Caution:**

- The student must have a matching lesson slot (subject + day + time) before attendance can be marked. Else, the command is blocked.
- The `INDEX` refers to the position in the **currently displayed list** — use `list` or `find` first if needed.
- Attendance status (`st/`) is case-insensitive (e.g., `present`, `Present`, and `PRESENT` are all accepted).
- The `l/LESSON` label is mandatory. Omitting it will result in an invalid command format error.
  </box>

Examples:

**Single Student:**
- `markattendance 1 s/Mathematics d/Monday ti/1400 l/2026-04-13 Algebra Lesson 2 st/Absent` marks the 1st student as Absent for their Mathematics lesson on Monday at 1400, labelled as "2026-04-13 Algebra Lesson 2".
- `markattendance 1 s/Mathematics d/Monday ti/1400 l/2026-04-13 Algebra Lesson 2 st/Excused` can update the same record to Excused (e.g., after receiving an MC).
- `markattendance 1 s/Mathematics d/Monday ti/1400 l/2026-04-20 Algebra Lesson 3 st/Present` creates a separate attendance entry for a different week.

**Multiple Students (Batch Operations):**
- `markattendance 1,2,3 s/Mathematics d/Monday ti/1400 l/Week 1 st/Present` marks students 1, 2, and 3 as Present for their Mathematics lesson on Monday at 1400.
- `markattendance 1, 2 s/English d/Wednesday ti/1600 l/Week 2 st/Absent` marks students 1 and 2 as Absent for their English lesson on Wednesday at 1600.

**Error Examples:**
- `markattendance 3 s/Mathematics d/Tuesday ti/0900 l/Lesson 1 st/Present` is blocked, since the third student does not have a lesson slot for Mathematics on Tuesday at 0900.
- `markattendance 1,2,5 s/Mathematics d/Monday ti/1400 l/Week 1 st/Present` will fail if student 5 doesn't have Mathematics on Monday at 1400.

Example result:
![view result](images/markattendance-error.png)

<div style="page-break-after: always;"></div>

### Viewing attendance records: `listattendance`

Displays a student's attendance records, optionally filtered by subject.

Format: `listattendance INDEX [s/SUBJECT]`

- The index refers to the index number shown in the displayed student list.
- The index **must be a positive integer** 1, 2, 3, ...
- If `s/SUBJECT` is provided, only attendance records for that subject are shown (case-insensitive).
- Results are organised by subject and lesson in the result display area.
- If no attendance records exist, a message indicates this.

<box type="tip" seamless>

**Tip:** This is useful before parent meetings to quickly review a student's attendance history. Use after `markattendance` to verify attendance was recorded correctly.

</box>

Examples:

- `listattendance 1` shows all attendance records for the 1st student.
- `listattendance 1 s/Mathematics` shows only Mathematics attendance records for the 1st student.

Example result:
![view result](images/listattendance-result.png)

<div style="page-break-after: always;"></div>

### Deleting a student : `delete`

Deletes the specified student from Tutor Central.

Format: `delete INDEX`

- Deletes the student at the specified `INDEX`.
- The index refers to the index number shown in the displayed student list.
- The index **must be a plain positive integer** 1, 2, 3, …​
- Inputs such as `+2` and `1.0` are not supported.
- The command only accepts one index input at a time.
- After a successful deletion, Tutor Central shows the full student list again.

Examples:

- `list` followed by `delete 2` deletes the 2nd student in Tutor Central.
- `find Betsy` followed by `delete 1` deletes the 1st student in the results of the `find` command.

Example result:
![delete result](images/delete-result.png)

<div style="page-break-after: always;"></div>

### Clearing all entries : `clear`

Clears all entries from Tutor Central.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TutorCentral data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

TutorCentral data are saved automatically as a JSON file `[JAR file location]/data/tutorcentral.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, TutorCentral will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the TutorCentral to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

---

## Edge Cases and Special Scenarios

This section covers special scenarios and edge cases that you may encounter while using TutorCentral.

### Multiple Students with Same Name

TutorCentral allows multiple students to have the same name. However, this can cause confusion when:
- Using `find` with name keywords - all matching students will be displayed
- Using index-based commands - ensure you select the correct index from the displayed list

**Best Practice:** Use additional identifiers like unique tags or include middle names/initials to distinguish students.

### Empty Search Results

When a `find` command returns no results:
- The message `0 persons listed!` will be displayed
- Use `list` to return to the full student list
- Check your search criteria for typos or try broader search terms

### Editing Lesson Slots

When editing lesson slots (`edit` command):
- **All existing lesson slots are replaced** when providing new `s/`, `d/`, `ti/` values
- **Partial updates are not supported** - you must provide complete triplets
- **Attendance records for removed slots are automatically deleted**

**Example:** If a student has 2 lesson slots and you edit with only 1 new slot, the student will end up with only 1 lesson slot.

### Attendance Record Management

**Multiple Lessons per Week:**
- You can mark multiple attendance entries for the same weekly slot using different lesson labels
- Example: `l/Week 1`, `l/Week 2`, `l/Week 3` for the same Monday 1400 Mathematics slot

**Attendance Record Persistence:**
- Attendance records are tied to specific lesson slots
- If you delete a lesson slot, all attendance records for that slot are also deleted
- This maintains data integrity between schedules and attendance

### Payment Status Limitations

**Current Limitations:**
- Payment status is a single value per student (`Paid`, `Due`, `Overdue`)
- No monthly tracking - changing status affects the entire record
- No payment history or date tracking

**Workaround:** Use the `remark` field to track payment details like "Paid for March 2026" or "Due for April 2026".

### Data File Corruption

**Symptoms:**
- App starts with empty student list
- Error messages about invalid JSON format
- Specific fields reported as missing

**Recovery Steps:**
1. Close the application
2. Restore from a recent backup of `tutorcentral.json`
3. If no backup exists, the app will create a new empty file

**Prevention:**
- Regularly backup the `data/tutorcentral.json` file
- Avoid manual editing of the JSON file unless necessary
- Use proper shutdown (`exit` command) to ensure data is saved correctly

### Large Number of Students

**Performance Considerations:**
- List display may become slower with 1000+ students
- Search operations remain efficient due to indexed filtering
- Consider using tags to organize large student groups

**Management Tips:**
- Use `find` with specific criteria rather than browsing full lists
- Leverage payment status and subject filters for targeted views
- Use descriptive tags for categorization

### Special Characters in Fields

**Supported Characters:**
- **Names:** Letters, numbers, spaces, and hyphens
- **Addresses:** Any characters (free text)
- **Tags:** Letters, numbers, hyphens, underscores (must start with alphanumeric)
- **Email:** Standard email format with @ and domain
- **Emergency Contact:** 3-15 digits (may be landline or short code)

**Unsupported Scenarios:**
- Tags with spaces or special characters (except hyphens and underscores)
- Names with only special characters
- Emergency contacts with fewer than 3 digits or more than 15 digits

---

## Error Messages

TutorCentral provides specific error messages to help you identify and fix command issues. Below are the most common error messages you may encounter:

### Command Format Errors

| Error Message | Cause | Solution |
|--------------|-------|----------|
| `Invalid command format!` | Command syntax is incorrect or missing required parameters | Check the command format in the [Command Summary](#command-summary) and ensure all required prefixes are present |
| `Unknown command` | Command word is not recognized | Verify the command spelling and use one of the supported commands |
| `The person index provided is invalid` | Index is out of range, not a number, or invalid format | Use a plain positive integer (e.g., `1`, `2`, `3`) that corresponds to an entry in the current list |

### Field Validation Errors

| Error Message | Cause | Solution |
|--------------|-------|----------|
| `Emails should be of the format local-part@domain...` | Email format is invalid | Use a valid email format like `user@example.com` |
| `Emergency contact should be exactly 8 digits starting with 8 or 9` | Emergency contact number is invalid | Use an 8-digit Singapore mobile number starting with 8 or 9 |
| `Tag names should start with an alphanumeric character...` | Tag format is invalid | Tags must start with a letter/number and contain only letters, numbers, hyphens, and underscores |
| `Addresses can take any values, and it should not be blank` | Address field is empty | Provide a non-empty address |

### Lesson Slot Errors

| Error Message | Cause | Solution |
|--------------|-------|----------|
| `Number of subjects, days and times must all match` | Unequal number of subjects, days, and times | Provide the same number of subjects, days, and times (e.g., 2 subjects need 2 days and 2 times) |
| `Subjects, days and times must all be specified together` | Incomplete lesson slot specification | When providing any of `s/`, `d/`, `ti/`, you must provide all three together |
| `Student does not have a lesson for [Subject] on [Day] at [Time]` | No matching lesson slot found | Check that the student has the specified lesson slot, or use `view INDEX` to see their current schedule |

### Attendance Errors

| Error Message | Cause | Solution |
|--------------|-------|----------|
| `The student does not take the subject: [Subject]` | Subject not found for student | Verify the subject name spelling or check the student's subjects with `view INDEX` |
| `Attendance key must be in 'Day Time - Lesson' format` | Invalid attendance record format (data file corruption) | This error occurs during data loading; restore from backup if needed |

### Data Errors

| Error Message | Cause | Solution |
|--------------|-------|----------|
| `This student already exists in Tutor Central` | Duplicate student detected | Check if the student already exists using `find` before adding |
| `Person's [field] field is missing!` | Data file corruption or invalid JSON format | Restore from backup or fix the JSON structure manually |

---

<div style="page-break-after: always;"></div>

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous TutorCentral home folder.

**Q**: Where is the data file stored?<br>
**A**: By default, TutorCentral saves data in a `data` folder next to the JAR file, in a file called `tutorcentral.json`. Check the footer at the bottom of the app to see the exact path. Keep dated backups so you can revert if needed.

**Q**: How do I back up my data?<br>
**A**: Copy the data JSON file `[JAR file location]/data/tutorcentral.json` to a safe location such as your Downloads folder or an external drive. To restore from a backup, replace the data file with your backup copy.

**Q**: What if I accidentally corrupt the data file?<br>
**A**: If the data file contains invalid JSON, TutorCentral will start with an empty data set. Keep dated backups so you can revert if needed.

**Q**: Can I import data from Excel?<br>
**A**: TutorCentral does not currently support CSV/Excel import. Students need to be added using the `add` command. CSV import is planned for a future release.

**Q**: The app window disappeared from my screen. What do I do?<br>
**A**: Delete the `preferences.json` file in the same folder as the JAR and restart the app. This resets the window position.

**Q**: Is TutorCentral free?<br>
**A**: Yes, TutorCentral is free and open-source, built by NUS students for the tutor community.

---

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.
3. **When using the `find` command with multiple prefixes** (e.g., `find s/Math d/Monday`), the results use AND logic. There is currently no way to perform OR searches across different fields.
4. **Attendance records are stored per lesson slot + lesson label (subject + day + time + lesson).** If a student's lesson slots are edited, attendance records for removed slots are deleted so that the saved attendance data stays aligned with the student's current lesson schedule.
5. **On macOS fullscreen mode, secondary dialogs may behave unexpectedly.** Commands that open separate windows, such as `help` and `view`, may not display as expected while the app is in fullscreen. Use windowed mode if this happens.

---

<div style="page-break-after: always;"></div>

## Command summary

| Action             | Format, Examples                                                                                                                                                                                                                   |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Add**            | `add n/NAME e/EMAIL a/ADDRESS ec/EMERGENCY_CONTACT [s/SUBJECT d/DAY ti/TIME]... [ps/PAYMENT_STATUS] [r/REMARK] [t/TAG]...` <br> e.g., `add n/John Doe e/johnd@example.com a/Clementi Ave 2 ec/91234567 s/Mathematics d/Monday ti/1400 ps/Due r/Needs help` |
| **Clear**          | `clear`                                                                                                                                                                                                                            |
| **Delete**         | `delete INDEX` <br> e.g., `delete 3`                                                                                                                                                                                               |
| **Edit**           | `edit INDEX [n/NAME] [e/EMAIL] [a/ADDRESS] [ec/EMERGENCY_CONTACT] [s/SUBJECT d/DAY ti/TIME]... [ps/PAYMENT_STATUS] [r/REMARK] [t/TAG]...` <br> e.g., `edit 1 e/johndoe@example.com`                                                |
| **Exit**           | `exit`                                                                                                                                                                                                                             |
| **Find**           | `find [n/NAME] [s/SUBJECT] [d/DAY] [ps/PAYMENT_STATUS] [t/TAG]` <br> e.g., `find s/Mathematics d/Monday`                                                                                                                           |
| **Help**           | `help`                                                                                                                                                                                                                             |
| **List**           | `list`                                                                                                                                                                                                                             |
| **ListAttendance** | `listattendance INDEX [s/SUBJECT]` <br> e.g., `listattendance 1 s/Mathematics`                                                                                                                                                     |
| **Mark**           | `mark INDEX ps/PAYMENT_STATUS` <br> e.g., `mark 1 ps/Paid`                                                                                                                                                                         |
| **MarkAttendance** | `markattendance INDEXES s/SUBJECT d/DAY ti/TIME l/LESSON st/STATUS` <br> e.g., `markattendance 1 s/Mathematics d/Monday ti/1400 l/2026-04-13 Algebra Lesson 2 st/Present` <br> e.g., `markattendance 1,2,3 s/Mathematics d/Monday ti/1400 l/Week 1 st/Present` |
| **Remark**         | `remark INDEX r/REMARK` <br> e.g., `remark 1 r/Needs help with algebra`                                                                                                                                                            |
| **View**           | `view INDEX` <br> e.g., `view 1`                                                                                                                                                                                                   |
