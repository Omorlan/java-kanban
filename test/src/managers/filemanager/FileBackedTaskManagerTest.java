package managers.filemanager;

import org.junit.jupiter.api.Test;
import sprint4.managers.filemanager.FileBackedTaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {



    @Test
    public void SaveAndLoadFromFileMustBeSame() throws IOException {

        File file = new File("src/resources/backup.csv");

        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

        Task task1 = new Task(1, "Task 1", "Description 1", TaskStatus.NEW);
        Epic epic1 = new Epic(2, "Epic 1", "Description of Epic 1");
        SubTask subTask1 = new SubTask(3, "SubTask 1", "Description of SubTask 1", TaskStatus.IN_PROGRESS, epic1);

        fileManager.createNewTask(task1);
        fileManager.createNewEpic(epic1);
        fileManager.createNewSubTask(subTask1);
        fileManager.getTaskById(task1.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(fileManager.getTaskById(1), loadedManager.getTaskById(1),"task created from the file " +
                "does not match the saved one");
        assertEquals(fileManager.getSubTaskById(3), loadedManager.getSubTaskById(3),"subtask created from the file" +
                " does not match the saved one");
        assertEquals(fileManager.getEpicById(2), loadedManager.getEpicById(2),"epic created from the file" +
                " does not match the saved one");


    }
}
