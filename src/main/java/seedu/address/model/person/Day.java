package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a day of the week for a student's lesson.
 * Guarantees: immutable; is valid as declared in {@link #isValidDay(String)}
 */
public class Day {

    public static final String MESSAGE_CONSTRAINTS =
            "Days should only contain alphanumeric characters and spaces, and should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String dayName;

    /**
     * Constructs a {@code Day}.
     *
     * @param day A valid day name.
     */
    public Day(String day) {
        requireNonNull(day);
        checkArgument(isValidDay(day), MESSAGE_CONSTRAINTS);
        dayName = day;
    }

    /**
     * Returns true if a given string is a valid day name.
     */
    public static boolean isValidDay(String test) {
        return test != null && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return dayName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Day)) {
            return false;
        }

        Day otherDay = (Day) other;
        return dayName.equals(otherDay.dayName);
    }

    @Override
    public int hashCode() {
        return dayName.hashCode();
    }
}
