package sprint.tasks;

import resources.UtilConstant;

import java.time.format.DateTimeFormatter;

public class TimeFormatter {
    private TimeFormatter() {
        throw new IllegalStateException("Utility class");
    }

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(UtilConstant.DATE_TIME_FORMAT);
}
