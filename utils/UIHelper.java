package utils;

public class UIHelper {
    public static final int HEADER_WIDTH = 50; // Centralize header width

    public static void printHeader(String title) {
        if (title.length() > HEADER_WIDTH) {
            title = title.substring(0, HEADER_WIDTH - 3) + "..."; // Truncate if too long
        }
        String separator = "=".repeat(HEADER_WIDTH);
        System.out.println(ConsoleColors.CYAN + separator);
        System.out.println(" ".repeat((HEADER_WIDTH - title.length()) / 2) + title);
        System.out.println(separator + ConsoleColors.RESET);
    }
}
