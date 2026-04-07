package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Day in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDay(String)}
 */
public class Day {
    public static final String MESSAGE_CONSTRAINTS =
            "Days should be valid day names (Monday/Mon, Tuesday/Tue, Wednesday/Wed, "
            + "Thursday/Thu, Friday/Fri, Saturday/Sat, Sunday/Sun) in any case";
    public static final String VALIDATION_REGEX = "^(?i)(Monday|Mon|Tuesday|Tue|Wednesday|Wed|Thursday|Thu|"
            + "Friday|Fri|Saturday|Sat|Sunday|Sun)$";
    public final String dayName;

    /**
     * Constructs a {@code Day}.
     *
     * @param day A valid day name.
     */
    public Day(String day) {
        requireNonNull(day);
        checkArgument(isValidDay(day), MESSAGE_CONSTRAINTS);
        dayName = normalize(day);
    }

    /**
     * Normalizes day abbreviations to their full names so that "Mon" and "Monday" are treated
     * as the same day and deduplicate correctly in a Set.
     */
    private static String normalize(String day) {
        switch (day.toLowerCase()) {
        case "mon": return "Monday";
        case "tue": return "Tuesday";
        case "wed": return "Wednesday";
        case "thu": return "Thursday";
        case "fri": return "Friday";
        case "sat": return "Saturday";
        case "sun": return "Sunday";
        default: return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
        }
    }

    /**
     * Returns true if a given string is a valid day name.
     */
    public static boolean isValidDay(String test) {
        return test.matches(VALIDATION_REGEX);
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

        // instanceof handles nulls
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
