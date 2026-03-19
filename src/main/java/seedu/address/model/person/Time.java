package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {
    public static final String MESSAGE_CONSTRAINTS =
            "Times should only contain numeric characters, and should not be blank";
    public static final String VALIDATION_REGEX = "[0-9]{4}";
    public final String timeValue;

    /**
     * Constructs a {@code Time}.
     *
     * @param time A valid time value.
     */
    public Time(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        timeValue = time;
    }

    /**
     * Returns true if a given string is a valid time value.
     */
    public static boolean isValidTime(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return timeValue;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Time)) {
            return false;
        }

        Time otherTime = (Time) other;
        return timeValue.equals(otherTime.timeValue);
    }

    @Override
    public int hashCode() {
        return timeValue.hashCode();
    }
}

