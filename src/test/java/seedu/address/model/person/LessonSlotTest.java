package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class LessonSlotTest {

    private static final Subject MATH = new Subject("Mathematics");
    private static final Subject ENGLISH = new Subject("English");
    private static final Day MONDAY = new Day("Monday");
    private static final Day TUESDAY = new Day("Tuesday");
    private static final Time TIME_1400 = new Time("1400");
    private static final Time TIME_0900 = new Time("0900");

    @Test
    public void constructor_nullSubject_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonSlot(null, MONDAY, TIME_1400));
    }

    @Test
    public void constructor_nullDay_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonSlot(MATH, null, TIME_1400));
    }

    @Test
    public void constructor_nullTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LessonSlot(MATH, MONDAY, null));
    }

    @Test
    public void getAttendanceKey() {
        LessonSlot slot = new LessonSlot(MATH, MONDAY, TIME_1400);
        assertEquals("Monday 1400", slot.getAttendanceKey());
    }

    @Test
    public void toStringMethod() {
        LessonSlot slot = new LessonSlot(MATH, MONDAY, TIME_1400);
        assertEquals("Mathematics: Monday 1400", slot.toString());
    }

    @Test
    public void equals() {
        LessonSlot slot = new LessonSlot(MATH, MONDAY, TIME_1400);

        // same object
        assertTrue(slot.equals(slot));

        // same values
        assertTrue(slot.equals(new LessonSlot(MATH, MONDAY, TIME_1400)));

        // null
        assertFalse(slot.equals(null));

        // different type
        assertFalse(slot.equals(5));

        // different subject
        assertFalse(slot.equals(new LessonSlot(ENGLISH, MONDAY, TIME_1400)));

        // different day
        assertFalse(slot.equals(new LessonSlot(MATH, TUESDAY, TIME_1400)));

        // different time
        assertFalse(slot.equals(new LessonSlot(MATH, MONDAY, TIME_0900)));
    }

    @Test
    public void hashCodeTest() {
        LessonSlot slot1 = new LessonSlot(MATH, MONDAY, TIME_1400);
        LessonSlot slot2 = new LessonSlot(MATH, MONDAY, TIME_1400);
        assertEquals(slot1.hashCode(), slot2.hashCode());

        LessonSlot slot3 = new LessonSlot(ENGLISH, MONDAY, TIME_1400);
        assertNotEquals(slot1.hashCode(), slot3.hashCode());
    }
}
