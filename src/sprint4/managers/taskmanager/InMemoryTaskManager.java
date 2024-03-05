package sprint4.managers.taskmanager;

import sprint4.managers.Managers;
import sprint4.managers.historymanager.HistoryManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected int taskIdCounter = 0;

    protected final HistoryManager history = Managers.getDefaultHistory();
    protected final Map<Integer, Task> taskMap = new HashMap<>();
    protected final Map<Integer, SubTask> subTaskMap = new HashMap<>();
    protected final Map<Integer, Epic> epicMap = new HashMap<>();

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
        for (Task task : taskMap.values()) {
            history.remove(task.getId());
        }
        taskMap.clear();

    }

    @Override
    public void removeAllSubTasks() {
        for (SubTask subTask : subTaskMap.values()) {
            history.remove(subTask.getId());
        }
        subTaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.removeAllSubtasks();
            updateEpicStatusBySubTasks(epic);
        }
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epicMap.values()) {
            history.remove(epic.getId());
            for (int subTaskId : epic.getSubTaskIds()) {
                history.remove(subTaskId);
            }
        }

        epicMap.clear();
        subTaskMap.clear();
    }


    /*
     c. find by id.
     */
    @Override
    public Task getTaskById(int id) {
        Task task = taskMap.get(id);
        history.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicMap.get(id);
        history.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        history.add(subTask);
        return subTask;
    }

    /*
     d. create
     */
    @Override
    public void createNewTask(Task task) {
        task.setId(generateTaskId());
        taskMap.put(task.getId(), task);
    }

    @Override
    public void createNewSubTask(SubTask subTask) {
        subTask.setId(generateTaskId());
        subTaskMap.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.addSubTaskId(subTask.getId());
        updateEpicStatusBySubTasks(epic);
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
        taskMap.put(updatedTask.getId(), updatedTask);

    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        subTaskMap.put(updatedSubTask.getId(), updatedSubTask);
        updateEpicStatusBySubTasks(updatedSubTask.getEpic());
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        epicMap.put(updatedEpic.getId(), updatedEpic);
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
            Epic epic = subTaskToRemove.getEpic();
            epic.removeSubTaskId(id);
            updateEpicStatusBySubTasks(epic);
            history.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (epicMap.get(id) != null) {
            Epic epicToRemove = epicMap.remove(id);
            for (int stId : epicToRemove.getSubTaskIds()) {
                history.remove(stId);
            }
            subTaskMap.values().removeIf(subTask -> subTask.getEpic() == epicToRemove);
            history.remove(id);
        }
    }


    private int generateTaskId() {
        return taskIdCounter++;
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Epic epic) {
        List<SubTask> subTasksList = new ArrayList<>();
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTaskIds()) {
                SubTask subTask = subTaskMap.get(subTaskId);
                if (subTask != null) {
                    subTasksList.add(subTask);
                }
            }
        }
        return subTasksList;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    protected void updateEpicStatusBySubTasks(Epic epic) {
        boolean allSubTasksDone = true;
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTaskMap.get(subTaskId);
            if (subTask.getStatus() != TaskStatus.DONE) {
                allSubTasksDone = false;
                break;
            }
        }
        if (allSubTasksDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
