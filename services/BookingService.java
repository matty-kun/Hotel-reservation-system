package services;

import db.DbFunctions;
import models.Customer;
import models.Reservation;
import models.Room;
import utils.InputValidator;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

// Handles the overall booking flow and room reservation tasks
public class BookingService {
    private final DbFunctions dbFunctions;
    private final Scanner scanner;

    private static final Logger logger = Logger.getLogger(BookingService.class.getName());
    private static final int MAX_STAY_DURATION = 30; // Maximum allowed number of days
    private static final List<String> VALID_ROOM_TYPES = List.of("Standard", "Deluxe", "Suite", "Family");

    // Constructor that initializes DbFunctions and Scanner for input
    public BookingService(DbFunctions dbFunctions, Scanner scanner) {
        this.dbFunctions = dbFunctions;
        this.scanner = scanner;
    }

    public Reservation processBooking(Customer customer, Connection conn) {
        // Step 1: Ask for Check-In and Check-Out Dates
        LocalDate checkInDate = getCheckInDate();
        LocalDate checkOutDate = getCheckOutDate(checkInDate);

        // Step 2: Allow the customer to select a room
        Room selectedRoom = selectRoom(conn, checkInDate, checkOutDate);

        // Step 3: Create Reservation in the Database
        int reservationId = createReservation(customer, selectedRoom, checkInDate, checkOutDate, conn);
        System.out.println("📋 Reservation created successfully! Your Reservation ID is: " + reservationId);

        // ✅ Step 4: Return a new Reservation object
        return new Reservation(
                reservationId,
                customer.getCustomerId(),
                selectedRoom.getRoomId(),
                checkInDate,
                checkOutDate,
                "Pending"  // Assuming the default status is "Pending"
        );
    }


    // Step 1: Get a valid Check-In date
    private LocalDate getCheckInDate() {
        return InputValidator.getValidDate(scanner, "Enter your Check-In date (YYYY-MM-DD): ");
    }

    // Step 2: Get a valid Check-Out date with validation
    private LocalDate getCheckOutDate(LocalDate checkInDate) {
        while (true) {
            LocalDate checkOutDate = InputValidator.getValidDate(scanner, "Enter your Check-Out date (YYYY-MM-DD): ");
            if (checkOutDate.isBefore(checkInDate)) {
                System.out.println("❌ Check-Out date must be after Check-In date.");
                continue;
            }
            if (checkOutDate.isAfter(checkInDate.plusDays(MAX_STAY_DURATION))) {
                System.out.println("❌ Stay cannot exceed " + MAX_STAY_DURATION + " days. Please choose a shorter duration.");
                continue;
            }
            return checkOutDate;
        }
    }

    // Step 3: Select an available room based on dates and type
    private Room selectRoom(Connection conn, LocalDate checkInDate, LocalDate checkOutDate) {
        while (true) {
            // Prompt the user to select room type
            System.out.print("Enter your preferred Room Type (e.g., Standard, Deluxe, Suite, Family): ");
            String roomType = scanner.nextLine().trim();

            if (!VALID_ROOM_TYPES.contains(roomType)) {
                System.out.println("❌ Invalid room type. Please choose from: " + VALID_ROOM_TYPES);
                continue;
            }

            try {
                // Fetch available rooms for the dates and type
                List<Room> availableRooms = dbFunctions.getAvailableRoomsByType(conn, roomType);

                if (availableRooms.isEmpty()) {
                    System.out.println("❌ No available rooms of type '" + roomType + "' for your selected dates. Try choosing another type.");
                    continue;
                }

                // Display available rooms
                System.out.println("Here's the list of available rooms for your selected dates:");
                for (Room room : availableRooms) {
                    System.out.printf("- Room [%d] Type: %s, Price: ₱%.2f per night%n",
                            room.getRoomId(), room.getRoomType(), room.getPrice());
                }

                // Prompt the user to select a room by ID
                System.out.print("Enter the Room ID you would like to book: ");
                int selectedRoomId = Integer.parseInt(scanner.nextLine());

                boolean isAvailable = dbFunctions.isRoomAvailable(conn, selectedRoomId, checkInDate, checkOutDate);
                if (!isAvailable) {
                    System.out.println("❌ Selected room is not available for the chosen dates.");
                    continue;
                }

                // Return the room if it matches the ID
                return availableRooms.stream()
                        .filter(room -> room.getRoomId() == selectedRoomId)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Room ID not available. Please try again."));
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid input. Please try again.");
            } catch (Exception e) {
                System.out.println("❌ An error occurred while selecting a room. Please try again later.");
                logger.severe("An error occurred during room selection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Step 4: Create a reservation and store it in the database
    private int createReservation(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate, Connection conn) {
        try {
            Reservation reservation = new Reservation(
                0, // Auto-generated ID
                customer.getCustomerId(),
                room.getRoomId(),
                checkInDate,
                checkOutDate,
                "Pending" // Default status upon creation
            );

            int reservationId = dbFunctions.insertReservation(conn, reservation);
            if (reservationId <= 0) {
                logger.severe("Failed to create reservation. No valid ID returned.");
                throw new RuntimeException("Failed to create reservation.");
            }

            // Update room's availability
            dbFunctions.updateRoomAvailability(conn, room.getRoomId(), false);

            logger.info("Reservation created successfully with ID: " + reservationId);

            reservation = dbFunctions.getReservationById(conn, reservationId);

            if (reservation != null) {
                dbFunctions.insertPayment(conn, reservationId, room.getPrice(), LocalDate.now(), "Paid");
                dbFunctions.updateReservationStatus(conn, reservationId, "Paid");
                System.out.println("✅ Payment processed successfully!");
            } else {
                logger.severe("Cannot process payment: Reservation ID " + reservationId + " does not exist.");
                System.out.println("❌ Payment failed. Reservation not found.");
            }
            return reservationId;
        } catch (Exception e) {
            logger.severe("Failed to create reservation: " + e.getMessage());
            throw new RuntimeException("Reservation creation failed.", e);
        }
    }
}