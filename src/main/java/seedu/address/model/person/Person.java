package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final List<LessonSlot> lessonSlots;
    private final EmergencyContact emergencyContact;
    private final PaymentStatus paymentStatus;
    private final Remark remark;
    private final Set<Tag> tags = new HashSet<>();
    private final Map<String, Map<String, AttendanceStatus>> attendanceRecords;

    /**
     * Every field must be present and not null.
     * Attendance records default to an empty map.
     */
    public Person(Name name, Email email, Address address,
                  List<LessonSlot> lessonSlots,
                  EmergencyContact emergencyContact,
                  PaymentStatus paymentStatus, Remark remark, Set<Tag> tags) {
        this(name, email, address, lessonSlots,
                emergencyContact, paymentStatus, remark, tags, new LinkedHashMap<>());
    }

    /**
     * Every field must be present and not null, including attendance records.
     */
    public Person(Name name, Email email, Address address,
                  List<LessonSlot> lessonSlots,
                  EmergencyContact emergencyContact,
                  PaymentStatus paymentStatus, Remark remark, Set<Tag> tags,
                  Map<String, Map<String, AttendanceStatus>> attendanceRecords) {
        requireAllNonNull(name, email, address, lessonSlots,
                emergencyContact, paymentStatus, remark, tags, attendanceRecords);
        this.name = name;
        this.email = email;
        this.address = address;
        this.lessonSlots = new ArrayList<>(lessonSlots);
        this.emergencyContact = emergencyContact;
        this.paymentStatus = paymentStatus;
        this.remark = remark;
        this.tags.addAll(tags);
        Map<String, Map<String, AttendanceStatus>> copy = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, AttendanceStatus>> entry : attendanceRecords.entrySet()) {
            copy.put(entry.getKey(), new LinkedHashMap<>(entry.getValue()));
        }
        this.attendanceRecords = copy;
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
     * Returns an unmodifiable view of the lesson slots.
     */
    public List<LessonSlot> getLessonSlots() {
        return Collections.unmodifiableList(lessonSlots);
    }

    /**
     * Returns an immutable subject set derived from lesson slots.
     */
    public Set<Subject> getSubjects() {
        return lessonSlots.stream()
                .map(LessonSlot::getSubject)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns an immutable day set derived from lesson slots.
     */
    public Set<Day> getDays() {
        return lessonSlots.stream()
                .map(LessonSlot::getDay)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns an immutable time set derived from lesson slots.
     */
    public Set<Time> getTimes() {
        return lessonSlots.stream()
                .map(LessonSlot::getTime)
                .collect(Collectors.toUnmodifiableSet());
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Remark getRemark() {
        return remark;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an unmodifiable view of the attendance records.
     * The outer key is the subject name, the inner key is the day-time string,
     * and the value is the {@code AttendanceStatus}.
     */
    public Map<String, Map<String, AttendanceStatus>> getAttendanceRecords() {
        return Collections.unmodifiableMap(attendanceRecords);
    }

    /**
     * Returns true if this person has a lesson slot matching the given subject (case-insensitive)
     * and attendance key (day + time).
     */
    public boolean hasLessonSlot(String subject, String dayTime) {
        return lessonSlots.stream().anyMatch(slot ->
                slot.getSubject().subjectName.equalsIgnoreCase(subject)
                && slot.getAttendanceKey().equals(dayTime));
    }

    /**
     * Returns a new {@code Person} with the attendance record for the given subject and
     * day-time updated. All other fields remain unchanged.
     *
     * @param subject The subject name.
     * @param dayTime The day-time key (e.g., "Monday 1400").
     * @param status  The new attendance status.
     * @return A new {@code Person} with the updated attendance record.
     */
    public Person markAttendance(String subject, String dayTime, AttendanceStatus status) {
        requireAllNonNull(subject, dayTime, status);
        Map<String, Map<String, AttendanceStatus>> updated = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, AttendanceStatus>> entry : attendanceRecords.entrySet()) {
            updated.put(entry.getKey(), new LinkedHashMap<>(entry.getValue()));
        }
        updated.computeIfAbsent(subject, k -> new LinkedHashMap<>()).put(dayTime, status);
        return new Person(name, email, address, lessonSlots,
                emergencyContact, paymentStatus, remark, tags, updated);
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
                && otherPerson.getName().fullName.equalsIgnoreCase(getName().fullName);
    }

    /**
     * Returns true if both persons have the same email.
     * Used for email uniqueness validation.
     */
    public boolean hasSameEmail(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getEmail().equals(getEmail());
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
                && lessonSlots.equals(otherPerson.lessonSlots)
                && emergencyContact.equals(otherPerson.emergencyContact)
                && paymentStatus.equals(otherPerson.paymentStatus)
                && remark.equals(otherPerson.remark)
                && tags.equals(otherPerson.tags)
                && attendanceRecords.equals(otherPerson.attendanceRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, address, lessonSlots,
                emergencyContact, paymentStatus, remark, tags, attendanceRecords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("email", email)
                .add("address", address)
                .add("lessonSlots", lessonSlots)
                .add("emergencyContact", emergencyContact)
                .add("paymentStatus", paymentStatus)
                .add("remark", remark)
                .add("tags", tags)
                .add("attendanceRecords", attendanceRecords)
                .toString();
    }

}
