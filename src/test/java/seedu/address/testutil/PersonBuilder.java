package seedu.address.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
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
    private List<LessonSlot> lessonSlots;
    private EmergencyContact emergencyContact;
    private PaymentStatus paymentStatus;
    private Remark remark;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        lessonSlots = new ArrayList<>();
        emergencyContact = new EmergencyContact(DEFAULT_EMERGENCY_CONTACT);
        paymentStatus = new PaymentStatus(DEFAULT_PAYMENT_STATUS);
        remark = new Remark("");
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        lessonSlots = new ArrayList<>(personToCopy.getLessonSlots());
        emergencyContact = personToCopy.getEmergencyContact();
        paymentStatus = personToCopy.getPaymentStatus();
        remark = personToCopy.getRemark();
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
     * Sets lesson slots from groups of 3 strings: subject, day, time.
     * Example: withLessonSlots("Mathematics", "Monday", "1400", "English", "Tuesday", "0900")
     */
    public PersonBuilder withLessonSlots(String ... triplets) {
        this.lessonSlots = SampleDataUtil.getLessonSlotList(triplets);
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
     * Sets the {@code Remark} of the {@code Person} that we are building.
     */
    public PersonBuilder withRemark(String remark) {
        this.remark = new Remark(remark);
        return this;
    }

    /**
     * Builds and returns the {@code Person}.
     */
    public Person build() {
        return new Person(name, email, address, lessonSlots,
                emergencyContact, paymentStatus, remark, tags);
    }

}
