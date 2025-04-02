package System.src;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

public class HotelBooking {
    private static final Logger logger = Logger.getLogger(HotelBooking.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String name;
        while (true) {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            if (name.matches("^[a-zA-Z\\s]+$") && !name.isEmpty()) {
                break; 
            }
            System.out.println("Invalid name! Please enter a valid name (letters and spaces only).");
        }

        String email;
        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (email.contains("@")) {
                break; 
            }
            System.out.println("Invalid email! Please enter a valid email containing '@'.");
        }

        String phone;
        while (true) {
            System.out.print("Enter your phone number (11 digits): ");
            phone = scanner.nextLine();
            if (phone.matches("\\d{11}")) {  
                break; 
            } else {
                System.out.println("Invalid phone number! Please enter exactly 11 digits (numbers only).");
            }
        }
        
        Customer customer = new Customer(1, name, email, phone);
        customer.register();
        customer.login();

        Room[] rooms = {
            new Room(104, "Extra Service", 500.0, true),
            new Room(103, "Deluxe", 150.0, true),
            new Room(102, "Suite", 200.0, true),
            new Room(101, "Standard", 100.0, true)
        };

        System.out.println("Available Rooms:");
        for (Room room : rooms) {
            System.out.println("Room ID: " + room.getRoomId() + ", Type: " + room.getRoomType() + ", Price: " + room.getPrice());
        }

        System.out.print("Enter the Room ID you want to book: ");
        int selectedRoomId = scanner.nextInt();
        scanner.nextLine(); 
        Room selectedRoom = null;
        
        for (Room room : rooms) {
            if (room.getRoomId() == selectedRoomId) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null || !selectedRoom.checkAvailability()) {
            System.out.println("Invalid selection or room is not available.");
            scanner.close();
            return;
        }

        System.out.print("Do you want to book this room? (yes/no): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("yes")) {
            Reservation reservation = new Reservation(1, customer.getCustomerId(), selectedRoom.getRoomId(), 2, "Pending", selectedRoom);
            reservation.confirmReservation();

            System.out.print("Do you want to proceed with payment? (yes/no): ");
            String paymentChoice = scanner.nextLine();
            if (paymentChoice.equalsIgnoreCase("yes")) {
                Payment payment = new Payment(1, reservation.getReservationId(), selectedRoom.getPrice(), new Date(), "Pending");
                payment.processPayment();
            } else {
                System.out.println("Payment not completed. Reservation remains pending.");
            }
        } else {
            System.out.println("Reservation cancelled.");
        }

        scanner.close();
    }
}

class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;
    
    public Customer(int customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    public void register() {
        System.out.println("Customer " + name + " registered successfully.");
    }
    
    public void login() {
        System.out.println("Customer " + name + " logged in.");
    }

    public int getCustomerId() {
        return customerId;
    }
}

class Room {
    private int roomId;
    private String roomType;
    private double price;
    private boolean availability;
    
    public Room(int roomId, String roomType, double price, boolean availability) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.price = price;
        this.availability = availability;
    }
    
    public boolean checkAvailability() {
        return availability;
    }

    public int getRoomId() {
        return roomId;
    }
    
    public String getRoomType() {
        return roomType;
    }
    
    public double getPrice() {
        return price;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}

class Reservation {
    private int reservationId;
    private int customerId;
    private int roomId;
    private int numOfGuests;
    private String status;
    private Room room;
    
    public Reservation(int reservationId, int customerId, int roomId, int numOfGuests, String status, Room room) {
        this.reservationId = reservationId;
        this.customerId = customerId;
        this.roomId = roomId;
        this.numOfGuests = numOfGuests;
        this.status = status;
        this.room = room;
    }
    
    public void confirmReservation() {
        if (room.checkAvailability()) {
            status = "Confirmed";
            room.setAvailability(false);
            System.out.println("Reservation " + reservationId + " confirmed for room " + roomId + ".");
        } else {
            System.out.println("Reservation failed. Room is not available.");
        }
    }
    
    public int getReservationId() {
        return reservationId;
    }
}

class Payment {
    private int paymentId;
    private int reservationId;
    private double amount;
    private Date paymentDate;
    private String paymentStatus;
    
    public Payment(int paymentId, int reservationId, double amount, Date paymentDate, String paymentStatus) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }
    
    public void processPayment() {
        paymentStatus = "Completed";
        System.out.println("Payment " + paymentId + " for reservation " + reservationId + " processed successfully.");
    }
}
