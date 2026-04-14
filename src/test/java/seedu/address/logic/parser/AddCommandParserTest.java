package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_LESSON_SLOT_INCOMPLETE;
import static seedu.address.logic.Messages.MESSAGE_SUBJECT_DAY_TIME_MISMATCH;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DAY_DESC_MONDAY;
import static seedu.address.logic.commands.CommandTestUtil.DAY_DESC_WEDNESDAY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DAY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMERGENCY_CONTACT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TIME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PAYMENT_STATUS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PAYMENT_STATUS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_ENGLISH;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_MATH;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_0900;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_1400;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB)
                .withTags(VALID_TAG_FRIEND).build();

        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE + NAME_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB + TAG_DESC_FRIEND,
                new AddCommand(expectedPerson));

        Person expectedPersonMultipleTags = new PersonBuilder(BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                + PAYMENT_STATUS_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser,
                NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple emails
        assertParseFailure(parser,
                EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser,
                ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_ADDRESS));

        // multiple emergency contacts
        assertParseFailure(parser,
                EMERGENCY_CONTACT_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_EMERGENCY_CONTACT));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + EMAIL_DESC_AMY
                        + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + EMERGENCY_CONTACT_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_NAME, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_EMERGENCY_CONTACT,
                        PREFIX_PAYMENT_STATUS));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser,
                INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid address
        assertParseFailure(parser,
                INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_ADDRESS));

        // invalid emergency contact
        assertParseFailure(parser,
                INVALID_EMERGENCY_CONTACT_DESC
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_EMERGENCY_CONTACT));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser,
                validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser,
                validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid address
        assertParseFailure(parser,
                validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_ADDRESS));

        // invalid emergency contact
        assertParseFailure(parser,
                validExpectedPersonString
                        + INVALID_EMERGENCY_CONTACT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_EMERGENCY_CONTACT));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        Person expectedPerson = new PersonBuilder(AMY).withTags()
                .withLessonSlots().build();
        assertParseSuccess(parser,
                NAME_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                        + EMERGENCY_CONTACT_DESC_AMY
                        + PAYMENT_STATUS_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser,
                VALID_NAME_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                        + EMERGENCY_CONTACT_DESC_BOB,
                expectedMessage);

        // missing emergency contact prefix
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + VALID_EMERGENCY_CONTACT_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser,
                VALID_NAME_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                        + VALID_EMERGENCY_CONTACT_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser,
                INVALID_NAME_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Name.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser,
                NAME_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                        + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Address.MESSAGE_CONSTRAINTS);

        // invalid emergency contact
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + INVALID_EMERGENCY_CONTACT_DESC
                        + PAYMENT_STATUS_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                EmergencyContact.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB
                        + INVALID_TAG_DESC + VALID_TAG_FRIEND,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
                INVALID_NAME_DESC + EMAIL_DESC_BOB
                        + INVALID_ADDRESS_DESC
                        + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + NAME_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                        + PAYMENT_STATUS_DESC_BOB
                        + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_subjectWithoutDayOrTime_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB + SUBJECT_DESC_MATH,
                MESSAGE_LESSON_SLOT_INCOMPLETE);
    }

    @Test
    public void parse_dayWithoutSubjectOrTime_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB + DAY_DESC_MONDAY,
                MESSAGE_LESSON_SLOT_INCOMPLETE);
    }

    @Test
    public void parse_timeWithoutSubjectOrDay_failure() {
        assertParseFailure(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB + TIME_DESC_1400,
                MESSAGE_LESSON_SLOT_INCOMPLETE);
    }

    @Test
    public void parse_subjectDayTimeMismatch_failure() {
        // 2 subjects, 2 days, but only 1 time
        String input = NAME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                + SUBJECT_DESC_MATH + SUBJECT_DESC_ENGLISH
                + DAY_DESC_MONDAY + DAY_DESC_WEDNESDAY + TIME_DESC_1400;
        String expectedMessage = String.format(MESSAGE_SUBJECT_DAY_TIME_MISMATCH, 2, 2, 1);
        assertParseFailure(parser, input, expectedMessage);
    }

    @Test
    public void parse_subjectDayTimeMismatchReverse_failure() {
        // 1 subject, 1 day, but 2 times
        String input = NAME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                + SUBJECT_DESC_MATH + DAY_DESC_MONDAY
                + TIME_DESC_1400 + TIME_DESC_0900;
        String expectedMessage = String.format(MESSAGE_SUBJECT_DAY_TIME_MISMATCH, 1, 1, 2);
        assertParseFailure(parser, input, expectedMessage);
    }

    @Test
    public void parse_matchingLessonSlots_success() {
        // 2 lesson slots with matching subjects, days, and times
        Person expectedPerson = new PersonBuilder(BOB)
                .withLessonSlots("Mathematics", "Monday", "1400",
                        "English", "Wednesday", "0900")
                .build();

        assertParseSuccess(parser, NAME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                + SUBJECT_DESC_MATH + SUBJECT_DESC_ENGLISH
                + DAY_DESC_MONDAY + DAY_DESC_WEDNESDAY
                + TIME_DESC_1400 + TIME_DESC_0900
                + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_noLessonSlots_success() {
        // No subjects, days, or times - should be valid
        Person expectedPerson = new PersonBuilder(BOB).build();

        assertParseSuccess(parser, NAME_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_invalidDay_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                        + SUBJECT_DESC_MATH + INVALID_DAY_DESC + TIME_DESC_1400,
                Day.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTime_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + EMAIL_DESC_BOB
                        + ADDRESS_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                        + SUBJECT_DESC_MATH + DAY_DESC_MONDAY + INVALID_TIME_DESC,
                Time.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_remarkField_success() {
        Person expectedPerson = new PersonBuilder(BOB)
                .withRemark("Needs extra help").build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB
                        + TAG_DESC_FRIEND + TAG_DESC_HUSBAND
                        + " r/Needs extra help",
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_noRemark_defaultsToEmpty() {
        Person expectedPerson = new PersonBuilder(BOB)
                .withRemark("").build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + EMERGENCY_CONTACT_DESC_BOB
                        + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                new AddCommand(expectedPerson));
    }
}
