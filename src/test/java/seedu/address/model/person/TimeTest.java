package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Time(null));
    }

    @Test
    public void constructor_invalidTime_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Time(""));
        assertThrows(IllegalArgumentException.class, () -> new Time("14"));
        assertThrows(IllegalArgumentException.class, () -> new Time("abcd"));
        assertThrows(IllegalArgumentException.class, () -> new Time("12345"));
    }

    @Test
    public void isValidTime() {
        assertFalse(Time.isValidTime(""));
        assertFalse(Time.isValidTime("14"));
        assertFalse(Time.isValidTime("12345"));
        assertFalse(Time.isValidTime("abcd"));

        assertTrue(Time.isValidTime("1400"));
        assertTrue(Time.isValidTime("0900"));
        assertTrue(Time.isValidTime("0000"));
        assertTrue(Time.isValidTime("2359"));
    }

    @Test
    public void toStringMethod() {
        Time time = new Time("1400");
        assertEquals("1400", time.toString());
    }

    @Test
    public void equals() {
        Time time = new Time("1400");

        assertTrue(time.equals(time));
        assertTrue(time.equals(new Time("1400")));

        assertFalse(time.equals(null));
        assertFalse(time.equals("1400"));
        assertFalse(time.equals(new Time("0900")));
    }

    @Test
    public void hashCodeMethod() {
        Time time = new Time("1400");
        assertEquals(time.hashCode(), new Time("1400").hashCode());
    }
}
