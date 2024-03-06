package tasks;

import org.junit.jupiter.api.Test;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    //Две задачи с одинаковым id должны выглядеть для менеджера как одна и та же.
    @Test
    void shouldBePositiveWhenIdAreEqual() {
        Task task1 = new Task("Таска поменьше", "Совсем маленькая", TaskStatus.DONE);
        Task task2 = new Task("Таска покрупнее", "Вот это она большая", TaskStatus.IN_PROGRESS);
        task1.setId(0);
        task2.setId(0);
        assertEquals(task1, task2, "Две задачи с одинаковым id для менеджера не выглядят как одна и та же.");
    }

    @Test
    void testCreateTask() {
        Task task = new Task("Name", "Description", TaskStatus.DONE);
        assertEquals("Name", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(TaskStatus.DONE, task.getStatus(), "Задача не была создана");
    }
}