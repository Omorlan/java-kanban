package managers.historymanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint4.managers.Managers;
import sprint4.managers.historymanager.HistoryManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagersTest {
    HistoryManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    public void testAddAndGetHistory() {
        // ������� �������� � ��������� ��������� �����

        Task task1 = new Task("Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSub", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "DescriptionEpic"));
        manager.add(task1);
        manager.add(subTask1);

        // �������� ������� � ���������, ��� ��� �������� ����������� ������
        assertEquals(task1, manager.getHistory().get(0), "������� �� �������� ����������� ������ 1");
        assertEquals(subTask1, manager.getHistory().get(1), "������� �� �������� ����������� ������ 2");
    }

    @Test
    public void testHistoryLimit() {
        // ������� �������� � ��������, ��������� � 10 ��������
        for (int i = 0; i < 9; i++) {
            manager.add(new Task("Task " + i, "Description " + i, TaskStatus.DONE));
        }

        // ��������� ����������� �������
        Task newTask = new Task("New Task", "New Description", TaskStatus.IN_PROGRESS);
        manager.add(newTask);

        // �������� ������� � ���������, ��� ��� �������� 10 �����, ������� �����
        assertEquals(10, manager.getHistory().size(), "�������� ����� �������");
        assertEquals(newTask, manager.getHistory().get(9), "����� ������� �������� �� � �����");
    }

}
