package utils;

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
        String emailRegex =  "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
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
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
            }
        }

    }
}
