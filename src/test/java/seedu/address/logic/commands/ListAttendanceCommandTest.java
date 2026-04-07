package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ListAttendanceCommand}.
 */
public class ListAttendanceCommandTest {

    private static final String VALID_SUBJECT = "Mathematics";
    private static final String OTHER_SUBJECT = "English";
    private static final String FIRST_LESSON = "Algebra Lesson 1";
    private static final String SECOND_LESSON = "Algebra Lesson 2";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexWithAttendanceRecords_success() {
        // EP: valid index with existing attendance records, no subject filter
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithAttendance = originalPerson
                .markAttendance(VALID_SUBJECT, FIRST_LESSON, AttendanceStatus.PRESENT)
                .markAttendance(VALID_SUBJECT, SECOND_LESSON, AttendanceStatus.ABSENT);
        model.setPerson(originalPerson, personWithAttendance);

        ListAttendanceCommand command = new ListAttendanceCommand(INDEX_FIRST_PERSON, null);
        String expectedMessage = String.format(
                "Attendance for %s%n%n%s%n  %s: %s%n  %s: %s",
                personWithAttendance.getName(),
                VALID_SUBJECT,
                FIRST_LESSON, AttendanceStatus.PRESENT.value,
                SECOND_LESSON, AttendanceStatus.ABSENT.value);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexWithSubjectFilter_success() {
        // EP: valid index with existing attendance records and matching subject filter
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithAttendance = originalPerson
                .markAttendance(VALID_SUBJECT, FIRST_LESSON, AttendanceStatus.PRESENT)
                .markAttendance(OTHER_SUBJECT, "Grammar Lesson 1", AttendanceStatus.EXCUSED);
        model.setPerson(originalPerson, personWithAttendance);

        ListAttendanceCommand command = new ListAttendanceCommand(INDEX_FIRST_PERSON, "mathematics");
        String expectedMessage = String.format(
                "Attendance for %s%n%n%s%n  %s: %s",
                personWithAttendance.getName(),
                VALID_SUBJECT,
                FIRST_LESSON, AttendanceStatus.PRESENT.value);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noAttendanceRecords_returnsNoRecordsMessage() {
        // EP: valid index with no attendance records
        Person personToList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ListAttendanceCommand command = new ListAttendanceCommand(INDEX_FIRST_PERSON, null);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        assertCommandSuccess(command, model,
                String.format(ListAttendanceCommand.MESSAGE_NO_ATTENDANCE_RECORDS, personToList.getName()),
                expectedModel);
    }

    @Test
    public void execute_subjectFilterNotFound_returnsSubjectSpecificNoRecordsMessage() {
        // EP: valid index with attendance records but non-matching subject filter
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithAttendance = originalPerson.markAttendance(VALID_SUBJECT,
                FIRST_LESSON, AttendanceStatus.PRESENT);
        model.setPerson(originalPerson, personWithAttendance);

        ListAttendanceCommand command = new ListAttendanceCommand(INDEX_FIRST_PERSON, OTHER_SUBJECT);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        assertCommandSuccess(command, model,
                String.format(ListAttendanceCommand.MESSAGE_NO_ATTENDANCE_RECORDS_FOR_SUBJECT,
                        personWithAttendance.getName(), OTHER_SUBJECT),
                expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        // Boundary value: index just outside the unfiltered displayed list
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ListAttendanceCommand command = new ListAttendanceCommand(outOfBoundIndex, null);

        assertCommandFailure(command, model, seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        // Boundary value: index valid in the full list but outside the filtered displayed list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        ListAttendanceCommand command = new ListAttendanceCommand(outOfBoundIndex, null);

        assertCommandFailure(command, model, seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        // EP: equality across same values, different values, null, and different types
        ListAttendanceCommand firstCommand = new ListAttendanceCommand(INDEX_FIRST_PERSON, VALID_SUBJECT);
        ListAttendanceCommand secondCommand = new ListAttendanceCommand(INDEX_SECOND_PERSON, OTHER_SUBJECT);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new ListAttendanceCommand(INDEX_FIRST_PERSON, VALID_SUBJECT)));

        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));

        assertFalse(firstCommand.equals(new ListAttendanceCommand(INDEX_SECOND_PERSON, VALID_SUBJECT)));
        assertFalse(firstCommand.equals(new ListAttendanceCommand(INDEX_FIRST_PERSON, OTHER_SUBJECT)));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        // EP: string representation includes both index and optional subject filter
        ListAttendanceCommand command = new ListAttendanceCommand(INDEX_FIRST_PERSON, VALID_SUBJECT);
        String expected = ListAttendanceCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + ", subject=" + VALID_SUBJECT + "}";
        assertEquals(expected, command.toString());
    }
}
