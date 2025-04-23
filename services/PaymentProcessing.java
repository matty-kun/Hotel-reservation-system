package services;

import db.DbFunctions;
import models.Reservation;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class PaymentProcessing {
    private DbFunctions dbFunctions;
    private Scanner scanner;

    public enum PaymentMethod {
        CREDIT_CARD("Credit Card"),
        DEBIT_CARD("Debit Card"),
        GCASH("GCash"),
        MAYA("Maya"),
        CASH("Cash");

        private final String displayName;

        PaymentMethod(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public PaymentProcessing() {
        dbFunctions = new DbFunctions();
        scanner = new Scanner(System.in);
    }

    public PaymentMethod selectedPaymentMethod() {
        while(true) {
            System.out.println("\n Available Payment Methods:");
            PaymentMethod[] methods = PaymentMethod.values();

            for (int i = 0; i < methods.length; i++) {
                System.out.printf("%d. %s%n", i + 1, methods[i].getDisplayName());
            }

            System.out.print("Select payment method (1-" + methods.length + "): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= methods.length) {
                    return methods[choice - 1];
                }
            }  catch (NumberFormatException e) {

            }
            System.out.println("Invalid selection. Please try again.");
        }
    }

    private double calculateTotalAmount(double pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight * nights;
    }

    public void processPayment(Connection conn, int reservationId, double pricePerNight) {
        Reservation reservation = dbFunctions.getReservationById(conn, reservationId);

        // ✅ Step 1: Null check to prevent NullPointerException
        if (reservation == null) {
            System.out.println("❌ Reservation not found. Payment aborted.");
            return; // Stop processing if reservation is null
        }

        // ✅ Step 2: Safe to use reservation fields now
        double totalAmount = calculateTotalAmount(pricePerNight, reservation.getCheckInDate(), reservation.getCheckOutDate());
        PaymentMethod method = selectedPaymentMethod();

        System.out.println("\n--- Payment Summary ---");
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Room ID: " + reservation.getRoomId());
        System.out.println("Check-in Date: " + reservation.getCheckInDate());
        System.out.println("Check-out Date: " + reservation.getCheckOutDate());
        System.out.println("Payment Method: " + method.getDisplayName());
        System.out.println("Total Amount: ₱" + totalAmount);

        // ✅ You can proceed to further steps like saving payment to DB, etc.
    }

}