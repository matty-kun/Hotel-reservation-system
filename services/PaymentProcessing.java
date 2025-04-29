package services;

import db.DbFunctions;
import models.Customer;
import models.Reservation;
import models.Room;
import utils.PriceCalculator;
import utils.ConsoleColors;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Logger;

public class PaymentProcessing {
    private static final Logger logger = Logger.getLogger(PaymentProcessing.class.getName());
    private final DbFunctions dbFunctions;

    public PaymentProcessing(DbFunctions dbFunctions) {
        this.dbFunctions = dbFunctions;
    }

    public boolean processPayment(Connection conn, Reservation reservation, Room selectedRoom, Scanner scanner) {
        try {
            // Step 1: Prompt user for promo code (optional)
            System.out.print(ConsoleColors.YELLOW + "\n📢 Do you have a promo code? (Enter or leave blank): " + ConsoleColors.RESET);
            String promoCode = scanner.nextLine().trim();
            if (!promoCode.isEmpty()) {
                System.out.println("🔎 Verifying promo code: " + promoCode + "...");
            }

            // Step 2: Calculate total price using PriceCalculator
            PriceCalculator.PriceBreakdown priceBreakdown = PriceCalculator.calculatePrice(
                    selectedRoom.getPrice(),
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate(),
                    promoCode // Pass the promo code to the PriceCalculator
            );

            // Step 3: Display payment details
            showPaymentDetails(priceBreakdown, selectedRoom, reservation);

            // Step 4: Confirm payment from user
            if (!confirmPayment(scanner)) {
                dbFunctions.updateReservationStatus(conn, reservation.getReservationId(), "Pending");
                System.out.println(ConsoleColors.YELLOW + "\n🔔 Payment deferred. The reservation status is now 'Pending'." + ConsoleColors.RESET);
                return false;
            }

            // Step 5: Insert payment to the database
            boolean paymentSuccess = dbFunctions.insertPayment(
                    conn,
                    reservation.getReservationId(),
                    priceBreakdown.getTotalAmount(),
                    LocalDate.now(),
                    "Paid"
            );

            if (paymentSuccess) {
                // Update reservation status to "Paid"
                dbFunctions.updateReservationStatus(conn, reservation.getReservationId(), "Paid");
                System.out.println(ConsoleColors.BRIGHT_GREEN + "✅ Payment processed successfully!" + ConsoleColors.RESET);
                return true;
            } else {
                System.out.println(ConsoleColors.BRIGHT_RED + "❌ Failed to process your payment. Please try again later." + ConsoleColors.RESET);
                return false;
            }
        } catch (Exception e) {
            logger.severe("Payment processing encountered an error: " + e.getMessage());
            System.out.println(ConsoleColors.BRIGHT_RED + "❌ An unexpected error occurred during payment processing. Please try again." + ConsoleColors.RESET);
            return false;
        }
    }

    private void showPaymentDetails(PriceCalculator.PriceBreakdown priceBreakdown, Room selectedRoom, Reservation reservation) {
        System.out.println(ConsoleColors.BRIGHT_BLUE + "\n=== Payment Details ===" + ConsoleColors.RESET);
        System.out.printf(ConsoleColors.PURPLE + "📅 Check-in Date: %s | Check-out Date: %s%n" + ConsoleColors.RESET,
                reservation.getCheckInDate(), reservation.getCheckOutDate());
        System.out.printf(ConsoleColors.BRIGHT_YELLOW + "🛏️ Room: %s (ID: %d)%n" + ConsoleColors.RESET,
                selectedRoom.getRoomType(), selectedRoom.getRoomId());
        System.out.printf(ConsoleColors.GREEN + "💵 Base Price: PHP %.2f%n" + ConsoleColors.RESET, priceBreakdown.getBasePrice());
        System.out.printf(ConsoleColors.CYAN + "🎁 Long Stay Discount: PHP %.2f%n" + ConsoleColors.RESET, priceBreakdown.getLongStayDiscount());
        System.out.printf(ConsoleColors.BRIGHT_CYAN + "🌟 Holiday Discount: PHP %.2f%n" + ConsoleColors.RESET, priceBreakdown.getHolidayDiscount());
        System.out.printf(ConsoleColors.BRIGHT_RED + "🔖 Tax: PHP %.2f%n" + ConsoleColors.RESET, priceBreakdown.getTax());
        System.out.printf(ConsoleColors.BRIGHT_GREEN + "💵 Total Amount Payable: PHP %.2f%n" + ConsoleColors.RESET, priceBreakdown.getTotalAmount());
    }

    private boolean confirmPayment(Scanner scanner) {
        System.out.print(ConsoleColors.YELLOW + "\n💳 Do you want to proceed with payment now? (yes/no): " + ConsoleColors.RESET);
        String paymentChoice = scanner.nextLine().trim().toLowerCase();
        return paymentChoice.equals("yes");
    }

    public static void proceedToPayment(Reservation reservation, Customer customer, Connection conn, DbFunctions db, Scanner scanner) {
        try {
            // Retrieve room details
            Room room = db.getRoomById(conn, reservation.getRoomId());
            if (room == null) {
                System.out.println(ConsoleColors.BRIGHT_RED + "❌ Failed to retrieve room details for payment. Please contact support." + ConsoleColors.RESET);
                return;
            }

            PaymentProcessing paymentProcessing = new PaymentProcessing(db);
            boolean isPaymentSuccessful = paymentProcessing.processPayment(conn, reservation, room, scanner);

            // Display final status
            if (isPaymentSuccessful) {
                System.out.println(ConsoleColors.BRIGHT_GREEN + "\n✅ Booking and payment completed successfully!" + ConsoleColors.RESET);
                System.out.println(ConsoleColors.YELLOW + "📝 You can view or save your receipt from the 'View Receipt' option in the main menu." + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.YELLOW + "\n⚠️ Your reservation is saved but payment has not been processed yet." + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            logger.severe("Error during payment processing workflow: " + e.getMessage());
            System.out.println(ConsoleColors.BRIGHT_RED + "❌ An unexpected error occurred during payment processing. Please try again later." + ConsoleColors.RESET);
        }
    }
}
