package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PAYMENT_STATUS_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION =
            new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION =
            new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(
                        temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(
                        temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage =
                new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        // Update the expected exception message to match new DeleteCommand behavior
        String expectedMessage = MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
        assertCommandException(deleteCommand, expectedMessage);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand,
                ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_readOnlyCommandWithStorageFailure_success() throws Exception {
        logic = new LogicManager(model, getStorageThatFailsOnSave(DUMMY_IO_EXCEPTION));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        CommandResult result = logic.execute(ListCommand.COMMAND_WORD);

        assertEquals(ListCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_helpCommandWithStorageFailure_success() throws Exception {
        logic = new LogicManager(model, getStorageThatFailsOnSave(DUMMY_AD_EXCEPTION));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        CommandResult result = logic.execute(HelpCommand.COMMAND_WORD);

        assertEquals(HelpCommand.SHOWING_HELP_MESSAGE, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(
                DUMMY_IO_EXCEPTION, String.format(
                        LogicManager.FILE_OPS_ERROR_FORMAT,
                        DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(
                DUMMY_AD_EXCEPTION, String.format(
                        LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT,
                        DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void getAddressBook_returnsModelAddressBook() {
        assertEquals(model.getAddressBook(), logic.getAddressBook());
    }

    @Test
    public void getAddressBookFilePath_returnsModelFilePath() {
        assertEquals(model.getAddressBookFilePath(), logic.getAddressBookFilePath());
    }

    @Test
    public void getGuiSettings_returnsModelGuiSettings() {
        assertEquals(model.getGuiSettings(), logic.getGuiSettings());
    }

    @Test
    public void setGuiSettings_updatesModelGuiSettings() {
        seedu.address.commons.core.GuiSettings newSettings =
                new seedu.address.commons.core.GuiSettings(800, 600, 0, 0);
        logic.setGuiSettings(newSettings);
        assertEquals(newSettings, model.getGuiSettings());
    }

    private void assertCommandSuccess(String inputCommand,
            String expectedMessage, Model expectedModel)
            throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    private void assertParseException(String inputCommand,
            String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class,
                expectedMessage);
    }

    private void assertCommandException(String inputCommand,
            String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class,
                expectedMessage);
    }

    private void assertCommandFailure(String inputCommand,
            Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(
                model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException,
                expectedMessage, expectedModel);
    }

    private void assertCommandFailure(String inputCommand,
            Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, ()
                -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     */
    private void assertCommandFailureForExceptionFromStorage(
            IOException e, String expectedMessage) {
        logic = new LogicManager(model, getStorageThatFailsOnSave(e));

        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + EMERGENCY_CONTACT_DESC_AMY + PAYMENT_STATUS_DESC_AMY;
        Person expectedPerson = new PersonBuilder(AMY).withTags()
                .withLessonSlots().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class,
                expectedMessage, expectedModel);
    }

    private StorageManager getStorageThatFailsOnSave(IOException e) {
        Path prefPath =
                temporaryFolder.resolve("ExceptionUserPrefs.json");

        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(prefPath) {
                    @Override
                    public void saveAddressBook(
                            ReadOnlyAddressBook addressBook, Path filePath)
                            throws IOException {
                        throw e;
                    }
                };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve(
                        "ExceptionUserPrefs.json"));
        return new StorageManager(addressBookStorage, userPrefsStorage);
    }
}
