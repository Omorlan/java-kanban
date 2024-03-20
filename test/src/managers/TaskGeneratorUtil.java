package managers;

import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Epic;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.util.stream.IntStream;

public class TaskGeneratorUtil {

    public static void genTasks(TaskManager manager, int amount, String type) {
        switch (type.toLowerCase()) {
            case "task" -> IntStream.range(0, amount)
                    .mapToObj(i -> new Task("Task " + i, "Description " + i, TaskStatus.NEW))
                    .forEach(manager::createNewTask);
            case "epic" -> IntStream.range(0, amount)
                    .mapToObj(i -> new Epic("Epic " + i, "Description " + i))
                    .forEach(manager::createNewEpic);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        }
    }
}
