package utils;

import java.io.Console;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        String email;
        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (email.matches(emailRegex)) {
                return email;
            }
            System.out.println("❌ Invalid email! Format should be like name@example.com.");
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
}