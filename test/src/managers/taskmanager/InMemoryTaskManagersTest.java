package managers.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.managers.Managers;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class InMemoryTaskManagersTest {
    TaskManager manager;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void createNewTaskShouldAddTaskToHistory() {

        Task task = new Task("New Task", "DescriptionTask", TaskStatus.IN_PROGRESS);
        manager.createNewTask(task);

        // Получаем задачу по идентификатору и проверяем, что она равна добавленной
        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);
        // Проверяем, что эта задача также добавлена в историю
        assertEquals(task, manager.getHistory().get(manager.getHistory().size() - 1), "Задача не добавленна в историю");
    }

    @Test
    void removeTaskByIdShouldRemoveTaskFromManager() {

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
    void getTasksByIdShouldReturnCorrectTasks() {

        Task task = new Task("Task", "DescriptionTask", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "DescriptionEpic");

        manager.createNewTask(task);
        manager.createNewEpic(epic);

        assertEquals(task, manager.getTaskById(task.getId()), "Задача не найдена по id");
        assertEquals(epic, manager.getEpicById(epic.getId()), "Задача не найдена по id");
    }

    @Test
    void updateEpicStatusShouldUpdateEpicStatusCorrectly() {
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

    @Test
    void removeEpicByIdShouldRemoveEpicAndSubTasks() {
        Epic epic = new Epic("Epic", "Description");
        SubTask subTask = new SubTask("SubTask", "Description", TaskStatus.IN_PROGRESS, epic);
        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask);
        manager.removeEpicById(epic.getId());
        assertNull(manager.getSubTaskById(subTask.getId()));
    }

    @Test
    void removeSubTaskShouldRemoveSubTaskFromEpic() {
        Epic epic = new Epic("Epic", "Description");
        SubTask subTask1 = new SubTask("SubTask1", "Description", TaskStatus.IN_PROGRESS, epic);
        SubTask subTask2 = new SubTask("SubTask2", "Description", TaskStatus.IN_PROGRESS, epic);

        manager.createNewEpic(epic);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        manager.removeSubTaskById(subTask1.getId());
        assertFalse(epic.getSubTaskIds().contains(subTask1.getId()));
    }

    @Test
    void updateTaskDescriptionShouldUpdateTaskDescription() {
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        manager.createNewTask(task);
        String newDescription = "New Description";
        task.setDescription(newDescription);
        Task updatedTask = manager.getTaskById(task.getId());
        assertEquals(newDescription, updatedTask.getDescription());
    }

    @Test
    void removeAllSubTasksShouldRemoveAllSubTasksFromManager() {
        Epic epic = new Epic("Epic", "DescriptionEpic");
        manager.createNewEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "DescriptionSubTask 1", TaskStatus.NEW, epic);
        SubTask subTask2 = new SubTask("SubTask 2", "DescriptionSubTask 2", TaskStatus.IN_PROGRESS, epic);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);

        manager.removeAllSubTasks();

        assertEquals(0, manager.getAllSubTasks().size(), "The subtasks have not been deleted");
        assertEquals(0, epic.getSubTaskIds().size(), "The epic have not been deleted");
    }

    @Test
    void updateTaskShouldUpdateTaskCorrectly() {
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        manager.createNewTask(task);
        Task updatedTask = new Task(task.getId(), "Updated Task", "Updated Description", TaskStatus.IN_PROGRESS);
        manager.updateTask(updatedTask);
        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(updatedTask, retrievedTask);
    }

    @Test
    void updateSubTaskShouldUpdateSubTaskCorrectly() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        SubTask subTask = new SubTask("SubTask", "Description", TaskStatus.NEW, epic);
        manager.createNewSubTask(subTask);
        SubTask updatedSubTask = new SubTask(subTask.getId(), "Updated SubTask", "Updated Description", TaskStatus.IN_PROGRESS, epic);
        manager.updateSubTask(updatedSubTask);
        SubTask retrievedSubTask = manager.getSubTaskById(subTask.getId());
        assertEquals(updatedSubTask, retrievedSubTask);
    }

    @Test
    void updateEpicShouldUpdateEpicCorrectly() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        Epic updatedEpic = new Epic(epic.getId(), "Updated Epic", "Updated Description");
        manager.updateEpic(updatedEpic);
        Epic retrievedEpic = manager.getEpicById(epic.getId());
        assertEquals(updatedEpic, retrievedEpic);
    }

    @Test
    void getSubTasksOfEpicShouldReturnSubTasksBelongingToEpic() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description", TaskStatus.DONE, epic);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description", TaskStatus.IN_PROGRESS, epic);
        SubTask subTask3 = new SubTask(3, "SubTask 3", "Description", TaskStatus.NEW, epic);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        manager.createNewSubTask(subTask3);
        List<SubTask> subTasks = manager.getSubTasksOfEpic(epic);
        assertEquals(3, subTasks.size());
        assertEquals(subTask1, subTasks.get(0));
        assertEquals(subTask2, subTasks.get(1));
        assertEquals(subTask3, subTasks.get(2));
    }
}
