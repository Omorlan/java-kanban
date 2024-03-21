package sprint.managers.taskmanager;

import sprint.managers.Managers;
import sprint.managers.historymanager.HistoryManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected int taskIdCounter = 0;
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>();

    @Override
    public Set<Task> getPrioritizedTasks() {

        taskMap.values().forEach(task -> {
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        });
        subTaskMap.values().forEach(subTask -> {
            if (subTask.getStartTime() != null) {
                prioritizedTasks.add(subTask);
            }
        });

        epicMap.values().forEach(epic -> {
            if (epic.getEpicStartTime() != null) {
                prioritizedTasks.add(epic);
            }
        });

        return prioritizedTasks;
    }

    /*
     a. Get all tasks.
     */
    @Override
    public Map<Integer, Task> getAllTasks() {
        return taskMap;
    }

    @Override
    public Map<Integer, SubTask> getAllSubTasks() {
        return subTaskMap;
    }

    @Override
    public Map<Integer, Epic> getAllEpics() {
        return epicMap;
    }

    /*
     b. remove.
     */
    @Override
    public void removeAllTasks() {
        taskMap.values().stream()
                .map(Task::getId)
                .forEach(history::remove);
        taskMap.clear();
    }


    @Override
    public void removeAllSubTasks() {
        subTaskMap.values().stream()
                .map(SubTask::getId)
                .forEach(history::remove);
        subTaskMap.clear();
        epicMap.values().forEach(epic -> {
            epic.removeAllSubtasks();
            updateEpicBySubTasks(epic);
        });
    }


    @Override
    public void removeAllEpics() {
        epicMap.values().stream()
                .map(Epic::getId)
                .forEach(history::remove);

        epicMap.values().stream()
                .flatMap(epic -> epic.getSubTaskIds().stream())
                .forEach(history::remove);
        epicMap.clear();
        subTaskMap.clear();
    }


    /*
     c. find by id.
     */
    @Override
    public Task getTaskById(int id) {
        history.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        history.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        history.add(subTaskMap.get(id));
        return subTaskMap.get(id);
    }

    /*
     d. create
     */
    @Override
    public void createNewTask(Task task) {
        if (!isCrossing(task)) {
            task.setId(generateTaskId());
            taskMap.put(task.getId(), task);
        }
    }

    @Override
    public void createNewSubTask(SubTask subTask) {
        if (!isCrossing(subTask)) {
            subTask.setId(generateTaskId());
            subTaskMap.put(subTask.getId(), subTask);
            subTask.getEpic().addSubTaskId(subTask.getId());
            updateEpicBySubTasks(subTask.getEpic());
        }
    }

    @Override
    public void createNewEpic(Epic epic) {
        epic.setId(generateTaskId());
        epicMap.put(epic.getId(), epic);
    }

    /*
     e. update
     */
    @Override
    public void updateTask(Task updatedTask) {
        if (!isCrossing(updatedTask)) {
            taskMap.put(updatedTask.getId(), updatedTask);
        }
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        if (!isCrossing(updatedSubTask)) {
            subTaskMap.put(updatedSubTask.getId(), updatedSubTask);
            updateEpicBySubTasks(updatedSubTask.getEpic());
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (!isCrossing(updatedEpic)) {
            epicMap.put(updatedEpic.getId(), updatedEpic);
            updateEpicBySubTasks(updatedEpic);
        }
    }

    /*
     f. remove by id
     */
    @Override
    public void removeTaskById(int id) {
        if (taskMap.get(id) != null) {
            taskMap.remove(id);
            history.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (subTaskMap.get(id) != null) {
            SubTask subTaskToRemove = subTaskMap.remove(id);
            subTaskToRemove.getEpic().removeSubTaskId(id);
            updateEpicBySubTasks(subTaskToRemove.getEpic());
            history.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epicToRemove = epicMap.remove(id);
        if (epicToRemove != null) {
            epicToRemove.getSubTaskIds().forEach(history::remove);
            subTaskMap.values().removeIf(subTask -> subTask.getEpic() == epicToRemove);
            history.remove(id);
        }
    }


    private int generateTaskId() {
        return taskIdCounter++;
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Epic epic) {
        if (epic == null) {
            return Collections.emptyList();
        }
        return epic.getSubTaskIds().stream()
                .map(subTaskMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }


    public boolean isCrossing(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            return false;
        }

        return getPrioritizedTasks().stream()
                .filter(otherTask -> otherTask.getStartTime() != null && otherTask.getEndTime() != null)
                .anyMatch(otherTask -> !(task.getEndTime().isBefore(otherTask.getStartTime()) ||
                        otherTask.getEndTime().isBefore(task.getStartTime())));
    }


    protected void updateEpicBySubTasks(Epic epic) {
        boolean allSubTasksDone = epic.getSubTaskIds().stream()
                .map(subTaskMap::get)
                .allMatch(subTask -> subTask.getStatus() == TaskStatus.DONE);
        LocalDateTime earliestStartTime = null;
        LocalDateTime latestEndTime = null;
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTaskMap.get(subTaskId);
            LocalDateTime subTaskStartTime = subTask.getStartTime();
            LocalDateTime subTaskEndTime = subTask.getEndTime();
            if (earliestStartTime == null || subTaskStartTime.isBefore(earliestStartTime)) {
                earliestStartTime = subTaskStartTime;
            }

            if (latestEndTime == null || subTaskEndTime.isAfter(latestEndTime)) {
                latestEndTime = subTaskEndTime;
            }
        }
        if (allSubTasksDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        if (!epic.getSubTaskIds().isEmpty()) {
            epic.setEpicStartTime(earliestStartTime);
            epic.setEpicEndTime(latestEndTime);
            if (earliestStartTime != null && latestEndTime != null) {
                Duration epicDuration = Duration.between(earliestStartTime, latestEndTime);
                epic.setEpicDuration(epicDuration);
            }

        } else {
            epic.setEpicDuration(null);
            epic.setEpicStartTime(null);
            epic.setEpicEndTime(null);
        }
    }
}
