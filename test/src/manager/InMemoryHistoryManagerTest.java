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
        // Создаем менеджер и добавляем несколько задач
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description", TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("SubTask 1", "Description", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "Description"));
        historyManager.add(task1);
        historyManager.add(subTask1);

        // Получаем историю и проверяем, что она содержит добавленные задачи
        assertEquals(2, historyManager.getHistory().size(),"История не добавилась");
        assertEquals(task1, historyManager.getHistory().get(0),"История не содержит добавленную задачу 1");
        assertEquals(subTask1, historyManager.getHistory().get(1),"История не содержит добавленную задачу 2");
    }

    @Test
    public void testHistoryLimit() {
        // Создаем менеджер с историей, заполняем её 10 задачами
        HistoryManager historyManager = new InMemoryHistoryManager();
        for (int i = 0; i < 9; i++) {
            historyManager.add(new Task("Task " + i, "Description " + i, TaskStatus.DONE));
        }

        // попробуем переполнить историю
        Task newTask = new Task("New Task", "New Description", TaskStatus.IN_PROGRESS);
        historyManager.add(newTask);

        // Получаем историю и проверяем, что она содержит 10 задач, включая новую
        assertEquals(10, historyManager.getHistory().size(),"Превышен лимит истории");
        assertEquals(newTask, historyManager.getHistory().get(9),"Новый элемент добавлен не в конец");
    }

}
