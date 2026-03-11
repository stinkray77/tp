package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmergencyContactTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EmergencyContact(null));
    }

    @Test
    public void constructor_invalidContact_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EmergencyContact(""));
        assertThrows(IllegalArgumentException.class, () -> new EmergencyContact("1234567"));
        assertThrows(IllegalArgumentException.class, () -> new EmergencyContact("123456789"));
        assertThrows(IllegalArgumentException.class, () -> new EmergencyContact("abcdefgh"));
        assertThrows(IllegalArgumentException.class, () -> new EmergencyContact("1234 567"));
    }

    @Test
    public void isValidEmergencyContact() {
        assertFalse(EmergencyContact.isValidEmergencyContact(""));
        assertFalse(EmergencyContact.isValidEmergencyContact("1234567"));
        assertFalse(EmergencyContact.isValidEmergencyContact("123456789"));
        assertFalse(EmergencyContact.isValidEmergencyContact("abcdefgh"));
        assertFalse(EmergencyContact.isValidEmergencyContact("1234 5678"));

        assertTrue(EmergencyContact.isValidEmergencyContact("91234567"));
        assertTrue(EmergencyContact.isValidEmergencyContact("00000000"));
        assertTrue(EmergencyContact.isValidEmergencyContact("99999999"));
    }

    @Test
    public void toStringMethod() {
        EmergencyContact contact = new EmergencyContact("91234567");
        assertEquals("91234567", contact.toString());
    }

    @Test
    public void equals() {
        EmergencyContact contact = new EmergencyContact("91234567");

        assertTrue(contact.equals(contact));
        assertTrue(contact.equals(new EmergencyContact("91234567")));

        assertFalse(contact.equals(null));
        assertFalse(contact.equals("91234567"));
        assertFalse(contact.equals(new EmergencyContact("98765432")));
    }

    @Test
    public void hashCodeMethod() {
        EmergencyContact contact = new EmergencyContact("91234567");
        assertEquals(contact.hashCode(),
                new EmergencyContact("91234567").hashCode());
    }
}
