package seedu.geekeep.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.geekeep.commons.core.UnmodifiableObservableList;
import seedu.geekeep.commons.exceptions.IllegalValueException;
import seedu.geekeep.logic.commands.exceptions.CommandException;
import seedu.geekeep.model.tag.Tag;
import seedu.geekeep.model.tag.UniqueTagList;
import seedu.geekeep.model.task.ReadOnlyTask;
import seedu.geekeep.model.task.Task;
import seedu.geekeep.model.task.UniqueTaskList;
import seedu.geekeep.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.geekeep.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.geekeep.model.util.IndexKeeper;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by .equals comparison)
 */
public class TaskManager implements ReadOnlyTaskManager {

    private final UniqueTaskList tasks;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication between
     * constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication among
     * constructors.
     */
    {
        tasks = new UniqueTaskList();
        tags = new UniqueTagList();
    }

    public TaskManager() {
    }

    /**
     * Creates an TaskManager using the Tasks and Tags in the {@code toBeCopied}
     */
    public TaskManager(ReadOnlyTaskManager toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /** generate a random 4-digit TaskId */
    public int getNextId() {
        Random numberGenerator = new Random();
        int id = numberGenerator.nextInt(9999);

        Iterator<Task> ir = tasks.iterator();
        while (ir.hasNext()) {
            Task task = ir.next();
            if (task.getId() == id) {
                return getNextId();
            }
        }
        IndexKeeper.addNewId(id);
        return id;
    }

    //// list overwrite operations

    /**
     * Adds a task to the address book. Also checks the new task's tags and updates {@link #tags} with any new tags
     * found, and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException
     *             if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicateTaskException {
        syncMasterTagListWith(p);
        p.setId(getNextId());
        tasks.add(p);
    }

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }


    public ReadOnlyTask getTaskById(int id) throws TaskNotFoundException {
        Iterator<Task> ir = tasks.iterator();
        while (ir.hasNext()) {
            Task task = ir.next();
            if (task.getId() == id) {
                return task;
            }
        }

        throw new TaskNotFoundException();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskManager // instanceof handles nulls
                        && this.tasks.equals(((TaskManager) other).tasks)
                        && this.tags.equalsOrderInsensitive(((TaskManager) other).tags));
    }

    //// task-level operations

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {

        return new UnmodifiableObservableList<>(tasks.asObservableList());

    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tasks, tags);
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        if (tasks.remove(key)) {
            boolean successRemoval = IndexKeeper.removeExistedId(key.getId());
            assert successRemoval == true;
            System.out.println(IndexKeeper.getExistedIds().size());
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

    public void resetData(ReadOnlyTaskManager newData) {
        assert newData != null;
        if (newData.getTaskList().isEmpty()) {
            IndexKeeper.resetIds();
        }
        try {

            setTasks(newData.getTaskList());

        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "TaskManager should not have duplicate tasks";
        } catch (IllegalValueException ive) {
            assert false : "TaskManager tasks startDateTime should be matched"
                    + "with a later endDateTime";
        }
        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "TaskMangaer should not have duplicate tags";
        }
        syncMasterTagListWith(tasks);
    }

    //// tag-level operations

    public void setTasks(List<? extends ReadOnlyTask> tasks)
            throws UniqueTaskList.DuplicateTaskException, IllegalValueException {
        Task task;
        for (final ReadOnlyTask readOnlyTask : tasks) {
            task = (Task) readOnlyTask;
            if (task.getId() == -1) {
                task.setId(getNextId());
            }
        }
        this.tasks.setTasks(tasks);
    }

    //// util methods

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    /**
     * Ensures that every tag in this task: - exists in the master list {@link #tags} - points to a Tag object in the
     * master list
     */
    private void syncMasterTagListWith(Task task) {
        final UniqueTagList taskTags = task.getTags();
        tags.mergeFrom(taskTags);

        // Create map with values = tag object references in the master list
        // used for checking task tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of task tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        taskTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        task.setTags(new UniqueTagList(correctTagReferences));
    }

    /**
     * Ensures that every tag in these tasks: - exists in the master list {@link #tags} - points to a Tag object in
     * the master list
     *
     * @see #syncMasterTagListWith(Task)
     */
    private void syncMasterTagListWith(UniqueTaskList tasks) {
        tasks.forEach(this::syncMasterTagListWith);
    }

    @Override
    public String toString() {
        return tasks.asObservableList().size() + " tasks, " + tags.asObservableList().size() + " tags";
        // TODO: refine later
    }

    /**
     * Updates the task in the list at position {@code index} with {@code editedReadOnlyTask}. {@code AddressBook}'s
     * tag list will be updated with the tags of {@code editedReadOnlyTask}.
     *
     * @see #syncMasterTagListWith(Task)
     *
     * @throws DuplicateTaskException
     *             if updating the task's details causes the task to be equivalent to another existing task in the
     *             list.
     * @throws TaskNotFoundException
     * @throws CommandException
     * @throws IndexOutOfBoundsException
     *             if {@code index} < 0 or >= the size of the list.
     */
    public void updateTask(int index, ReadOnlyTask editedReadOnlyTask)
            throws UniqueTaskList.DuplicateTaskException, IllegalValueException,
            TaskNotFoundException {
        assert editedReadOnlyTask != null;

        Task editedTask;
        try {
            editedTask = new Task(editedReadOnlyTask);
        } catch (IllegalValueException ive) {
            throw new IllegalValueException(ive.getMessage());
        }
        syncMasterTagListWith(editedTask);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any task
        // in the task list.
        tasks.updateTask(index, editedTask);
    }

    public void markTaskDone(int index) {
        try {
            Task task = (Task) getTaskById(index);
            task.markDone();
        } catch (TaskNotFoundException e) {

        }


    }

    public void markTaskUndone(int index) {
        try {
            Task task = (Task) getTaskById(index);
            task.markUndone();
        } catch (TaskNotFoundException e) {

        }
    }
}
