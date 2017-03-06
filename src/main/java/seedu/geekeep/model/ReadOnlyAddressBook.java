package seedu.geekeep.model;

import javafx.collections.ObservableList;
import seedu.geekeep.model.person.ReadOnlyPerson;
import seedu.geekeep.model.tag.Tag;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list. This list will not contain any duplicate persons.
     */
    ObservableList<ReadOnlyPerson> getPersonList();

    /**
     * Returns an unmodifiable view of the tags list. This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
