package sprint4.managers.taskmanager;

import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;

import java.util.List;

public interface TaskManager {
    /*
     a. ��������� ������ ���� �����.
     */
    List<Task> getAllTasks();

    List<Task> getAllSubTasks();

    List<Task> getAllEpics();

    /*
     b. �������� ���� �����.
     */
    void removeAllTasks();

    void removeAllSubTasks();

    void removeAllEpics();

    /*
     c. ��������� �� ��������������.
     */
    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    /*
     d. ��������. ��� ������ ������ ������������ � �������� ���������.
     */
    void createNewTask(Task task);

    void createNewSubTask(SubTask subTask);

    void createNewEpic(Epic epic);

    /*
     e. ����������. ����� ������ ������� � ������ ��������������� ��������� � ���� ���������.
     */
    void updateTask(Task updatedTask);

    void updateSubTask(SubTask updatedSubTask);

    void updateEpic(Epic updatedEpic);

    /*
     f. �������� �� ��������������.
     */
    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicById(int id);

    List<SubTask> getSubTasksOfEpic(Epic epic);

    List<Task> getHistory();

}
