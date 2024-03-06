package sprint.managers.filemanager;

import sprint.managers.filemanager.exception.ManagerCreateException;
import sprint.managers.filemanager.exception.ManagerSaveException;
import sprint.managers.historymanager.HistoryManager;
import sprint.managers.taskmanager.InMemoryTaskManager;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;
import sprint.tasks.TaskType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            saveTasks(writer, super.getAllTasks().values());
            saveTasks(writer, super.getAllEpics().values());
            saveTasks(writer, super.getAllSubTasks().values());
            writer.write("\n");
            writer.write(toString(this.history));
        } catch (IOException e) {
            throw new ManagerSaveException("File creating error");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(file);
        Map<Integer, Task> tasks = new HashMap<>();
        Map<Integer, Epic> epics = new HashMap<>();
        Map<Integer, SubTask> subTasks = new HashMap<>();
        Map<Integer, Task> allTasks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            String historyLine = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (!line.isEmpty()) {
                    if (parts.length >= 5) {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[2];
                        String description = parts[4];
                        TaskStatus status = TaskStatus.valueOf(parts[3]);
                        TaskType type = TaskType.valueOf(parts[1]);

                        switch (type) {
                            case TASK -> tasks.put(id, new Task(id, name, description, status));
                            case EPIC -> epics.put(id, new Epic(id, name, description));
                            case SUBTASK -> {
                                int epicId = Integer.parseInt(parts[5]);
                                subTasks.put(id, new SubTask(id, name, description, status, epics.get(epicId)));
                            }
                        }
                    }
                } else {
                    historyLine = reader.readLine();
                }
            }
            fileBackedTasksManager.createTasksFromFile(tasks);
            fileBackedTasksManager.createTasksFromFile(epics);
            fileBackedTasksManager.createTasksFromFile(subTasks);
            allTasks.putAll(tasks);
            allTasks.putAll(epics);
            allTasks.putAll(subTasks);
            if (historyLine != null) {
                String[] parts = historyLine.split(",");
                for (String strId : parts) {
                    int id = Integer.parseInt(strId);
                    fileBackedTasksManager.history.add(allTasks.get(id));
                }
            }
        } catch (IOException e) {
            throw new ManagerCreateException("File reading error.");
        }
        return fileBackedTasksManager;
    }

    private void createTasksFromFile(Map<Integer, ? extends Task> tasks) {
        for (Task task : tasks.values()) {
            if (task.getClass() == Epic.class) {
                createNewEpicFromFile((Epic) task);
            } else if (task.getClass() == SubTask.class) {
                createNewSubTaskFromFile((SubTask) task);
            } else {
                createNewTaskFromFile(task);
            }
        }
    }

    private void saveTasks(Writer writer, Collection<? extends Task> tasks) throws IOException {
        for (Task task : tasks) {
            writer.write(task.toStringFromFile() + "\n");
        }
    }

    private String toString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        manager.getHistory().stream()
                .map(task -> String.valueOf(task.getId()))
                .forEach(id -> {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(id);
                });
        return sb.toString();
    }


    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }


    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void createNewSubTask(SubTask subTask) {
        super.createNewSubTask(subTask);
        save();
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    public void createNewTaskFromFile(Task task) {
        task.setId(task.getId());
        taskMap.put(task.getId(), task);
        save();
    }


    public void createNewSubTaskFromFile(SubTask subTask) {
        subTask.setId(subTask.getId());
        subTaskMap.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.addSubTaskId(subTask.getId());
        updateEpicStatusBySubTasks(epic);
        save();
    }


    public void createNewEpicFromFile(Epic epic) {
        epic.setId(epic.getId());
        epicMap.put(epic.getId(), epic);
        save();
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        super.updateSubTask(updatedSubTask);
        save();
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Epic epic) {
        List<SubTask> subTasks = super.getSubTasksOfEpic(epic);
        save();
        return subTasks;
    }

}