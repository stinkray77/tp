package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditPersonDescriptorBuilder {

    private EditPersonDescriptor descriptor;

    public EditPersonDescriptorBuilder() {
        descriptor = new EditPersonDescriptor();
    }

    public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
        this.descriptor = new EditPersonDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
     */
    public EditPersonDescriptorBuilder(Person person) {
        descriptor = new EditPersonDescriptor();
        descriptor.setName(person.getName());
        descriptor.setEmail(person.getEmail());
        descriptor.setAddress(person.getAddress());
        descriptor.setSubjects(person.getSubjects());
        descriptor.setDays(person.getDays());
        descriptor.setTimes(person.getTimes());
        descriptor.setEmergencyContact(person.getEmergencyContact());
        descriptor.setPaymentStatus(person.getPaymentStatus());
        descriptor.setTags(person.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code subjects} into a {@code Set<Subject>} and set it to the
     * {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withSubjects(String... subjects) {
        Set<Subject> subjectSet = Stream.of(subjects)
                .map(Subject::new).collect(Collectors.toSet());
        descriptor.setSubjects(subjectSet);
        return this;
    }

    /**
     * Sets the {@code EmergencyContact} of the {@code EditPersonDescriptor}.
     */
    public EditPersonDescriptorBuilder withEmergencyContact(String contact) {
        descriptor.setEmergencyContact(new EmergencyContact(contact));
        return this;
    }

    /**
     * Sets the {@code PaymentStatus} of the {@code EditPersonDescriptor}.
     */
    public EditPersonDescriptorBuilder withPaymentStatus(String status) {
        descriptor.setPaymentStatus(new PaymentStatus(status));
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRemark(String remark) {
        descriptor.setRemark(new Remark(remark));
        return this;
    }

    /**
     * Parses the {@code days} into a {@code Set<Day>} and set it to the
     * {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withDays(String... days) {
        Set<Day> daySet = Stream.of(days)
                .map(Day::new).collect(Collectors.toSet());
        descriptor.setDays(daySet);
        return this;
    }

    /**
     * Parses the {@code times} into a {@code Set<Time>} and set it to the
     * {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withTimes(String... times) {
        Set<Time> timeSet = Stream.of(times)
                .map(Time::new).collect(Collectors.toSet());
        descriptor.setTimes(timeSet);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new)
                .collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditPersonDescriptor build() {
        return descriptor;
    }
}
