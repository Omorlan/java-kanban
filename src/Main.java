import sprint4.manager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task1 = new Task("Задача 1", "1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "2", TaskStatus.IN_PROGRESS);
        manager.createNewTask(task1);
        manager.createNewTask(task2);

        Epic epic1 = new Epic("Эпик 1", "э1");
        Epic epic2 = new Epic("Эпик 2", "э2");
        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);
        System.out.println("смотрим что у новых эпиков статус NEW");
        for (Map.Entry<Integer, Epic> entry : manager.getEpicMap().entrySet()) {
            System.out.println(entry.getValue());
        }
        SubTask sb1 = new SubTask("Сабтаска эпика1 1", "стэ1-1", TaskStatus.NEW, epic1);
        SubTask sb2 = new SubTask("Сабтаска эпика1 2", "стэ1-2", TaskStatus.IN_PROGRESS, epic1);
        SubTask sb3 = new SubTask("Сабтаска эпика2 1", "стэ1-1", TaskStatus.NEW, epic2);

        manager.createNewSubTask(sb1);
        manager.createNewSubTask(sb2);
        manager.createNewSubTask(sb3);
        printAll(manager);
        System.out.println("\nЭпик до обновления сабтасков " +manager.getEpicById(epic1.getId()));
        SubTask sb1up = new SubTask(sb1.getId(),"АпСабтаска эпика1 1", "стэ1-1", TaskStatus.DONE, epic1);
        SubTask sb2up = new SubTask(sb2.getId(),"АпСабтаска эпика1 2", "стэ1-2", TaskStatus.DONE, epic1);
        manager.updateSubTask(sb1up);
        manager.updateSubTask(sb2up);
        System.out.println("\nЭпик после обновления сабтасков " +  manager.getEpicById(2));
        System.out.println("Сабтаски до удаления эпика");
        printSubtasks(manager);
        manager.removeEpicById(epic1.getId());
        System.out.println("Сабтаски после удаленного эпика");
        printSubtasks(manager);
        manager.removeTaskById(task1.getId());
        printAll(manager);


    }
    public static void printAll(TaskManager taskManager) {
        System.out.println("ВЫВОД ВСЕХ МАП");
        System.out.println("\nTask Map contents:");
        for (Map.Entry<Integer, Task> entry : taskManager.getTaskMap().entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println("\nEpic Map contents:");
        for (Map.Entry<Integer, Epic> entry : taskManager.getEpicMap().entrySet()) {
            System.out.println(entry.getValue());
        }
        System.out.println("\nSubTask Map contents:");
        for (Map.Entry<Integer, SubTask> entry : taskManager.getSubTaskMap().entrySet()) {
            System.out.println(entry.getValue());
        }

    }
    public static void printSubtasks(TaskManager taskManager){
        System.out.println("\nSubTask Map contents:");
        for (Map.Entry<Integer, SubTask> entry : taskManager.getSubTaskMap().entrySet()) {
            System.out.println(entry.getValue());
        }
    }
    public static void printEpics(TaskManager taskManager){
        System.out.println("\nEpic Map contents:");
        for (Map.Entry<Integer, Epic> entry : taskManager.getEpicMap().entrySet()) {
            System.out.println(entry.getValue());
        }
    }

}
