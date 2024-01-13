package Sprint4.Tasks;

public class SubTask extends Task {
    private Epic epic;  // Идентификатор эпика, к которому относится подзадача

    public SubTask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        this.type = TaskType.SUBTASK;
        epic.addSubTaskId(id);
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
                '}';
    }
}