package sprint4.manager;

        import sprint4.tasks.Epic;
        import sprint4.tasks.SubTask;
        import sprint4.tasks.Task;
        import sprint4.tasks.TaskStatus;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int taskIdCounter = 0; //  ������� ��� ��������� ���������������

    private final HistoryManager history = Managers.getDefaultHistory();
    private Map<Integer, Task> taskMap = new HashMap<>(); //��������� ��� �������� �����
    private Map<Integer, SubTask> subTaskMap = new HashMap<>(); //��������� ��� �������� ��������
    private Map<Integer, Epic> epicMap = new HashMap<>(); //��������� ��� �������� ������

    /*
     a. ��������� ������ ���� �����.
     */
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Task> getAllSubTasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    @Override
    public List<Task> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    /*
     b. �������� ���� �����.
     */
    @Override
    public void removeAllTasks() {
        taskMap.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTaskMap.clear();
        for (Epic epic : epicMap.values()) {
            epic.removeAllSubtasks();
            updateEpicStatusBySubTasks(epic);
        }
    }

    @Override
    public void removeAllEpics() {
        epicMap.clear();
        subTaskMap.clear();
    }


    /*
     c. ��������� �� ��������������.
     */
    @Override
    public Task getTaskById(int id) {
        Task task = taskMap.get(id); //�� ���� �� ������� ����� ���� ���� � ���������� ������� �������
        history.add(task);           // taskMap.get(id),��� ����� ������� ������ �������
        return task;                 // �� ����� ������� ����� � ����� �������������
    }

    @Override
    public Epic getEpicById(int id) {
       Epic epic = epicMap.get(id);
        history.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTaskMap.get(id);
        history.add(subTask);
        return subTask;
    }

    /*
     d. ��������. ��� ������ ������ ������������ � �������� ���������.
     */
    @Override
    public void createNewTask(Task task) {
        task.setId(generateTaskId());
        taskMap.put(task.getId(), task);
    }

    @Override
    public void createNewSubTask(SubTask subTask) {
        subTask.setId(generateTaskId());
        subTaskMap.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.addSubTaskId(subTask.getId());
        updateEpicStatusBySubTasks(epic);


    }

    @Override
    public void createNewEpic(Epic epic) {
        epic.setId(generateTaskId());
        epicMap.put(epic.getId(), epic);
    }

    /*
     e. ����������. ����� ������ ������� � ������ ��������������� ��������� � ���� ���������.
     */
    @Override
    public void updateTask(Task updatedTask) {
        int key = updatedTask.getId();
        taskMap.put(key, updatedTask);

    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        int key = updatedSubTask.getId();
        subTaskMap.put(key, updatedSubTask); //��������� ��������
        Epic epic = updatedSubTask.getEpic();
        updateEpicStatusBySubTasks(epic); //��������� ������ �����
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        int key = updatedEpic.getId();
        epicMap.put(key, updatedEpic);
    }

    /*
     f. �������� �� ��������������.
     */
    @Override
    public void removeTaskById(int id) {
        taskMap.remove(id);

    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTaskToRemove = subTaskMap.remove(id);
        Epic epic = subTaskToRemove.getEpic();
        epic.removeSubTaskId(id);
        updateEpicStatusBySubTasks(epic);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epicToRemove = epicMap.remove(id);
        subTaskMap.values().removeIf(subTask -> subTask.getEpic() == epicToRemove);
    }

    // ����� ��� ��������� ������ ����������� �������������� ������
    private int generateTaskId() {
        return taskIdCounter++;
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Epic epic) {
        List<SubTask> subTasksList = new ArrayList<>();
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTaskIds()) {
                SubTask subTask = subTaskMap.get(subTaskId);
                if (subTask != null) {
                    subTasksList.add(subTask);
                }
            }
        }
        return subTasksList;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private void updateEpicStatusBySubTasks(Epic epic) {
        boolean allSubTasksDone = true;
        for (Integer subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTaskMap.get(subTaskId);
            if (subTask.getStatus() != TaskStatus.DONE) {
                allSubTasksDone = false;
                break;
            }
        }
        if (allSubTasksDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
