package sprint4.managers.historymanager;

import sprint4.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> historyTasks = new LinkedList<>();
    private static final int HISTORY_MAX_SIZE = 10;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyTasks.size() > HISTORY_MAX_SIZE) { //сначала смотрим не "переполнен" ли список
                historyTasks.removeFirst();
            }
            historyTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }
}
