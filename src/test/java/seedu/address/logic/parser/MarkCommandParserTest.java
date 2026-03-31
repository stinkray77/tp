package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MarkCommand;
import seedu.address.model.person.PaymentStatus;

public class MarkCommandParserTest {

    private MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_validArgs_returnsMarkCommand() {
        assertParseSuccess(parser,
                "1 " + PREFIX_PAYMENT_STATUS + "Paid",
                new MarkCommand(INDEX_FIRST_PERSON, new PaymentStatus("Paid")));
    }

    @Test
    public void parse_caseInsensitiveStatus_returnsMarkCommand() {
        assertParseSuccess(parser,
                "1 " + PREFIX_PAYMENT_STATUS + "paid",
                new MarkCommand(INDEX_FIRST_PERSON, new PaymentStatus("Paid")));

        assertParseSuccess(parser,
                "1 " + PREFIX_PAYMENT_STATUS + "OVERDUE",
                new MarkCommand(INDEX_FIRST_PERSON, new PaymentStatus("Overdue")));
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser,
                PREFIX_PAYMENT_STATUS + "Paid",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPaymentStatus_failure() {
        assertParseFailure(parser,
                "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPaymentStatus_failure() {
        assertParseFailure(parser,
                "1 " + PREFIX_PAYMENT_STATUS + "invalid",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertParseFailure(parser,
                "abc " + PREFIX_PAYMENT_STATUS + "Paid",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        assertParseFailure(parser,
                "0 " + PREFIX_PAYMENT_STATUS + "Paid",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }
}
