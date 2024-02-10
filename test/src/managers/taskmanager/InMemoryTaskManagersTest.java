package managers.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint4.managers.Managers;
import sprint4.managers.taskmanager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagersTest {
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @DisplayName("�������� ����� ������")
    @Test
    void createNewTaskShouldAddTaskToHistory() {

        Task task = new Task("New Task", "DescriptionTask", TaskStatus.IN_PROGRESS);
        manager.createNewTask(task);

        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        assertEquals(1, manager.getHistory().size(), "������ �� ��������� � �������");
        assertEquals(task, manager.getHistory().get(0), "������ �� ��������� � �������");
    }

    @DisplayName("�������� ������ �� id")
    @Test
    void removeTaskByIdShouldRemoveTaskFromManager() {

        Task task = new Task("Task to Remove", "DescriptionTaskToRemove", TaskStatus.NEW);
        manager.createNewTask(task);

        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        manager.removeTaskById(task.getId());
        assertNull(manager.getTaskById(task.getId()), "������ �� ���� �������");

    }

    @DisplayName("����� �� id")
    @Test
    void getTasksByIdShouldReturnCorrectTasks() {

        Task task = new Task("Task", "DescriptionTask", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "DescriptionEpic");

        manager.createNewTask(task);
        manager.createNewEpic(epic);

        assertEquals(task, manager.getTaskById(task.getId()), "������ �� ������� �� id");
        assertEquals(epic, manager.getEpicById(epic.getId()), "������ �� ������� �� id");
    }

    @DisplayName("���������� ������� �����")
    @Test
    void updateEpicStatusShouldUpdateEpicStatusCorrectly() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "������ ������ ���������� �����!=NEW");
        SubTask subTask1 = new SubTask(1, "SubTusk 1", "Description", TaskStatus.DONE, epic);
        manager.createNewSubTask(subTask1);
        assertEquals(TaskStatus.DONE, epic.getStatus(), "������ ����� � ����� ��������� �� �������� DONE != DONE");
        SubTask subTask2 = new SubTask(2, "SubTusk 1", "Description", TaskStatus.NEW, epic);
        manager.createNewSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "������ ����� � ����� ��������� �� �� �������� DONE != IN_PROGRESS");
    }

    @DisplayName("�������� ��������� ��� �������� �����")
    @Test
    void removeEpicByIdShouldRemoveEpicAndSubTasks() {
        Epic epic = new Epic("Epic", "Description");
        SubTask subTask = new SubTask("SubTask", "Description", TaskStatus.IN_PROGRESS, epic);
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.removeEpicById(epic.getId());
        assertNull(manager.getSubTaskById(subTask.getId()));
    }

    @DisplayName("�������� ��������� �� ������ ����� ��� �������� ���������")
    @Test
    void removeSubTaskShouldRemoveSubTaskFromEpic() {
        Epic epic = new Epic("Epic", "Description");
        SubTask subTask1 = new SubTask("SubTask1", "Description", TaskStatus.IN_PROGRESS, epic);
        SubTask subTask2 = new SubTask("SubTask2", "Description", TaskStatus.IN_PROGRESS, epic);

        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        manager.removeSubTaskById(subTask1.getId());
        assertFalse(epic.getSubTaskIds().contains(subTask1.getId()));
    }

    @DisplayName("��������� ��������")
    @Test
    void updateTaskDescriptionShouldUpdateTaskDescription() {
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        manager.createNewTask(task);
        String newDescription = "New Description";
        task.setDescription(newDescription);
        Task updatedTask = manager.getTaskById(task.getId());
        assertEquals(newDescription, updatedTask.getDescription());
    }

    @DisplayName("�������� ���� �����")
    @Test
    void removeAllTasksShouldRemoveAllTasksFromManager() {
        Task task1 = new Task("Task 1", "DescriptionTask 1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "DescriptionTask 2", TaskStatus.IN_PROGRESS);
        manager.createNewTask(task1);
        manager.createNewTask(task2);

        manager.removeAllTasks();

        assertNull(manager.getTaskById(task1.getId()), "������ 1 �� ���� �������");
        assertNull(manager.getTaskById(task2.getId()), "������ 2 �� ���� �������");
        assertEquals(0, manager.getAllTasks().size(), "�� ��� ������ ���� �������");
    }

    @DisplayName("�������� ���� ��������")
    @Test
    void removeAllSubTasksShouldRemoveAllSubTasksFromManager() {
        Epic epic = new Epic("Epic", "DescriptionEpic");
        manager.createNewEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSubTask 1", TaskStatus.NEW, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "DescriptionSubTask 2", TaskStatus.IN_PROGRESS, epic);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);

        manager.removeAllSubTasks();

        assertEquals(0, manager.getAllSubTasks().size(), "�� ��� ��������� ���� �������");
        assertEquals(0, epic.getSubTaskIds().size(), "� ����� �������� ��������� ����� ��������");
    }

    @DisplayName("�������� ���� ������")
    @Test
    void removeAllEpicsShouldRemoveAllEpicsAndRelatedSubTasks() {
        Epic epic1 = new Epic("Epic 1", "DescriptionEpic 1");
        Epic epic2 = new Epic("Epic 2", "DescriptionEpic 2");
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSubTask 1", TaskStatus.NEW, epic1);
        SubTask subTask2 = new SubTask("SubTask 2", "DescriptionSubTask 2", TaskStatus.IN_PROGRESS, epic2);
        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        manager.removeAllEpics();

        assertNull(manager.getEpicById(epic1.getId()), "���� 1 �� ��� ������");
        assertNull(manager.getEpicById(epic2.getId()), "���� 2 �� ��� ������");
        assertEquals(0, manager.getAllEpics().size(), "�� ��� ����� ���� �������");
        assertEquals(0, manager.getAllSubTasks().size(), "�� ��� ��������� ���� ������� ��� �������� ������");
    }

    @Test
    @DisplayName("���������� ������")
    void updateTaskShouldUpdateTaskCorrectly() {
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        manager.createNewTask(task);
        Task updatedTask = new Task(task.getId(), "Updated Task", "Updated Description", TaskStatus.IN_PROGRESS);
        manager.updateTask(updatedTask);
        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(updatedTask, retrievedTask);
    }

    @Test
    @DisplayName("���������� ���������")
    void updateSubTaskShouldUpdateSubTaskCorrectly() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        SubTask subTask = new SubTask("SubTask", "Description", TaskStatus.NEW, epic);
        manager.createNewSubTask(subTask);
        SubTask updatedSubTask = new SubTask(subTask.getId(), "Updated SubTask", "Updated Description", TaskStatus.IN_PROGRESS, epic);
        manager.updateSubTask(updatedSubTask);
        SubTask retrievedSubTask = manager.getSubTaskById(subTask.getId());
        assertEquals(updatedSubTask, retrievedSubTask);
    }

    @Test
    @DisplayName("���������� �����")
    void updateEpicShouldUpdateEpicCorrectly() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        Epic updatedEpic = new Epic(epic.getId(), "Updated Epic", "Updated Description");
        manager.updateEpic(updatedEpic);
        Epic retrievedEpic = manager.getEpicById(epic.getId());
        assertEquals(updatedEpic, retrievedEpic);
    }

    @DisplayName("���������� �������� �����")
    @Test
    void getSubTasksOfEpicShouldReturnSubTasksBelongingToEpic() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description", TaskStatus.DONE, epic);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description", TaskStatus.IN_PROGRESS, epic);
        SubTask subTask3 = new SubTask(3, "SubTask 3", "Description", TaskStatus.NEW, epic);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        manager.createNewSubTask(subTask3);
        List<SubTask> subTasks = manager.getSubTasksOfEpic(epic);
        assertEquals(3, subTasks.size());
        assertEquals(subTask1, subTasks.get(0));
        assertEquals(subTask2, subTasks.get(1));
        assertEquals(subTask3, subTasks.get(2));
    }
}


