package server.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import sprint.tasks.Epic;
import sprint.tasks.SubTask;
import sprint.tasks.Task;
import sprint.tasks.TaskStatus;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskAdapter implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement jsonElement,
                            Type type,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.has("epicEndTime")) {
            return context.deserialize(object, Epic.class);
        } else if (object.has("epic")) {
            return context.deserialize(object, SubTask.class);
        } else {
            Task task = new Task(object.get("name").getAsString(), object.get("description").getAsString(), TaskStatus.valueOf(object.get("status").getAsString()));
            if (object.has("id")) {
                task.setId(object.get("id").getAsInt());
            }
            if (object.has("duration")) {
                task.setDuration(Duration.ofDays(object.get("duration").getAsLong()));
            }
            if (object.has("startTime")) {
                String startTimeStr = object.get("startTime").getAsString();
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                task.setStartTime(startTime);
            }
            return task;
        }
    }
}