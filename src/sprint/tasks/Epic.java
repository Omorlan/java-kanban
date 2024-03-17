package sprint.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Epic extends Task {
    private final List<Integer> subTaskIds;
    private Duration epicDuration;
    private LocalDateTime epicStartTime;
    private LocalDateTime epicEndTime;

    public Epic(String name, String description) {
        super(name, description, null);
        subTaskIds = new ArrayList<>();
        this.type = TaskType.EPIC;
        this.status = TaskStatus.NEW;
    }

    public Duration getEpicDuration() {
        return epicDuration;
    }

    public void setEpicDuration(Duration epicDuration) {
        this.epicDuration = epicDuration;
    }

    public LocalDateTime getEpicStartTime() {
        return epicStartTime;
    }

    public void setEpicStartTime(LocalDateTime epicStartTime) {
        this.epicStartTime = epicStartTime;
    }

    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
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
    public String toStringFromFile() {
        String durationString = (epicDuration != null) ? epicDuration.toMinutes() + "" : "N/A";
        String startTimeString = (epicStartTime != null) ? epicStartTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        String endTimeString = (epicEndTime != null) ? epicEndTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                id, type, name, status, description, "N/A", durationString, startTimeString, endTimeString);
    }

    @Override
    public String toString() {
        String durationString = (epicDuration != null) ? epicDuration.toMinutes() + "" : "N/A";
        String startTimeString = (epicStartTime != null) ? epicStartTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        String endTimeString = (epicEndTime != null) ? epicEndTime.format(TimeFormatter.TIMEFORMATTER) : "N/A";
        return "EPIC{" +
                "id=" + id +
                ", Name='" + name + '\'' +
                ", Description='" + description + '\'' +
                ", Status='" + status + '\'' +
                ", SubtasksIDs='" + subTaskIds + '\'' +
                ", Duration='" + durationString + '\'' +
                ", Start time='" + startTimeString + '\'' +
                ", End time='" + endTimeString + '\'' +
                '}';
    }
}