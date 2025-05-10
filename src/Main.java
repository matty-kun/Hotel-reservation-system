import db.DbFunctions;
import models.Customer;
import models.Reservation;
import services.BookingService;
import services.CustomerMenuService;
import services.CustomerService;
import services.PaymentProcessing;
import utils.ConsoleColors;
import utils.InputValidator;
import utils.UIHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

// Main Class for the Hotel Transylvania Application
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static Customer currentCustomer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DbFunctions db = new DbFunctions();
        Connection conn = null;

        try {
            // Step 1: Connect to the database
            conn = db.connect_to_db("hotel_transylvania", "postgres", "011006");
            if (conn == null) {
                System.out.println(ConsoleColors.BRIGHT_RED + "❌ Failed to connect to the database. Exiting..." + ConsoleColors.RESET);
                return;
            }

            // Initialize database tables and seed data
            db.createTables(conn);
            Seeder.seedRooms(db, conn);

            // Step 2: Display Welcome Message with Fun UI
            displayWelcomeMessage();

            // Step 3: Handle Login/Registration
            currentCustomer = handleLoginOrRegistration(scanner, db, conn);
            if (currentCustomer == null) {
                System.out.println(ConsoleColors.BRIGHT_RED + "💔 Goodbye! We're sad to see you go." + ConsoleColors.RESET);
                return;
            }

            // Step 4: Main Menu Actions
            CustomerMenuService menuService = new CustomerMenuService(db, scanner);
            while (true) {
                int menuChoice = menuService.showMenu(currentCustomer, conn);

                switch (menuChoice) {
                    case 1 -> handleRoomBooking(scanner, db, conn);
                    case 2 -> handleViewReservations(scanner, db, conn);
                    case 3 -> {
                        displayExitMessage();
                        return;
                    }
                    default -> System.out.println("❌ Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error: " + e.getMessage());
            System.out.println(ConsoleColors.BRIGHT_RED + "❌ A system error occurred. Please try again later." + ConsoleColors.RESET);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.severe("❌ Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    // Display a fun welcome message
    private static void displayWelcomeMessage() {
        System.out.println(ConsoleColors.BRIGHT_PURPLE + "\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                 🏨 WELCOME TO HOTEL TRANSYLVANIA 🏨            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║              A Place Where Comfort Meets Elegance!             ║");
        System.out.println("║            Enjoy your stay with us, where luxury reigns.       ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝" + ConsoleColors.RESET);
    }

    // Display a fun exit message
    private static void displayExitMessage() {
        System.out.println(ConsoleColors.BRIGHT_PURPLE + "\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            👋 THANK YOU FOR VISITING!            ║");
        System.out.println("║  We hope to see you again at Hotel Transylvania! ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n" + ConsoleColors.RESET);
    }

    private static void handleRoomBooking(Scanner scanner, DbFunctions db, Connection conn) throws SQLException {
        // Styled Room Booking Header
        System.out.println(ConsoleColors.BRIGHT_GREEN + "\n╔════════════════════════════════════════════════╗");
        System.out.println("║                🛌 ROOM BOOKING                 ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  Welcome to the booking section! Choose from  ║");
        System.out.println("║  a variety of rooms to make your stay amazing.║");
        System.out.println("╚════════════════════════════════════════════════╝" + ConsoleColors.RESET);

        BookingService bookingService = new BookingService(db, scanner);

        // Step 1: Process Room Booking
        Reservation reservation = bookingService.processBooking(currentCustomer, conn);

        if (reservation == null) {
            System.out.println(ConsoleColors.BRIGHT_YELLOW + "⚠️ Booking canceled. Returning to the main menu." + ConsoleColors.RESET);
            return;
        }

        // Step 2: Proceed to Payment
        PaymentProcessing.proceedToPayment(reservation, currentCustomer, conn, db, scanner);
    }

    // Handle Login or Registration Process
    private static Customer handleLoginOrRegistration(Scanner scanner, DbFunctions db, Connection conn) throws SQLException {
        Customer customer = null;

        while (customer == null) {
            UIHelper.printHeader("Login or Register");

            System.out.println(ConsoleColors.BRIGHT_BLUE + "\n🔐 What would you like to do?");
            System.out.println("1. Log In");
            System.out.println("2. Register" + ConsoleColors.RESET);
            System.out.print("Enter 1 or 2: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    customer = CustomerService.loginCustomer(scanner, db, conn);
                    if (customer == null && InputValidator.getYesOrNo(scanner, "Would you like to register instead? (yes/no): ")) {
                        customer = CustomerService.registerCustomer(scanner, db, conn);
                    }
                    break;
                case "2":
                    customer = CustomerService.registerCustomer(scanner, db, conn);
                    break;
                default:
                    System.out.println(ConsoleColors.BRIGHT_RED + "❌ Invalid input. Please enter 1 or 2." + ConsoleColors.RESET);
            }
        }
        return customer;
    }

    // Handle Viewing Reservations
    private static void handleViewReservations(Scanner scanner, DbFunctions db, Connection conn) throws SQLException {
        // Styled Reservations Header
        System.out.println(ConsoleColors.BRIGHT_BLUE + "\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                 📋 YOUR RESERVATIONS                 ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║ Here are your current reservations. You can view    ║");
        System.out.println("║ details or print receipts for each reservation.      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝" + ConsoleColors.RESET);

        List<Reservation> customerReservations = db.getReservationByCustomerId(conn, currentCustomer.getCustomerId());
        if (customerReservations.isEmpty()) {
            System.out.println(ConsoleColors.BRIGHT_RED + "❌ You don't have any reservations yet." + ConsoleColors.RESET);
            return;
        }

        // Print reservations in a styled table
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ ID  | Check-in Date | Check-out Date | Room | Status     ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");

        for (Reservation res : customerReservations) {
            System.out.printf("║ %-3d | %-13s | %-14s | %-4d | %-11s ║%n",
                    res.getReservationId(),
                    res.getCheckInDate(),
                    res.getCheckOutDate(),
                    res.getRoomId(),
                    res.getStatus());
        }
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        // Fetch and print receipt option
        while (true) {
            System.out.print(ConsoleColors.BRIGHT_YELLOW + "\n🔎 Enter Reservation ID to view receipt: " + ConsoleColors.RESET);

            String idInput = scanner.nextLine().trim();
            try {
                int resId = Integer.parseInt(idInput);
                Reservation reservation = db.getReservationById(conn, resId);

                if (reservation == null || reservation.getCustomerId() != currentCustomer.getCustomerId()) {
                    System.out.println(ConsoleColors.BRIGHT_RED + "❌ Invalid Reservation ID. Please try again." + ConsoleColors.RESET);
                    continue;
                }

                // Show receipt
                String receipt = db.getReceiptByReservationId(conn, resId, currentCustomer.getCustomerId());
                System.out.println(receipt);

                // Save receipt option
                System.out.print(ConsoleColors.BRIGHT_GREEN + "\n💾 Would you like to save this receipt? (yes/no): " + ConsoleColors.RESET);
                String saveChoice = scanner.nextLine().trim().toLowerCase();

                if (saveChoice.equals("yes")) {
                    System.out.println(ConsoleColors.BRIGHT_GREEN + "✅ Receipt saved successfully!" + ConsoleColors.RESET);
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.BRIGHT_RED + "❌ Please enter a valid numeric Reservation ID." + ConsoleColors.RESET);
            }
        }
    }
}