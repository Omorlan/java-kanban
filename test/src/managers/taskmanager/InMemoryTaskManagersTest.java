package managers.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint4.managers.Managers;
import sprint4.managers.taskmanager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InMemoryTaskManagersTest {
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void testCreateNewTask() {

        Task task = new Task("New Task", "DescriptionTask", TaskStatus.IN_PROGRESS);
        manager.createNewTask(task);

        // Получаем задачу по идентификатору и проверяем, что она равна добавленной
        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        // Проверяем, что эта задача также добавлена в историю
        assertEquals(1, manager.getHistory().size(), "Задача не добавленна в историю");
        assertEquals(task, manager.getHistory().get(0), "Задача не добавленна в историю");
    }

    @Test
    void testRemoveTaskById() {

        Task task = new Task("Task to Remove", "DescriptionTaskToRemove", TaskStatus.NEW);
        manager.createNewTask(task);

        // Проверяем, что задача существует
        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        // Удаляем задачу и проверяем, что онабыла удалена
        manager.removeTaskById(task.getId());
        assertNull(manager.getTaskById(task.getId()), "Задача не была удалена");

    }

    @Test
    void testInMemoryTaskManagerAddAndFindTasks() {

        Task task = new Task("Task", "DescriptionTask", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "DescriptionEpic");

        manager.createNewTask(task);
        manager.createNewEpic(epic);

        assertEquals(task, manager.getTaskById(task.getId()), "Задача не найдена по id");
        assertEquals(epic, manager.getEpicById(epic.getId()), "Задача не найдена по id");
    }

    @Test
    void testInMemoryTaskManagerUpdateEpicStatus() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус только созданного эпика!=NEW");
        SubTask subTask1 = new SubTask(1, "SubTusk 1", "Description", TaskStatus.DONE, epic);
        manager.createNewSubTask(subTask1);
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус эпика с одной подзадчей со статусом DONE != DONE");
        SubTask subTask2 = new SubTask(2, "SubTusk 1", "Description", TaskStatus.NEW, epic);
        manager.createNewSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус эпика с одной подзадчей не со статусом DONE != IN_PROGRESS");
    }

}
