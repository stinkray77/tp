package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_EMERGENCY_CONTACT = "+651234";
    private static final String INVALID_PAYMENT_STATUS = "Unknown";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS =
            BENSON.getAddress().toString();
    private static final List<JsonAdaptedLessonSlot> VALID_LESSON_SLOTS =
            BENSON.getLessonSlots().stream()
                    .map(JsonAdaptedLessonSlot::new)
                    .collect(Collectors.toList());
    private static final String VALID_EMERGENCY_CONTACT =
            BENSON.getEmergencyContact().toString();
    private static final String VALID_PAYMENT_STATUS =
            BENSON.getPaymentStatus().toString();
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
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                null, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, INVALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, null, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, INVALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, null,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
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
                VALID_LESSON_SLOTS, INVALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = EmergencyContact.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmergencyContact_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, null,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
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
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                INVALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = PaymentStatus.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPaymentStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                null, VALID_REMARK, VALID_TAGS, null);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT,
                PaymentStatus.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, invalidTags, null);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_nullAttendanceRecords_defaultsToEmpty() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, null);
        Person modelPerson = person.toModelType();
        assertTrue(modelPerson.getAttendanceRecords().isEmpty());
    }

    @Test
    public void toModelType_validAttendanceRecords_roundTrip() throws Exception {
        Map<String, Map<String, String>> records = new LinkedHashMap<>();
        Map<String, String> mathLessons = new LinkedHashMap<>();
        mathLessons.put("Monday 1400", "Present");
        mathLessons.put("Wednesday 1600", "Absent");
        records.put("Mathematics", mathLessons);
        Map<String, String> engLessons = new LinkedHashMap<>();
        engLessons.put("Tuesday 0900", "Excused");
        records.put("English", engLessons);

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, records);

        Person modelPerson = adapted.toModelType();
        assertEquals(AttendanceStatus.PRESENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Monday 1400"));
        assertEquals(AttendanceStatus.ABSENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Wednesday 1600"));
        assertEquals(AttendanceStatus.EXCUSED,
                modelPerson.getAttendanceRecords().get("English").get("Tuesday 0900"));
    }

    @Test
    public void toModelType_attendanceRoundTripViaPersonConstructor() throws Exception {
        Person original = BENSON
                .markAttendance("English", "Tuesday 0900", AttendanceStatus.PRESENT)
                .markAttendance("English", "Thursday 1500", AttendanceStatus.ABSENT);

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(original);
        Person restored = adapted.toModelType();

        assertEquals(original, restored);
        assertEquals(AttendanceStatus.PRESENT,
                restored.getAttendanceRecords().get("English").get("Tuesday 0900"));
        assertEquals(AttendanceStatus.ABSENT,
                restored.getAttendanceRecords().get("English").get("Thursday 1500"));
    }

    @Test
    public void toModelType_invalidAttendanceStatus_throwsIllegalValueException() {
        Map<String, Map<String, String>> invalidRecords = new LinkedHashMap<>();
        Map<String, String> lessons = new LinkedHashMap<>();
        lessons.put("Monday 1400", "InvalidStatus");
        invalidRecords.put("Mathematics", lessons);

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, invalidRecords);
        assertThrows(IllegalValueException.class,
                AttendanceStatus.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_caseInsensitiveAttendanceStatus_normalises() throws Exception {
        Map<String, Map<String, String>> records = new LinkedHashMap<>();
        Map<String, String> lessons = new LinkedHashMap<>();
        lessons.put("Monday 1400", "present");
        lessons.put("Wednesday 1600", "ABSENT");
        records.put("Mathematics", lessons);

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, records);

        Person modelPerson = person.toModelType();
        assertEquals(AttendanceStatus.PRESENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Monday 1400"));
        assertEquals(AttendanceStatus.ABSENT,
                modelPerson.getAttendanceRecords().get("Mathematics").get("Wednesday 1600"));
    }

    @Test
    public void toModelType_invalidAttendanceSubjectKey_throwsIllegalValueException() {
        Map<String, Map<String, String>> invalidRecords = new LinkedHashMap<>();
        Map<String, String> lessons = new LinkedHashMap<>();
        lessons.put("Monday 1400", "Present");
        invalidRecords.put("!InvalidSubject", lessons);

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, invalidRecords);
        assertThrows(IllegalValueException.class,
                seedu.address.model.person.Subject.MESSAGE_CONSTRAINTS, person::toModelType);
    }

    @Test
    public void toModelType_invalidAttendanceKey_throwsIllegalValueException() {
        Map<String, Map<String, String>> invalidRecords = new LinkedHashMap<>();
        Map<String, String> lessons = new LinkedHashMap<>();
        lessons.put("!InvalidKey", "Present");
        invalidRecords.put("Mathematics", lessons);

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, invalidRecords);
        assertThrows(IllegalValueException.class,
                JsonAdaptedPerson.INVALID_ATTENDANCE_KEY_FORMAT, person::toModelType);
    }

    @Test
    public void toModelType_attendanceKeyWithExtraSpaces_throwsIllegalValueException() {
        Map<String, Map<String, String>> invalidRecords = new LinkedHashMap<>();
        Map<String, String> lessons = new LinkedHashMap<>();
        lessons.put("Monday 1400 extra", "Present");
        invalidRecords.put("Mathematics", lessons);

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_LESSON_SLOTS, VALID_EMERGENCY_CONTACT,
                VALID_PAYMENT_STATUS,
                VALID_REMARK, VALID_TAGS, invalidRecords);
        assertThrows(IllegalValueException.class,
                JsonAdaptedPerson.INVALID_ATTENDANCE_KEY_FORMAT, person::toModelType);
    }

    @Test
    public void constructor_personWithLessonSlots_serialisesCorrectly() throws Exception {
        Person personWithSlots = new seedu.address.testutil.PersonBuilder()
                .withName("Test Person").withEmail("test@example.com")
                .withAddress("123 Test Street").withEmergencyContact("91234567")
                .withPaymentStatus("Paid")
                .withLessonSlots("Mathematics", "Monday", "1000",
                        "English", "Wednesday", "1200")
                .build();
        JsonAdaptedPerson adapted = new JsonAdaptedPerson(personWithSlots);
        assertEquals(personWithSlots, adapted.toModelType());
    }

    @Test
    public void constructor_attendanceWithNullSubjectKey_skipsSilently() throws Exception {
        Map<String, Map<String, AttendanceStatus>> records = new LinkedHashMap<>();
        records.put(null, Map.of("Monday 1400", AttendanceStatus.PRESENT));
        records.put("Math", Map.of("Monday 1400", AttendanceStatus.PRESENT));

        Person person = new seedu.address.testutil.PersonBuilder()
                .withName("Test")
                .withEmail("test@example.com")
                .withAddress("1 Street")
                .withEmergencyContact("91234567")
                .withPaymentStatus("Paid")
                .build();
        Person personWithRecords = new Person(
                person.getName(), person.getEmail(), person.getAddress(),
                person.getLessonSlots(), person.getEmergencyContact(),
                person.getPaymentStatus(), person.getRemark(),
                person.getTags(), records);

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(personWithRecords);
        Person restored = adapted.toModelType();
        assertTrue(restored.getAttendanceRecords().containsKey("Math"));
        assertEquals(1, restored.getAttendanceRecords().size());
    }

    @Test
    public void constructor_attendanceWithNullLessonKey_skipsSilently() throws Exception {
        Map<String, Map<String, AttendanceStatus>> records = new LinkedHashMap<>();
        Map<String, AttendanceStatus> innerMap = new LinkedHashMap<>();
        innerMap.put(null, AttendanceStatus.PRESENT);
        innerMap.put("Monday 1400", AttendanceStatus.ABSENT);
        records.put("Math", innerMap);

        Person person = new seedu.address.testutil.PersonBuilder()
                .withName("Test")
                .withEmail("test@example.com")
                .withAddress("1 Street")
                .withEmergencyContact("91234567")
                .withPaymentStatus("Paid")
                .build();
        Person personWithRecords = new Person(
                person.getName(), person.getEmail(), person.getAddress(),
                person.getLessonSlots(), person.getEmergencyContact(),
                person.getPaymentStatus(), person.getRemark(),
                person.getTags(), records);

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(personWithRecords);
        Person restored = adapted.toModelType();
        Map<String, Map<String, AttendanceStatus>> restoredRecords =
                restored.getAttendanceRecords();
        assertEquals(1, restoredRecords.get("Math").size());
        assertTrue(restoredRecords.get("Math").containsKey("Monday 1400"));
    }

    @Test
    public void constructor_attendanceWithNullStatus_skipsSilently() throws Exception {
        Map<String, Map<String, AttendanceStatus>> records = new LinkedHashMap<>();
        Map<String, AttendanceStatus> innerMap = new LinkedHashMap<>();
        innerMap.put("Monday 1400", null);
        innerMap.put("Tuesday 1600", AttendanceStatus.PRESENT);
        records.put("Math", innerMap);

        Person person = new seedu.address.testutil.PersonBuilder()
                .withName("Test")
                .withEmail("test@example.com")
                .withAddress("1 Street")
                .withEmergencyContact("91234567")
                .withPaymentStatus("Paid")
                .build();
        Person personWithRecords = new Person(
                person.getName(), person.getEmail(), person.getAddress(),
                person.getLessonSlots(), person.getEmergencyContact(),
                person.getPaymentStatus(), person.getRemark(),
                person.getTags(), records);

        JsonAdaptedPerson adapted = new JsonAdaptedPerson(personWithRecords);
        Person restored = adapted.toModelType();
        Map<String, Map<String, AttendanceStatus>> restoredRecords =
                restored.getAttendanceRecords();
        assertEquals(1, restoredRecords.get("Math").size());
        assertTrue(restoredRecords.get("Math").containsKey("Tuesday 1600"));
    }
}
