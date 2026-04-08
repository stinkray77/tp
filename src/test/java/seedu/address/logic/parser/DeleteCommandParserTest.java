package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        // EP: valid plain positive integer index
        assertParseSuccess(parser, "1", new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // EP: wrong command shape - empty input
        assertParseFailure(parser, " ", DeleteCommandParser.MESSAGE_INVALID_FORMAT);

        // EP: wrong command shape - more than one argument
        assertParseFailure(parser, "1 2", DeleteCommandParser.MESSAGE_INVALID_FORMAT);

        // EP: not numeric at all
        assertParseFailure(parser, "a", DeleteCommandParser.MESSAGE_NOT_A_NUMBER);

        // EP: wrong command shape - extra trailing argument
        assertParseFailure(parser, "1 a", DeleteCommandParser.MESSAGE_INVALID_FORMAT);

        // EP: numeric, but not a valid index - zero
        assertParseFailure(parser, "0", DeleteCommandParser.MESSAGE_INVALID_INDEX);

        // EP: numeric, but not a valid index - negative integer
        assertParseFailure(parser, "-1", DeleteCommandParser.MESSAGE_INVALID_INDEX);

        // EP: numeric, but not a valid index - explicitly signed positive integer
        assertParseFailure(parser, "+2", DeleteCommandParser.MESSAGE_INVALID_INDEX);

        // EP: numeric, but not a valid index - decimal with zero fractional part
        assertParseFailure(parser, "1.0", DeleteCommandParser.MESSAGE_INVALID_INDEX);

        // EP: numeric, but not a valid index - decimal with non-zero fractional part
        assertParseFailure(parser, "1.5", DeleteCommandParser.MESSAGE_INVALID_INDEX);
    }
}
