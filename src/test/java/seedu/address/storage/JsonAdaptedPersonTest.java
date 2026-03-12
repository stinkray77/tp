package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Time;

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
    private static final List<String> VALID_SUBJECTS =
            BENSON.getSubjects().stream()
                    .map(s -> s.subjectName)
                    .collect(Collectors.toList());
    private static final List<String> VALID_DAYS =
            BENSON.getDays().stream()
                    .map(d -> d.dayName)
                    .collect(Collectors.toList());
    private static final List<String> VALID_TIMES =
            BENSON.getTimes().stream()
                    .map(t -> t.value)
                    .collect(Collectors.toList());
    private static final String VALID_EMERGENCY_CONTACT =
            BENSON.getEmergencyContact().toString();
    private static final String VALID_PAYMENT_STATUS =
            BENSON.getPaymentStatus().toString();
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
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                null, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, INVALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, null, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, INVALID_ADDRESS,
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, null,
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
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
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                INVALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = EmergencyContact.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmergencyContact_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                null, VALID_PAYMENT_STATUS, VALID_TAGS);
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
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, INVALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = PaymentStatus.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPaymentStatus_throwsIllegalValueException() {
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, null, VALID_TAGS);
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
                VALID_SUBJECTS, VALID_DAYS, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, invalidTags);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidDay_throwsIllegalValueException() {
        List<String> invalidDays = new ArrayList<>(VALID_DAYS);
        invalidDays.add("Monday@");
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, invalidDays, VALID_TIMES,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = Day.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTime_throwsIllegalValueException() {
        List<String> invalidTimes = new ArrayList<>(VALID_TIMES);
        invalidTimes.add("14");
        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_DAYS, invalidTimes,
                VALID_EMERGENCY_CONTACT, VALID_PAYMENT_STATUS, VALID_TAGS);
        String expectedMessage = Time.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class,
                expectedMessage, person::toModelType);
    }

}
