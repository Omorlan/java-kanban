package tasks;

import org.junit.jupiter.api.Test;
import sprint4.tasks.Epic;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicTest {
    @Test
    void testTaskInheritanceEqualityById() {
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Epic2", "Description2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2);
    }


    @Test
    public void testCreateEpic() {
        Epic epic = new Epic("EpicName", "Description");

        assertEquals("EpicName", epic.getName());
        assertEquals("Description", epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertTrue(epic.getSubTaskIds().isEmpty());
    }

    @Test
    public void testAddSubTaskIdToEpic() {
        Epic epic = new Epic("EpicName", "Description");
        epic.addSubTaskId(1);
        assertEquals(1, epic.getSubTaskIds().size());
        assertTrue(epic.getSubTaskIds().contains(1));
    }

    @Test
    public void testRemoveSubTaskIdFromEpic() {
        Epic epic = new Epic("EpicName", "Description");
        epic.addSubTaskId(1);
        epic.removeSubTaskId(1);

        assertTrue(epic.getSubTaskIds().isEmpty(),"Сабтаска не удаляется из эпика");
    }

    @Test
    public void testRemoveAllSubtasksFromEpic() {
        Epic epic = new Epic("EpicName", "Description");
        epic.addSubTaskId(1);
        epic.addSubTaskId(2);
        epic.removeAllSubtasks();
        assertTrue(epic.getSubTaskIds().isEmpty(),"Список подзадач не очищается");
    }
}
