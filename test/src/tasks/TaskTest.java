package tasks;

import org.junit.jupiter.api.Test;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    //��� ������ � ���������� id ������ ��������� ��� ��������� ��� ���� � �� ��.
    @Test
    public void taskInheritanceEqualityByIdShouldReturnTrueForEqualIds() {
        Task task1 = new Task("����� ��������", "������ ���������", TaskStatus.DONE);
        Task task2 = new Task("����� ���������", "��� ��� ��� �������", TaskStatus.IN_PROGRESS);
        task1.setId(0);
        task2.setId(0);
        assertEquals(task1, task2, "��� ������ � ���������� id ��� ��������� �� �������� ��� ���� � �� ��.");
    }

    @Test
    public void createTaskShouldCreateTaskWithExpectedValues() {
        Task task = new Task("Name", "Description", TaskStatus.DONE);
        assertEquals("Name", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(TaskStatus.DONE, task.getStatus(), "������ �� ���� �������");
    }
    @Test
    public void tasksWithDifferentIdsShouldNotEqual() {
        Task task1 = new Task("Task", "Description", TaskStatus.NEW);
        Task task2 = new Task("Task, but another", "Description", TaskStatus.IN_PROGRESS);
        task1.setId(1);
        task2.setId(2);
        assertNotEquals(task1, task2, "������ � ������� id  �� ������ ���� �����");
    }
    @Test
    public void setStatusShouldUpdateTaskStatusAsExpected() {
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        assertEquals(TaskStatus.NEW, task.getStatus(), "������ ������ ������ ���� NEW");

        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "������ ������ ������ ���� IN_PROGRESS");

        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus(), "������ ������ ������ ���� DONE");
    }
}