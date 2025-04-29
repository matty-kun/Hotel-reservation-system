import db.DbFunctions;
import java.sql.Connection;
import java.util.logging.Logger;

public class Seeder {
    private static final Logger logger = Logger.getLogger(Seeder.class.getName());

    public static void seedRooms(DbFunctions db, Connection conn) {
        try {
            // Step 1: Seed Standard rooms
            for (int i = 101; i <= 120; i++) {
                db.insertRoom(
    conn, i,
    "Standard",
    1200.00,
    true,
    "A cozy room with essential amenities, suitable for solo travelers or couples on a budget." // Description is included here
);
            }

            // Step 2: Seed Deluxe rooms
            for (int i = 201; i <= 220; i++) {
                db.insertRoom(
                        conn, i,
                        "Deluxe",
                        1800.00,
                        true,
                        "A spacious room featuring upgraded furniture and a great view, perfect for small families or couples."
                );
            }

            // Step 3: Seed Suite rooms
            for (int i = 301; i <= 320; i++) {
                db.insertRoom(
                        conn, i,
                        "Suite",
                        2500.00,
                        true,
                        "A luxurious suite with premium facilities, including a living area and king-size bed for maximum comfort."
                );
            }

            // Step 4: Seed Family rooms
            for (int i = 401; i <= 420; i++) {
                db.insertRoom(
                        conn, i,
                        "Family",
                        3000.00,
                        true,
                        "A large, well-equipped family room designed to accommodate up to 5 guests with ease and comfort."
                );
            }

            // Log success
            logger.info("✅ Room Seeding Complete (with conflict handling).");
        } catch (Exception e) {
            logger.severe("❌ Failed to seed rooms: " + e.getMessage());
            throw new RuntimeException("Error during room seeding", e);
        }
    }
}