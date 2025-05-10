<<<<<<< HEAD:src/utils/ConsoleColors.java
package utils;

public class ConsoleColors {
    private static final boolean ENABLE_COLORS = !System.getProperty("os.name").toLowerCase().contains("win");

    public static final String RESET = ENABLE_COLORS ? "\u001B[0m" : "";
    public static final String BLACK = ENABLE_COLORS ? "\u001B[30m" : "";
    public static final String RED = ENABLE_COLORS ? "\u001B[31m" : "";
    public static final String GREEN = ENABLE_COLORS ? "\u001B[32m" : "";
    public static final String YELLOW = ENABLE_COLORS ? "\u001B[33m" : "";
    public static final String BLUE = ENABLE_COLORS ? "\u001B[34m" : "";
    public static final String PURPLE = ENABLE_COLORS ? "\u001B[35m" : "";
    public static final String CYAN = ENABLE_COLORS ? "\u001B[36m" : "";
    public static final String WHITE = ENABLE_COLORS ? "\u001B[37m" : "";

    // Bold and bright options
    public static final String BOLD = ENABLE_COLORS ? "\u001B[1m" : "";
    public static final String BRIGHT_RED = ENABLE_COLORS ? "\u001B[91m" : "";
    public static final String BRIGHT_GREEN = ENABLE_COLORS ? "\u001B[92m" : "";
    public static final String BRIGHT_YELLOW = ENABLE_COLORS ? "\u001B[93m" : "";
    public static final String BRIGHT_BLUE = ENABLE_COLORS ? "\u001B[94m" : "";
    public static final String BRIGHT_PURPLE = ENABLE_COLORS ? "\u001B[95m" : "";
    public static final String BRIGHT_CYAN = ENABLE_COLORS ? "\u001B[96m" : "";
    public static final String BRIGHT_WHITE = ENABLE_COLORS ? "\u001B[97m" : "";
}
=======
package utils;

public class ConsoleColors {
    private static final boolean ENABLE_COLORS = !System.getProperty("os.name").toLowerCase().contains("win");

    public static final String RESET = ENABLE_COLORS ? "\u001B[0m" : "";
    public static final String BLACK = ENABLE_COLORS ? "\u001B[30m" : "";
    public static final String RED = ENABLE_COLORS ? "\u001B[31m" : "";
    public static final String GREEN = ENABLE_COLORS ? "\u001B[32m" : "";
    public static final String YELLOW = ENABLE_COLORS ? "\u001B[33m" : "";
    public static final String BLUE = ENABLE_COLORS ? "\u001B[34m" : "";
    public static final String PURPLE = ENABLE_COLORS ? "\u001B[35m" : "";
    public static final String CYAN = ENABLE_COLORS ? "\u001B[36m" : "";
    public static final String WHITE = ENABLE_COLORS ? "\u001B[37m" : "";

    // Bold and bright options
    public static final String BOLD = ENABLE_COLORS ? "\u001B[1m" : "";
    public static final String BRIGHT_RED = ENABLE_COLORS ? "\u001B[91m" : "";
    public static final String BRIGHT_GREEN = ENABLE_COLORS ? "\u001B[92m" : "";
    public static final String BRIGHT_YELLOW = ENABLE_COLORS ? "\u001B[93m" : "";
    public static final String BRIGHT_BLUE = ENABLE_COLORS ? "\u001B[94m" : "";
    public static final String BRIGHT_PURPLE = ENABLE_COLORS ? "\u001B[95m" : "";
    public static final String BRIGHT_CYAN = ENABLE_COLORS ? "\u001B[96m" : "";
    public static final String BRIGHT_WHITE = ENABLE_COLORS ? "\u001B[97m" : "";
}
>>>>>>> 7e3406ce4df201df35e6d3556d8654a04774ca96:utils/ConsoleColors.java
