package utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerConfig {
    public static Logger getLogger(String className) {
        return Logger.getLogger(className);
    }
    public static void disableConsoleLogging() {
        Logger rootLogger = Logger.getLogger("");
        for (var handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(Level.OFF); // Turn of console input
            }
        }
    }
}
