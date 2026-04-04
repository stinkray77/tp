package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Day} matches any of the days given.
 */
public class DayMatchesPredicate implements Predicate<Person> {
    private final List<String> days;

    public DayMatchesPredicate(List<String> days) {
        this.days = days;
    }

    @Override
    public boolean test(Person person) {
        return days.stream()
                .anyMatch(day -> person.getDays().stream()
                        .anyMatch(personDay -> personDay.dayName.equalsIgnoreCase(day)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DayMatchesPredicate)) {
            return false;
        }

        DayMatchesPredicate otherDayMatchesPredicate = (DayMatchesPredicate) other;
        return days.equals(otherDayMatchesPredicate.days);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("days", days).toString();
    }
}
