package sprint.managers;


import sprint.managers.filemanager.FileBackedTaskManager;
import sprint.managers.historymanager.HistoryManager;
import sprint.managers.historymanager.InMemoryHistoryManager;
import sprint.managers.taskmanager.TaskManager;

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