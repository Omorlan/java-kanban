package managers.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.managers.Managers;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        assertEquals(task, manager.getHistory().get(manager.getHistory().size() - 1), "Task was not added to history");
    }

    @Test
    void removeTaskByIdShouldRemoveTaskFromManager() {

        Task task = new Task("Task to Remove", "DescriptionTaskToRemove", TaskStatus.NEW);
        manager.createNewTask(task);

        Task retrievedTask = manager.getTaskById(task.getId());
        assertEquals(task, retrievedTask);

        manager.removeTaskById(task.getId());
        assertNull(manager.getTaskById(task.getId()), "Task was not removed");

    }

    @Test
    void getTasksByIdShouldReturnCorrectTasks() {

        Task task = new Task("Task", "DescriptionTask", TaskStatus.NEW);
        Epic epic = new Epic("Epic", "DescriptionEpic");

        manager.createNewTask(task);
        manager.createNewEpic(epic);

        assertEquals(task, manager.getTaskById(task.getId()), "Task not found by id");
        assertEquals(epic, manager.getEpicById(epic.getId()), "Task not found by id");
    }

    @Test
    void epicStatusWithAllSubtasksNewShouldBeInProgress() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Status of newly created epic should be NEW");

        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description", TaskStatus.NEW, epic);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description", TaskStatus.NEW, epic);

        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Status of epic with subtasks with status NEW should be IN_PROGRESS");
    }

    @Test
    void epicStatusWithAllSubtasksDoneShouldBeDone() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Status of newly created epic should be NEW");

        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description", TaskStatus.DONE, epic);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description", TaskStatus.DONE, epic);

        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Status of epic with subtasks with status DONE should be DONE");
    }

    @Test
    void epicStatusWithMixedSubtaskStatusesShouldBeInProgress() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Status of newly created epic should be NEW");

        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description", TaskStatus.NEW, epic);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description", TaskStatus.DONE, epic);

        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Status of epic with mixed status subtasks should be IN_PROGRESS");
    }

    @Test
    void epicStatusWithInProgressSubtasksShouldBeInProgress() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Status of newly created epic should be NEW");

        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description", TaskStatus.IN_PROGRESS, epic);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description", TaskStatus.IN_PROGRESS, epic);

        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Status of epic with subtasks with status IN_PROGRESS should be IN_PROGRESS");
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
    void epicIdsForNullEpicShouldBeNull() {
        List<SubTask> subTasks = manager.getSubTasksOfEpic(null);
        assertNotNull(subTasks, "List of subtasks should not be null");
        assertTrue(subTasks.isEmpty(), "List of subtasks for null Epic should be empty");
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
    void getPrioritizedTasksShouldIncludeEpicsWithStartTime() {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);
        SubTask subTask = new SubTask("Task with start time", "Description", TaskStatus.DONE, epic, 15, "17.03.2024 09:00");
        manager.createNewSubTask(subTask);
        Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(prioritizedTasks.contains(epic));
    }

    @Test
    void removeAllTasksShouldRemoveAllTasksFromHistory() {

        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.IN_PROGRESS);
        Task task2 = new Task(2, "Task 2", "Description 2", TaskStatus.DONE);
        Task task3 = new Task(3, "Task 3", "Description 3", TaskStatus.NEW);

        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.createNewTask(task3);

        manager.removeAllTasks();

        assertNull(manager.getTaskById(1));
        assertNull(manager.getTaskById(2));
        assertNull(manager.getTaskById(3));
    }

    @Test
    void removeAllEpicsShouldRemoveAllEpicsAndSubTasksFromHistory() {
        Epic epic1 = new Epic(1, "Epic 1", "Description 1");
        Epic epic2 = new Epic(2, "Epic 2", "Description 2");

        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description 1", TaskStatus.IN_PROGRESS, epic1);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description 2", TaskStatus.DONE, epic2);
        SubTask subTask3 = new SubTask(3, "SubTask 3", "Description 3", TaskStatus.NEW, epic2);

        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);
        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);
        manager.createNewSubTask(subTask3);

        manager.removeAllEpics();
        assertNull(manager.getEpicById(1));
        assertNull(manager.getEpicById(2));

        assertNull(manager.getSubTaskById(1));
        assertNull(manager.getSubTaskById(2));
        assertNull(manager.getSubTaskById(3));
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

    @Test
    void getPrioritizedTasksShouldReturnTasksInStartTimeOrder() {
        Task task1 = new Task("Task 1", "t1", TaskStatus.IN_PROGRESS, 10, "17.03.2024 09:00"); //should be 3
        Task task2 = new Task("Task 2", "t2", TaskStatus.IN_PROGRESS, 40, "17.03.2024 08:00"); //should be 2
        Task task3 = new Task("Task 3", "t3", TaskStatus.IN_PROGRESS, 13, "17.03.2024 19:00"); //should be 4
        Task task4 = new Task("Task 4", "t4", TaskStatus.IN_PROGRESS, 25, "17.03.2024 05:00"); //should be 1
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.createNewTask(task3);
        manager.createNewTask(task4);
        Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
        Comparator<Task> startTimeComparator = Comparator.comparing(Task::getStartTime);
        TreeSet<Task> expectedTasks = new TreeSet<>(startTimeComparator);
        expectedTasks.add(task4);
        expectedTasks.add(task2);
        expectedTasks.add(task1);
        expectedTasks.add(task3);
        assertEquals(expectedTasks, prioritizedTasks);
    }

    @Test
    void ifTimeOfTaskIsCrossingTaskShouldNotCreated() {
        Task task1 = new Task("Task 1", "t1", TaskStatus.IN_PROGRESS, 10, "17.03.2024 09:00");
        Task task2 = new Task("Task 2", "t2", TaskStatus.IN_PROGRESS, 40, "17.03.2024 08:45");
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        assertEquals(1, manager.getAllTasks().size());
    }
}
