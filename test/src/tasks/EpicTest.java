package tasks;

import org.junit.jupiter.api.Test;
import sprint4.tasks.Epic;
import sprint4.tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class EpicTest {
    private static final String EPIC_NAME = "EpicName";
    private static final String DESCRIPTION = "Description";
    @Test
    void taskInheritanceEqualityByIdShouldReturnTrueForEqualIds() {
        Epic epic1 = new Epic("Epic1", "Description1");
        Epic epic2 = new Epic("Совсем другая задача", "И описание ни на что ранее не похожее");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Две задачи с одинаковым id для менеджера не выглядят как одна и та же.");
    }


    @Test
    public void createEpicShouldCreateEpicWithDefaultValues() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);

        assertEquals(EPIC_NAME, epic.getName());
        assertEquals(DESCRIPTION, epic.getDescription());
        assertEquals(TaskStatus.NEW, epic.getStatus());
        assertTrue(epic.getSubTaskIds().isEmpty(), "эпик не был создан");
    }

    @Test
    public void addSubTaskIdToEpicShouldAddSubTaskIdToEpic() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        epic.addSubTaskId(1);
        assertEquals(1, epic.getSubTaskIds().size());
        assertTrue(epic.getSubTaskIds().contains(1));
    }

    @Test
    public void removeSubTaskIdFromEpicShouldRemoveSubTaskIdFromEpic() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        epic.addSubTaskId(1);
        epic.removeSubTaskId(1);

        assertTrue(epic.getSubTaskIds().isEmpty(), "Сабтаска не удаляется из эпика");
    }

    @Test
    public void removeAllSubtasksFromEpicShouldRemoveAllSubtasksFromEpic() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        epic.addSubTaskId(1);
        epic.addSubTaskId(2);
        epic.removeAllSubtasks();
        assertTrue(epic.getSubTaskIds().isEmpty(), "Список подзадач не очищается");
    }
}
