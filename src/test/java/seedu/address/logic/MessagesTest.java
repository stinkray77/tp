package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.Prefix;

public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_singlePrefix_usesLabel() {
        String msg = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME);
        assertTrue(msg.contains("Name"),
                "Expected label 'Name' in duplicate-prefix message but got: " + msg);
        assertFalse(msg.contains("n/"),
                "Duplicate-prefix message should surface field label, not prefix token");
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_multiplePrefixes_joinedWithComma() {
        String msg = Messages.getErrorMessageForDuplicatePrefixes(
                PREFIX_NAME, PREFIX_EMAIL, PREFIX_PAYMENT_STATUS);
        assertTrue(msg.contains("Name"));
        assertTrue(msg.contains("Email"));
        assertTrue(msg.contains("Payment Status"));
        assertTrue(msg.contains(","),
                "Multiple duplicates should be comma-separated but got: " + msg);
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_unknownPrefix_fallsBackToToString() {
        Prefix unknown = new Prefix("zz/");
        String msg = Messages.getErrorMessageForDuplicatePrefixes(unknown);
        assertTrue(msg.contains("zz/"),
                "Unknown prefix should fall back to its toString form, got: " + msg);
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_preservesStandardPrefixText() {
        String msg = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME);
        assertEquals(
                Messages.MESSAGE_DUPLICATE_FIELDS + "Name",
                msg);
    }
}
