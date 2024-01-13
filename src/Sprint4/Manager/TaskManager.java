package Sprint4.Manager;

import Sprint4.Tasks.Epic;
import Sprint4.Tasks.SubTask;
import Sprint4.Tasks.Task;
import Sprint4.Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private static int taskIdCounter = 0; // Статический счетчик для генерации идентификаторов

    public Map<Integer, Task> taskMap = new HashMap<>(); //коллекция для хранения задач
    public static Map<Integer, SubTask> subTaskMap = new HashMap<>(); //коллекция для хранения подзадач
    public Map<Integer, Epic> epicMap = new HashMap<>(); //коллекция для хранения эпиков

    /*
     a. Получение списка всех задач.
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public List<Task> getAllSubTasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    public List<Task> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    /*
     b. Удаление всех задач.
     */
    public void removeAllTasks() {
        taskMap.clear();
    }

    public void removeAllSubTasks() {
        subTaskMap.clear();
    }

    public void removeAllEpics() {
        epicMap.clear();
    }

    public void removeAll() {
        taskMap.clear();
        subTaskMap.clear();
        epicMap.clear();
    }

    /*
     c. Получение по идентификатору.
     */
    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public Task getEpicById(int id) {
        return epicMap.get(id);
    }

    public Task getSubTaskById(int id) {
        return subTaskMap.get(id);
    }

    /*
     d. Создание. Сам объект должен передаваться в качестве параметра.
     */
    public void createNewTask(Task task) {
        int taskId = task.getId();
        taskMap.put(taskId, task);
    }

    public void createNewSubTask(SubTask subTask) {
        int taskId = subTask.getId();
        subTaskMap.put(taskId, subTask);
        Epic epic = subTask.getEpic();
        updateEpicStatusBySubTasks(epic);

    }

    public void createNewEpic(Epic epic) {
        int taskId = epic.getId();
        epicMap.put(taskId, epic);
    }

    /*
     e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
     */
    public void updateTask(Task oldTask, Task updatedTask) {
        int taskId = updatedTask.getId();
        int oldId = oldTask.getId();
        taskMap.remove(oldId);
        taskMap.put(taskId, updatedTask);
    }

    public void updateSubTask(SubTask oldSubTask, SubTask updatedSubTask) {
        int taskId = updatedSubTask.getId();
        int oldId = oldSubTask.getId();
        subTaskMap.remove(oldId);
        Epic epic = oldSubTask.getEpic();
        epic.removeSubTaskId(oldId);
        subTaskMap.put(taskId, updatedSubTask);
        updateEpicStatusBySubTasks(epic);

    }

    public void updateEpic(Epic oldEpic, Epic updatedEpic) {
        int taskId = updatedEpic.getId();
        int oldId = oldEpic.getId();
        epicMap.remove(oldId);
        epicMap.put(taskId, updatedEpic);
    }

    /*
     f. Удаление по идентификатору.
     */
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    public void removeSubTaskById(int id) {
        subTaskMap.remove(id);
    }

    public void removeEpicById(int id) {
        epicMap.remove(id);
    }

    // Метод для получения нового уникального идентификатора задачи
    public static int generateTaskId() {
        return taskIdCounter++;
    }

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

    public static void updateEpicStatusBySubTasks(Epic epic) {
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
