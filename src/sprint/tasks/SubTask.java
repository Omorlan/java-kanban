package sprint.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public SubTask(String name, String description, TaskStatus status, Epic epic, long duration, String startTime) {
        super(name, description, status);
        this.epic = epic;
        this.type = TaskType.SUBTASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, TimeFormatter.TIMEFORMATTER);
        this.endTime = this.startTime.plusMinutes(duration);
    }

    public SubTask(int id, String name, String description, TaskStatus status, Epic epic, long duration, String startTime) {
        super(name, description, status);
        this.id = id;
        this.epic = epic;
        this.type = TaskType.SUBTASK;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, TimeFormatter.TIMEFORMATTER);
        this.endTime = this.startTime.plusMinutes(duration);
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        String durationString = (duration != null) ? duration.toMinutes() + "" : "N/A";
        String startTimeString = (startTime != null) ? startTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        String endTimeString = (getEndTime() != null) ? getEndTime().format(TimeFormatter.TIMEFORMATTER) : "N/A";
        return "Subtask{" +
                " id=" + id +
                ", Name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", Status='" + status + '\'' +
                ", Subtask of epic = '" + epic.getId() + '\'' +
                ", Duration='" + durationString + '\'' +
                ", Start time='" + startTimeString + '\'' +
                ", End time='" + endTimeString + '\'' +
                '}';
    }

    @Override
    public String toStringFromFile() {
        String durationString = (duration != null) ? duration.toMinutes() + "" : "N/A";
        String startTimeString = (startTime != null) ? startTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        String endTimeString = (getEndTime() != null) ? getEndTime().format(TimeFormatter.TIMEFORMATTER) : "N/A";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                id, type, name, status, description, epic.getId(), durationString, startTimeString, endTimeString);
    }
}