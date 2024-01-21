package sprint4.managers.historymanager;



import sprint4.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}