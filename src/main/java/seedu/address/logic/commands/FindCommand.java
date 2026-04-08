package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Finds and lists all students in Tutor Central whose names contain any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all students whose names, subjects, days, "
            + "payment status, or tags contain any of the specified keywords (case-insensitive) and displays them "
            + "as a list with index numbers.\n"
            + "Parameters: [n/NAME] [s/SUBJECT] [d/DAY] [ps/PAYMENT_STATUS] [t/TAG] [KEYWORDS...]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " alice bob charlie          -> find by name without prefixes\n"
            + "  " + COMMAND_WORD + " n/alice                    -> find by name\n"
            + "  " + COMMAND_WORD + " s/Mathematics              -> find by subject\n"
            + "  " + COMMAND_WORD + " d/Monday                   -> find by day\n"
            + "  " + COMMAND_WORD + " ps/Due                     -> find by payment status\n"
            + "  " + COMMAND_WORD + " t/priority                 -> find by tag\n"
            + "  " + COMMAND_WORD + " s/Math d/Monday            -> find students with Math on Monday (AND logic)";

    private final Predicate<Person> predicate;

    public FindCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
