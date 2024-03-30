package sprint.managers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;
import server.adapter.TaskAdapter;
import sprint.managers.filemanager.FileBackedTaskManager;
import sprint.managers.historymanager.HistoryManager;
import sprint.managers.historymanager.InMemoryHistoryManager;
import sprint.managers.taskmanager.TaskManager;
import sprint.tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    private Managers() {
        throw new IllegalStateException("Utility class");
    }

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(new File("src/resources/backup.csv"));
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}