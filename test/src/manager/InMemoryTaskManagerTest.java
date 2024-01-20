package manager;

import org.junit.jupiter.api.Test;
import sprint4.manager.InMemoryTaskManager;
import sprint4.manager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagerTest {

    @Test
    public void testCreateNewTask() {
        // ������� �������� � ��������� ����� ������
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("New Task", "Description", TaskStatus.IN_PROGRESS);
        taskManager.createNewTask(task);

        // �������� ������ �� �������������� � ���������, ��� ��� ����� �����������
        Task retrievedTask = taskManager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        // ���������, ��� ��� ������ ����� ��������� � �������
        assertEquals(1, taskManager.getHistory().size(),"������ �� ���������� � �������");
        assertEquals(task, taskManager.getHistory().get(0),"������ �� ���������� � �������");
    }

    @Test
    public void testRemoveTaskById() {
        // ������� �������� � ��������� ������
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task to Remove", "Description", TaskStatus.NEW);
        taskManager.createNewTask(task);

        // ���������, ��� ������ ����������
        Task retrievedTask = taskManager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        // ������� ������ � ���������, ��� ������� �������
        taskManager.removeTaskById(task.getId());
        assertNull(taskManager.getTaskById(task.getId()),"������ �� ���� �������");

    }
    @Test
    void testInMemoryTaskManagerAddAndFindTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Task", "Description", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "Description");

        taskManager.createNewTask(task);
        taskManager.createNewEpic(epic);

        assertEquals(task, taskManager.getTaskById(task.getId()),"������ �� ������� �� id");
        assertEquals(epic, taskManager.getEpicById(epic.getId()),"������ �� ������� �� id");
    }
    @Test
    void testInMemoryTaskManagerUpdateEpicStatus(){
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Description");
        taskManager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW,epic.getStatus(),"������ ������ ���������� �����!=NEW");
        SubTask subTask1 = new SubTask(1,"SubTusk 1","Description",TaskStatus.DONE ,epic);
        taskManager.createNewSubTask(subTask1);
        assertEquals(TaskStatus.DONE,epic.getStatus(),"������ ����� � ����� ��������� �� �������� DONE != DONE");
        SubTask subTask2 = new SubTask(2,"SubTusk 1","Description",TaskStatus.NEW ,epic);
        taskManager.createNewSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS,epic.getStatus(),"������ ����� � ����� ��������� �� �� �������� DONE != IN_PROGRESS");
    }

}
