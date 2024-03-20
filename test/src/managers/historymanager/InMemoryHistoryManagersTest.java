package managers.historymanager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sprint.managers.Managers;
import sprint.managers.filemanager.FileBackedTaskManager;
import sprint.managers.historymanager.HistoryManager;
import sprint.tasks.Task;

import java.io.File;
import java.io.IOException;

import static managers.TaskGeneratorUtil.genTasks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryHistoryManagersTest {
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private File file;

    {
        try {
            file = File.createTempFile("test_tasks", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileBackedTaskManager manager = new FileBackedTaskManager(file);


    @Test
    void addAndGetHistoryShouldReturnCorrectHistory() {
        genTasks(manager, 1, "task");
        genTasks(manager, 1, "epic");
        manager.getTaskById(0);
        manager.getEpicById(1);
        assertEquals(2, manager.getHistory().size(), "History should contain two elements");
        assertEquals(manager.getAllTasks().get(0), manager.getHistory().get(0), "history does not contain the added task 1");
        assertEquals(manager.getAllEpics().get(1), manager.getHistory().get(1), "history does not contain the added task 2");
        manager.getTaskById(0);
        assertEquals(2, manager.getHistory().size(), "size of the history should not change when you add it again");
        assertEquals(manager.getAllTasks().get(0), manager.getHistory().get(1), "the repeated occurrence is placed at the end");
    }

    @Test
    void addHistoryShouldAddTaskToHistory() {
        genTasks(manager, 3, "task");
        historyManager.add(manager.getAllTasks().get(0));
        assertEquals(1, historyManager.getHistory().size(), "The first task has not been added to the history");
        historyManager.add(manager.getAllTasks().get(1));
        assertEquals(2, historyManager.getHistory().size(), "The second task has not been added to the history");
        historyManager.add(manager.getAllTasks().get(2));
        assertEquals(3, historyManager.getHistory().size(), "The third task has not been added to the history");
    }

    @Test
    void emptyHistoryShouldNotReturnAnything() {
        assertEquals(0, historyManager.getHistory().size(), "The history should be empty");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    void removeFromHistoryShouldRemoveTasksFromList(int id) {
        int num = 5;
        genTasks(manager, num, "task");
        for (int i = 0; i < num; i++) {
            historyManager.add(manager.getAllTasks().get(i));
        }
        assertEquals(num, historyManager.getHistory().size(), "Not all tasks have been added to the history");
        Task taskToRemove = manager.getAllTasks().get(id);
        historyManager.remove(taskToRemove.getId());
        assertFalse(historyManager.getHistory().contains(taskToRemove), "Task is present in the history");
        assertEquals(4, historyManager.getHistory().size());
    }

}
