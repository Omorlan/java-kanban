package manager;

import org.junit.jupiter.api.Test;
import sprint4.manager.InMemoryTaskManager;
import sprint4.manager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagerTest {

    @Test
    public void testCreateNewTask() {
        // Создаем менеджер и добавляем новую задачу
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("New Task", "Description", TaskStatus.IN_PROGRESS);
        taskManager.createNewTask(task);

        // Получаем задачу по идентификатору и проверяем, что она равна добавленной
        Task retrievedTask = taskManager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        // Проверяем, что эта задача также добавлена в историю
        assertEquals(1, taskManager.getHistory().size());
        assertEquals(task, taskManager.getHistory().get(0));
    }

    @Test
    public void testRemoveTaskById() {
        // Создаем менеджер и добавляем задачу
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task to Remove", "Description", TaskStatus.NEW);
        taskManager.createNewTask(task);

        // Проверяем, что задача существует
        Task retrievedTask = taskManager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        // Удаляем задачу и проверяем, что онабыла удалена
        taskManager.removeTaskById(task.getId());
        assertNull(taskManager.getTaskById(task.getId()));

    }
    @Test
    void testInMemoryTaskManagerAddAndFindTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Task", "Description", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "Description");

        taskManager.createNewTask(task);
        taskManager.createNewEpic(epic);

        assertEquals(task, taskManager.getTaskById(task.getId()));
        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }


}
