package sprint4.managers;


import sprint4.managers.filemanager.FileBackedTaskManager;
import sprint4.managers.historymanager.HistoryManager;
import sprint4.managers.historymanager.InMemoryHistoryManager;
import sprint4.managers.taskmanager.TaskManager;

import java.io.File;

public class Managers {
    private Managers() {
        throw new IllegalStateException("Utility class");
    }
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("src/resources/backup.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}