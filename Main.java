// RECEIPT GENERATOR
// PAYMENT OPTIONS
// BOOKING SERVICE

import db.DbFunctions;
import models.Customer;
import models.Reservation;
import models.Room;
import services.BookingService;
import services.CustomerMenuService;
import services.CustomerService;
import utils.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DbFunctions db = new DbFunctions();
        Connection conn = null;

        try {
            // Step 1: Connect to the database
            conn = db.connect_to_db("hotel_transylvania", "postgres", "011006");
            if (conn == null) {
                System.out.println("❌ Failed to connect to the database.");
                return;
            }

            db.createTables(conn);
            Seeder.seedRooms(db, conn);

            System.out.println("=== Welcome to Hotel Transylvania! ===");

            // Step 2: Handle Customer Login or Registration
            Customer customer = null;
            while (customer == null) {
                System.out.println("\n🔐 Do you want to (1) Log In or (2) Register?");
                System.out.print("Enter 1 or 2: ");

                String choice = scanner.nextLine().trim();
                if (choice.equals("1")) {
                    customer = CustomerService.loginCustomer(scanner, db, conn);
                    if (customer == null) {
                        System.out.print("Would you like to register instead? (yes/no): ");
                        if (scanner.nextLine().equalsIgnoreCase("yes")) {
                            customer = CustomerService.registerCustomer(scanner, db, conn);
                        }
                    }
                } else if (choice.equals("2")) {
                    customer = CustomerService.registerCustomer(scanner, db, conn);
                } else {
                    System.out.println("❌ Invalid input. Please enter 1 or 2.");
                }
            }

            System.out.println("✅ Welcome, " + customer.getName() + "!");

            CustomerMenuService menuService = new CustomerMenuService(db, scanner);
            int menuChoice = menuService.showMenu(customer, conn);

            if (menuChoice == 1) {
                BookingService bookingService = new BookingService(db, scanner);
                Reservation reservation = bookingService.processBooking(customer, conn);
                proceedToPayment(reservation, customer, conn, db, scanner);
            } else if (menuChoice == 2) {
                System.out.print("🔎 Enter Reservation ID to view your receipt: ");
                String idInput = scanner.nextLine().trim();
                try {
                    int resId = Integer.parseInt(idInput);
                    String receipt = db.getReceiptByReservationId(conn, resId);
                    System.out.println(receipt);
                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid Reservation ID format.");
                }
            } else if (menuChoice == 3) {
                System.out.println("👋 Thank you for visiting Hotel Transylvania! Goodbye!");
                return;
            }


        } catch (SQLException e) {
            System.out.println("💥 Database Error: " + e.getMessage());
            logger.severe("Database Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println("⚠️ Error closing the connection: " + ex.getMessage());
                logger.warning("Error closing the connection: " + ex.getMessage());
            }
        }
    }

    // Handles the payment process after booking
    private static void proceedToPayment(Reservation reservation, Customer customer, Connection conn, DbFunctions db, Scanner scanner) {
        System.out.printf("You selected Room %d for payment.%n", reservation.getRoomId());
        System.out.print("💳 Do you want to proceed with payment now? (yes/no): ");

        String paymentChoice = scanner.nextLine();
        if (paymentChoice.equalsIgnoreCase("yes")) {
            try {
                LocalDate paymentDate = LocalDate.now();
                int reservationId = reservation.getReservationId(); // ✅ Now it's safe
                double price = db.getRoomById(conn, reservation.getRoomId()).getPrice(); // If you still want to fetch the price

                db.insertPayment(conn, reservationId, price, paymentDate, "Paid");
                db.updateReservationStatus(conn, reservationId, "Paid");
                System.out.println("✅ Payment processed successfully!");
                System.out.println("🏨 Thank you for booking with Hotel Transylvania!");
            } catch (Exception e) {
                System.out.println("❌ An error occurred while processing the payment.");
                logger.severe("Payment Error: " + e.getMessage());
            }
        } else {
            System.out.println("🔔 You opted to pay later. Your reservation status remains 'Pending'.");
        }

    }
}