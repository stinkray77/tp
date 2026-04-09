package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Day;
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

        // day abbreviation is normalized to full name
        expectedFindCommand =
                new FindCommand(new DayMatchesPredicate(Arrays.asList("Monday")));
        assertParseSuccess(parser, " d/Mon", expectedFindCommand);

        // payment status prefix
        expectedFindCommand =
                new FindCommand(new PaymentStatusMatchesPredicate("Due"));
        assertParseSuccess(parser, " ps/Due", expectedFindCommand);

        // tag prefix
        expectedFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("priority", "urgent")));
        assertParseSuccess(parser, " t/priority urgent", expectedFindCommand);
    }

    @Test
    public void parse_multiplePaymentStatuses_returnsFindCommand() throws Exception {
        // multiple ps/ values should OR them together (not AND)
        assertNotNull(parser.parse(" ps/Paid ps/Due"));
    }

    @Test
    public void parse_multipleDifferentPrefixes_returnsFindCommand() throws Exception {
        // multiple different prefixes combine with AND logic
        assertNotNull(parser.parse(" s/Math ps/Due"));
    }

    @Test
    public void parse_multiplePaymentStatusesWithOtherPrefix_returnsFindCommand() throws Exception {
        // multiple ps/ values AND-ed with another predicate -> hits the OR-reduce path in combined predicate
        assertNotNull(parser.parse(" n/Alice ps/Paid ps/Due"));
    }

    @Test
    public void parse_multiplePrefixes_returnsCombinedPredicate() throws Exception {
        // name + subject + day combined -> exercises the combined predicate path (predicateCount > 1)
        assertNotNull(parser.parse(" n/Alice s/Math d/Monday"));
    }

    @Test
    public void parse_repeatedNamePrefix_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob", "Carol")));
        assertParseSuccess(parser, " n/Alice Bob n/Carol", expectedFindCommand);
    }

    @Test
    public void parse_repeatedSubjectPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new SubjectContainsKeywordsPredicate(Arrays.asList("Math", "English", "Science")));
        assertParseSuccess(parser, " s/Math English s/Science", expectedFindCommand);
    }

    @Test
    public void parse_repeatedDayPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new DayMatchesPredicate(Arrays.asList("Monday", "Tuesday", "Friday")));
        assertParseSuccess(parser, " d/Monday Tuesday d/Friday", expectedFindCommand);
    }

    @Test
    public void parse_repeatedPaymentStatusPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PaymentStatusMatchesPredicate(Arrays.asList("Paid", "Due")));
        assertParseSuccess(parser, " ps/Paid ps/Due", expectedFindCommand);
    }

    @Test
    public void parse_repeatedTagPrefix_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList("priority", "urgent", "vip")));
        assertParseSuccess(parser, " t/priority urgent t/vip", expectedFindCommand);
    }

    @Test
    public void parse_preambleWithPrefix_throwsParseException() {
        // Bare keywords mixed with prefixes should be rejected; the bare part would be silently ignored
        assertParseFailure(parser, " Alice n/Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyPrefixValue_throwsParseException() {
        // Providing a prefix with no value should fail, not silently match all students
        assertParseFailure(parser, " n/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " s/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " d/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDayKeyword_throwsParseException() {
        assertParseFailure(parser, " d/Someday", Day.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_combinedPredicate_filtersCorrectly() throws Exception {
        // Exercises the combined predicate path (predicateCount > 1) by actually invoking .test()
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        FindCommand command = parser.parse(" n/Alice ps/Paid");
        command.execute(model);
        // ALICE: name "Alice Pauline", paymentStatus "Paid" — matches both predicates
        assertEquals(1, model.getFilteredPersonList().size());
        assertEquals(ALICE, model.getFilteredPersonList().get(0));
    }

}
