package seedu.geekeep.model.task;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.geekeep.commons.core.UnmodifiableObservableList;
import seedu.geekeep.commons.exceptions.DuplicateDataException;
import seedu.geekeep.commons.util.CollectionUtil;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicatePersonException extends DuplicateDataException {
        protected DuplicatePersonException() {
            super("Operation would result in duplicate persons");
        }
    }

    /**
     * Signals that an operation targeting a specified person in the list would fail because there is no such matching
     * person in the list.
     */
    public static class PersonNotFoundException extends Exception {
    }

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();

    /**
     * Adds a person to the list.
     *
     * @throws DuplicatePersonException
     *             if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Task toAdd) throws DuplicatePersonException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    public UnmodifiableObservableList<Task> asObservableList() {
        return new UnmodifiableObservableList<>(internalList);
    }

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                        && this.internalList.equals(((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws PersonNotFoundException
     *             if no such person could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws PersonNotFoundException {
        assert toRemove != null;
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new PersonNotFoundException();
        }
        return personFoundAndDeleted;
    }

    public void setPersons(List<? extends ReadOnlyTask> persons) throws DuplicatePersonException {
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final ReadOnlyTask person : persons) {
            replacement.add(new Task(person));
        }
        setPersons(replacement);
    }

    public void setPersons(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    /**
     * Updates the person in the list at position {@code index} with {@code editedPerson}.
     *
     * @throws DuplicatePersonException
     *             if updating the person's details causes the person to be equivalent to another existing person in the
     *             list.
     * @throws IndexOutOfBoundsException
     *             if {@code index} < 0 or >= the size of the list.
     */
    public void updatePerson(int index, ReadOnlyTask editedPerson) throws DuplicatePersonException {
        assert editedPerson != null;

        Task taskToUpdate = internalList.get(index);
        if (!taskToUpdate.equals(editedPerson) && internalList.contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        taskToUpdate.resetData(editedPerson);
        // TODO: The code below is just a workaround to notify observers of the updated person.
        // The right way is to implement observable properties in the Person class.
        // Then, PersonCard should then bind its text labels to those observable properties.
        internalList.set(index, taskToUpdate);
    }

}
