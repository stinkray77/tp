package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Time;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_EMERGENCY_CONTACT = "+651234";
    private static final String INVALID_PAYMENT_STATUS = "Unknown";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_DAY = "!v@lid";
    private static final String INVALID_TIME = "7654321";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS =
            BENSON.getAddress().toString();
    private static final List<String> VALID_SUBJECTS =
            BENSON.getSubjects().stream()
                    .map(s -> s.subjectName)
                    .collect(Collectors.toList());
    private static final String VALID_EMERGENCY_CONTACT =
            BENSON.getEmergencyContact().toString();
    private static final String VALID_PAYMENT_STATUS =
            BENSON.getPaymentStatus().toString();
    private static final List<String> VALID_DAYS =
            BENSON.getDays().stream()
                    .map(d -> d.dayName)
                    .collect(Collectors.toList());
    private static final List<String> VALID_TIMES =
            BENSON.getTimes().stream()
                    .map(t -> t.timeValue)
                    .collect(Collectors.toList());
    private static final String VALID_REMARK = BENSON.getRemark().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS =
            BENSON.getTags().stream()
                    .map(JsonAdaptedTag::new)
                    .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson()
            throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                INVALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                null, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, INVALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, null, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, INVALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, null,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT,
                Address.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmergencyContact_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, INVALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = EmergencyContact.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmergencyContact_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, null,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT,
                EmergencyContact.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPaymentStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                INVALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = PaymentStatus.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPaymentStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                null, VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT,
                PaymentStatus.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidDay_throwsIllegalValueException() {
        List<String> invalidDays = new ArrayList<>(VALID_DAYS);
        invalidDays.add(INVALID_DAY);
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                invalidDays, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Day.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_multipleDays_success() throws Exception {
        List<String> multipleDays = Arrays.asList("Monday", "Wednesday", "Friday");
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                multipleDays, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);

        Person modelPerson = person.toModelType();
        assertEquals(3, modelPerson.getDays().size());
    }

    @Test
    public void toModelType_duplicateDays_deduplicated() throws Exception {
        List<String> duplicateDays = Arrays.asList("Monday", "Monday", "Tuesday");
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                duplicateDays, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);

        Person modelPerson = person.toModelType();
        assertEquals(2, modelPerson.getDays().size());
    }

    @Test
    public void toModelType_invalidTime_throwsIllegalValueException() {
        List<String> invalidTimes = new ArrayList<>(VALID_TIMES);
        invalidTimes.add(INVALID_TIME);
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, invalidTimes, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Time.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_multipleTimes_success() throws Exception {
        List<String> multipleTimes = Arrays.asList("0800", "1200", "1600");
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, multipleTimes, VALID_REMARK, VALID_TAGS, null);

        Person modelPerson = person.toModelType();
        assertEquals(3, modelPerson.getTimes().size());
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, invalidTags, null);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_nullAttendanceRecords_defaultsToEmpty() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, null);
        Person modelPerson = person.toModelType();
        assertTrue(modelPerson.getAttendanceRecords().isEmpty());
    }

    @Test
    public void toModelType_validAttendanceRecords_roundTrip() throws Exception {
        Map<String, Map<String, String>> records = new LinkedHashMap<>();
        Map<String, String> mathLessons = new LinkedHashMap<>();
        mathLessons.put("Algebra Lesson 1", "Present");
        mathLessons.put("Algebra Lesson 2", "Absent");
        records.put("Mathematics", mathLessons);
        Map<String, String> engLessons = new LinkedHashMap<>();
        engLessons.put("Grammar Lesson 1", "Excused");
        records.put("English", engLessons);

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, records);

        Person modelPerson = adapted.toModelType();
        assertEquals(AttendanceStatus.PRESENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Algebra Lesson 1"));
        assertEquals(AttendanceStatus.ABSENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Algebra Lesson 2"));
        assertEquals(AttendanceStatus.EXCUSED,
                modelPerson.getAttendanceRecords().get("English").get("Grammar Lesson 1"));
    }

    @Test
    public void toModelType_attendanceRoundTripViaPersonConstructor() throws Exception {
        Person original = BENSON
                .markAttendance("Mathematics", "Lesson 1", AttendanceStatus.PRESENT)
                .markAttendance("Mathematics", "Lesson 2", AttendanceStatus.ABSENT);

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(original);
        Person restored = adapted.toModelType();

        assertEquals(original, restored);
        assertEquals(AttendanceStatus.PRESENT,
                restored.getAttendanceRecords().get("Mathematics").get("Lesson 1"));
        assertEquals(AttendanceStatus.ABSENT,
                restored.getAttendanceRecords().get("Mathematics").get("Lesson 2"));
    }

    @Test
    public void toModelType_invalidAttendanceStatus_throwsIllegalValueException() {
        Map<String, Map<String, String>> invalidRecords = new LinkedHashMap<>();
        Map<String, String> lessons = new LinkedHashMap<>();
        lessons.put("Lesson 1", "InvalidStatus");
        invalidRecords.put("Mathematics", lessons);

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, invalidRecords);
        assertThrows(IllegalValueException.class,
                AttendanceStatus.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_caseInsensitiveAttendanceStatus_normalises() throws Exception {
        Map<String, Map<String, String>> records = new LinkedHashMap<>();
        Map<String, String> lessons = new LinkedHashMap<>();
        lessons.put("Lesson 1", "present");
        lessons.put("Lesson 2", "ABSENT");
        records.put("Mathematics", lessons);

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_DAYS, VALID_TIMES, VALID_REMARK, VALID_TAGS, records);

        Person modelPerson = person.toModelType();
        assertEquals(AttendanceStatus.PRESENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Lesson 1"));
        assertEquals(AttendanceStatus.ABSENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Lesson 2"));
    }
}
