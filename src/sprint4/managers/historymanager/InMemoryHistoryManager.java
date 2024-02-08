package sprint4.managers.historymanager;

import sprint4.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> historyTasks = new LinkedList<>();


    @Override
    public void add(Task task) {
        if (task != null) {
            if (!historyTasks.contains(task)) {
                historyTasks.add(task);
            } else {
                remove(task.getId());
                historyTasks.add(task);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTasks;
    }

    @Override
    public void remove(int id) {
        Task taskToRemove = null;
        for (Task task : historyTasks) {
            if (task.getId() == id) {
                taskToRemove = task;
                break;
            }
        }
        if (taskToRemove != null) {
            historyTasks.remove(taskToRemove);
        }
    }
}
