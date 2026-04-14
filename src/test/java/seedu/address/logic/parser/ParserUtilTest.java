package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Subject;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_EMERGENCY_CONTACT = "+651234";
    private static final String INVALID_PAYMENT_STATUS = "Unknown";
    private static final String INVALID_SUBJECT = "@Math";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_EMERGENCY_CONTACT = "91234567";
    private static final String VALID_PAYMENT_STATUS = "Paid";
    private static final String VALID_SUBJECT_1 = "Mathematics";
    private static final String VALID_SUBJECT_2 = "English";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_TAG_WITH_SYMBOLS = "exam-prep_2026";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
                -> ParserUtil.parseIndex(
                        Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_veryLargeNumber_throwsParseException() {
        // Ensures extremely large numbers do not cause crashes
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseIndex("99999999999999"));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));
        assertEquals(INDEX_FIRST_PERSON,
                ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName()
            throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName()
            throws Exception {
        String nameWithWhitespace =
                WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName,
                ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseEmergencyContact_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseEmergencyContact((String) null));
    }

    @Test
    public void parseEmergencyContact_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseEmergencyContact(
                        INVALID_EMERGENCY_CONTACT));
    }

    @Test
    public void parseEmergencyContact_validValue_returnsEmergencyContact()
            throws Exception {
        EmergencyContact expected =
                new EmergencyContact(VALID_EMERGENCY_CONTACT);
        assertEquals(expected, ParserUtil.parseEmergencyContact(
                VALID_EMERGENCY_CONTACT));
    }

    @Test
    public void parseEmergencyContact_validValueWithWhitespace_returnsTrimmed()
            throws Exception {
        String withWhitespace =
                WHITESPACE + VALID_EMERGENCY_CONTACT + WHITESPACE;
        EmergencyContact expected =
                new EmergencyContact(VALID_EMERGENCY_CONTACT);
        assertEquals(expected,
                ParserUtil.parseEmergencyContact(withWhitespace));
    }

    @Test
    public void parsePaymentStatus_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parsePaymentStatus((String) null));
    }

    @Test
    public void parsePaymentStatus_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parsePaymentStatus(
                        INVALID_PAYMENT_STATUS));
    }

    @Test
    public void parsePaymentStatus_validValue_returnsPaymentStatus()
            throws Exception {
        PaymentStatus expected =
                new PaymentStatus(VALID_PAYMENT_STATUS);
        assertEquals(expected,
                ParserUtil.parsePaymentStatus(VALID_PAYMENT_STATUS));
    }

    @Test
    public void parseSubject_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseSubject((String) null));
    }

    @Test
    public void parseSubject_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseSubject(INVALID_SUBJECT));
    }

    @Test
    public void parseSubject_validValue_returnsSubject() throws Exception {
        Subject expected = new Subject(VALID_SUBJECT_1);
        assertEquals(expected,
                ParserUtil.parseSubject(VALID_SUBJECT_1));
    }

    @Test
    public void parseSubjects_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseSubjects(null));
    }

    @Test
    public void parseSubjects_collectionWithInvalid_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseSubjects(
                        Arrays.asList(VALID_SUBJECT_1, INVALID_SUBJECT)));
    }

    @Test
    public void parseSubjects_emptyCollection_returnsEmptySet()
            throws Exception {
        assertTrue(ParserUtil.parseSubjects(
                Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseSubjects_collectionWithValid_returnsSubjectSet()
            throws Exception {
        Set<Subject> actualSet = ParserUtil.parseSubjects(
                Arrays.asList(VALID_SUBJECT_1, VALID_SUBJECT_2));
        Set<Subject> expectedSet = new HashSet<>(Arrays.asList(
                new Subject(VALID_SUBJECT_1),
                new Subject(VALID_SUBJECT_2)));
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress()
            throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress,
                ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress()
            throws Exception {
        String addressWithWhitespace =
                WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress,
                ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail()
            throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail()
            throws Exception {
        String emailWithWhitespace =
                WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail,
                ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag()
            throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithAllowedSymbols_returnsTag()
            throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_WITH_SYMBOLS);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_WITH_SYMBOLS));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag()
            throws Exception {
        String tagWithWhitespace =
                WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag,
                ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, ()
                -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, ()
                -> ParserUtil.parseTags(
                        Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet()
            throws Exception {
        assertTrue(ParserUtil.parseTags(
                Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet()
            throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(
                Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(
                new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));
        assertEquals(expectedTagSet, actualTagSet);
    }

    // --- parseLessonName ---

    @Test
    public void parseLessonName_validName_returnsName() throws Exception {
        assertEquals("Lesson 1", ParserUtil.parseLessonName("Lesson 1"));
        assertEquals("2026-04-13 Algebra", ParserUtil.parseLessonName("  2026-04-13 Algebra  "));
    }

    @Test
    public void parseLessonName_invalidName_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLessonName("!invalid"));
    }

    @Test
    public void parseLessonName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseLessonName(null));
    }

    // --- parseLessonSlots dedup & overlap detection ---

    @Test
    public void parseLessonSlots_exactDuplicate_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseLessonSlots(
                        Arrays.asList("Math", "Math"),
                        Arrays.asList("Monday", "Monday"),
                        Arrays.asList("1400", "1400")));
    }

    @Test
    public void parseLessonSlots_overlappingDifferentSubject_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseLessonSlots(
                        Arrays.asList("Math", "English"),
                        Arrays.asList("Friday", "Friday"),
                        Arrays.asList("1400", "1400")));
    }

    @Test
    public void parseLessonSlots_sameSubjectDifferentDays_success()
            throws ParseException {
        List<LessonSlot> slots = ParserUtil.parseLessonSlots(
                Arrays.asList("Math", "Math"),
                Arrays.asList("Monday", "Wednesday"),
                Arrays.asList("1400", "1600"));
        assertEquals(2, slots.size());
    }

    @Test
    public void parseLessonSlots_sameDayDifferentTimes_success()
            throws ParseException {
        List<LessonSlot> slots = ParserUtil.parseLessonSlots(
                Arrays.asList("Math", "English"),
                Arrays.asList("Friday", "Friday"),
                Arrays.asList("1400", "1600"));
        assertEquals(2, slots.size());
    }
}
