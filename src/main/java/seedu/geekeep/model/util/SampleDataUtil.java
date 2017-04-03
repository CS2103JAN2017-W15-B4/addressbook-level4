package seedu.geekeep.model.util;

import seedu.geekeep.commons.exceptions.IllegalValueException;
import seedu.geekeep.model.GeeKeep;
import seedu.geekeep.model.ReadOnlyGeeKeep;
import seedu.geekeep.model.tag.UniqueTagList;
import seedu.geekeep.model.task.DateTime;
import seedu.geekeep.model.task.Description;
import seedu.geekeep.model.task.Task;
import seedu.geekeep.model.task.Title;
import seedu.geekeep.model.task.UniqueTaskList.DuplicateTaskException;

public class SampleDataUtil {
    public static ReadOnlyGeeKeep getSampleGeeKeep() {
        try {
            GeeKeep sampleAB = new GeeKeep();
            for (Task sampleTask : getSampleTasks()) {
                sampleAB.addTask(sampleTask);
            }
            return sampleAB;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate tasks", e);
        }
    }

    //TODO to add floating tasks and deadlines
    public static Task[] getSampleTasks() {
        try {
            return new Task[] {
                new Task(new Title("Watch Beauty and the Beast"), new DateTime("08-04-17 1900"),
                            new DateTime("08-04-17 2100"),
                            new Description("JCube"),
                            new UniqueTagList("Leisure"),
                            false),
                new Task(new Title("Internship Interview"), new DateTime("07-04-17 1300"),
                            new DateTime("17-04-17 1400"),
                            new Description("Garena"),
                            new UniqueTagList("Work"),
                            false),
                new Task(new Title("Buy new spectacles"), null,
                            null,
                            new Description(""),
                            new UniqueTagList("Personal"),
                            false),
                new Task(new Title("Learn Haskell"), null,
                            null,
                            new Description(""),
                            new UniqueTagList("School"),
                            false),
                new Task(new Title("GeeKeep V05"), null,
                            new DateTime("04-04-17 1500"),
                            new Description("NUS COM1-B103"),
                            new UniqueTagList("CS2103T"),
                            true),
                new Task(new Title("CS3230 Assignment 2"), null,
                            new DateTime("14-04-17 2359"),
                            new Description("CodeCrunch"),
                            new UniqueTagList("CS3230"),
                            true),
                new Task(new Title("CS2100 Assignment 3"), null,
                            new DateTime("10-04-17 1700"),
                            new Description("CodeCrunch"),
                            new UniqueTagList("CS2100"),
                            false)
                };
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }
}
