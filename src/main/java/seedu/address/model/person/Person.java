package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Subject> subjects = new HashSet<>();
    private final Set<Day> days = new HashSet<>();
    private final Set<Time> times = new HashSet<>();
    private final EmergencyContact emergencyContact;
    private final PaymentStatus paymentStatus;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Email email, Address address,
                  Set<Subject> subjects, Set<Day> days, Set<Time> times,
                  EmergencyContact emergencyContact,
                  PaymentStatus paymentStatus, Set<Tag> tags) {
        requireAllNonNull(name, email, address, subjects, days, times,
                emergencyContact, paymentStatus, tags);
        this.name = name;
        this.email = email;
        this.address = address;
        this.subjects.addAll(subjects);
        this.days.addAll(days);
        this.times.addAll(times);
        this.emergencyContact = emergencyContact;
        this.paymentStatus = paymentStatus;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable subject set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(subjects);
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable day set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Day> getDays() {
        return Collections.unmodifiableSet(days);
    }

    /**
     * Returns an immutable time set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Time> getTimes() {
        return Collections.unmodifiableSet(times);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && subjects.equals(otherPerson.subjects)
                && days.equals(otherPerson.days)
                && times.equals(otherPerson.times)
                && emergencyContact.equals(otherPerson.emergencyContact)
                && paymentStatus.equals(otherPerson.paymentStatus)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, address, subjects,
                days, times, emergencyContact, paymentStatus, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("email", email)
                .add("address", address)
                .add("subjects", subjects)
                .add("days", days)
                .add("times", times)
                .add("emergencyContact", emergencyContact)
                .add("paymentStatus", paymentStatus)
                .add("tags", tags)
                .toString();
    }

}
