package Sprint4.Tasks;


import Sprint4.Manager.TaskManager;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected TaskType type;

    public Task(String name, String description, TaskStatus status) {
        this.id = TaskManager.generateTaskId();
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TASK{" +
                "id=" + id +
                ", Name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", Status='" + status + '\'' +
                '}';
    }


}
