package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.model.person.PaymentStatus;

public class MarkCommandParserTest {

    private MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser, "1 ps/Paid",
                new MarkCommand(INDEX_FIRST_PERSON, new PaymentStatus("Paid")));
    }

    @Test
    public void parse_caseInsensitiveStatus_success() {
        assertParseSuccess(parser,
                "1 " + PREFIX_PAYMENT_STATUS + "paid",
                new MarkCommand(INDEX_FIRST_PERSON, new PaymentStatus("Paid")));

        assertParseSuccess(parser,
                "1 " + PREFIX_PAYMENT_STATUS + "OVERDUE",
                new MarkCommand(INDEX_FIRST_PERSON, new PaymentStatus("Overdue")));
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "ps/Paid", expectedMessage);
    }

    @Test
    public void parse_missingPaymentStatus_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1", expectedMessage);
    }

    @Test
    public void parse_invalidPaymentStatus_failure() {
        assertParseFailure(parser, "1 ps/Invalid", PaymentStatus.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = "Invalid index: " + ParserUtil.MESSAGE_INVALID_INDEX
                + "\n" + MarkCommand.MESSAGE_USAGE;
        assertParseFailure(parser, "0 ps/Paid", expectedMessage);
        assertParseFailure(parser, "-1 ps/Paid", expectedMessage);
    }

    @Test
    public void parse_emptyArgs_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_duplicatePaymentStatus_failure() {
        assertParseFailure(parser, "1 ps/Paid ps/Due",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PAYMENT_STATUS));
    }
}
