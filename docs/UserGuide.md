---

layout: default.md
title: "User Guide"
pageNav: 3
---

# TutorCentral User Guide

TutorCentral Level 3 (TC3) is a **desktop app for freelance tutors to manage student information**, optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, Tutor Central can get your student management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S2-CS2103T-T09-2/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your TutorCentral.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar tutorcentral.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all students.

   * `add n/John Doe e/johnd@example.com a/John street, block 123, #01-01 ec/98765432` : Adds a student named `John Doe` to Tutor Central.

   * `delete 3` : Deletes the 3rd student shown in the current list.

   * `clear` : Deletes all students.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

</box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME e/EMAIL`, `e/EMAIL n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

**Parameter constraints:**

| Prefix | Field | Rules |
|--------|-------|-------|
| `n/` | Name | Alphanumeric characters and spaces, cannot be blank |
| `e/` | Email | Valid email format (e.g., `user@example.com`) |
| `a/` | Address | Any text, cannot be blank |
| `ec/` | Emergency Contact | Exactly 8 digits |
| `s/` | Subject | Alphanumeric characters, no spaces |
| `d/` | Day | Monday-Sunday (or Mon-Sun), case-insensitive |
| `ti/` | Time | 4-digit 24-hour format, 0000-2359 |
| `ps/` | Payment Status | One of: `Paid`, `Due`, `Overdue` |
| `t/` | Tag | Alphanumeric characters, no spaces |
| `r/` | Remark | Any text (free-form) |

**Important:** Days and times must be specified in matching pairs. If you provide 2 days, you must provide exactly 2 times.

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a student: `add`

Adds a student to Tutor Central.

Format: `add n/NAME e/EMAIL a/ADDRESS ec/EMERGENCY_CONTACT [s/SUBJECT]… [d/DAY]… [ti/TIME]… [ps/PAYMENT_STATUS] [t/TAG]…`

</box type="tip" seamless>

**Tip:** A student can have any number of tags (including 0)
</box>

Examples:
* `add n/John Doe e/johnd@example.com a/311, Clementi Ave 2, #02-25 ec/98765432 s/Mathematics s/English d/Monday d/Wednesday ti/1400 ti/1600 ps/Due`

### Listing all students : `list`

Shows a list of all students in Tutor Central.

Format: `list`

### Editing a student : `edit`

Edits an existing student in Tutor Central.

Format: `edit INDEX [n/NAME] [e/EMAIL] [a/ADDRESS] [ec/EMERGENCY_CONTACT] [s/SUBJECT]… [d/DAY]… [ti/TIME]… [ps/PAYMENT_STATUS] [t/TAG]…`

**Caution:** If you edit days (`d/`), you must also edit times (`ti/`), and vice versa.

* Edits the student at the specified `INDEX`. The index refers to the index number shown in the displayed student list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the student will be removed i.e adding of tags is not cumulative.
* You can remove all the student’s tags by typing `t/` without
    specifying any tags after it.

Examples:
*  `edit 1 e/johndoe@example.com` Edits the email address of the 1st student to be `johndoe@example.com`.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd student to be `Betsy Crower` and clears all existing tags.

### Locating students: `find`

Finds students matching the given criteria.

Format: `find [n/NAME_KEYWORDS] [s/SUBJECT] [d/DAY] [ps/PAYMENT_STATUS] [t/TAG]`

* **Backward compatible:** `find KEYWORD [MORE_KEYWORDS]` (without prefixes) still searches by name.
* When using prefixes, multiple criteria use AND logic.
* Name search is case-insensitive and matches full words only.
* Subject, day, and tag searches are case-insensitive.
* Payment status must be one of: `Paid`, `Due`, `Overdue`.

Examples:
* `find John` returns `john` and `John Doe` (backward compatible)
* `find s/Mathematics` returns all students taking Mathematics
* `find d/Monday` returns all students with Monday lessons
* `find ps/Due` returns all students with unpaid fees
* `find s/Math d/Monday` returns students taking Math on Mondays
* `find t/priority` returns students tagged as priority

### Viewing a student : `view`

Shows the full details of the student at the given index.

Format: `view INDEX`

* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `view 1` shows the full details of the 1st student in the current list.
* `find Alex` followed by `view 1` shows the full details of the 1st student in the filtered results.

### Adding a remark to a student: `remark`

Adds or updates a free-text remark for the student at the given index. Useful for notes like "needs help with algebra" or "prefers morning lessons".

Format: `remark INDEX r/REMARK`

* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, ...
* The remark replaces any existing remark for that student.
* To remove a remark, use `remark INDEX r/` with nothing after `r/`.

Examples:
* `remark 1 r/Needs extra help with algebra` adds a remark to the 1st student.
* `remark 2 r/` removes the remark from the 2nd student.

### Updating payment status: `mark`

Quickly updates the payment status of a student.

Format: `mark INDEX ps/PAYMENT_STATUS`

* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, ...
* Payment status must be one of: `Paid`, `Due`, `Overdue`.

Examples:
* `mark 1 ps/Paid` marks the 1st student's payment as Paid.
* `mark 3 ps/Overdue` marks the 3rd student's payment as Overdue.

### Deleting a student : `delete`

Deletes the specified student from Tutor Central.

Format: `delete INDEX`

* Deletes the student at the specified `INDEX`.
* The index refers to the index number shown in the displayed student list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd student in Tutor Central.
* `find Betsy` followed by `delete 1` deletes the 1st student in the results of the `find` command.

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

</box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, TutorCentral will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the TutorCentral to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous TutorCentral home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action     | Format, Examples |
|------------|------------------|
| **Add**    | `add n/NAME e/EMAIL a/ADDRESS ec/EMERGENCY_CONTACT [s/SUBJECT]... [d/DAY]... [ti/TIME]... [ps/PAYMENT_STATUS] [t/TAG]...` <br> e.g., `add n/John Doe e/johnd@example.com a/Clementi Ave 2 ec/91234567 s/Mathematics d/Monday ti/1400 ps/Due` |
| **Clear**  | `clear` |
| **Delete** | `delete INDEX` <br> e.g., `delete 3` |
| **Edit**   | `edit INDEX [n/NAME] [e/EMAIL] [a/ADDRESS] [ec/EMERGENCY_CONTACT] [s/SUBJECT]... [d/DAY]... [ti/TIME]... [ps/PAYMENT_STATUS] [t/TAG]...` <br> e.g., `edit 1 e/johndoe@example.com` |
| **Find**   | `find [n/NAME] [s/SUBJECT] [d/DAY] [ps/STATUS] [t/TAG]` <br> e.g., `find s/Mathematics d/Monday` |
| **Help**   | `help` |
| **List**   | `list` |
| **Mark**   | `mark INDEX ps/PAYMENT_STATUS` <br> e.g., `mark 1 ps/Paid` |
| **Remark** | `remark INDEX r/REMARK` <br> e.g., `remark 1 r/Needs help with algebra` |
| **View**   | `view INDEX` <br> e.g., `view 1` |

