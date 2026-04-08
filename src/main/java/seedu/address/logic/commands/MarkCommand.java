package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;

/**
 * Updates the payment status of an existing student in Tutor Central.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the payment status of the student identified "
            + "by the index number used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) ps/PAYMENT_STATUS\n"
            + "Example: " + COMMAND_WORD + " 1 ps/Paid";

    public static final String MESSAGE_MARK_SUCCESS = "Updated payment status of %1$s to %2$s";

    private final Index targetIndex;
    private final PaymentStatus newStatus;

    /**
     * @param targetIndex of the person in the filtered person list to edit
     * @param newStatus new payment status of the person
     */
    public MarkCommand(Index targetIndex, PaymentStatus newStatus) {
        requireNonNull(targetIndex);
        requireNonNull(newStatus);

        this.targetIndex = targetIndex;
        this.newStatus = newStatus;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(targetIndex.getZeroBased());
        Person markedPerson = new Person(
                personToMark.getName(),
                personToMark.getEmail(),
                personToMark.getAddress(),
                personToMark.getSubjects(),
                personToMark.getDays(),
                personToMark.getTimes(),
                personToMark.getEmergencyContact(),
                newStatus,
                personToMark.getRemark(),
                personToMark.getTags(),
                personToMark.getAttendanceRecords()
        );

        model.setPerson(personToMark, markedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_MARK_SUCCESS, personToMark.getName(), newStatus.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MarkCommand otherMarkCommand)) {
            return false;
        }

        return targetIndex.equals(otherMarkCommand.targetIndex)
                && newStatus.equals(otherMarkCommand.newStatus);
    }
}
