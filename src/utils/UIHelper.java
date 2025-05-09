<<<<<<< HEAD:src/utils/UIHelper.java
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
=======
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
>>>>>>> 7e3406ce4df201df35e6d3556d8654a04774ca96:utils/UIHelper.java
