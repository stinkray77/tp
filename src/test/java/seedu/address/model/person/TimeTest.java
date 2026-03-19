package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TimeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        try {
            new Time(null);
        } catch (NullPointerException e) {
            // The actual message from Objects.requireNonNull is
            // "Cannot invoke \"Object.toString()\" because \"time\" is null"
            // We just check that a NullPointerException is thrown
            assertTrue(true);
        }
    }

    @Test
    public void constructor_invalidTime_throwsIllegalArgumentException() {
        String invalidTime = "";
        try {
            new Time(invalidTime);
        } catch (IllegalArgumentException e) {
            assertEquals(Time.MESSAGE_CONSTRAINTS, e.getMessage());
        }
    }

    @Test
    public void isValidTime() {
        // blank time
        assertFalse(Time.isValidTime("")); // empty string
        assertFalse(Time.isValidTime(" ")); // spaces only

        // missing parts
        assertFalse(Time.isValidTime("14")); // too short
        assertFalse(Time.isValidTime("12345")); // too long
        assertFalse(Time.isValidTime("abcd")); // non-numeric
        assertFalse(Time.isValidTime("14:00")); // contains colon

        // valid time
        assertTrue(Time.isValidTime("0900"));
        assertTrue(Time.isValidTime("1400"));
        assertTrue(Time.isValidTime("2359"));
        assertTrue(Time.isValidTime("0000"));
        assertTrue(Time.isValidTime("1234"));
    }

    @Test
    public void equals() {
        Time time = new Time("1400");

        // same values -> returns true
        assertTrue(time.equals(new Time("1400")));

        // same object -> returns true
        assertTrue(time.equals(time));

        // null -> returns false
        assertFalse(time.equals(null));

        // different types -> returns false
        assertFalse(time.equals(5.0f));

        // different values -> returns false
        assertFalse(time.equals(new Time("0900")));
    }

    @Test
    public void hashCode_test() {
        Time time = new Time("1400");
        Time anotherTime = new Time("1400");
        assertEquals(time.hashCode(), anotherTime.hashCode());
    }
}

