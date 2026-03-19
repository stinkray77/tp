package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_EMERGENCY_CONTACT = "85355255";
    public static final String DEFAULT_PAYMENT_STATUS = "Due";

    private Name name;
    private Email email;
    private Address address;
    private Set<Subject> subjects;
    private Set<Day> days;
    private Set<Time> times;
    private EmergencyContact emergencyContact;
    private PaymentStatus paymentStatus;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        subjects = new HashSet<>();
        days = new HashSet<>();
        times = new HashSet<>();
        emergencyContact = new EmergencyContact(DEFAULT_EMERGENCY_CONTACT);
        paymentStatus = new PaymentStatus(DEFAULT_PAYMENT_STATUS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        subjects = new HashSet<>(personToCopy.getSubjects());
        days = new HashSet<>(personToCopy.getDays());
        times = new HashSet<>(personToCopy.getTimes());
        emergencyContact = personToCopy.getEmergencyContact();
        paymentStatus = personToCopy.getPaymentStatus();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Parses the {@code subjects} into a {@code Set<Subject>} and set it to the {@code Person}.
     */
    public PersonBuilder withSubjects(String ... subjects) {
        this.subjects = SampleDataUtil.getSubjectSet(subjects);
        return this;
    }

    /**
     * Parses the {@code days} into a {@code Set<Day>} and set it to the {@code Person}.
     */
    public PersonBuilder withDays(String ... days) {
        this.days = SampleDataUtil.getDaySet(days);
        return this;
    }

    /**
     * Parses the {@code times} into a {@code Set<Time>} and set it to the {@code Person}.
     */
    public PersonBuilder withTimes(String ... times) {
        this.times = SampleDataUtil.getTimeSet(times);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code EmergencyContact} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmergencyContact(String contact) {
        this.emergencyContact = new EmergencyContact(contact);
        return this;
    }

    /**
     * Sets the {@code PaymentStatus} of the {@code Person} that we are building.
     */
    public PersonBuilder withPaymentStatus(String status) {
        this.paymentStatus = new PaymentStatus(status);
        return this;
    }

    /**
     * Builds and returns the {@code Person}.
     */
    public Person build() {
        return new Person(name, email, address, subjects,
                days, times,
                emergencyContact, paymentStatus, tags);
    }

}
