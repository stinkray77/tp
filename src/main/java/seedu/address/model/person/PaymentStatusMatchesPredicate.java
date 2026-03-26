package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code PaymentStatus} matches the given status.
 */
public class PaymentStatusMatchesPredicate implements Predicate<Person> {
    private final String status;

    public PaymentStatusMatchesPredicate(String status) {
        this.status = status;
    }

    @Override
    public boolean test(Person person) {
        return person.getPaymentStatus().value.equalsIgnoreCase(status);
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
        return status.equals(otherPaymentStatusMatchesPredicate.status);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("status", status).toString();
    }
}
