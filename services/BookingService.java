package services;

import db.DbFunctions;
import models.Customer;
import models.Reservation;
import models.Room;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class BookingService {
    private final DbFunctions dbFunctions;
    private final Scanner scanner;
    private static final Logger logger = Logger.getLogger(BookingService.class.getName());

    // List of valid room types
    private static final List<String> VALID_ROOM_TYPES = List.of("Standard", "Deluxe", "Suite", "Family");

    // Constructor to initialize the booking service with DbFunctions and Scanner
    public BookingService(DbFunctions dbFunctions, Scanner scanner) {
        this.dbFunctions = dbFunctions;
        this.scanner = scanner;
    }

    // Process a room booking
    public Reservation processBooking(Customer customer, Connection conn) {
        LocalDate checkInDate = getCheckInDate();
        LocalDate checkOutDate = getCheckOutDate(checkInDate);

        Room selectedRoom = selectRoom(conn, checkInDate, checkOutDate);

        if (selectedRoom == null) {
            System.out.println("❌ Booking canceled. Returning to the main menu...");
            return null;
        }

        Reservation reservation = createReservation(customer, selectedRoom, checkInDate, checkOutDate, conn);

        System.out.println("📋 Booking success! Reservation ID: " + reservation.getReservationId());
        return reservation;
    }

    // Step 1: Get a valid Check-In date from the user
    private LocalDate getCheckInDate() {
        System.out.print("Enter Check-In Date (YYYY-MM-DD): ");
        String inputDate = scanner.nextLine();
        return LocalDate.parse(inputDate); // Simplified, assumes valid input
    }

    // Step 2: Get a valid Check-Out date
    private LocalDate getCheckOutDate(LocalDate checkInDate) {
        while (true) {
            System.out.print("Enter Check-Out Date (YYYY-MM-DD): ");
            String inputDate = scanner.nextLine();
            LocalDate checkOutDate = LocalDate.parse(inputDate);

            if (checkOutDate.isAfter(checkInDate)) {
                return checkOutDate;
            } else {
                System.out.println("❌ Check-Out must be after Check-In. Try again.");
            }
        }
    }

    // Step 3: Select an available room for booking
    private Room selectRoom(Connection conn, LocalDate checkInDate, LocalDate checkOutDate) {
        while (true) {
            // Prompt the user to choose a room type with its description displayed
            System.out.println("\nChoose from the following room types:");
            System.out.println("Standard - A cozy room with essential amenities, suitable for solo travelers or couples on a budget.");
            System.out.println("Deluxe   - A spacious room featuring upgraded furniture and a great view, perfect for small families.");
            System.out.println("Suite    - A luxurious suite with premium facilities, including a living area and king-size bed.");
            System.out.println("Family   - A large, well-equipped family room designed to accommodate up to 5 guests comfortably.");
            System.out.print("Enter your preferred Room Type (Standard, Deluxe, Suite, Family): ");
            String roomType = scanner.nextLine().trim();

            if (!VALID_ROOM_TYPES.contains(roomType)) {
                System.out.println("❌ Invalid room type. Please choose from: " + VALID_ROOM_TYPES);
                continue;
            }

            try {
                // Fetch and display available rooms
                List<Room> availableRooms = dbFunctions.getAvailableRoomsByType(conn, roomType);
                if (availableRooms.isEmpty()) {
                    System.out.println("❌ No rooms of type '" + roomType + "' are available. Try another type.");
                    continue;
                }

                // Display only Room ID and Price
                System.out.println("\nAvailable Rooms for " + roomType + ":");
                System.out.println("╔═══════════════════════╗");
                System.out.println("║ Room ID | Price        ║");
                System.out.println("╠═══════════════════════╣");

                for (Room room : availableRooms) {
                    System.out.printf("║ %-7d | PHP %-8.2f ║%n",
                            room.getRoomId(),
                            room.getPrice());
                }
                System.out.println("╚════════════════════════╝");

                // Prompt user for room ID
                System.out.print("\nEnter the Room ID to book: ");
                int roomId = Integer.parseInt(scanner.nextLine().trim());

                // Validate room selection and check availability
                Room selectedRoom = availableRooms.stream()
                        .filter(room -> room.getRoomId() == roomId)
                        .findFirst()
                        .orElse(null);

                if (selectedRoom == null) {
                    System.out.println("❌ Invalid Room ID. Please select from the list.");
                    continue;
                }

                if (!dbFunctions.isRoomAvailable(conn, roomId, checkInDate, checkOutDate)) {
                    System.out.println("❌ Selected room is no longer available. Try another.");
                    continue;
                }

                return selectedRoom; // Return the successfully selected room

            } catch (Exception e) {
                System.out.println("❌ Error occurred: " + e.getMessage());
            }
        }
    }

    // Step 4: Create a reservation
    private Reservation createReservation(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate, Connection conn) {
        try {
            System.out.println("Creating your reservation...");
            Reservation reservation = new Reservation(0, customer.getCustomerId(), room.getRoomId(), checkInDate, checkOutDate, "Pending");
            int reservationId = dbFunctions.insertReservation(conn, reservation);
            reservation.setReservationId(reservationId);
            return reservation;
        } catch (Exception e) {
            System.out.println("❌ Failed to create reservation. Please try again later.");
            throw new RuntimeException(e);
        }
    }
}
