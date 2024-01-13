import Sprint4.Manager.TaskManager;
import Sprint4.Tasks.*;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        TaskManager tc = new TaskManager();

        Task task1 = new Task("Задача 1", "1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "2", TaskStatus.IN_PROGRESS);
        tc.createNewTask(task1);
        tc.createNewTask(task2);

        Epic epic1 = new Epic("Эпик 1", "э1");
        Epic epic2 = new Epic("Эпик 2", "э2");
        tc.createNewEpic(epic1);
        tc.createNewEpic(epic2);

        SubTask sb1 = new SubTask("Сабтаска эпика1 1", "стэ1-1", TaskStatus.NEW, epic1);
        SubTask sb2 = new SubTask("Сабтаска эпика1 2", "стэ1-2", TaskStatus.IN_PROGRESS, epic1);
        SubTask sb3 = new SubTask("Сабтаска эпика2 1", "стэ1-1", TaskStatus.NEW, epic2);
        tc.createNewSubTask(sb1);
        tc.createNewSubTask(sb2);
        tc.createNewSubTask(sb3);
        System.out.println("Эпик до обновления сабтасков " +tc.getEpicById(2));
        SubTask sb1up = new SubTask("Сабтаска эпика1 1", "стэ1-1", TaskStatus.DONE, epic1);
        SubTask sb2up = new SubTask("Сабтаска эпика1 2", "стэ1-2", TaskStatus.DONE, epic1);
        tc.updateSubTask(sb1,sb1up);
        tc.updateSubTask(sb2,sb2up);
        System.out.println("Эпик после обновления сабтасков " +  tc.getEpicById(2));
       // System.out.println(tc.getAllTasks());
       // System.out.println(tc.getAllEpics());
       // System.out.println(tc.getAllSubTasks());
        tc.removeEpicById(3);
        tc.removeTaskById(0);
        
        printAll(tc);

    }
    public static void printAll(TaskManager taskManager) {
        System.out.println("Task Map contents:");
        for (Map.Entry<Integer, Task> entry : taskManager.taskMap.entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println("Epic Map contents:");
        for (Map.Entry<Integer, Epic> entry : taskManager.epicMap.entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println("\nSubTask Map contents:");
        for (Map.Entry<Integer, SubTask> entry : taskManager.subTaskMap.entrySet()) {
            System.out.println(entry.getValue());
        }

    }
}
