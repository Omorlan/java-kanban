package managers.historymanager;

import org.junit.jupiter.api.Test;
import sprint.managers.Managers;
import sprint.managers.filemanager.FileBackedTaskManager;
import sprint.managers.historymanager.HistoryManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryHistoryManagersTest {
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
    void addAndGetHistoryShouldReturnCorrectHistory() {
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

    @Test
    void addHistoryShouldAddTaskToHistory() {
        Task task1 = new Task("Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS, 60, "17.03.2024 16:10");
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSub", TaskStatus.IN_PROGRESS, new Epic("Epic 1", "DescriptionEpic"), 10, "17.03.2024 14:40");
        Task task2 = new Task("Task 2", "DescriptionTask2", TaskStatus.NEW, 40, "17.03.2024 19:40");
        manager.createNewSubTask(subTask1); // for generating ids
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        historyManager.add(subTask1);
        assertEquals(1, historyManager.getHistory().size(), "The first task has not been added to the history");
        historyManager.add(task1);
        assertEquals(2, historyManager.getHistory().size(), "The second task has not been added to the history");
        historyManager.add(task2);
        assertEquals(3, historyManager.getHistory().size(), "The third task has not been added to the history");
    }

    @Test
    void emptyHistoryShouldNotReturnAnything() {
        assertEquals(0, historyManager.getHistory().size(), "The history should be empty");
    }

    @Test
    void removeFromHistoryShouldRemoveTasksFromList() {
        Task task1 = new Task("Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 16:10");
        Task task2 = new Task("Task 2", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 16:21");
        Task task3 = new Task("Task 3", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 16:32");
        Task task4 = new Task("Task 4", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 16:43");
        Task task5 = new Task("Task 5", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 16:54");
        Task task6 = new Task("Task 6", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 17:05");
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.createNewTask(task3);
        manager.createNewTask(task4);
        manager.createNewTask(task5);
        manager.createNewTask(task6);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        assertEquals(5, historyManager.getHistory().size(), "not all tasks have been added to the history");
        List<Task> history = historyManager.getHistory();
        assertEquals(task1.toString(), history.get(0).toString(), "task1 should be first element in history");
        assertEquals(task5.toString(), history.get(history.size() - 1).toString(), "task5 should be last element in history");
        assertEquals(task3.toString(), history.get(2).toString(), "task3 should be middle element in history");
        historyManager.remove(task5.getId());
        assertFalse(historyManager.getHistory().contains(task6), "task6 is present in the history");
        historyManager.remove(task1.getId());
        assertFalse(historyManager.getHistory().contains(task1), "task1 is present in the history");
        historyManager.remove(task3.getId());
        assertFalse(historyManager.getHistory().contains(task3), "task3 is present in the history");
        assertEquals(task2.toString(), history.get(0).toString(), " now task2 should be first element in history");
        assertEquals(task4.toString(), history.get(history.size() - 1).toString(), "now task4 should be last element in history");
    }

    @Test
    void duplicatingTaskGetShouldBeLastAndUniqueInTheHistory() {
        Task task1 = new Task("Task 1", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 16:10");
        Task task2 = new Task("Task 2", "DescriptionTask", TaskStatus.IN_PROGRESS, 10, "17.03.2024 16:21");
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        List<Task> history = manager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1.toString(), history.get(0).toString(), "task1 should be first element in history");
        assertEquals(task2.toString(), history.get(1).toString(), "task2 should be last element in history");
        manager.getTaskById(task1.getId()); //duplicating task
        assertEquals(2, history.size());
        assertEquals(task2.toString(), history.get(0).toString(), "now task2 should be first element in history");
        assertEquals(task1.toString(), history.get(1).toString(), "now task1 should be last element in history" +
                " because it duplicated");
    }
}
