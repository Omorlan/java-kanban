package Sprint4.Tasks;

import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<Integer> subTaskIds;

    public Epic(String name, String description) {
        super(name, description, null);

        subTaskIds = new ArrayList<>();
        this.type = TaskType.EPIC;
        this.status = status;

    }

    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    public void addSubTaskId(int subId) {
        subTaskIds.add(subId);
    }

    public void removeSubTaskId(int subId) {
        if (subTaskIds.contains(subId)) {
            subTaskIds.remove(Integer.valueOf(subId));
        }
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