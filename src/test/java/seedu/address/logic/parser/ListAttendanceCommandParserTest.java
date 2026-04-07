package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ListAttendanceCommand;
import seedu.address.model.person.Subject;

public class ListAttendanceCommandParserTest {

    private ListAttendanceCommandParser parser = new ListAttendanceCommandParser();

    @Test
    public void parse_validIndexOnly_success() {
        // EP: valid index without optional subject filter
        assertParseSuccess(parser, "1", new ListAttendanceCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void parse_validIndexAndSubject_success() {
        // EP: valid index with valid subject filter
        assertParseSuccess(parser, "1 " + PREFIX_SUBJECT + "Mathematics",
                new ListAttendanceCommand(INDEX_FIRST_PERSON, "Mathematics"));
    }

    @Test
    public void parse_validIndexAndSubjectWithWhitespace_success() {
        // EP: valid input with extra surrounding whitespace
        assertParseSuccess(parser, "   1   " + PREFIX_SUBJECT + "  Mathematics  ",
                new ListAttendanceCommand(INDEX_FIRST_PERSON, "Mathematics"));
    }

    @Test
    public void parse_missingIndex_failure() {
        // EP: missing required index
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREFIX_SUBJECT + "Mathematics", expectedMessage);
    }

    @Test
    public void parse_invalidSubject_failure() {
        // EP: invalid subject format
        assertParseFailure(parser, "1 " + PREFIX_SUBJECT + "!!invalid", Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // EP: invalid index values from the non-positive integer partition
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "0", expectedMessage);
        assertParseFailure(parser, "-1", expectedMessage);
    }

    @Test
    public void parse_emptyArgs_failure() {
        // EP: empty input string
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAttendanceCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_duplicateSubject_failure() {
        // EP: duplicate optional subject prefix
        assertParseFailure(parser,
                "1 " + PREFIX_SUBJECT + "Mathematics " + PREFIX_SUBJECT + "English",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SUBJECT));
    }

    @Test
    public void parse_nullArg_throwsNullPointerException() {
        // EP: null parser input
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }
}
