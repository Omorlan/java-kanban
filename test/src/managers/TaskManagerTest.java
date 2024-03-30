package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;
import sprint.tasks.TimeFormatter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static managers.TaskGeneratorUtil.genTasks;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;


    private void genSubTasks(int amount, int epicId) {
        IntStream.range(0, amount)
                .mapToObj(i -> new SubTask("SubTask " + i, "Description " + i, TaskStatus.NEW, manager.getEpicById(epicId)))
                .forEach(manager::createNewSubTask);
    }

    private static Stream<TaskStatus[]> provideSubTaskStatusCombinations() {
        return Stream.of(
                new TaskStatus[]{TaskStatus.NEW, TaskStatus.NEW},
                new TaskStatus[]{TaskStatus.IN_PROGRESS, TaskStatus.IN_PROGRESS},
                new TaskStatus[]{TaskStatus.DONE, TaskStatus.DONE},
                new TaskStatus[]{TaskStatus.NEW, TaskStatus.IN_PROGRESS},
                new TaskStatus[]{TaskStatus.NEW, TaskStatus.DONE},
                new TaskStatus[]{TaskStatus.IN_PROGRESS, TaskStatus.DONE}
        );
    }

    @Test
    void getTaskShouldAddTaskToHistory() {
        genTasks(manager, 1, "task");
        assertEquals(manager.getTaskById(0), manager.getHistory().get(manager.getHistory().size() - 1), "Task was not added to history");
    }

    @Test
    void removeTaskByIdShouldRemoveTaskFromManager() {
        genTasks(manager, 1, "task");
        manager.removeTaskById(manager.getTaskById(0).getId());
        assertNull(manager.getTaskById(0), "Task was not removed");

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


    @ParameterizedTest
    @MethodSource("provideSubTaskStatusCombinations")
    void testEpicStatusWithSubtasks(TaskStatus subTaskStatus1, TaskStatus subTaskStatus2) {
        Epic epic = new Epic("Epic", "Description");
        manager.createNewEpic(epic);

        SubTask subTask1 = new SubTask(1, "SubTask 1", "Description", subTaskStatus1, epic);
        SubTask subTask2 = new SubTask(2, "SubTask 2", "Description", subTaskStatus2, epic);

        manager.createNewSubTask(subTask1);
        manager.createNewSubTask(subTask2);

        TaskStatus expectedEpicStatus = TaskStatus.IN_PROGRESS;
        if (subTaskStatus1 == TaskStatus.DONE && subTaskStatus2 == TaskStatus.DONE) {
            expectedEpicStatus = TaskStatus.DONE;
        }
        assertEquals(expectedEpicStatus, epic.getStatus(), "Status of epic with subtasks should be correct");
    }

    @Test
    void removeEpicByIdShouldRemoveEpicAndSubTasks() {
        genTasks(manager, 1, "Epic");
        genSubTasks(1, 0);
        manager.removeEpicById(manager.getEpicById(0).getId());
        assertNull(manager.getSubTaskById(1));
    }

    @Test
    void removeSubTaskShouldRemoveSubTaskFromEpic() {
        genTasks(manager, 1, "Epic");
        genSubTasks(2, 0);
        manager.removeSubTaskById(1);
        assertFalse(manager.getEpicById(0).getSubTaskIds().contains(1));
    }

    @Test
    void updateTaskDescriptionShouldUpdateTaskDescription() {
        genTasks(manager, 1, "task");
        String newDescription = "New Description";
        manager.getTaskById(0).setDescription(newDescription);
        assertEquals(newDescription, manager.getTaskById(0).getDescription());
    }

    @Test
    void epicIdsForNullEpicShouldBeNull() {
        List<SubTask> subTasks = manager.getSubTasksOfEpic(null);
        assertNotNull(subTasks, "List of subtasks should not be null");
        assertTrue(subTasks.isEmpty(), "List of subtasks for null Epic should be empty");
    }

    @Test
    void removeAllSubTasksShouldRemoveAllSubTasksFromManager() {
        genTasks(manager, 1, "Epic");
        genSubTasks(2, 0);
        manager.removeAllSubTasks();
        assertEquals(0, manager.getAllSubTasks().size(), "The subtasks have not been deleted");
        assertEquals(0, manager.getEpicById(0).getSubTaskIds().size(), "The epic have not been deleted");
    }

    @Test
    void updateTaskShouldUpdateTaskCorrectly() {
        genTasks(manager, 2, "task");
        Task updatedTask = manager.getTaskById(1);
        manager.removeTaskById(1);
        updatedTask.setId(0);
        manager.updateTask(updatedTask);
        assertEquals(updatedTask, manager.getTaskById(0));
    }

    @Test
    void updateSubTaskShouldUpdateSubTaskCorrectly() {
        genTasks(manager, 1, "epic");
        genSubTasks(2, 0);
        SubTask updatedSubTask = manager.getSubTaskById(2);
        manager.removeSubTaskById(2);
        updatedSubTask.setId(1);
        manager.updateSubTask(updatedSubTask);
        assertEquals(updatedSubTask, manager.getSubTaskById(1));
    }

    @Test
    void updateEpicShouldUpdateEpicCorrectly() {
        genTasks(manager, 2, "epic");
        Epic updatedEpic = manager.getEpicById(1);
        manager.removeEpicById(1);
        updatedEpic.setId(0);
        manager.updateEpic(updatedEpic);
        assertEquals(updatedEpic, manager.getEpicById(0));
    }

    @Test
    void getPrioritizedTasksShouldIncludeEpicsWithStartTime() {
        int num = 1;
        genTasks(manager, num, "Epic");
        genSubTasks(1, 0);
        manager.getSubTaskById(1).setDuration(Duration.ofMinutes(15));
        manager.getSubTaskById(1).setStartTime(LocalDateTime.parse("17.03.2024 09:00", TimeFormatter.TIME_FORMATTER));
        manager.updateEpic(manager.getEpicById(0));
        Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertTrue(prioritizedTasks.contains(manager.getEpicById(0)));
    }

    @Test
    void removeAllTasksShouldRemoveAllTasks() {
        int num = 3;
        genTasks(manager, num, "Task");
        assertEquals(num, manager.getAllTasks().size());
        manager.removeAllTasks();
        assertEquals(0, manager.getAllTasks().size());
    }


    @Test
    void removeAllEpicsShouldRemoveAllEpicsAndSubTasks() {

        genTasks(manager, 2, "epic");
        genSubTasks(1, 0);
        genSubTasks(2, 0);

        manager.removeAllEpics();
        assertEquals(0, manager.getAllEpics().size());
        assertEquals(0, manager.getAllSubTasks().size());
    }

    @Test
    void getSubTasksOfEpicShouldReturnSubTasksBelongingToEpic() {
        genTasks(manager, 1, "epic");
        genSubTasks(3, 0);
        List<SubTask> subTasks = manager.getSubTasksOfEpic(manager.getEpicById(0));
        assertEquals(3, subTasks.size());
        assertEquals(manager.getSubTaskById(1), subTasks.get(0));
        assertEquals(manager.getSubTaskById(2), subTasks.get(1));
        assertEquals(manager.getSubTaskById(3), subTasks.get(2));
    }

    @Test
    void getPrioritizedTasksShouldReturnTasksInStartTimeOrder() {
        genTasks(manager, 3, "task");
        String startTime = "17.03.2024 10:";
        int dec = 59;
        for (Map.Entry<Integer, Task> entry : manager.getAllTasks().entrySet()) {
            StringBuilder startTimeSb = new StringBuilder(startTime);
            entry.getValue().setDuration(Duration.ofMinutes(10));
            entry.getValue().setStartTime(LocalDateTime.parse(startTimeSb.append(dec).toString(), TimeFormatter.TIME_FORMATTER));
            dec -= 15;
        }

        Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
        Comparator<Task> startTimeComparator = Comparator.comparing(Task::getStartTime);
        TreeSet<Task> expectedTasks = new TreeSet<>(startTimeComparator);
        expectedTasks.add(manager.getTaskById(0));
        expectedTasks.add(manager.getTaskById(1));
        expectedTasks.add(manager.getTaskById(2));
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
