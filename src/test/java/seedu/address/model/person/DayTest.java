package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DayTest {

    @Test
    public void isValidDay() {
        // blank day
        assertFalse(Day.isValidDay("")); // empty string
        assertFalse(Day.isValidDay(" ")); // spaces only

        // missing parts
        assertFalse(Day.isValidDay("Monday!")); // contains special character

        // valid day
        assertTrue(Day.isValidDay("Monday"));
        assertTrue(Day.isValidDay("Tuesday"));
        assertTrue(Day.isValidDay("Wednesday"));
        assertTrue(Day.isValidDay("Thursday"));
        assertTrue(Day.isValidDay("Friday"));
        assertTrue(Day.isValidDay("Saturday"));
        assertTrue(Day.isValidDay("Sunday"));
        assertTrue(Day.isValidDay("Mon"));
        assertTrue(Day.isValidDay("Tue"));
        assertTrue(Day.isValidDay("Wed"));
        assertTrue(Day.isValidDay("Thu"));
        assertTrue(Day.isValidDay("Fri"));
        assertTrue(Day.isValidDay("Sat"));
        assertTrue(Day.isValidDay("Sun"));
        assertTrue(Day.isValidDay("monday"));
        assertTrue(Day.isValidDay("tuesday"));
        assertTrue(Day.isValidDay("wednesday"));
        assertTrue(Day.isValidDay("thursday"));
        assertTrue(Day.isValidDay("friday"));
        assertTrue(Day.isValidDay("saturday"));
        assertTrue(Day.isValidDay("sunday"));
        assertTrue(Day.isValidDay("mon"));
        assertTrue(Day.isValidDay("tue"));
        assertTrue(Day.isValidDay("wed"));
        assertTrue(Day.isValidDay("thu"));
        assertTrue(Day.isValidDay("fri"));
        assertTrue(Day.isValidDay("sat"));
        assertTrue(Day.isValidDay("sun"));
    }

    @Test
    public void equals() {
        Day day = new Day("Monday");

        // same values -> returns true
        assertTrue(day.equals(new Day("Monday")));

        // same object -> returns true
        assertTrue(day.equals(day));

        // null -> returns false
        assertFalse(day.equals(null));

        // different types -> returns false
        assertFalse(day.equals(5.0f));

        // different values -> returns false
        assertFalse(day.equals(new Day("Tuesday")));
    }

    @Test
    public void hashCode_test() {
        Day day = new Day("Monday");
        Day anotherDay = new Day("Monday");
        assertEquals(day.hashCode(), anotherDay.hashCode());
    }
}

