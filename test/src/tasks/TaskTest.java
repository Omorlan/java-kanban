package tasks;

import org.junit.jupiter.api.Test;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskTest {
    //Две задачи с одинаковым id должны выглядеть для менеджера как одна и та же.
    @Test
    public void taskInheritanceEqualityByIdShouldReturnTrueForEqualIds() {
        Task task1 = new Task("Таска поменьше", "Совсем маленькая", TaskStatus.DONE);
        Task task2 = new Task("Таска покрупнее", "Вот это она большая", TaskStatus.IN_PROGRESS);
        task1.setId(0);
        task2.setId(0);
        assertEquals(task1, task2, "Две задачи с одинаковым id для менеджера не выглядят как одна и та же.");
    }

    @Test
    public void сreateTaskShouldCreateTaskWithExpectedValues() {
        Task task = new Task("Name", "Description", TaskStatus.DONE);
        assertEquals("Name", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(TaskStatus.DONE, task.getStatus(), "Задача не была создана");
    }
    @Test
    public void tasksWithDifferentIdsShouldNotEqual() {
        Task task1 = new Task("Task", "Description", TaskStatus.NEW);
        Task task2 = new Task("Task, but another", "Description", TaskStatus.IN_PROGRESS);
        task1.setId(1);
        task2.setId(2);
        assertNotEquals(task1, task2, "Задачи с разными id  не должны быть равны");
    }
    @Test
    public void setStatusShouldUpdateTaskStatusAsExpected() {
        Task task = new Task("Task", "Description", TaskStatus.NEW);
        assertEquals(TaskStatus.NEW, task.getStatus(), "Статус задачи должен быть NEW");

        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus(), "Статус задачи должен быть IN_PROGRESS");

        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus(), "Статус задачи должен быть DONE");
    }
}