import sprint.managers.Managers;
import sprint.managers.filemanager.FileBackedTaskManager;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task 1", "t1", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "t2", TaskStatus.IN_PROGRESS, 40, "17.03.2024 09:00");
        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.getTaskById(0);
        manager.getTaskById(1);
        System.out.println(manager.getHistory());
        manager.getTaskById(0);
        System.out.println(manager.getHistory());


        Epic epic1 = new Epic("Epic 1", "e1");
        Epic epic2 = new Epic("Epic 2", "e2");
        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);

        SubTask sb1 = new SubTask("SubTask of epic1 1", "ste1-1", TaskStatus.NEW, epic1);
        SubTask sb2 = new SubTask("SubTask of epic1 2", "ste1-2", TaskStatus.IN_PROGRESS, epic1);
        SubTask sb3 = new SubTask("SubTask of epic2 1", "ste2-1", TaskStatus.NEW, epic2,40, "17.03.2024 19:00");

        manager.createNewSubTask(sb1);
        manager.createNewSubTask(sb2);
        manager.createNewSubTask(sb3);
        manager.getSubTaskById(sb2.getId());
        System.out.println(manager.getHistory());
        System.out.println("\nEpic before st update" + manager.getEpicById(epic1.getId()));
        SubTask sb1up = new SubTask(sb1.getId(), "UpSubTask of epic1 1", "ste1-1", TaskStatus.DONE, epic1);
        SubTask sb2up = new SubTask(sb2.getId(), "UpSubTask of epic1 2", "ste1-2", TaskStatus.DONE, epic1);
        manager.updateSubTask(sb1up);
        manager.updateSubTask(sb2up);

        System.out.println("\nEpic after st update" + manager.getEpicById(2));
        manager.removeEpicById(epic1.getId());
        manager.removeTaskById(task1.getId());

        System.out.println("All tasks");
        for (Task task : manager.getAllTasks().values()) {
            System.out.println(task);
        }
        System.out.println("All epics");
        for (Epic task : manager.getAllEpics().values()) {
            System.out.println(task);
        }
        System.out.println("All subtasks");
        for (SubTask task : manager.getAllSubTasks().values()) {
            System.out.println(task);
        }

        System.out.println("history");
        System.out.println(manager.getHistory());
        TaskManager manager2 = FileBackedTaskManager.loadFromFile(new File("src/resources/backup.csv"));
       System.out.println(manager2.getHistory());
        System.out.println(manager.getPrioritizedTasks());
    }
}
