package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a student's emergency contact number.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmergencyContact(String)}
 */
public class EmergencyContact {

    public static final String MESSAGE_CONSTRAINTS =
            "Emergency contact must be a 3 to 15 digit number (may be a landline or short code).";

    public static final String VALIDATION_REGEX = "\\d{3,15}";

    public final String value;

    /**
     * Constructs an {@code EmergencyContact}.
     *
     * @param contact A valid emergency contact number (3 to 15 digits).
     */
    public EmergencyContact(String contact) {
        requireNonNull(contact);
        checkArgument(isValidEmergencyContact(contact), MESSAGE_CONSTRAINTS);
        value = contact;
    }

    /**
     * Returns true if a given string is a valid emergency contact number.
     */
    public static boolean isValidEmergencyContact(String test) {
        return test.matches(VALIDATION_REGEX);
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

        if (!(other instanceof EmergencyContact)) {
            return false;
        }

        EmergencyContact otherContact = (EmergencyContact) other;
        return value.equals(otherContact.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
