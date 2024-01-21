package tasks;

import org.junit.jupiter.api.Test;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {
    @Test
    void testTaskInheritanceEqualityById() {
        Epic epic = new Epic("Epic", "Description");
        Epic anotherEpic = new Epic("Epic", "Description");
        SubTask subTask1 = new SubTask("Subtask", "Description1", TaskStatus.DONE, epic);
        SubTask subTask2 = new SubTask("���������,���������� ������ ��������",
                "� �������� � ��� ����� ���������, ���� �� ����� �� ������", TaskStatus.NEW, anotherEpic);

        subTask1.setId(1);
        subTask2.setId(1);

        assertEquals(subTask1, subTask2, "��� ������ � ���������� id ��� ��������� �� �������� ��� ���� � �� ��.");
    }

    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("EpicName", "Description");
        SubTask subTask = new SubTask("SubTaskName", "Description", TaskStatus.IN_PROGRESS, epic);

        assertEquals("SubTaskName", subTask.getName());
        assertEquals("Description", subTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subTask.getStatus());
        assertEquals(epic, subTask.getEpic(), "����� �������� �� ���� �������");
    }


}
