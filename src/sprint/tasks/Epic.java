package sprint.tasks;

import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    private final List<Integer> subTaskIds;

    public Epic(String name, String description) {
        super(name, description, null);
        subTaskIds = new ArrayList<>();
        this.type = TaskType.EPIC;
        this.status = TaskStatus.NEW;
    }

    public Epic(int id, String name, String description) {
        super(name, description, null);
        subTaskIds = new ArrayList<>();
        this.id = id;
        this.type = TaskType.EPIC;
        this.status = TaskStatus.NEW;

    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addSubTaskId(int subId) {
        subTaskIds.add(subId);
    }

    public void removeSubTaskId(int subId) {
        if (subTaskIds.contains(subId)) {
            subTaskIds.remove(Integer.valueOf(subId));
        }
    }

    public void removeAllSubtasks() {
        subTaskIds.clear();
    }

    @Override
    public String toString() {
        return "EPIC{" +
                "id=" + id +
                ", Name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", Status='" + status + '\'' +
                ", SubtasksIDs='" + subTaskIds + '\'' +
                '}';
    }
}