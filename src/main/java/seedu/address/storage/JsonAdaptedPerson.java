package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String INVALID_ATTENDANCE_KEY_FORMAT =
            "Attendance key must be in 'Day Time' format (e.g., 'Monday 1400')";

    private final String name;
    private final String email;
    private final String address;
    private final List<JsonAdaptedLessonSlot> lessonSlots = new ArrayList<>();
    private final String emergencyContact;
    private final String paymentStatus;
    private final String remark;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final Map<String, Map<String, String>> attendanceRecords = new LinkedHashMap<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("address") String address,
            @JsonProperty("lessonSlots") List<JsonAdaptedLessonSlot> lessonSlots,
            @JsonProperty("emergencyContact") String emergencyContact,
            @JsonProperty("paymentStatus") String paymentStatus,
            @JsonProperty("remark") String remark,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("attendanceRecords") Map<String, Map<String, String>> attendanceRecords) {
        this.name = name;
        this.email = email;
        this.address = address;
        if (lessonSlots != null) {
            this.lessonSlots.addAll(lessonSlots);
        }
        this.emergencyContact = emergencyContact;
        this.paymentStatus = paymentStatus;
        this.remark = remark;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (attendanceRecords != null) {
            attendanceRecords.forEach((subject, lessons) -> {
                Map<String, String> lessonCopy = new LinkedHashMap<>(lessons);
                this.attendanceRecords.put(subject, lessonCopy);
            });
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        email = source.getEmail().value;
        address = source.getAddress().value;
        lessonSlots.addAll(source.getLessonSlots().stream()
                .map(JsonAdaptedLessonSlot::new)
                .collect(Collectors.toList()));
        emergencyContact = source.getEmergencyContact().value;
        remark = source.getRemark().value;
        paymentStatus = source.getPaymentStatus().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        source.getAttendanceRecords().forEach((subject, lessons) -> {
            if (subject == null) {
                return;
            }
            Map<String, String> lessonMap = new LinkedHashMap<>();
            lessons.forEach((lesson, status) -> {
                if (lesson != null && status != null) {
                    lessonMap.put(lesson, status.value);
                }
            });
            attendanceRecords.put(subject, lessonMap);
        });
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

        final List<LessonSlot> modelLessonSlots = new ArrayList<>();
        for (JsonAdaptedLessonSlot adaptedSlot : lessonSlots) {
            modelLessonSlots.add(adaptedSlot.toModelType());
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

        final Remark modelRemark = new Remark(remark != null ? remark : "");

        final Map<String, Map<String, AttendanceStatus>> modelAttendanceRecords = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, String>> subjectEntry : attendanceRecords.entrySet()) {
            String subjectKey = subjectEntry.getKey();
            if (!Subject.isValidSubject(subjectKey)) {
                throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
            }
            Map<String, AttendanceStatus> lessonMap = new LinkedHashMap<>();
            for (Map.Entry<String, String> lessonEntry : subjectEntry.getValue().entrySet()) {
                String lessonKey = lessonEntry.getKey();
                if (!isValidAttendanceKey(lessonKey)) {
                    throw new IllegalValueException(INVALID_ATTENDANCE_KEY_FORMAT);
                }
                if (!AttendanceStatus.isValidStatus(lessonEntry.getValue())) {
                    throw new IllegalValueException(AttendanceStatus.MESSAGE_CONSTRAINTS);
                }
                lessonMap.put(lessonKey, AttendanceStatus.fromString(lessonEntry.getValue()));
            }
            modelAttendanceRecords.put(subjectKey, lessonMap);
        }

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Person(modelName, modelEmail, modelAddress,
                modelLessonSlots,
                modelEmergencyContact, modelPaymentStatus, modelRemark, modelTags,
                modelAttendanceRecords);
    }

    /**
     * Returns true if the attendance key is in valid "Day Time" format.
     */
    private static boolean isValidAttendanceKey(String key) {
        String[] parts = key.split(" ");
        if (parts.length != 2) {
            return false;
        }
        return Day.isValidDay(parts[0]) && Time.isValidTime(parts[1]);
    }

}
