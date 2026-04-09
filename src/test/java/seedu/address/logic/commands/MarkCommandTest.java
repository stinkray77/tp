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
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for MarkCommand.
 */
public class MarkCommandTest {

    private static final PaymentStatus STATUS_PAID = new PaymentStatus("Paid");
    private static final PaymentStatus STATUS_OVERDUE = new PaymentStatus("Overdue");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        // EP: valid index in unfiltered list with valid payment status
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = createMarkedPerson(personToMark, STATUS_PAID);

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, STATUS_PAID);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_SUCCESS,
                personToMark.getName(), STATUS_PAID.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        // EP: valid index in filtered list with valid payment status
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = createMarkedPerson(personToMark, STATUS_OVERDUE);

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, STATUS_OVERDUE);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_SUCCESS,
                personToMark.getName(), STATUS_OVERDUE.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        // EP: invalid out-of-range index in unfiltered list
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex, STATUS_PAID);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        // EP: invalid out-of-range index in filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MarkCommand markCommand = new MarkCommand(outOfBoundIndex, STATUS_PAID);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexUnfilteredList_preservesAttendanceRecords() {
        // EP: valid update on a student who already has attendance records
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased())
                .markAttendance("Mathematics", "Algebra Lesson 1", AttendanceStatus.PRESENT);
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToMark);

        Person markedPerson = createMarkedPerson(personToMark, STATUS_PAID);
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, STATUS_PAID);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_SUCCESS,
                personToMark.getName(), STATUS_PAID.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sameStatusUnfilteredList_success() {
        // EP: valid index where the requested payment status matches the current status
        Person personToMark = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        PaymentStatus currentStatus = personToMark.getPaymentStatus();
        Person markedPerson = createMarkedPerson(personToMark, currentStatus);

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, currentStatus);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_SUCCESS,
                personToMark.getName(), currentStatus.value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        // Utility behavior: equality partitions for command identity
        MarkCommand markFirstCommand = new MarkCommand(INDEX_FIRST_PERSON, STATUS_PAID);
        MarkCommand markSecondCommand = new MarkCommand(INDEX_SECOND_PERSON, STATUS_OVERDUE);

        assertTrue(markFirstCommand.equals(markFirstCommand));
        assertTrue(markFirstCommand.equals(new MarkCommand(INDEX_FIRST_PERSON, STATUS_PAID)));
        assertFalse(markFirstCommand.equals(1));
        assertFalse(markFirstCommand.equals(null));
        assertFalse(markFirstCommand.equals(markSecondCommand));
        assertFalse(markFirstCommand.equals(new MarkCommand(INDEX_FIRST_PERSON, STATUS_OVERDUE)));
    }

    private static Person createMarkedPerson(Person personToMark, PaymentStatus newStatus) {
        return new Person(
                personToMark.getName(),
                personToMark.getEmail(),
                personToMark.getAddress(),
                personToMark.getLessonSlots(),
                personToMark.getEmergencyContact(),
                newStatus,
                personToMark.getRemark(),
                personToMark.getTags(),
                personToMark.getAttendanceRecords()
        );
    }
}
