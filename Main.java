// TO DO:
// CLEAN UP THE MAIN.JAVA TOMORROW ( APRIL 15, 2025 )
// CLEAN UP THE PAYMENT.JAVA AND ALL OTHER CLASSES TODAY ( APRIL 15, 2025 ) CHECK

// VALIDATE USER'S INPUT TOMORROW ( APRIL 16, 2025 )

import db.DbFunctions;
import models.Customer;
import models.Room;
import models.Reservation;
import models.Payment;
import models.Seeder;

import utils.InputValidator;
import utils.LoggerConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = LoggerConfig.getLogger(Main.class.getName());

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DbFunctions db = new DbFunctions();
        Connection conn = null;

        try {
            conn = db.connect_to_db("hotel_transylvania", "postgres", "011006");
            db.createTables(conn);
            Seeder.seedRooms(db, conn);

            System.out.println("Welcome to Hotel Transylvania!");

            String name = InputValidator.getValidName(scanner);
            String email = InputValidator.getValidEmail(scanner);
            String phone = InputValidator.getValidPhone(scanner);

            Customer customer = new Customer(name, email, phone);
            int customerId = db.insertCustomer(conn, customer.getName(), customer.getEmail(), customer.getPhone());
            customer.setCustomerId(customerId);

            System.out.println("\n\uD83D\uDD39 Room Selection \uD83D\uDD39");
            System.out.print("What type of room do you want? (e.g., Standard, Deluxe, Suite, Family): ");
            String roomType = scanner.nextLine();
            roomType = roomType.substring(0, 1).toUpperCase() + roomType.substring(1).toLowerCase();

            List<Room> availableRooms = db.getAvailableRoomsByType(conn, roomType);

            if (availableRooms.isEmpty()) {
                System.out.println("❌ No available rooms of that type.");
                return;
            }
            System.out.println("✅ Available Rooms: ");
            for (Room r : availableRooms) {
                System.out.printf("Room ID: %d | Type: %s | Price: ₱%.2f%n", r.getRoomId(), r.getRoomType(), r.getPrice());
            }
            System.out.print("Enter the Room ID you want to book: ");
            int chosenRoomId;
            try {
                chosenRoomId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid Room Id. Please enter a valid number.");
                return;
            }
            // Get the selected room
            Room selectedRoom = availableRooms.stream()
                    .filter(r -> r.getRoomId() == chosenRoomId)
                    .findFirst()
                    .orElse(null);
            if (selectedRoom == null) {
                System.out.println("❌ Invalid Room ID.");
                return;
            }
            // Show room price
            System.out.printf("You selected Room ID %d. Price: ₱%.2f%n", selectedRoom.getRoomId(), selectedRoom.getPrice());

            // Check-In Date
            LocalDate checkInDate = InputValidator.getValidDate(scanner, "Enter Check-In Date (YYYY-MM-DD): ");
            LocalDate checkOutDate = InputValidator.getValidDate(scanner, "Enter Check-Out Date (YYYY-MM-DD): ");

            if (checkOutDate.isBefore(checkInDate)) {
                System.out.println("❌ Check-out date must be after check-in date.");
                return;
            }

            // Summary
            System.out.println("n\uD83D\uDCDD Booking Summary:");
            System.out.printf("Customer: %s | Email: %s | Phone %s%n", name, email, phone);
            System.out.printf("Room ID: %d | Type: %s | Price:  ₱%.2f%n", selectedRoom.getRoomId(), selectedRoom.getRoomType(), selectedRoom.getPrice());
            System.out.printf("Check-In: %s | Check-Out: %s%n", checkInDate, checkOutDate);

            System.out.print("Proceed with booking? (yes/no): ");
            String confirm = scanner.nextLine();
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("❌ Booking cancelled.");
                return;
            }

            String status = "Booked";

            Reservation reservation = new Reservation(
                    customer.getCustomerId(),
                    selectedRoom.getRoomId(),
                    checkInDate.toString(),
                    checkOutDate.toString(),
                    status
            );

            int reservationId = db.insertReservation(conn, reservation);
            db.updateRoomAvailability(conn, selectedRoom.getRoomId(), false);

            String paymentStatus = "Paid";
            LocalDate today = java.time.LocalDate.now();
            Payment payment = new Payment(
                    reservationId, selectedRoom.getPrice(), today, paymentStatus
            );
            db.insertPayment(conn, reservationId, selectedRoom.getPrice(), today, paymentStatus);


            System.out.println("\uD83C\uDFE8 Booking completed successfully.");

        } catch (SQLException e) {
            System.out.println("\uD83D\uDCA5 Database Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.out.println("⚠\uFE0F Error closing the connection: " + ex.getMessage());
            }
        }
    }
}