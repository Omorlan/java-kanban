package manager;

import org.junit.jupiter.api.Test;
import sprint4.manager.HistoryManager;
import sprint4.manager.InMemoryHistoryManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    @Test
    public void testAddAndGetHistory() {
        // ������� �������� � ��������� ��������� �����
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description", TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("SubTask 1", "Description", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "Description"));
        historyManager.add(task1);
        historyManager.add(subTask1);

        // �������� ������� � ���������, ��� ��� �������� ����������� ������
        assertEquals(2, historyManager.getHistory().size(),"������� �� ����������");
        assertEquals(task1, historyManager.getHistory().get(0),"������� �� �������� ����������� ������ 1");
        assertEquals(subTask1, historyManager.getHistory().get(1),"������� �� �������� ����������� ������ 2");
    }

    @Test
    public void testHistoryLimit() {
        // ������� �������� � ��������, ��������� � 10 ��������
        HistoryManager historyManager = new InMemoryHistoryManager();
        for (int i = 0; i < 9; i++) {
            historyManager.add(new Task("Task " + i, "Description " + i, TaskStatus.DONE));
        }

        // ��������� ����������� �������
        Task newTask = new Task("New Task", "New Description", TaskStatus.IN_PROGRESS);
        historyManager.add(newTask);

        // �������� ������� � ���������, ��� ��� �������� 10 �����, ������� �����
        assertEquals(10, historyManager.getHistory().size(),"�������� ����� �������");
        assertEquals(newTask, historyManager.getHistory().get(9),"����� ������� �������� �� � �����");
    }

}
