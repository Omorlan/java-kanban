package tasks;

import org.junit.jupiter.api.Test;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    //��� ������ � ���������� id ������ ��������� ��� ��������� ��� ���� � �� ��.
    @Test
    public void shouldBePositiveWhenIdAreEqual() {
        Task task1 = new Task("Name", "Description",TaskStatus.DONE);
        task1.setId(0);
        Task task2 = new Task("Name", "Description", TaskStatus.DONE);
        task2.setId(0);
        assertEquals(task1, task2, "��� ������ � ���������� id ��� ��������� �� �������� ��� ���� � �� ��.");
    }
    @Test
    public void testCreateTask() {
        Task task = new Task("Name", "Description", TaskStatus.DONE);
        assertEquals("Name", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(TaskStatus.DONE, task.getStatus());
    }
}