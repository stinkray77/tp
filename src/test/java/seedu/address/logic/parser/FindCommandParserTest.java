package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.DayMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PaymentStatusMatchesPredicate;
import seedu.address.model.person.SubjectContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_prefixArgs_returnsFindCommand() {
        // name prefix
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, " n/Alice Bob", expectedFindCommand);

        // subject prefix
        expectedFindCommand =
                new FindCommand(new SubjectContainsKeywordsPredicate(Arrays.asList("Math", "English")));
        assertParseSuccess(parser, " s/Math English", expectedFindCommand);

        // day prefix
        expectedFindCommand =
                new FindCommand(new DayMatchesPredicate(Arrays.asList("Monday", "Tuesday")));
        assertParseSuccess(parser, " d/Monday Tuesday", expectedFindCommand);

        // payment status prefix
        expectedFindCommand =
                new FindCommand(new PaymentStatusMatchesPredicate("Due"));
        assertParseSuccess(parser, " ps/Due", expectedFindCommand);

        // tag prefix
        expectedFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("priority", "urgent")));
        assertParseSuccess(parser, " t/priority urgent", expectedFindCommand);
    }

}
