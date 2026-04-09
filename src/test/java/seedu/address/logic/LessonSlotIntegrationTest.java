package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

/**
 * Integration tests for the LessonSlot model exercised through
 * {@code Logic.execute()}. Covers add, edit, delete, remark, mark,
 * markattendance, and listattendance commands end-to-end.
 */
public class LessonSlotIntegrationTest {

    @TempDir
    public Path tempFolder;

    private Logic logic;
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage abStorage = new JsonAddressBookStorage(
                tempFolder.resolve("ab.json"));
        JsonUserPrefsStorage upStorage = new JsonUserPrefsStorage(
                tempFolder.resolve("prefs.json"));
        logic = new LogicManager(model,
                new StorageManager(abStorage, upStorage));
    }

    // ================================================================
    // Add with lesson slots
    // ================================================================

    @Test
    public void execute_addWithLessonSlots_success() throws Exception {
        int before = logic.getFilteredPersonList().size();
        CommandResult result = logic.execute(
                "add n/Jane Doe e/jane@example.com a/123 Elm St "
                + "s/Mathematics d/Monday ti/1400 "
                + "s/English d/Wednesday ti/0900 "
                + "ec/91234567 ps/Due");
        assertTrue(result.getFeedbackToUser().toLowerCase()
                .contains("new student added"));
        assertEquals(before + 1,
                logic.getFilteredPersonList().size());

        Person added = logic.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName.equals("Jane Doe"))
                .findFirst().orElseThrow();
        assertEquals(2, added.getLessonSlots().size());
        assertEquals("Mathematics", added.getLessonSlots().get(0)
                .getSubject().subjectName);
        assertEquals("Monday", added.getLessonSlots().get(0)
                .getDay().dayName);
        assertEquals("1400", added.getLessonSlots().get(0)
                .getTime().timeValue);
    }

    @Test
    public void execute_addNoLessonSlots_success() throws Exception {
        int before = logic.getFilteredPersonList().size();
        logic.execute(
                "add n/No Lessons e/nolesson@test.com a/456 Oak Ave "
                + "ec/88887777 ps/Paid");
        assertEquals(before + 1,
                logic.getFilteredPersonList().size());
        Person added = logic.getFilteredPersonList().stream()
                .filter(p -> p.getName().fullName
                        .equals("No Lessons"))
                .findFirst().orElseThrow();
        assertTrue(added.getLessonSlots().isEmpty());
    }

    @Test
    public void execute_addMismatchedCounts_fails() {
        int before = logic.getFilteredPersonList().size();
        assertThrowsParseOrCommand(() -> logic.execute(
                "add n/Mismatch e/m@t.com a/Addr "
                + "s/Math s/English d/Monday ti/1400 "
                + "ec/91234567 ps/Due"));
        assertEquals(before, logic.getFilteredPersonList().size());
    }

    @Test
    public void execute_addIncompleteSlot_fails() {
        int before = logic.getFilteredPersonList().size();
        assertThrowsParseOrCommand(() -> logic.execute(
                "add n/Incomplete e/i@t.com a/Addr "
                + "s/Math ec/91234567 ps/Due"));
        assertEquals(before, logic.getFilteredPersonList().size());
    }

    @Test
    public void execute_addInvalidDay_fails() {
        int before = logic.getFilteredPersonList().size();
        assertThrowsParseOrCommand(() -> logic.execute(
                "add n/BadDay e/b@t.com a/Addr "
                + "s/Math d/Funday ti/1400 ec/91234567 ps/Due"));
        assertEquals(before, logic.getFilteredPersonList().size());
    }

    @Test
    public void execute_addInvalidTime_fails() {
        int before = logic.getFilteredPersonList().size();
        assertThrowsParseOrCommand(() -> logic.execute(
                "add n/BadTime e/bt@t.com a/Addr "
                + "s/Math d/Monday ti/9999 ec/91234567 ps/Due"));
        assertEquals(before, logic.getFilteredPersonList().size());
    }

    // ================================================================
    // Edit lesson slots
    // ================================================================

    @Test
    public void execute_editChangeLessonSlots_success() throws Exception {
        // Alice (index 1) has Mathematics/Monday/1400
        logic.execute("edit 1 s/English d/Friday ti/1000");
        Person edited = logic.getFilteredPersonList().get(0);
        assertEquals(1, edited.getLessonSlots().size());
        assertEquals("English", edited.getLessonSlots().get(0)
                .getSubject().subjectName);
        assertEquals("Friday", edited.getLessonSlots().get(0)
                .getDay().dayName);
        assertEquals("1000", edited.getLessonSlots().get(0)
                .getTime().timeValue);
    }

    @Test
    public void execute_editMultipleSlots_success() throws Exception {
        logic.execute(
                "edit 1 s/Math s/Science d/Monday d/Tuesday "
                + "ti/1400 ti/1500");
        Person edited = logic.getFilteredPersonList().get(0);
        assertEquals(2, edited.getLessonSlots().size());
    }

    @Test
    public void execute_editClearSlots_success() throws Exception {
        logic.execute("edit 1 s/ d/ ti/");
        Person edited = logic.getFilteredPersonList().get(0);
        assertTrue(edited.getLessonSlots().isEmpty());
    }

    @Test
    public void execute_editLessonSlots_removesStaleAttendanceRecords() throws Exception {
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Present");
        logic.execute("edit 1 s/English d/Friday ti/1000");

        Person edited = logic.getFilteredPersonList().get(0);
        assertTrue(edited.getAttendanceRecords().isEmpty());
    }

    @Test
    public void execute_editPartialSlotPrefixes_fails() {
        // Only subject without day/time — should fail
        assertThrowsParseOrCommand(() -> logic.execute(
                "edit 1 s/Physics"));
    }

    // ================================================================
    // Mark attendance
    // ================================================================

    @Test
    public void execute_markAttendancePresent_success() throws Exception {
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Present");
        Person alice = logic.getFilteredPersonList().get(0);
        assertEquals(AttendanceStatus.PRESENT,
                alice.getAttendanceRecords()
                        .get("Mathematics").get("Monday 1400"));
    }

    @Test
    public void execute_markAttendanceAbsent_success() throws Exception {
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Absent");
        Person alice = logic.getFilteredPersonList().get(0);
        assertEquals(AttendanceStatus.ABSENT,
                alice.getAttendanceRecords()
                        .get("Mathematics").get("Monday 1400"));
    }

    @Test
    public void execute_markAttendanceExcused_success() throws Exception {
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Excused");
        Person alice = logic.getFilteredPersonList().get(0);
        assertEquals(AttendanceStatus.EXCUSED,
                alice.getAttendanceRecords()
                        .get("Mathematics").get("Monday 1400"));
    }

    @Test
    public void execute_markAttendanceCaseInsensitive_success()
            throws Exception {
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/present");
        Person alice = logic.getFilteredPersonList().get(0);
        assertEquals(AttendanceStatus.PRESENT,
                alice.getAttendanceRecords()
                        .get("Mathematics").get("Monday 1400"));
    }

    @Test
    public void execute_markAttendanceOverwrite_success()
            throws Exception {
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Present");
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Absent");
        Person alice = logic.getFilteredPersonList().get(0);
        assertEquals(AttendanceStatus.ABSENT,
                alice.getAttendanceRecords()
                        .get("Mathematics").get("Monday 1400"));
    }

    @Test
    public void execute_markAttendanceInvalidStatus_fails() {
        assertThrowsParseOrCommand(() -> logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Maybe"));
        Person alice = logic.getFilteredPersonList().get(0);
        assertTrue(alice.getAttendanceRecords().isEmpty());
    }

    @Test
    public void execute_markAttendanceInvalidDay_fails() {
        assertThrowsParseOrCommand(() -> logic.execute(
                "markattendance 1 s/Mathematics d/Funday "
                + "ti/1400 st/Present"));
        Person alice = logic.getFilteredPersonList().get(0);
        assertTrue(alice.getAttendanceRecords().isEmpty());
    }

    @Test
    public void execute_markAttendanceInvalidTime_fails() {
        assertThrowsParseOrCommand(() -> logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/9999 st/Present"));
        Person alice = logic.getFilteredPersonList().get(0);
        assertTrue(alice.getAttendanceRecords().isEmpty());
    }

    @Test
    public void execute_markAttendanceMissingFields_fails() {
        assertThrowsParseOrCommand(() -> logic.execute(
                "markattendance 1 s/Mathematics d/Monday ti/1400"));
        Person alice = logic.getFilteredPersonList().get(0);
        assertTrue(alice.getAttendanceRecords().isEmpty());
    }

    @Test
    public void execute_markAttendanceInvalidIndex_fails() {
        assertThrowsParseOrCommand(() -> logic.execute(
                "markattendance 0 s/Mathematics d/Monday "
                + "ti/1400 st/Present"));
    }

    // ================================================================
    // List attendance
    // ================================================================

    @Test
    public void execute_listAttendanceWithRecords_showsRecords()
            throws Exception {
        logic.execute(
                "markattendance 1 s/Mathematics d/Monday "
                + "ti/1400 st/Present");
        CommandResult result = logic.execute("listattendance 1");
        String feedback = result.getFeedbackToUser();
        assertTrue(feedback.contains("Mathematics")
                || feedback.contains("Monday")
                || feedback.contains("1400"));
    }

    @Test
    public void execute_listAttendanceNoRecords_noError()
            throws Exception {
        // Carl (index 3) has no attendance
        CommandResult result = logic.execute("listattendance 3");
        assertFalse(result.getFeedbackToUser().toLowerCase()
                .contains("invalid command"));
    }

    @Test
    public void execute_listAttendanceInvalidIndex_fails() {
        assertThrowsParseOrCommand(() -> logic.execute(
                "listattendance 0"));
    }

    // ================================================================
    // Find by subject / day / payment status
    // ================================================================

    @Test
    public void execute_findBySubject_filtersCorrectly()
            throws Exception {
        logic.execute("find s/Mathematics");
        assertTrue(logic.getFilteredPersonList().size() >= 1);
        assertTrue(logic.getFilteredPersonList().stream()
                .allMatch(p -> p.getSubjects().stream()
                        .anyMatch(s -> s.subjectName.toLowerCase()
                                .contains("math"))));
    }

    @Test
    public void execute_findByDay_filtersCorrectly()
            throws Exception {
        logic.execute("find d/Monday");
        assertTrue(logic.getFilteredPersonList().size() >= 1);
        assertTrue(logic.getFilteredPersonList().stream()
                .allMatch(p -> p.getDays().stream()
                        .anyMatch(d -> d.dayName.toLowerCase()
                                .contains("monday"))));
    }

    @Test
    public void execute_findByPaymentStatus_filtersCorrectly()
            throws Exception {
        logic.execute("find ps/Paid");
        assertTrue(logic.getFilteredPersonList().size() >= 1);
        assertTrue(logic.getFilteredPersonList().stream()
                .allMatch(p -> p.getPaymentStatus().value
                        .equals("Paid")));
    }

    // ================================================================
    // Remark and mark preserve lesson slots
    // ================================================================

    @Test
    public void execute_remarkPreservesLessonSlots() throws Exception {
        int slotsBefore = logic.getFilteredPersonList().get(0)
                .getLessonSlots().size();
        logic.execute("remark 1 r/Test remark");
        Person after = logic.getFilteredPersonList().get(0);
        assertEquals(slotsBefore, after.getLessonSlots().size());
        assertEquals("Test remark", after.getRemark().value);
    }

    @Test
    public void execute_markPaymentPreservesLessonSlots()
            throws Exception {
        int slotsBefore = logic.getFilteredPersonList().get(0)
                .getLessonSlots().size();
        logic.execute("mark 1 ps/Overdue");
        Person after = logic.getFilteredPersonList().get(0);
        assertEquals(slotsBefore, after.getLessonSlots().size());
        assertEquals("Overdue", after.getPaymentStatus().value);
    }

    // ================================================================
    // Delete
    // ================================================================

    @Test
    public void execute_deletePerson_removedFromList() throws Exception {
        int before = logic.getFilteredPersonList().size();
        String name = logic.getFilteredPersonList().get(0)
                .getName().fullName;
        logic.execute("delete 1");
        assertEquals(before - 1,
                logic.getFilteredPersonList().size());
        assertTrue(logic.getFilteredPersonList().stream()
                .noneMatch(p -> p.getName().fullName.equals(name)));
    }

    // ================================================================
    // Case-insensitive commands
    // ================================================================

    @Test
    public void execute_caseInsensitiveAdd_works() throws Exception {
        int before = logic.getFilteredPersonList().size();
        logic.execute(
                "ADD n/CaseTest e/case@test.com a/Addr "
                + "s/Math d/Monday ti/1400 ec/91234567 ps/Due");
        assertEquals(before + 1,
                logic.getFilteredPersonList().size());
    }

    // ================================================================
    // End-to-end flow
    // ================================================================

    @Test
    public void execute_endToEnd_addMarkEditDeleteFlow()
            throws Exception {
        int before = logic.getFilteredPersonList().size();

        // Add student with lesson slot
        logic.execute(
                "add n/E2E Student e/e2e@test.com a/E2E Street "
                + "s/Physics d/Thursday ti/1600 "
                + "ec/99990000 ps/Due t/test");
        assertEquals(before + 1,
                logic.getFilteredPersonList().size());

        int idx = -1;
        for (int i = 0; i < logic.getFilteredPersonList().size(); i++) {
            if (logic.getFilteredPersonList().get(i).getName()
                    .fullName.equals("E2E Student")) {
                idx = i;
                break;
            }
        }
        assertTrue(idx >= 0);
        int displayIdx = idx + 1;

        // Verify lesson slot
        Person student = logic.getFilteredPersonList().get(idx);
        assertEquals(1, student.getLessonSlots().size());
        assertEquals("Physics", student.getLessonSlots().get(0)
                .getSubject().subjectName);

        // Mark attendance
        logic.execute("markattendance " + displayIdx
                + " s/Physics d/Thursday ti/1600 st/Present");
        student = logic.getFilteredPersonList().get(idx);
        assertEquals(AttendanceStatus.PRESENT,
                student.getAttendanceRecords()
                        .get("Physics").get("Thursday 1600"));

        // List attendance
        CommandResult listResult = logic.execute(
                "listattendance " + displayIdx);
        assertTrue(listResult.getFeedbackToUser()
                .contains("Physics"));

        // Edit lesson slots
        logic.execute("edit " + displayIdx
                + " s/Chemistry d/Friday ti/0900");
        student = logic.getFilteredPersonList().get(idx);
        assertEquals("Chemistry", student.getLessonSlots().get(0)
                .getSubject().subjectName);
        assertTrue(student.getAttendanceRecords().isEmpty());

        // Remark — slots preserved
        logic.execute("remark " + displayIdx + " r/Great progress");
        student = logic.getFilteredPersonList().get(idx);
        assertEquals(1, student.getLessonSlots().size());

        // Mark payment — slots preserved
        logic.execute("mark " + displayIdx + " ps/Paid");
        student = logic.getFilteredPersonList().get(idx);
        assertEquals(1, student.getLessonSlots().size());
        assertEquals("Paid", student.getPaymentStatus().value);

        // Delete
        logic.execute("delete " + displayIdx);
        assertTrue(logic.getFilteredPersonList().stream()
                .noneMatch(p -> p.getName().fullName
                        .equals("E2E Student")));
    }

    /**
     * Asserts that the given executable throws either a
     * {@code ParseException} or {@code CommandException}.
     */
    private void assertThrowsParseOrCommand(
            org.junit.jupiter.api.function.Executable executable) {
        try {
            executable.execute();
            throw new AssertionError("Expected exception was not thrown");
        } catch (ParseException | CommandException e) {
            // expected
        } catch (Throwable t) {
            throw new AssertionError(
                    "Unexpected exception type: " + t.getClass(), t);
        }
    }
}
