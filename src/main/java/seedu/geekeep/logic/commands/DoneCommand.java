package seedu.geekeep.logic.commands;

import seedu.geekeep.commons.core.Messages;
import seedu.geekeep.logic.commands.exceptions.CommandException;
import seedu.geekeep.model.task.ReadOnlyTask;
import seedu.geekeep.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Marks 'done' for task identified using it's last displayed index from the address book.
 */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the index number used in the last task listing as done.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Marked as done: %1$s";

    public final int targetIndex;

    public DoneCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        ReadOnlyTask taskToMark;
        try {
            taskToMark = model.getTaskById(targetIndex);
        } catch (TaskNotFoundException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        model.markTaskDone(targetIndex);

        return new CommandResult(String.format(MESSAGE_DONE_TASK_SUCCESS, taskToMark));
    }

}
