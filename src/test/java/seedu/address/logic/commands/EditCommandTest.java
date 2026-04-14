package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(
            getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // EP: valid index with a fully populated replacement person
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand =
                new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        // EP: valid index with some editable fields specified
        Index indexLastPerson = Index.fromOneBased(
                model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList()
                .get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB)
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                        .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                        .withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand =
                new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_remarkFieldSpecified_success() {
        Person personToEdit = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit)
                .withRemark(VALID_REMARK_AMY).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withRemark(VALID_REMARK_AMY).build();
        EditCommand editCommand =
                new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        // EP: valid index with no fields specified
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        // EP: valid index in filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList)
                .withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder()
                        .withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(
                model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_preservesAttendanceRecords() {
        // EP: valid edit on a student who already has attendance records
        Person originalFirstPerson = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        Person firstPerson = originalFirstPerson.markAttendance(
                "Mathematics", "Monday 1400 - Lesson 1", AttendanceStatus.PRESENT);
        model.setPerson(originalFirstPerson, firstPerson);

        PersonBuilder personInList = new PersonBuilder(firstPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB)
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        editedPerson = new Person(
                editedPerson.getName(), editedPerson.getEmail(), editedPerson.getAddress(),
                editedPerson.getLessonSlots(),
                editedPerson.getEmergencyContact(), editedPerson.getPaymentStatus(),
                editedPerson.getRemark(), editedPerson.getTags(), firstPerson.getAttendanceRecords());

        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                        .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                        .withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand =
                new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(
                EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(editCommand, model,
                expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        // EP: edited student duplicates another student in unfiltered list
        Person firstPerson = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand =
                new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model,
                EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        // EP: edited student duplicates another student in filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInList = model.getAddressBook().getPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model,
                EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        // EP: invalid out-of-range index in unfiltered list
        Index outOfBoundIndex = Index.fromOneBased(
                model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder()
                        .withName(VALID_NAME_BOB).build();
        EditCommand editCommand =
                new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        // EP: invalid out-of-range index in filtered list
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        assertTrue(outOfBoundIndex.getZeroBased()
                < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder()
                        .withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_editNameOnly_preservesAttendanceRecords() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Person personWithAttendance = firstPerson.markAttendance(
                "Mathematics", "Monday 1400 - Lesson 1",
                seedu.address.model.person.AttendanceStatus.PRESENT);
        model.setPerson(firstPerson, personWithAttendance);

        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        editCommand.execute(model);

        Person edited = model.getFilteredPersonList().get(0);
        assertEquals(personWithAttendance.getAttendanceRecords(),
                edited.getAttendanceRecords());
    }

    @Test
    public void execute_editLessonSlots_prunesRemovedAttendanceRecords() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Person personWithAttendance = firstPerson
                .markAttendance("Mathematics", "Monday 1400 - Lesson 1", AttendanceStatus.PRESENT);
        model.setPerson(firstPerson, personWithAttendance);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withLessonSlots("English", "Friday", "1000")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        editCommand.execute(model);

        Person edited = model.getFilteredPersonList().get(0);
        assertTrue(edited.getAttendanceRecords().isEmpty());
    }

    @Test
    public void execute_editLessonSlots_keepsMatchingAttendanceRecords() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Person personWithAttendance = firstPerson
                .markAttendance("Mathematics", "Monday 1400 - Lesson 1", AttendanceStatus.PRESENT);
        model.setPerson(firstPerson, personWithAttendance);

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withLessonSlots("Mathematics", "Monday", "1400", "English", "Friday", "1000")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        editCommand.execute(model);

        Person edited = model.getFilteredPersonList().get(0);
        assertEquals(AttendanceStatus.PRESENT,
                edited.getAttendanceRecords().get("Mathematics").get("Monday 1400 - Lesson 1"));
    }

    @Test
    public void execute_editLessonSlots_prunesNonMatchingSlotWithinSubject() throws Exception {
        Person firstPerson = model.getFilteredPersonList().get(0);
        Person personWithAttendance = firstPerson
                .markAttendance("Mathematics", "Monday 1400 - Lesson 1", AttendanceStatus.PRESENT);
        model.setPerson(firstPerson, personWithAttendance);

        // Keep Mathematics but change to a different day/time
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withLessonSlots("Mathematics", "Wednesday", "1600")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        editCommand.execute(model);

        Person edited = model.getFilteredPersonList().get(0);
        assertTrue(edited.getAttendanceRecords().isEmpty());
    }

    @Test
    public void equals() {
        // Utility behavior: equality partitions for command identity
        final EditCommand standardCommand =
                new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        EditPersonDescriptor copyDescriptor =
                new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues =
                new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        assertTrue(standardCommand.equals(standardCommand));

        assertFalse(standardCommand.equals(null));

        assertFalse(standardCommand.equals(new ClearCommand()));

        assertFalse(standardCommand.equals(
                new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        assertFalse(standardCommand.equals(
                new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        // Utility behavior: string representation of command state
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor =
                new EditPersonDescriptor();
        EditCommand editCommand =
                new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName()
                + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
