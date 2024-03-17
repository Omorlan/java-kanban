package sprint.tasks;

import java.time.format.DateTimeFormatter;

public class TimeFormatter {
    private TimeFormatter() {
        throw new IllegalStateException("Utility class");
    }
    public static final DateTimeFormatter TIMEFORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyy HH:mm");
}
