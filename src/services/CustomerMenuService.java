<<<<<<< HEAD:src/services/CustomerMenuService.java
package services;

import db.DbFunctions;
import models.Customer;
import models.Reservation;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CustomerMenuService {
    private final DbFunctions db;
    private final Scanner scanner;

    public CustomerMenuService(DbFunctions db, Scanner scanner) {
        this.db = db;
        this.scanner = scanner;
    }

    public int showMenu(Customer customer, Connection conn) {
        int choice;
        do {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Book a Room");
            System.out.println("2. View Reservations");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            if (input.matches("[1-3]")) {
                choice = Integer.parseInt(input);
                break;
            } else {
                System.out.println("❌ Invalid option. Please try again.");
            }
        } while (true);

        return choice;
    }
}

=======
package services;

import db.DbFunctions;
import models.Customer;
import models.Reservation;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CustomerMenuService {
    private final DbFunctions db;
    private final Scanner scanner;

    public CustomerMenuService(DbFunctions db, Scanner scanner) {
        this.db = db;
        this.scanner = scanner;
    }

    public int showMenu(Customer customer, Connection conn) {
        int choice;
        do {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Book a Room");
            System.out.println("2. View Reservations");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();
            if (input.matches("[1-3]")) {
                choice = Integer.parseInt(input);
                break;
            } else {
                System.out.println("❌ Invalid option. Please try again.");
            }
        } while (true);

        return choice;
    }
}

>>>>>>> 7e3406ce4df201df35e6d3556d8654a04774ca96:services/CustomerMenuService.java
