import sprint4.manager.TaskManager;
import sprint4.tasks.Epic;
import sprint4.tasks.SubTask;
import sprint4.tasks.Task;
import sprint4.tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        Task task1 = new Task("������ 1", "1", TaskStatus.NEW);
        Task task2 = new Task("������ 2", "2", TaskStatus.IN_PROGRESS);
        manager.createNewTask(task1);
        manager.createNewTask(task2);

        Epic epic1 = new Epic("���� 1", "�1");
        Epic epic2 = new Epic("���� 2", "�2");
        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);

        SubTask sb1 = new SubTask("�������� �����1 1", "���1-1", TaskStatus.NEW, epic1);
        SubTask sb2 = new SubTask("�������� �����1 2", "���1-2", TaskStatus.IN_PROGRESS, epic1);
        SubTask sb3 = new SubTask("�������� �����2 1", "���1-1", TaskStatus.NEW, epic2);

        manager.createNewSubTask(sb1);
        manager.createNewSubTask(sb2);
        manager.createNewSubTask(sb3);
        System.out.println("\n���� �� ���������� ��������� " + manager.getEpicById(epic1.getId()));
        SubTask sb1up = new SubTask(sb1.getId(), "���������� �����1 1", "���1-1", TaskStatus.DONE, epic1);
        SubTask sb2up = new SubTask(sb2.getId(), "���������� �����1 2", "���1-2", TaskStatus.DONE, epic1);
        manager.updateSubTask(sb1up);
        manager.updateSubTask(sb2up);
        System.out.println("\n���� ����� ���������� ��������� " + manager.getEpicById(2));
        manager.removeEpicById(epic1.getId());
        manager.removeTaskById(task1.getId());

    }
}
