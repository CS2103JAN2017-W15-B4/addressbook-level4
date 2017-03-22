package seedu.geekeep.model.util;

import java.util.ArrayList;

public class IndexKeeper {

    private static final ArrayList<Integer> allIndexes = new ArrayList<Integer>();

    public static ArrayList<Integer> getExistedIds() {
        return allIndexes;
    }

    public static void addNewId(Integer id) {
        allIndexes.add(id);
    }

    public static boolean removeExistedId(Integer id) {
        return allIndexes.remove(id);
    }

    public static void resetIds() {
        allIndexes.clear();
    }
}
