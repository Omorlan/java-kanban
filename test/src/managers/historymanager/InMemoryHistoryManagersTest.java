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
        // Создаем менеджер и добавляем несколько задач

        Task task1 = new Task(1,"Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask(2,"SubTask 1", "DescriptionSub", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "DescriptionEpic"));
        manager.add(task1);
        manager.add(subTask1);

        // Получаем историю и проверяем, что она содержит добавленные задачи
        assertEquals(task1, manager.getHistory().get(0), "История не содержит добавленную задачу 1");
        assertEquals(subTask1, manager.getHistory().get(1), "История не содержит добавленную задачу 2");
    }



}
