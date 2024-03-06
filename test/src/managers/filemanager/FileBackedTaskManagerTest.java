package managers.filemanager;

import org.junit.jupiter.api.Test;
import sprint.managers.filemanager.FileBackedTaskManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FileBackedTaskManagerTest {



    @Test
    void saveAndLoadFromFileMustBeSame() throws IOException {

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
    @Test
    void loadFromEmptyFileShouldNotCreateAnything() throws IOException {
        File tempDir = Files.createTempDirectory("test_files").toFile();
        File file = Files.createTempFile(tempDir.toPath(), "empty_tasks", ".csv").toFile();

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(fileManager.getAllTasks().isEmpty(),"Nothing should be created from an empty file");
        assertTrue(fileManager.getAllEpics().isEmpty(),"Nothing should be created from an empty file");
        assertTrue(fileManager.getAllSubTasks().isEmpty(),"Nothing should be created from an empty file");
        assertTrue(fileManager.getHistory().isEmpty(),"Nothing should be created from an empty file");
    }
    @Test
    void saveEmptyManagerToFileShouldCreateEmptyFile() throws IOException {
        File tempDir = Files.createTempDirectory("test_files").toFile();
        File file = Files.createTempFile(tempDir.toPath(), "empty_file", ".csv").toFile();

        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        assertTrue(file.exists(), "Saved file should exist");
        assertEquals(0, file.length(), "Saved file should be empty");
    }
    @Test
    void createTasksFromFileShouldBeCreatedInManager() throws IOException {
        File tempDir = Files.createTempDirectory("test_files").toFile();
        File file = Files.createTempFile(tempDir.toPath(), "tasks", ".csv").toFile();
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            writer.write("1,TASK,Task 1,NEW,Description 1,\n");
            writer.write("2,EPIC,Epic 1,NEW,Description of Epic 1,\n");
            writer.write("3,SUBTASK,SubTask 1,NEW,Description of SubTask 1,2\n");
        }

        FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);

        Task task = fileManager.getTaskById(1);
        assertNotNull(task, "Task should be created from the file");
        assertEquals("Task 1", task.getName(), "Task name should match");
        assertEquals("Description 1", task.getDescription(), "Task description should match");
        assertEquals(TaskStatus.NEW, task.getStatus(), "Task status should match");

        Epic epic = fileManager.getEpicById(2);
        assertNotNull(epic, "Epic should be created from the file");
        assertEquals("Epic 1", epic.getName(), "Epic name should match");
        assertEquals("Description of Epic 1", epic.getDescription(), "Epic description should match");
        assertNotEquals(TaskStatus.NEW,epic.getStatus(), "Epic status should be changed");
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Epic status should match");

        SubTask subTask = fileManager.getSubTaskById(3);
        assertNotNull(subTask, "Subtask should be created from the file");
        assertEquals("SubTask 1", subTask.getName(), "Subtask name should match");
        assertEquals("Description of SubTask 1", subTask.getDescription(), "Subtask description should match");
        assertEquals(TaskStatus.NEW, subTask.getStatus(), "Subtask status should match");
    }







}
