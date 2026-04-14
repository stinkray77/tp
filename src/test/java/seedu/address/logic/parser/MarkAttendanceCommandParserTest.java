package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Day;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;

public class MarkAttendanceCommandParserTest {

    private static final String VALID_SUBJECT = "Mathematics";
    private static final String VALID_DAY = "Monday";
    private static final String VALID_TIME = "1400";
    private static final String VALID_STATUS = "Present";

    private MarkAttendanceCommandParser parser = new MarkAttendanceCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        MarkAttendanceCommand expected = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, AttendanceStatus.PRESENT);

        assertParseSuccess(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expected);
    }

    @Test
    public void parse_caseInsensitiveStatus_success() {
        MarkAttendanceCommand expected = new MarkAttendanceCommand(
                INDEX_FIRST_PERSON, VALID_SUBJECT, VALID_DAY, VALID_TIME, AttendanceStatus.PRESENT);

        assertParseSuccess(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + "present",
                expected);

        assertParseSuccess(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + "PRESENT",
                expected);
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser,
                PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expectedMessage);
    }

    @Test
    public void parse_missingSubject_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser,
                "1 " + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expectedMessage);
    }

    @Test
    public void parse_missingDay_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expectedMessage);
    }

    @Test
    public void parse_missingTime_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expectedMessage);
    }

    @Test
    public void parse_missingStatus_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME,
                expectedMessage);
    }

    @Test
    public void parse_invalidStatus_failure() {
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + "Invalid",
                AttendanceStatus.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidSubjectFormat_failure() {
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + "!!invalid "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDayFormat_failure() {
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + "Mon@day "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                Day.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTimeFormat_failure() {
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + "14pm "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                Time.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = "Invalid index: " + ParserUtil.MESSAGE_INVALID_INDEX
                + "\n" + MarkAttendanceCommand.MESSAGE_USAGE;
        assertParseFailure(parser,
                "0 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expectedMessage);
        assertParseFailure(parser,
                "-1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expectedMessage);
    }

    @Test
    public void parse_emptyArgs_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                MarkAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_duplicatePrefixes_failure() {
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " " + PREFIX_SUBJECT + "English "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SUBJECT));

        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " " + PREFIX_DAY + "Tuesday "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DAY));

        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS + " "
                        + PREFIX_ATTENDANCE_STATUS + "Absent",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ATTENDANCE_STATUS));
    }

    @Test
    public void parse_multipleIndices_success() {
        Index[] expectedIndices = new Index[]{INDEX_FIRST_PERSON, INDEX_SECOND_PERSON};
        MarkAttendanceCommand expected = new MarkAttendanceCommand(
                expectedIndices, VALID_SUBJECT, VALID_DAY, VALID_TIME, AttendanceStatus.PRESENT);

        assertParseSuccess(parser,
                "1,2 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expected);
    }

    @Test
    public void parse_multipleIndicesWithSpaces_success() {
        Index[] expectedIndices = new Index[]{INDEX_FIRST_PERSON, INDEX_SECOND_PERSON};
        MarkAttendanceCommand expected = new MarkAttendanceCommand(
                expectedIndices, VALID_SUBJECT, VALID_DAY, VALID_TIME, AttendanceStatus.PRESENT);

        assertParseSuccess(parser,
                "1, 2 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expected);
    }

    @Test
    public void parse_threeIndices_success() {
        Index[] expectedIndices = new Index[]{
            INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, Index.fromOneBased(3)};
        MarkAttendanceCommand expected = new MarkAttendanceCommand(
                expectedIndices, VALID_SUBJECT, VALID_DAY, VALID_TIME, AttendanceStatus.PRESENT);

        assertParseSuccess(parser,
                "1,2,3 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expected);
    }

    @Test
    public void parse_multipleIndicesInvalidIndex_failure() {
        String expectedMessage = "Invalid index: " + ParserUtil.MESSAGE_INVALID_INDEX
                + "\n" + MarkAttendanceCommand.MESSAGE_USAGE;
        assertParseFailure(parser,
                "1,0 " + PREFIX_SUBJECT + VALID_SUBJECT + " "
                        + PREFIX_DAY + VALID_DAY + " "
                        + PREFIX_TIME + VALID_TIME + " "
                        + PREFIX_ATTENDANCE_STATUS + VALID_STATUS,
                expectedMessage);
    }
}
