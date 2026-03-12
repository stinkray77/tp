package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DayTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Day(null));
    }

    @Test
    public void constructor_invalidDay_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Day(""));
        assertThrows(IllegalArgumentException.class, () -> new Day(" "));
        assertThrows(IllegalArgumentException.class, () -> new Day("Monday@"));
    }

    @Test
    public void isValidDay() {
        assertFalse(Day.isValidDay(""));
        assertFalse(Day.isValidDay(" "));
        assertFalse(Day.isValidDay("Monday!"));
        assertFalse(Day.isValidDay("@Wednesday"));

        assertTrue(Day.isValidDay("Monday"));
        assertTrue(Day.isValidDay("Wednesday"));
        assertTrue(Day.isValidDay("Fri"));
    }

    @Test
    public void toStringMethod() {
        Day day = new Day("Monday");
        assertEquals("Monday", day.toString());
    }

    @Test
    public void equals() {
        Day day = new Day("Monday");

        assertTrue(day.equals(day));
        assertTrue(day.equals(new Day("Monday")));

        assertFalse(day.equals(null));
        assertFalse(day.equals("Monday"));
        assertFalse(day.equals(new Day("Tuesday")));
    }

    @Test
    public void hashCodeMethod() {
        Day day = new Day("Monday");
        assertEquals(day.hashCode(), new Day("Monday").hashCode());
    }
}
