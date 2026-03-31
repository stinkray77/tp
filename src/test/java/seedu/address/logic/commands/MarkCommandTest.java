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
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for MarkCommand.
 */
public class MarkCommandTest {

    private static final PaymentStatus PAYMENT_STATUS_PAID = new PaymentStatus("Paid");
    private static final PaymentStatus PAYMENT_STATUS_DUE = new PaymentStatus("Due");
    private static final PaymentStatus PAYMENT_STATUS_OVERDUE = new PaymentStatus("Overdue");

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_markPaidUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = new Person(
                firstPerson.getName(), firstPerson.getEmail(),
                firstPerson.getAddress(), firstPerson.getSubjects(),
                firstPerson.getDays(), firstPerson.getTimes(),
                firstPerson.getEmergencyContact(),
                PAYMENT_STATUS_PAID, firstPerson.getRemark(), firstPerson.getTags());

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, PAYMENT_STATUS_PAID);

        String expectedMessage = String.format(
                MarkCommand.MESSAGE_MARK_SUCCESS, markedPerson.getName(), PAYMENT_STATUS_PAID);

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_markOverdueUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = new Person(
                firstPerson.getName(), firstPerson.getEmail(),
                firstPerson.getAddress(), firstPerson.getSubjects(),
                firstPerson.getDays(), firstPerson.getTimes(),
                firstPerson.getEmergencyContact(),
                PAYMENT_STATUS_OVERDUE, firstPerson.getRemark(), firstPerson.getTags());

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, PAYMENT_STATUS_OVERDUE);

        String expectedMessage = String.format(
                MarkCommand.MESSAGE_MARK_SUCCESS, markedPerson.getName(), PAYMENT_STATUS_OVERDUE);

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_markDueFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = new Person(
                personInFilteredList.getName(), personInFilteredList.getEmail(),
                personInFilteredList.getAddress(), personInFilteredList.getSubjects(),
                personInFilteredList.getDays(), personInFilteredList.getTimes(),
                personInFilteredList.getEmergencyContact(),
                PAYMENT_STATUS_DUE, personInFilteredList.getRemark(), personInFilteredList.getTags());

        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, PAYMENT_STATUS_DUE);

        String expectedMessage = String.format(
                MarkCommand.MESSAGE_MARK_SUCCESS, markedPerson.getName(), PAYMENT_STATUS_DUE);

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInFilteredList, markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex, PAYMENT_STATUS_PAID);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MarkCommand markCommand = new MarkCommand(outOfBoundIndex, PAYMENT_STATUS_PAID);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final MarkCommand standardCommand = new MarkCommand(INDEX_FIRST_PERSON, PAYMENT_STATUS_PAID);

        // same values -> returns true
        MarkCommand commandWithSameValues = new MarkCommand(INDEX_FIRST_PERSON, PAYMENT_STATUS_PAID);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new MarkCommand(INDEX_SECOND_PERSON, PAYMENT_STATUS_PAID)));

        // different payment status -> returns false
        assertFalse(standardCommand.equals(new MarkCommand(INDEX_FIRST_PERSON, PAYMENT_STATUS_DUE)));
    }
}
