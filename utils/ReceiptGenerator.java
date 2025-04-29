package utils;

import models.Payment;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class ReceiptGenerator {
    private static final Logger logger = Logger.getLogger(ReceiptGenerator.class.getName());
    private static final String RECEIPT_DIRECTORY = "receipts";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String generateReceipt(String customerName, int roomId, String roomType, 
                                       double price, LocalDate paymentDate, String status,
                                       LocalDate checkInDate, LocalDate checkOutDate) {
        // Calculate number of nights and total amount
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double totalAmount = price * nights;

        return String.format("""
            ╔══════════════════════════════════════╗
            ║         HOTEL BOOKING RECEIPT        ║
            ╠══════════════════════════════════════╣
            ║ Customer: %-25s ║
            ║ Room Number: %-22d ║
            ║ Room Type: %-24s ║
            ║──────────────────────────────────────║
            ║ Check-in Date: %-20s ║
            ║ Check-out Date: %-19s ║
            ║ Number of Nights: %-18d ║
            ║──────────────────────────────────────║
            ║ Price per Night: PHP %-15.2f ║
            ║ Total Amount: PHP %-17.2f ║
            ║──────────────────────────────────────║
            ║ Payment Date: %-21s ║
            ║ Status: %-27s ║
            ╚══════════════════════════════════════╝
            """,
            customerName, roomId, roomType,
            checkInDate.format(DATE_FORMATTER),
            checkOutDate.format(DATE_FORMATTER),
            nights,
            price,
            totalAmount,
            paymentDate.format(DATE_FORMATTER),
            status
        );
    }

    public static void saveReceiptToFile(String receipt, int reservationId, String customerName) {
        try {
            // Create receipts directory if it doesn't exist
            Path receiptDir = Paths.get(RECEIPT_DIRECTORY);
            if (!Files.exists(receiptDir)) {
                Files.createDirectory(receiptDir);
            }

            // Generate filename with timestamp
            String filename = String.format("%s/receipt_%d_%s_%s.txt",
                    RECEIPT_DIRECTORY,
                    reservationId,
                    customerName.replaceAll("\\s+", "_"),
                    LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

            // Write receipt to file
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(receipt);
                logger.info("✅ Receipt saved to: " + filename);
            }
        } catch (IOException e) {
            logger.severe("❌ Error saving receipt: " + e.getMessage());
        }
    }
}