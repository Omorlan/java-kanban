package sprint4.managers.historymanager;


import sprint4.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);

    List<Task> getTasks();

    void linkLast(Task element);
}