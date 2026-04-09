package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a student's emergency contact number.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmergencyContact(String)}
 */
public class EmergencyContact {

    public static final String MESSAGE_CONSTRAINTS =
            "Emergency contact should be a valid Singapore number: 8 digits starting with 8 or 9";

    public static final String VALIDATION_REGEX = "[89]\\d{7}";

    public final String value;

    /**
     * Constructs an {@code EmergencyContact}.
     *
     * @param contact A valid 8-digit emergency contact number.
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
