package sprint4.tasks;

public class SubTask extends Task {
    private Epic epic;

    public SubTask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        this.type = TaskType.SUBTASK;
    }

    public SubTask(int id, String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.id = id;
        this.epic = epic;
        this.type = TaskType.SUBTASK;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                " id=" + id +
                ", Name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", Status='" + status + '\'' +
                ", Subtask of epic = '" + epic.getId() + '\'' +
                '}';
    }

    @Override
    public String toStringFromFile() {
        return String.format("%s,%s,%s,%s,%s,%s", id, type, name, status, description, epic.getId());
    }
}