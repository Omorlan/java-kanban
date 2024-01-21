package sprint4.managers.taskmanager;

import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;

import java.util.List;

public interface TaskManager {
    /*
     a. Получение списка всех задач.
     */
    List<Task> getAllTasks();

    List<Task> getAllSubTasks();

    List<Task> getAllEpics();

    /*
     b. Удаление всех задач.
     */
    void removeAllTasks();

    void removeAllSubTasks();

    void removeAllEpics();

    /*
     c. Получение по идентификатору.
     */
    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    /*
     d. Создание. Сам объект должен передаваться в качестве параметра.
     */
    void createNewTask(Task task);

    void createNewSubTask(SubTask subTask);

    void createNewEpic(Epic epic);

    /*
     e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
     */
    void updateTask(Task updatedTask);

    void updateSubTask(SubTask updatedSubTask);

    void updateEpic(Epic updatedEpic);

    /*
     f. Удаление по идентификатору.
     */
    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicById(int id);

    List<SubTask> getSubTasksOfEpic(Epic epic);

    List<Task> getHistory();

}
