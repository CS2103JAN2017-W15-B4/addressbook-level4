package seedu.geekeep.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.geekeep.commons.exceptions.IllegalValueException;
import seedu.geekeep.logic.commands.exceptions.CommandException;
import seedu.geekeep.model.tag.Tag;
import seedu.geekeep.model.tag.UniqueTagList;
import seedu.geekeep.model.task.Location;
import seedu.geekeep.model.task.StartDateTime;
import seedu.geekeep.model.task.EndDateTime;
import seedu.geekeep.model.task.Task;
import seedu.geekeep.model.task.Title;
import seedu.geekeep.model.task.UniqueTaskList;

/**
 * Adds a task to the Task Manager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the Task Manager. "
            + "Parameters: TITLE st/STARTING_DATETIME et/EMAIL a/ADDRESS  [t/TAG]...\n"
            + "Example: " + COMMAND_WORD
            + " John Doe p/98765432 e/johnd@gmail.com a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Task toAdd;

    /**
     * Creates an AddCommand using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String name, String phone, String email, String address, Set<String> tags)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toAdd = new Task(
                new Title(name),
                new StartDateTime(phone),
                new EndDateTime(email),
                new Location(address),
                new UniqueTagList(tagSet)
        );
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

    }

}
