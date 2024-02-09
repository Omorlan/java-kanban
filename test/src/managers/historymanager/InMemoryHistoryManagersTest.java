package managers.historymanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sprint4.managers.Managers;
import sprint4.managers.historymanager.HistoryManager;
import sprint4.managers.taskmanager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryHistoryManagersTest {
    HistoryManager historyManager;
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        manager = Managers.getDefault();
    }

    @DisplayName("Проверка работоспособности истории")
    @Test
    public void testAddAndGetHistory() {
        Task task1 = new Task("Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSub", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "DescriptionEpic"));
        manager.createNewTask(task1);
        manager.createNewSubTask(subTask1);
        assertTrue(manager.getHistory().isEmpty(), "История должна быть пуста перед добавлением задач");
        manager.getTaskById(task1.getId()); // обратились к задаче 1
        manager.getSubTaskById(subTask1.getId()); // обратились к задаче 2
        assertEquals(2, manager.getHistory().size(), "История содержит неверное количество задач");
        assertEquals(task1, manager.getHistory().get(0), "История не содержит добавленную задачу 1");
        assertEquals(subTask1, manager.getHistory().get(1), "История не содержит добавленную задачу 2");
        manager.getTaskById(task1.getId()); // еще раз обратились к задаче 1
        assertEquals(2, manager.getHistory().size(), "История не должна увеличиваться при повторном добавлении задачи");
        assertEquals(task1, manager.getHistory().get(1), "История должна содержать последний просмотр задачи");
    }

    @Test
    void testLinkLast() {
        Task task1 = new Task("Задача 1", "Описание 1", TaskStatus.DONE);
        Task task2 = new Task("Задача 2", "Описание 2", TaskStatus.DONE);

        historyManager.linkLast(task1);
        historyManager.linkLast(task2);

        List<Task> tasks = historyManager.getTasks();
        assertEquals(2, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
    }

    @Test
    void testGetTasks() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", TaskStatus.DONE);
        Task task2 = new Task(2, "Задача 2", "Описание 2", TaskStatus.DONE);
        Task task3 = new Task(3, "Задача 3", "Описание 3", TaskStatus.DONE);
        historyManager.linkLast(task1);
        historyManager.linkLast(task2);
        historyManager.linkLast(task3);

        List<Task> tasks = historyManager.getTasks();

        // Проверяем, что все задачи были получены в правильном порядке
        assertEquals(3, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
        assertEquals(task3, tasks.get(2));
    }


}
