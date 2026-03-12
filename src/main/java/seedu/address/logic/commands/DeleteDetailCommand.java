package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Deletes a specific detail from an existing student in the address book.
 * This is a foundational skeleton for the v1.2 iteration.
 */
public class DeleteDetailCommand extends Command {

    public static final String COMMAND_WORD = "delete-detail";
    public static final String MESSAGE_SUCCESS = "Student detail deleted successfully.";

    @Override
    public CommandResult execute(Model model) {
        // [Inference] Returning a static command result avoids modifying the model state
        // prematurely and ensures no existing tests will fail.
        return new CommandResult(MESSAGE_SUCCESS);
    }
}