package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AttendanceRecordKeyTest {

    @Test
    public void of_validInputs_returnsCorrectKey() {
        assertEquals("Monday 1400 - 2026-04-13 Algebra Lesson 2",
                AttendanceRecordKey.of("Monday", "1400", "2026-04-13 Algebra Lesson 2"));
    }

    @Test
    public void getLessonSlotKey_validKey_returnsSlotPortion() {
        assertEquals("Monday 1400",
                AttendanceRecordKey.getLessonSlotKey("Monday 1400 - 2026-04-13 Algebra Lesson 2"));
    }

    @Test
    public void getLessonSlotKey_noSeparator_returnsEmpty() {
        assertEquals("", AttendanceRecordKey.getLessonSlotKey("Monday 1400"));
    }

    @Test
    public void isValid_validKey_returnsTrue() {
        assertTrue(AttendanceRecordKey.isValid("Monday 1400 - 2026-04-13 Algebra Lesson 2"));
        assertTrue(AttendanceRecordKey.isValid("Friday 0900 - Lesson 1"));
        assertTrue(AttendanceRecordKey.isValid("Wednesday 1600 - Week3"));
    }

    @Test
    public void isValid_noSeparator_returnsFalse() {
        assertFalse(AttendanceRecordKey.isValid("Monday 1400"));
    }

    @Test
    public void isValid_emptyLesson_returnsFalse() {
        assertFalse(AttendanceRecordKey.isValid("Monday 1400 - "));
    }

    @Test
    public void isValid_missingTimePart_returnsFalse() {
        assertFalse(AttendanceRecordKey.isValid("Monday - Lesson 1"));
    }

    @Test
    public void isValid_invalidDay_returnsFalse() {
        assertFalse(AttendanceRecordKey.isValid("Funday 1400 - Lesson 1"));
    }

    @Test
    public void isValid_invalidTime_returnsFalse() {
        assertFalse(AttendanceRecordKey.isValid("Monday 9999 - Lesson 1"));
    }

    @Test
    public void isValid_malformedKey_returnsFalse() {
        assertFalse(AttendanceRecordKey.isValid("!InvalidKey"));
        assertFalse(AttendanceRecordKey.isValid(""));
    }

    @Test
    public void isValid_invalidLessonName_returnsFalse() {
        assertFalse(AttendanceRecordKey.isValid("Monday 1400 - !invalid"));
    }
}
