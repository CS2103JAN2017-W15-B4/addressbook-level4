package seedu.geekeep.testutil;

import java.util.Random;

import seedu.geekeep.commons.exceptions.IllegalValueException;
import seedu.geekeep.model.TaskManager;
import seedu.geekeep.model.task.Task;
import seedu.geekeep.model.task.UniqueTaskList;
import seedu.geekeep.model.util.IndexKeeper;

/**
 *
 */
public class TypicalTestPersons {

    public TestTask alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalTestPersons() {
        try {
            alice = new PersonBuilder().withName("Alice Pauline")
                    .withLocation("123, Jurong West Ave 6, #08-111").withStartDateTime("01-04-17 1630")
                    .withEndDateTime("01-05-17 1630")
                    .withTags("friends").build();
            benson = new PersonBuilder().withName("Benson Meier").withLocation("311, Clementi Ave 2, #02-25")
                    .withStartDateTime("01-04-17 1630").withEndDateTime("01-05-17 1630")
                    .withTags("owesMoney", "friends").build();
            carl = new PersonBuilder().withName("Carl Kurz").withEndDateTime("01-05-17 1630")
                    .withStartDateTime("01-04-17 1630").withLocation("wall street").build();
            daniel = new PersonBuilder().withName("Daniel Meier").withEndDateTime("01-05-17 1630")
                    .withStartDateTime("01-04-17 1630").withLocation("10th street").build();
            elle = new PersonBuilder().withName("Elle Meyer").withEndDateTime("01-05-17 1630")
                    .withStartDateTime("01-04-17 1630").withLocation("michegan ave").build();
            fiona = new PersonBuilder().withName("Fiona Kunz").withEndDateTime("01-05-17 1630")
                    .withStartDateTime("01-04-17 1630").withLocation("little tokyo").build();
            george = new PersonBuilder().withName("George Best").withEndDateTime("01-05-17 1630")
                    .withStartDateTime("01-04-17 1630").withLocation("4th street").build();

            // Manually added
            hoon = new PersonBuilder().withName("Hoon Meier").withEndDateTime("01-05-17 1630")
                    .withStartDateTime("01-04-17 1630").withLocation("little india").build();
            ida = new PersonBuilder().withName("Ida Mueller").withEndDateTime("01-05-17 1630")
                    .withStartDateTime("01-04-17 1630").withLocation("chicago ave").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadTaskManagerWithSampleData(TaskManager ab) {
        for (TestTask person : new TypicalTestPersons().getTypicalPersons()) {
            try {
                ab.addTask(new Task(person));
            } catch (UniqueTaskList.DuplicateTaskException e) {
                assert false : "not possible";
            } catch (IllegalValueException ive) {
                assert false : "not possible";
            }
        }
    }

    public TestTask[] getTypicalPersons() {
        TestTask[] testTasks = new TestTask[]{alice, benson, carl, daniel, elle, fiona, george};
        Random r = new Random();
        for (TestTask test : testTasks) {
            test.setId(r.nextInt(9999));
        }
        return testTasks;
    }

    public TaskManager getTypicalTaskManager() {
        TaskManager ab = new TaskManager();
        loadTaskManagerWithSampleData(ab);

      //TODO shouldn't be there
        TestTask[] testTasks = getTypicalPersons();
        if (testTasks.length == IndexKeeper.getExistedIds().size()) {
            for (int i = 0; i < testTasks.length; i++) {
                testTasks[i].setId(IndexKeeper.getExistedIds().get(i));
            }
        }

        return ab;
    }
}
