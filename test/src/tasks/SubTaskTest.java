package tasks;

import org.junit.jupiter.api.Test;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {
    @Test
    public void testCreateSubTask() {
        Epic epic = new Epic("EpicName", "Description");
        SubTask subTask = new SubTask("SubTaskName", "Description", TaskStatus.IN_PROGRESS, epic);

        assertEquals("SubTaskName", subTask.getName());
        assertEquals("Description", subTask.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, subTask.getStatus());
        assertEquals(epic, subTask.getEpic());
    }


}
