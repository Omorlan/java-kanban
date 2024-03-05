package managers.historymanager;

import org.junit.jupiter.api.Test;
import sprint4.managers.Managers;
import sprint4.managers.filemanager.FileBackedTaskManager;
import sprint4.managers.historymanager.HistoryManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagersTest {
    HistoryManager historyManager = Managers.getDefaultHistory();
    File file;

    {
        try {
            file = File.createTempFile("test_tasks", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    FileBackedTaskManager manager = new FileBackedTaskManager(file);

    @Test
    public void addAndGetHistoryShouldReturnCorrectHistory() {
        Task task1 = new Task("Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS);
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSub", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "DescriptionEpic"));
        manager.createNewTask(task1);
        manager.createNewSubTask(subTask1);
        manager.getTaskById(task1.getId());
        manager.getSubTaskById(subTask1.getId());
        assertEquals(2, manager.getHistory().size(), "History should contain two elements");
        assertEquals(task1, manager.getHistory().get(0), "history does not contain the added task 1");
        assertEquals(subTask1, manager.getHistory().get(1), "history does not contain the added task 2");
        manager.getTaskById(task1.getId());
        assertEquals(2, manager.getHistory().size(), "size of the history should not change when you add it again");
        assertEquals(task1, manager.getHistory().get(1), "the repeated occurrence is placed at the end");
    }


}
