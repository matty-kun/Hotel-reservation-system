package utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerConfig {

    public static Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);

        // Remove default ConsoleHandler
        Logger rootLogger = Logger.getLogger(""); // Root logger
        rootLogger.setUseParentHandlers(false);
        for (var handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        // Add a new ConsoleHandler with a higher log level
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.WARNING); // Display only WARNING and above
        rootLogger.addHandler(handler);

        // Set the level for the desired logger
        logger.setLevel(Level.WARNING); // Ignore INFO messages
        return logger;
    }
}
