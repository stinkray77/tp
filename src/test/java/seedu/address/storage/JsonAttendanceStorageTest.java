package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;

/**
 * Integration tests for attendance record persistence via {@link JsonAddressBookStorage}.
 */
public class JsonAttendanceStorageTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonAttendanceStorageTest");

    @TempDir
    public Path testFolder;

    // ==================== Round-trip tests ====================

    @Test
    public void saveAndRead_personWithAttendance_roundTrip() throws Exception {
        Person personWithAttendance = ALICE
                .markAttendance("Mathematics", "Lesson 1", AttendanceStatus.PRESENT)
                .markAttendance("Mathematics", "Lesson 2", AttendanceStatus.ABSENT);

        AddressBook original = new AddressBook();
        original.addPerson(personWithAttendance);

        Path filePath = testFolder.resolve("attendance_roundtrip.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original, filePath);

        ReadOnlyAddressBook readBack = storage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        Person restored = readBack.getPersonList().get(0);
        assertEquals(AttendanceStatus.PRESENT,
                restored.getAttendanceRecords().get("Mathematics").get("Lesson 1"));
        assertEquals(AttendanceStatus.ABSENT,
                restored.getAttendanceRecords().get("Mathematics").get("Lesson 2"));
    }

    @Test
    public void saveAndRead_multipleSubjectsAndLessons_allPreserved() throws Exception {
        Person person = ALICE
                .markAttendance("Mathematics", "Algebra Lesson 1", AttendanceStatus.PRESENT)
                .markAttendance("Mathematics", "Algebra Lesson 2", AttendanceStatus.ABSENT)
                .markAttendance("English", "Grammar Lesson 1", AttendanceStatus.EXCUSED);

        AddressBook original = new AddressBook();
        original.addPerson(person);

        Path filePath = testFolder.resolve("multi_subject.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original, filePath);

        ReadOnlyAddressBook readBack = storage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));

        Person restored = readBack.getPersonList().get(0);
        assertEquals(2, restored.getAttendanceRecords().get("Mathematics").size());
        assertEquals(1, restored.getAttendanceRecords().get("English").size());
        assertEquals(AttendanceStatus.EXCUSED,
                restored.getAttendanceRecords().get("English").get("Grammar Lesson 1"));
    }

    @Test
    public void saveAndRead_emptyAttendance_remainsEmpty() throws Exception {
        AddressBook original = new AddressBook();
        original.addPerson(ALICE);

        Path filePath = testFolder.resolve("empty_attendance.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(original, filePath);

        ReadOnlyAddressBook readBack = storage.readAddressBook(filePath).get();
        assertEquals(original, new AddressBook(readBack));
        assertTrue(readBack.getPersonList().get(0).getAttendanceRecords().isEmpty());
    }

    @Test
    public void saveAndRead_overwriteAttendance_updatesCorrectly() throws Exception {
        Person original = ALICE.markAttendance("Mathematics", "Lesson 1", AttendanceStatus.ABSENT);
        AddressBook ab = new AddressBook();
        ab.addPerson(original);

        Path filePath = testFolder.resolve("overwrite_attendance.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(filePath);
        storage.saveAddressBook(ab, filePath);

        // Update attendance and save again
        Person updated = original.markAttendance("Mathematics", "Lesson 1", AttendanceStatus.PRESENT);
        ab.setPerson(original, updated);
        storage.saveAddressBook(ab, filePath);

        ReadOnlyAddressBook readBack = storage.readAddressBook(filePath).get();
        Person restored = readBack.getPersonList().get(0);
        assertEquals(AttendanceStatus.PRESENT,
                restored.getAttendanceRecords().get("Mathematics").get("Lesson 1"));
    }

    // ==================== Backward compatibility ====================

    @Test
    public void readAddressBook_missingAttendanceField_defaultsToEmpty() throws Exception {
        ReadOnlyAddressBook readBack = readAddressBook("personWithoutAttendanceField.json").get();
        Person person = readBack.getPersonList().get(0);
        assertTrue(person.getAttendanceRecords().isEmpty());
    }

    // ==================== Validation ====================

    @Test
    public void readAddressBook_invalidAttendanceStatus_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, ()
                -> readAddressBook("personWithInvalidAttendanceStatus.json"));
    }

    // ==================== Helpers ====================

    private java.util.Optional<ReadOnlyAddressBook> readAddressBook(String fileName) throws Exception {
        Path filePath = TEST_DATA_FOLDER.resolve(fileName);
        return new JsonAddressBookStorage(filePath).readAddressBook(filePath);
    }

    private void saveAddressBook(ReadOnlyAddressBook addressBook, String fileName) {
        try {
            Path filePath = TEST_DATA_FOLDER.resolve(fileName);
            new JsonAddressBookStorage(filePath).saveAddressBook(addressBook, filePath);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }
}
