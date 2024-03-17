package sprint.managers.taskmanager;

import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {
    Set<Task> getPrioritizedTasks();

    /*
         a. ��������� ������ ���� �����.
         */
    Map<Integer, Task> getAllTasks();

    Map<Integer, SubTask> getAllSubTasks();

    Map<Integer, Epic> getAllEpics();


    void removeAllTasks();

    void removeAllSubTasks();

    void removeAllEpics();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);


    void createNewTask(Task task);

    void createNewSubTask(SubTask subTask);

    void createNewEpic(Epic epic);


    void updateTask(Task updatedTask);

    void updateSubTask(SubTask updatedSubTask);

    void updateEpic(Epic updatedEpic);


    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicById(int id);

    List<SubTask> getSubTasksOfEpic(Epic epic);

    List<Task> getHistory();

}
