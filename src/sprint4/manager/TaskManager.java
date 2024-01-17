package sprint4.manager;

import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int taskIdCounter = 0; // Статический счетчик для генерации идентификаторов


    private Map<Integer, Task> taskMap = new HashMap<>(); //коллекция для хранения задач
    private Map<Integer, SubTask> subTaskMap = new HashMap<>(); //коллекция для хранения подзадач
    private Map<Integer, Epic> epicMap = new HashMap<>(); //коллекция для хранения эпиков

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public Map<Integer, Epic> getEpicMap() {
        return epicMap;
    }

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
        for (Epic epic : epicMap.values()) {
            epic.removeAllSubtasks();
            updateEpicStatusBySubTasks(epic);
        }
    }

    public void removeAllEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }


    /*
     c. Получение по идентификатору.
     */
    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTaskMap.get(id);
    }

    /*
     d. Создание. Сам объект должен передаваться в качестве параметра.
     */
    public void createNewTask(Task task) {
        task.setId(generateTaskId());
        taskMap.put(task.getId(), task);
    }

    public void createNewSubTask(SubTask subTask) {
        subTask.setId(generateTaskId());
        subTaskMap.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.addSubTaskId(subTask.getId());
        updateEpicStatusBySubTasks(epic);


    }

    public void createNewEpic(Epic epic) {
        epic.setId(generateTaskId());
        epicMap.put(epic.getId(), epic);
    }

    /*
     e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
     */
    public void updateTask(Task updatedTask) {
        int key = updatedTask.getId();
        taskMap.put(key, updatedTask);

    }

    public void updateSubTask(SubTask updatedSubTask) {
        int key = updatedSubTask.getId();
        subTaskMap.put(key, updatedSubTask); //обновляем сабтаску
        Epic epic = updatedSubTask.getEpic();
        subTaskMap.put(key, updatedSubTask);
        updateEpicStatusBySubTasks(epic); //обновляем статус эпика
    }

    public void updateEpic(Epic updatedEpic) {
        int key = updatedEpic.getId();
        epicMap.put(key, updatedEpic);
    }

    /*
     f. Удаление по идентификатору.
     */
    public void removeTaskById(int id) {
        taskMap.remove(id);

    }

    public void removeSubTaskById(int id) {
        SubTask subTaskToRemove = subTaskMap.get(id);
        subTaskMap.remove(id);
        Epic epic = subTaskToRemove.getEpic();
        epic.removeSubTaskId(id);
        updateEpicStatusBySubTasks(epic);
    }

    public void removeEpicById(int id) {
        Epic epicToRemove = epicMap.get(id);
        epicMap.remove(id);
        subTaskMap.values().removeIf(subTask -> subTask.getEpic() == epicToRemove);
    }

    // Метод для получения нового уникального идентификатора задачи
    private int generateTaskId() {
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

    private void updateEpicStatusBySubTasks(Epic epic) {
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
