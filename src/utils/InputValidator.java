<<<<<<< HEAD:src/utils/InputValidator.java
package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class InputValidator {
    public static String getValidName(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            if (name.matches("^[a-zA-Z\\s\\-']+$") && !name.isEmpty()) {
                return name;
            }
            System.out.println("❌ Invalid name! Please enter letters and spaces only.");
        }
    }

    public static String getValidEmail(Scanner scanner) {
        // List of allowed domains
        List<String> allowedDomains = List.of("gmail.com", "yahoo.com", "outlook.com", "hotmail.com");

        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"; // Basic email format
        String email;

        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine().trim();

            if (!email.matches(emailRegex)) {
                System.out.println("❌ Invalid email! Format should be like name@example.com.");
                continue; // Re-prompt for invalid format
            }

            // Extract the domain part of the email
            String domain = email.substring(email.indexOf("@") + 1);

            if (!allowedDomains.contains(domain)) {
                System.out.println("❌ Invalid email domain! Allowed domains are: " + allowedDomains);
                continue; // Re-prompt for invalid domain
            }

            return email; // Email is valid
        }
    }

    public static String getValidPhone(Scanner scanner) {
        String phone;
        while (true) {
            System.out.print("Enter your phone number (11 digits): ");
            phone = scanner.nextLine();
            if (phone.matches("\\d{11}")) {
                return phone;
            }
            System.out.println("❌ Invalid phone number! Enter exactly 11 digits.");
        }
    }

    public static LocalDate getValidDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("❌ Cannot select a past date. Please enter a future date.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
            }
        }

    }
    public static String getValidPassword(Scanner scanner) {
        String password;
        String show;

        // Loop until the user types either "yes" or "no"
        while (true) {
            System.out.print("👁️ Do you want to show password as you type (yes/no)?: ");
            show = scanner.nextLine().trim().toLowerCase();

            if (show.equals("yes") || show.equals("no")) {
                break; // valid input, exit loop
            } else {
                System.out.println("❌ Please type only 'yes' or 'no'.");
            }
        }

        boolean showPassword = show.equals("yes");

        // Always show warning when user selects 'no' in IDE environment
        if (!showPassword) {
            System.out.println("⚠️ Warning: Password hiding is not supported in IDE environment. Your password will be visible as you type.\n");
        }

        while (true) {
            if (!showPassword) {
                System.out.print("🔒 Enter your password (visible): ");
                password = scanner.nextLine();
            } else {
                System.out.print("🔒 Enter your password: ");
                password = scanner.nextLine();
            }

            if (password.trim().isEmpty()) {
                System.out.println("❌ Password cannot be empty.");
                continue;
            }

            if (!isValidPassword(password)) {
                System.out.println("❌ Invalid password! Your password must contain:");
                System.out.println("1. At least 8 characters.");
                System.out.println("2. At least one uppercase letter.");
                System.out.println("3. At least one lowercase letter.");
                System.out.println("4. At least one number.");
                continue;
            }

            return password;
        }
    }


    private static boolean isValidPassword(String password) {
        // Password must be at least 8 characters, include uppercase, lowercase and a number.
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }

    public static boolean getYesOrNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt); // Display the prompt message
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                return true; // Return true if the response is "yes"
            } else if (response.equals("no")) {
                return false; // Return false if the response is "no"
            } else {
                System.out.println("❌ Invalid input! Please type 'yes' or 'no'."); // Re-prompt for invalid input
            }
        }
    }

=======
package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class InputValidator {
    public static String getValidName(Scanner scanner) {
        String name;
        while (true) {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            if (name.matches("^[a-zA-Z\\s\\-']+$") && !name.isEmpty()) {
                return name;
            }
            System.out.println("❌ Invalid name! Please enter letters and spaces only.");
        }
    }

    public static String getValidEmail(Scanner scanner) {
        // List of allowed domains
        List<String> allowedDomains = List.of("gmail.com", "yahoo.com", "outlook.com", "hotmail.com");

        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"; // Basic email format
        String email;

        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine().trim();

            if (!email.matches(emailRegex)) {
                System.out.println("❌ Invalid email! Format should be like name@example.com.");
                continue; // Re-prompt for invalid format
            }

            // Extract the domain part of the email
            String domain = email.substring(email.indexOf("@") + 1);

            if (!allowedDomains.contains(domain)) {
                System.out.println("❌ Invalid email domain! Allowed domains are: " + allowedDomains);
                continue; // Re-prompt for invalid domain
            }

            return email; // Email is valid
        }
    }

    public static String getValidPhone(Scanner scanner) {
        String phone;
        while (true) {
            System.out.print("Enter your phone number (11 digits): ");
            phone = scanner.nextLine();
            if (phone.matches("\\d{11}")) {
                return phone;
            }
            System.out.println("❌ Invalid phone number! Enter exactly 11 digits.");
        }
    }

    public static LocalDate getValidDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                LocalDate date = LocalDate.parse(input);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("❌ Cannot select a past date. Please enter a future date.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
            }
        }

    }
    public static String getValidPassword(Scanner scanner) {
        String password;
        String show;

        // Loop until the user types either "yes" or "no"
        while (true) {
            System.out.print("👁️ Do you want to show password as you type (yes/no)?: ");
            show = scanner.nextLine().trim().toLowerCase();

            if (show.equals("yes") || show.equals("no")) {
                break; // valid input, exit loop
            } else {
                System.out.println("❌ Please type only 'yes' or 'no'.");
            }
        }

        boolean showPassword = show.equals("yes");

        // Always show warning when user selects 'no' in IDE environment
        if (!showPassword) {
            System.out.println("⚠️ Warning: Password hiding is not supported in IDE environment. Your password will be visible as you type.\n");
        }

        while (true) {
            if (!showPassword) {
                System.out.print("🔒 Enter your password (visible): ");
                password = scanner.nextLine();
            } else {
                System.out.print("🔒 Enter your password: ");
                password = scanner.nextLine();
            }

            if (password.trim().isEmpty()) {
                System.out.println("❌ Password cannot be empty.");
                continue;
            }

            if (!isValidPassword(password)) {
                System.out.println("❌ Invalid password! Your password must contain:");
                System.out.println("1. At least 8 characters.");
                System.out.println("2. At least one uppercase letter.");
                System.out.println("3. At least one lowercase letter.");
                System.out.println("4. At least one number.");
                continue;
            }

            return password;
        }
    }


    private static boolean isValidPassword(String password) {
        // Password must be at least 8 characters, include uppercase, lowercase and a number.
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return password.matches(passwordRegex);
    }

    public static boolean getYesOrNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt); // Display the prompt message
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                return true; // Return true if the response is "yes"
            } else if (response.equals("no")) {
                return false; // Return false if the response is "no"
            } else {
                System.out.println("❌ Invalid input! Please type 'yes' or 'no'."); // Re-prompt for invalid input
            }
        }
    }

>>>>>>> 7e3406ce4df201df35e6d3556d8654a04774ca96:utils/InputValidator.java
}