package sprint.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected TaskType type;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    //Constructor without id and time
    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
    }

    //Constructor for update methods without time
    public Task(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
    }

    //Constructor without id
    public Task(String name, String description, TaskStatus status, long duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, TimeFormatter.TIMEFORMATTER);
        this.endTime = this.startTime.plusMinutes(duration);
    }

    //Constructor for update methods
    public Task(int id, String name, String description, TaskStatus status, long duration, String startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, TimeFormatter.TIMEFORMATTER);
        this.endTime = this.startTime.plusMinutes(duration);
    }

    public String toStringFromFile() {
        String durationString = (duration != null) ? duration.toMinutes() + "" : "N/A";
        String startTimeString = (startTime != null) ? startTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        String endTimeString = (getEndTime() != null) ? getEndTime().format(TimeFormatter.TIMEFORMATTER) : "N/A";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", id, type, name, status, description, "N/A", durationString, startTimeString, endTimeString);
    }

    public String getName() {
        return name;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskType getType() {
        return type;
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (duration != null && startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        String durationString = (duration != null) ? duration.toMinutes() + "" : "N/A";
        String startTimeString = (startTime != null) ? startTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        String endTimeString = (getEndTime() != null) ? getEndTime().format(TimeFormatter.TIMEFORMATTER) : "N/A";

        return "TASK{" + "id=" + id + ", Name='" + name + '\'' + ", Description='" + description + '\'' + ", Status='" + status + '\'' + ", Duration='" + durationString + '\'' + ", Start time='" + startTimeString + '\'' + ", End time='" + endTimeString + '\'' + '}';
    }


}
