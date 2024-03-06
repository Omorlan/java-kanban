package tasks;

import org.junit.jupiter.api.Test;
import sprint.tasks.Epic;
import sprint.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
class EpicTest {
    private static final String EPIC_NAME = "EpicName";
    private static final String DESCRIPTION = "Description";
    @Test
    void testTaskInheritanceEqualityById() {
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Совсем другая задача", "И описание ни на что ранее не похожее");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Две задачи с одинаковым id для менеджера не выглядят как одна и та же.");
    }


    @Test
    void testCreateEpic() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);

        assertEquals(EPIC_NAME, epic.getName());
        assertEquals(DESCRIPTION, epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertTrue(epic.getSubTaskIds().isEmpty(), "эпик не был создан");
    }

    @Test
    void testAddSubTaskIdToEpic() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        epic.addSubTaskId(1);
        assertEquals(1, epic.getSubTaskIds().size());
        assertTrue(epic.getSubTaskIds().contains(1));
    }

    @Test
    void testRemoveSubTaskIdFromEpic() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        epic.addSubTaskId(1);
        epic.removeSubTaskId(1);

        assertTrue(epic.getSubTaskIds().isEmpty(), "Сабтаска не удаляется из эпика");
    }

    @Test
    void testRemoveAllSubtasksFromEpic() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        epic.addSubTaskId(1);
        epic.addSubTaskId(2);
        epic.removeAllSubtasks();
        assertTrue(epic.getSubTaskIds().isEmpty(), "Список подзадач не очищается");
    }
}
