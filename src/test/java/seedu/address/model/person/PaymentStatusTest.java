package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PaymentStatusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PaymentStatus(null));
    }

    @Test
    public void constructor_invalidStatus_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PaymentStatus(""));
        assertThrows(IllegalArgumentException.class, () -> new PaymentStatus("Unknown"));
        assertThrows(IllegalArgumentException.class, () -> new PaymentStatus("pending"));
    }

    @Test
    public void constructor_validStatus_capitalised() {
        assertEquals("Paid", new PaymentStatus("paid").value);
        assertEquals("Due", new PaymentStatus("DUE").value);
        assertEquals("Overdue", new PaymentStatus("overdue").value);
        assertEquals("Overdue", new PaymentStatus("OVERDUE").value);
    }

    @Test
    public void isValidPaymentStatus() {
        assertFalse(PaymentStatus.isValidPaymentStatus(""));
        assertFalse(PaymentStatus.isValidPaymentStatus("Unknown"));
        assertFalse(PaymentStatus.isValidPaymentStatus("pending"));
        assertFalse(PaymentStatus.isValidPaymentStatus("123"));

        assertTrue(PaymentStatus.isValidPaymentStatus("Paid"));
        assertTrue(PaymentStatus.isValidPaymentStatus("paid"));
        assertTrue(PaymentStatus.isValidPaymentStatus("PAID"));
        assertTrue(PaymentStatus.isValidPaymentStatus("Due"));
        assertTrue(PaymentStatus.isValidPaymentStatus("due"));
        assertTrue(PaymentStatus.isValidPaymentStatus("Overdue"));
        assertTrue(PaymentStatus.isValidPaymentStatus("overdue"));
    }

    @Test
    public void toStringMethod() {
        PaymentStatus status = new PaymentStatus("Paid");
        assertEquals("Paid", status.toString());
    }

    @Test
    public void equals() {
        PaymentStatus status = new PaymentStatus("Paid");

        assertTrue(status.equals(status));
        assertTrue(status.equals(new PaymentStatus("Paid")));
        assertTrue(status.equals(new PaymentStatus("paid")));

        assertFalse(status.equals(null));
        assertFalse(status.equals("Paid"));
        assertFalse(status.equals(new PaymentStatus("Due")));
    }

    @Test
    public void hashCodeMethod() {
        PaymentStatus status = new PaymentStatus("Paid");
        assertEquals(status.hashCode(), new PaymentStatus("paid").hashCode());
    }
}
