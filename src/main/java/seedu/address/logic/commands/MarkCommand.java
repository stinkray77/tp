package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;

/**
 * Marks the payment status of an existing person in the address book.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the payment status of the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_PAYMENT_STATUS + "PAYMENT_STATUS\n"
            + "Payment status must be one of: Paid, Due, Overdue (case-insensitive)\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_PAYMENT_STATUS + "Paid";

    public static final String MESSAGE_MARK_SUCCESS = "Marked %1$s as %2$s.";

    private final Index index;
    private final PaymentStatus paymentStatus;

    /**
     * @param index         of the person in the filtered person list to mark
     * @param paymentStatus the new payment status to set
     */
    public MarkCommand(Index index, PaymentStatus paymentStatus) {
        requireNonNull(index);
        requireNonNull(paymentStatus);

        this.index = index;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(index.getZeroBased());
        Person markedPerson = new Person(
                personToMark.getName(), personToMark.getEmail(),
                personToMark.getAddress(), personToMark.getSubjects(),
                personToMark.getDays(), personToMark.getTimes(),
                personToMark.getEmergencyContact(),
                paymentStatus, personToMark.getRemark(), personToMark.getTags());

        model.setPerson(personToMark, markedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(
                String.format(MESSAGE_MARK_SUCCESS, markedPerson.getName(), paymentStatus));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return index.equals(otherMarkCommand.index)
                && paymentStatus.equals(otherMarkCommand.paymentStatus);
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "{index=" + index + ", paymentStatus=" + paymentStatus + "}";
    }
}
