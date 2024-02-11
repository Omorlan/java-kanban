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
    public void addAndGetHistoryShouldReturnCorrectHistory() {
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

}
