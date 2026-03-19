package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    public static final String MESSAGE_INVALID_FORMAT = "Invalid command format. Usage: delete INDEX";
    public static final String MESSAGE_NOT_A_NUMBER = "Not a number: Invalid index. Use an integer (e.g. delete 3)";

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        // Reject multiple arguments (e.g.. "delete 1 2")
        String[] splitArgs = trimmedArgs.split("\\s+");
        if (splitArgs.length > 1) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        try {
            Index index = ParserUtil.parseIndex(trimmedArgs); // Use trimmedArgs here
            return new DeleteCommand(index);
        } catch (ParseException pe) {
            // Throw your specific custom error message
            throw new ParseException(MESSAGE_NOT_A_NUMBER);
        }
    }

}
