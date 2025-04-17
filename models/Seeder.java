package models;

import db.DbFunctions;

import java.sql.Connection;
import java.util.logging.Logger;

public class Seeder {
    private static final Logger logger = Logger.getLogger(Seeder.class.getName());
    public static void seedRooms(DbFunctions db, Connection conn) {
        for (int i = 101; i <= 120; i++) {
            db.insertRoom(conn, i, "Standard", 1200.00, true);
        }

        for (int i = 201; i <= 220; i++) {
            db.insertRoom(conn, i, "Deluxe", 1800.00, true);
        }

        for (int i = 301; i <= 320; i++) {
            db.insertRoom(conn, i, "Suite", 2500.00, true);
        }

        for (int i = 401; i <= 420; i++) {
            db.insertRoom(conn, i, "Family", 3000.00, true);
        }

        logger.info("✅ Room Seeding Complete");
    }
}
