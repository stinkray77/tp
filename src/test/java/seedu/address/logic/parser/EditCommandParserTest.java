package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_DAY_TIME_INCOMPLETE;
import static seedu.address.logic.Messages.MESSAGE_DAY_TIME_MISMATCH;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DAY_DESC_MONDAY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMERGENCY_CONTACT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_0900;
import static seedu.address.logic.commands.CommandTestUtil.TIME_DESC_1400;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, VALID_NAME_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser, "1",
                EditCommand.MESSAGE_NOT_EDITED);

        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + NAME_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser, "0" + NAME_DESC_AMY,
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser, "1 some random string",
                MESSAGE_INVALID_FORMAT);

        assertParseFailure(parser, "1 i/ string",
                MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC,
                Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser,
                "1" + INVALID_EMERGENCY_CONTACT_DESC,
                EmergencyContact.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC,
                Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC,
                Address.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + INVALID_TAG_DESC,
                Tag.MESSAGE_CONSTRAINTS);

        // invalid ec followed by valid email
        assertParseFailure(parser,
                "1" + INVALID_EMERGENCY_CONTACT_DESC + EMAIL_DESC_AMY,
                EmergencyContact.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                "1" + TAG_DESC_FRIEND + TAG_DESC_HUSBAND + TAG_EMPTY,
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser,
                "1" + TAG_DESC_FRIEND + TAG_EMPTY + TAG_DESC_HUSBAND,
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser,
                "1" + TAG_EMPTY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser,
                "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC
                        + VALID_ADDRESS_AMY
                        + VALID_EMERGENCY_CONTACT_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased()
                + EMERGENCY_CONTACT_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY
                + TAG_DESC_FRIEND;

        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                        .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                        .withEmail(VALID_EMAIL_AMY)
                        .withAddress(VALID_ADDRESS_AMY)
                        .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
                        .build();
        EditCommand expectedCommand =
                new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased()
                + EMERGENCY_CONTACT_DESC_BOB + EMAIL_DESC_AMY;

        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder()
                        .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                        .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand =
                new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder()
                        .withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand =
                new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // emergency contact
        userInput = targetIndex.getOneBased()
                + EMERGENCY_CONTACT_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder()
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_AMY)
                .build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder()
                .withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder()
                .withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditPersonDescriptorBuilder()
                .withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // remark
        userInput = targetIndex.getOneBased() + REMARK_DESC_AMY;
        descriptor = new EditPersonDescriptorBuilder()
                .withRemark(VALID_REMARK_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased()
                + INVALID_EMERGENCY_CONTACT_DESC
                + EMERGENCY_CONTACT_DESC_BOB;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_EMERGENCY_CONTACT));

        userInput = targetIndex.getOneBased()
                + EMERGENCY_CONTACT_DESC_BOB
                + INVALID_EMERGENCY_CONTACT_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_EMERGENCY_CONTACT));

        userInput = targetIndex.getOneBased()
                + EMERGENCY_CONTACT_DESC_AMY + ADDRESS_DESC_AMY
                + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + EMERGENCY_CONTACT_DESC_AMY + ADDRESS_DESC_AMY
                + EMAIL_DESC_AMY + TAG_DESC_FRIEND
                + EMERGENCY_CONTACT_DESC_BOB + ADDRESS_DESC_BOB
                + EMAIL_DESC_BOB + TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_EMERGENCY_CONTACT, PREFIX_EMAIL,
                        PREFIX_ADDRESS));

        userInput = targetIndex.getOneBased()
                + INVALID_EMERGENCY_CONTACT_DESC
                + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC
                + INVALID_EMERGENCY_CONTACT_DESC
                + INVALID_ADDRESS_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(
                        PREFIX_EMERGENCY_CONTACT, PREFIX_EMAIL,
                        PREFIX_ADDRESS));

        // duplicate remark
        userInput = targetIndex.getOneBased()
                + REMARK_DESC_AMY + REMARK_DESC_AMY;
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditPersonDescriptor descriptor =
                new EditPersonDescriptorBuilder().withTags().build();
        EditCommand expectedCommand =
                new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_daysWithoutTimes_failure() {
        // Edit with day, but no time
        assertParseFailure(parser, "1" + DAY_DESC_MONDAY, MESSAGE_DAY_TIME_INCOMPLETE);
    }

    @Test
    public void parse_timesWithoutDays_failure() {
        // Edit with time, but no day
        assertParseFailure(parser, "1" + TIME_DESC_1400, MESSAGE_DAY_TIME_INCOMPLETE);
    }

    @Test
    public void parse_dayTimeMismatch_failure() {
        // Edit with 2 days, but only 1 time
        String input = "1" + DAY_DESC_MONDAY + TIME_DESC_0900 + TIME_DESC_1400;
        String expectedMessage = String.format(MESSAGE_DAY_TIME_MISMATCH, 1, 2);
        assertParseFailure(parser, input, expectedMessage);
    }

    @Test
    public void parse_matchingDaysAndTimes_success() {
        // Edit with matching day and time
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withDays("Monday")
                .withTimes("1400")
                .build();

        assertParseSuccess(parser, "1" + DAY_DESC_MONDAY + TIME_DESC_1400,
                new EditCommand(INDEX_FIRST_PERSON, descriptor));
    }

    @Test
    public void parse_bothDaysAndTimesCleared_success() {
        // Edit with empty day and empty time (clearing both)
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withDays()
                .withTimes()
                .build();

        assertParseSuccess(parser, "1 d/ ti/",
                new EditCommand(INDEX_FIRST_PERSON, descriptor));
    }
}
