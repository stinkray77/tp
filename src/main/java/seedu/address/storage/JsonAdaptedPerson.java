package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String email;
    private final String address;
    private final List<String> subjects = new ArrayList<>();
    private final String emergencyContact;
    private final String paymentStatus;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("address") String address,
            @JsonProperty("subjects") List<String> subjects,
            @JsonProperty("emergencyContact") String emergencyContact,
            @JsonProperty("paymentStatus") String paymentStatus,
            @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.email = email;
        this.address = address;
        if (subjects != null) {
            this.subjects.addAll(subjects);
        }
        this.emergencyContact = emergencyContact;
        this.paymentStatus = paymentStatus;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        email = source.getEmail().value;
        address = source.getAddress().value;
        subjects.addAll(source.getSubjects().stream()
                .map(s -> s.subjectName)
                .collect(Collectors.toList()));
        emergencyContact = source.getEmergencyContact().value;
        paymentStatus = source.getPaymentStatus().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                            Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (email == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                            Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                            Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Subject> modelSubjects = new HashSet<>();
        for (String subjectName : subjects) {
            if (!Subject.isValidSubject(subjectName)) {
                throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
            }
            modelSubjects.add(new Subject(subjectName));
        }

        if (emergencyContact == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                            EmergencyContact.class.getSimpleName()));
        }
        if (!EmergencyContact.isValidEmergencyContact(emergencyContact)) {
            throw new IllegalValueException(
                    EmergencyContact.MESSAGE_CONSTRAINTS);
        }
        final EmergencyContact modelEmergencyContact =
                new EmergencyContact(emergencyContact);

        if (paymentStatus == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT,
                            PaymentStatus.class.getSimpleName()));
        }
        if (!PaymentStatus.isValidPaymentStatus(paymentStatus)) {
            throw new IllegalValueException(
                    PaymentStatus.MESSAGE_CONSTRAINTS);
        }
        final PaymentStatus modelPaymentStatus =
                new PaymentStatus(paymentStatus);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Person(modelName, modelEmail, modelAddress,
                modelSubjects, new HashSet<>(), new HashSet<>(),
                modelEmergencyContact, modelPaymentStatus, modelTags);
    }

}
