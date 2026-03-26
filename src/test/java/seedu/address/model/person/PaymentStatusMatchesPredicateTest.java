package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PaymentStatusMatchesPredicateTest {

    @Test
    public void equals() {
        PaymentStatusMatchesPredicate firstPredicate = new PaymentStatusMatchesPredicate("Due");
        PaymentStatusMatchesPredicate secondPredicate = new PaymentStatusMatchesPredicate("Paid");

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PaymentStatusMatchesPredicate firstPredicateCopy = new PaymentStatusMatchesPredicate("Due");
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_paymentStatusMatches_returnsTrue() {
        // Exact match
        PaymentStatusMatchesPredicate predicate = new PaymentStatusMatchesPredicate("Due");
        assertTrue(predicate.test(new PersonBuilder().withPaymentStatus("Due").build()));

        // Case insensitive match
        predicate = new PaymentStatusMatchesPredicate("dUe");
        assertTrue(predicate.test(new PersonBuilder().withPaymentStatus("Due").build()));

        // Paid status
        predicate = new PaymentStatusMatchesPredicate("Paid");
        assertTrue(predicate.test(new PersonBuilder().withPaymentStatus("Paid").build()));
    }

    @Test
    public void test_paymentStatusDoesNotMatch_returnsFalse() {
        // Non-matching status
        PaymentStatusMatchesPredicate predicate = new PaymentStatusMatchesPredicate("Paid");
        assertFalse(predicate.test(new PersonBuilder().withPaymentStatus("Due").build()));

        // Keywords match name, email and address, but does not match payment status
        predicate = new PaymentStatusMatchesPredicate("Alice");
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@email.com")
                .withAddress("Main Street").withPaymentStatus("Due").build()));
    }
}
