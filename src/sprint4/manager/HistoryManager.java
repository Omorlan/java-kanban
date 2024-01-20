package sprint4.manager;



import sprint4.tasks.Task;

import java.util.List;

public interface HistoryManager {
    public void add(Task task);
    public List<Task> getHistory();
}