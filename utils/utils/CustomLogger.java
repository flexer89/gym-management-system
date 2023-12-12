package utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.*;

public class CustomLogger {

    private static Logger logger;

    static {
        try {
            // Create a logger with a custom formatter
            logger = Logger.getLogger(CustomLogger.class.getName());
            FileHandler fileHandler = new FileHandler("logs.log", true);
            fileHandler.setFormatter(new LogIdFormatter());
            logger.addHandler(fileHandler);

            // Set the default log level
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logInfo(String message) {
        logger.info("[" + generateLogId() + "] " + message);
    }

    public static void logWarning(String message) {
        logger.warning("[" + generateLogId() + "] " + message);
    }

    public static void logError(String message) {
        logger.severe("[" + generateLogId() + "] " + message);
    }

    private static String generateLogId() {
        return UUID.randomUUID().toString();
    }

    private static class LogIdFormatter extends Formatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        @Override
        public String format(LogRecord record) {
            StringBuilder logMessage = new StringBuilder();
            logMessage.append("[")
                    .append(record.getLevel())
                    .append("] ")
                    .append("[")
                    .append(dateFormat.format(new Date(record.getMillis())))
                    .append("] ")
                    .append(getLogId(record))
                    .append(formatMessage(record))
                    .append("\n");

            if (record.getThrown() != null) {
                logMessage.append(getStackTraceString(record.getThrown())).append("\n");
            }

            return logMessage.toString();
        }

        private String getLogId(LogRecord record) {
            String[] tokens = record.getMessage().split("\\s+");
            for (String token : tokens) {
                if (token.length() == 36 && token.matches("[a-f0-9\\-]+")) {
                    return "[" + token + "]";
                }
            }
            return "";
        }
        

        private String getStackTraceString(Throwable throwable) {
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : throwable.getStackTrace()) {
                stackTrace.append("\t").append(element.toString()).append("\n");
            }
            return stackTrace.toString();
        }
    }
}
