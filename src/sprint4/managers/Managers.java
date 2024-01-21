package sprint4.managers;


import sprint4.managers.historymanager.HistoryManager;
import sprint4.managers.historymanager.InMemoryHistoryManager;
import sprint4.managers.taskmanager.InMemoryTaskManager;
import sprint4.managers.taskmanager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}