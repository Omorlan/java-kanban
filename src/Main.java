import sprint4.manager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

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

        SubTask sb1 = new SubTask("Сабтаска эпика1 1", "стэ1-1", TaskStatus.NEW, epic1);
        SubTask sb2 = new SubTask("Сабтаска эпика1 2", "стэ1-2", TaskStatus.IN_PROGRESS, epic1);
        SubTask sb3 = new SubTask("Сабтаска эпика2 1", "стэ1-1", TaskStatus.NEW, epic2);

        manager.createNewSubTask(sb1);
        manager.createNewSubTask(sb2);
        manager.createNewSubTask(sb3);
        System.out.println("\nЭпик до обновления сабтасков " + manager.getEpicById(epic1.getId()));
        SubTask sb1up = new SubTask(sb1.getId(), "АпСабтаска эпика1 1", "стэ1-1", TaskStatus.DONE, epic1);
        SubTask sb2up = new SubTask(sb2.getId(), "АпСабтаска эпика1 2", "стэ1-2", TaskStatus.DONE, epic1);
        manager.updateSubTask(sb1up);
        manager.updateSubTask(sb2up);
        System.out.println("\nЭпик после обновления сабтасков " + manager.getEpicById(2));
        manager.removeEpicById(epic1.getId());
        manager.removeTaskById(task1.getId());

    }
}
