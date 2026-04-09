package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code PaymentStatus} matches any of the given statuses.
 */
public class PaymentStatusMatchesPredicate implements Predicate<Person> {
    private final List<String> statuses;

    public PaymentStatusMatchesPredicate(String status) {
        this(List.of(status));
    }

    public PaymentStatusMatchesPredicate(List<String> statuses) {
        this.statuses = List.copyOf(statuses);
    }

    @Override
    public boolean test(Person person) {
        return statuses.stream()
                .anyMatch(status -> person.getPaymentStatus().value.equalsIgnoreCase(status));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PaymentStatusMatchesPredicate)) {
            return false;
        }

        PaymentStatusMatchesPredicate otherPaymentStatusMatchesPredicate = (PaymentStatusMatchesPredicate) other;
        return statuses.equals(otherPaymentStatusMatchesPredicate.statuses);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("statuses", statuses).toString();
    }
}
