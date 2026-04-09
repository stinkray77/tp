package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {

    private RemarkCommandParser parser = new RemarkCommandParser();
    private final String nonEmptyRemark = "Some remark.";

    @Test
    public void parse_indexSpecified_success() {
        // EP: valid index with non-empty remark
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_REMARK + nonEmptyRemark;
        RemarkCommand expectedCommand = new RemarkCommand(
                INDEX_FIRST_PERSON, new Remark(nonEmptyRemark));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);

        // EP: wrong command shape - no parameters
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD, expectedMessage);

        // EP: wrong command shape - missing index
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD + " "
                + nonEmptyRemark, expectedMessage);
    }

    @Test
    public void parse_noRemarkPrefix_failure() {
        // EP: missing required remark prefix
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        String userInput = String.valueOf(INDEX_FIRST_PERSON.getOneBased());
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_emptyRemark_success() {
        // EP: valid index with empty remark
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_REMARK;
        RemarkCommand expectedCommand = new RemarkCommand(
                INDEX_FIRST_PERSON, new Remark(""));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_duplicateRemarkPrefix_failure() {
        // EP: duplicate single-valued remark prefix
        assertParseFailure(parser,
                INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_REMARK + "one "
                        + PREFIX_REMARK + "two",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
    }
}
