package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a student's payment status.
 * Guarantees: immutable; is valid as declared in {@link #isValidPaymentStatus(String)}
 */
public class PaymentStatus {

    public static final String MESSAGE_CONSTRAINTS =
            "Payment status should be one of: Paid, Due, Overdue (case-insensitive)";

    public final String value;

    /**
     * Constructs a {@code PaymentStatus}.
     *
     * @param status A valid payment status.
     */
    public PaymentStatus(String status) {
        requireNonNull(status);
        checkArgument(isValidPaymentStatus(status), MESSAGE_CONSTRAINTS);
        value = capitalise(status);
    }

    /**
     * Returns true if a given string is a valid payment status.
     */
    public static boolean isValidPaymentStatus(String test) {
        String lower = test.trim().toLowerCase();
        return lower.equals("paid") || lower.equals("due") || lower.equals("overdue");
    }

    private static String capitalise(String status) {
        String lower = status.trim().toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PaymentStatus)) {
            return false;
        }

        PaymentStatus otherStatus = (PaymentStatus) other;
        return value.equals(otherStatus.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
