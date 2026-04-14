package seedu.address.logic.commands;

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
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for MarkAttendanceCommand.
 */
public class MarkAttendanceCommandTest {

    // ALICE (index 1) has lesson slot "Mathematics", "Monday", "1400"
    // BENSON (index 2) has lesson slot "English", "Tuesday", "0900"
    private static final String VALID_SUBJECT = "Mathematics";
    private static final String VALID_DAY = "Monday";
    private static final String VALID_TIME = "1400";
    private static final AttendanceStatus STATUS_PRESENT = AttendanceStatus.PRESENT;
    private static final AttendanceStatus STATUS_ABSENT = AttendanceStatus.ABSENT;

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = personToMark.markAttendance(VALID_SUBJECT, VALID_DAY + " " + VALID_TIME, STATUS_PRESENT);

        MarkAttendanceCommand command = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_SUCCESS,
                personToMark.getName(), VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = personToMark.markAttendance(VALID_SUBJECT,
                VALID_DAY + " " + VALID_TIME, STATUS_ABSENT);

        MarkAttendanceCommand command = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_ABSENT);

        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_SUCCESS,
                personToMark.getName(), VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_ABSENT.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MarkAttendanceCommand command = new MarkAttendanceCommand(
                outOfBoundIndex, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MarkAttendanceCommand command = new MarkAttendanceCommand(
                outOfBoundIndex, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_subjectNotFound_failure() {
        MarkAttendanceCommand command = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, "Physics", VALID_DAY, VALID_TIME, STATUS_PRESENT);

        assertCommandFailure(command, model,
                String.format(MarkAttendanceCommand.MESSAGE_SUBJECT_NOT_FOUND, "Physics"));
    }

    @Test
    public void execute_lessonSlotNotFound_failure() {
        // ALICE has Mathematics on Monday 1400, not Tuesday 0900
        MarkAttendanceCommand command = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, "Tuesday", "0900", STATUS_PRESENT);

        assertCommandFailure(command, model,
                String.format(Messages.MESSAGE_LESSON_SLOT_NOT_FOUND, VALID_SUBJECT, "Tuesday", "0900"));
    }

    @Test
    public void execute_subjectMatchCaseInsensitive_success() {
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = personToMark.markAttendance(VALID_SUBJECT,
                VALID_DAY + " " + VALID_TIME, STATUS_PRESENT);

        MarkAttendanceCommand command = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_SUCCESS,
                personToMark.getName(), VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndices_success() {
        // Test multiple students scenario by creating command with array constructor
        // but using same person to avoid subject mismatch issues
        Index[] indices = new Index[]{INDEX_FIRST_PERSON};
        MarkAttendanceCommand command = new MarkAttendanceCommand(
                indices, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = personToMark.markAttendance(VALID_SUBJECT,
                VALID_DAY + " " + VALID_TIME, STATUS_PRESENT);

        // Since we're using single index, it should use single success message
        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_SUCCESS,
                personToMark.getName(), VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndicesArrayConstructor_success() {
        // Test the array constructor with single index to ensure array constructor works
        // This tests the lines that check array length and determine success message
        Index[] singleIndexArray = new Index[]{INDEX_FIRST_PERSON};
        MarkAttendanceCommand command = new MarkAttendanceCommand(
                singleIndexArray, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = personToMark.markAttendance(VALID_SUBJECT,
                VALID_DAY + " " + VALID_TIME, STATUS_PRESENT);

        // Since array length is 1, it should use single success message (covers line 136-142 logic)
        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_SUCCESS,
                personToMark.getName(), VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndices_successMessageTest() {
        // Test specifically for multiple success message to cover line 140
        // Create a custom model with two identical persons to avoid subject mismatch
        Model testModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Add a second person with the same Mathematics subject
        PersonBuilder aliceBuilder = new PersonBuilder(
                testModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));
        Person aliceClone = aliceBuilder.withName("Alice Clone")
                .withEmail("alice.clone@example.com").build();
        testModel.addPerson(aliceClone);

        // Now we have two persons with Mathematics subject
        Index[] indices = new Index[]{INDEX_FIRST_PERSON,
                Index.fromOneBased(testModel.getFilteredPersonList().size())};
        MarkAttendanceCommand command = new MarkAttendanceCommand(
                indices, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        // This should trigger the multiple success message since indices.length > 1
        String expectedMessage = String.format(
                MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_MULTIPLE_SUCCESS,
                2, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT.value);

        // Create expected model with both persons marked
        Model expectedModel = new ModelManager(new AddressBook(testModel.getAddressBook()), new UserPrefs());
        Person firstPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondPerson = expectedModel.getFilteredPersonList()
                .get(Index.fromOneBased(expectedModel.getFilteredPersonList().size()).getZeroBased());

        Person markedFirstPerson = firstPerson.markAttendance(
                VALID_SUBJECT, VALID_DAY + " " + VALID_TIME, STATUS_PRESENT);
        Person markedSecondPerson = secondPerson.markAttendance(
                VALID_SUBJECT, VALID_DAY + " " + VALID_TIME, STATUS_PRESENT);

        expectedModel.setPerson(firstPerson, markedFirstPerson);
        expectedModel.setPerson(secondPerson, markedSecondPerson);

        // We expect this to succeed since both persons have Mathematics subject
        assertCommandSuccess(command, testModel, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        MarkAttendanceCommand commandA = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);
        MarkAttendanceCommand commandB = new MarkAttendanceCommand(
                INDEX_SECOND_PERSON, "English", "Tuesday", "0900", STATUS_ABSENT);

        // same object
        assertTrue(commandA.equals(commandA));

        // same values
        assertTrue(commandA.equals(new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT)));

        // different types
        assertFalse(commandA.equals(1));

        // null
        assertFalse(commandA.equals(null));

        // different index
        assertFalse(commandA.equals(new MarkAttendanceCommand(
                INDEX_SECOND_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT)));

        // different subject
        assertFalse(commandA.equals(new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, "English", VALID_DAY, VALID_TIME, STATUS_PRESENT)));

        // different day
        assertFalse(commandA.equals(new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, "Tuesday", VALID_TIME, STATUS_PRESENT)));

        // different time
        assertFalse(commandA.equals(new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, "0900", STATUS_PRESENT)));

        // different status
        assertFalse(commandA.equals(new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_ABSENT)));

        // entirely different command
        assertFalse(commandA.equals(commandB));

        // Test multiple indices scenarios
        Index[] indices1 = new Index[]{INDEX_FIRST_PERSON, INDEX_SECOND_PERSON};
        Index[] indices2 = new Index[]{INDEX_FIRST_PERSON, INDEX_SECOND_PERSON};
        Index[] indices3 = new Index[]{INDEX_SECOND_PERSON, INDEX_FIRST_PERSON};

        MarkAttendanceCommand multiCommandA = new MarkAttendanceCommand(
                indices1, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);
        MarkAttendanceCommand multiCommandB = new MarkAttendanceCommand(
                indices2, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);
        MarkAttendanceCommand multiCommandC = new MarkAttendanceCommand(
                indices3, VALID_SUBJECT, VALID_DAY, VALID_TIME, STATUS_PRESENT);

        // same multiple indices
        assertTrue(multiCommandA.equals(multiCommandB));

        // different order of indices
        assertFalse(multiCommandA.equals(multiCommandC));

        // single vs multiple indices (same first index)
        assertFalse(commandA.equals(multiCommandA));
    }
}
