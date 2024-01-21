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

        Task task1 = new Task("Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSub", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "DescriptionEpic"));
        manager.add(task1);
        manager.add(subTask1);

        // Получаем историю и проверяем, что она содержит добавленные задачи
        assertEquals(task1, manager.getHistory().get(0), "История не содержит добавленную задачу 1");
        assertEquals(subTask1, manager.getHistory().get(1), "История не содержит добавленную задачу 2");
    }

    @Test
    public void testHistoryLimit() {
        // Создаем менеджер с историей, заполняем её 10 задачами
        for (int i = 0; i < 9; i++) {
            manager.add(new Task("Task " + i, "Description " + i, TaskStatus.DONE));
        }

        // попробуем переполнить историю
        Task newTask = new Task("New Task", "New Description", TaskStatus.IN_PROGRESS);
        manager.add(newTask);

        // Получаем историю и проверяем, что она содержит 10 задач, включая новую
        assertEquals(10, manager.getHistory().size(), "Превышен лимит истории");
        assertEquals(newTask, manager.getHistory().get(9), "Новый элемент добавлен не в конец");
    }

}
