package manager;

import org.junit.jupiter.api.Test;
import sprint4.manager.InMemoryTaskManager;
import sprint4.manager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
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
        assertEquals(1, taskManager.getHistory().size(),"Задача не добавленна в историю");
        assertEquals(task, taskManager.getHistory().get(0),"Задача не добавленна в историю");
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
        assertNull(taskManager.getTaskById(task.getId()),"Задача не была удалена");

    }
    @Test
    void testInMemoryTaskManagerAddAndFindTasks() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task = new Task("Task", "Description", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "Description");

        taskManager.createNewTask(task);
        taskManager.createNewEpic(epic);

        assertEquals(task, taskManager.getTaskById(task.getId()),"Задача не найдена по id");
        assertEquals(epic, taskManager.getEpicById(epic.getId()),"Задача не найдена по id");
    }
    @Test
    void testInMemoryTaskManagerUpdateEpicStatus(){
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Description");
        taskManager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW,epic.getStatus(),"Статус только созданного эпика!=NEW");
        SubTask subTask1 = new SubTask(1,"SubTusk 1","Description",TaskStatus.DONE ,epic);
        taskManager.createNewSubTask(subTask1);
        assertEquals(TaskStatus.DONE,epic.getStatus(),"Статус эпика с одной подзадчей со статусом DONE != DONE");
        SubTask subTask2 = new SubTask(2,"SubTusk 1","Description",TaskStatus.NEW ,epic);
        taskManager.createNewSubTask(subTask2);
        assertEquals(TaskStatus.IN_PROGRESS,epic.getStatus(),"Статус эпика с одной подзадчей не со статусом DONE != IN_PROGRESS");
    }

}
