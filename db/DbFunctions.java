package db;

import models.Reservation;
import models.Room;
import models.Customer;
import models.Payment;

import utils.LoggerConfig;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DbFunctions {
    private static final Logger logger = Logger.getLogger(DbFunctions.class.getName());
    public Connection connect_to_db(String dbname, String user, String pass){
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname, user, pass);

            if (conn != null) {
                logger.info("✅ Connection Established!");
            } else {
                logger.warning("❌ Connection Failed");
            }
        } catch (Exception e) {
            logger.severe("❌ Connection Failed: " + e.getMessage());
        }
        return conn;
    }

    public void createTables(Connection conn) throws SQLException {
        try {
            Statement statement = conn.createStatement();

            String customerTable = "CREATE TABLE IF NOT EXISTS customers (" +
                                   "customer_id SERIAL PRIMARY KEY," +
                                   "name VARCHAR(100) NOT NULL," +
                                   "email VARCHAR(100) NOT NULL UNIQUE," +
                                   "phone VARCHAR(15) NOT NULL," +
                                   "password VARCHAR(100) NOT NULL);";

            String roomTable = "CREATE TABLE IF NOT EXISTS rooms (" +
                               "room_id SERIAL PRIMARY KEY," +
                               "room_type VARCHAR(50)," +
                               "price DECIMAL(10,2)," +
                               "availability BOOLEAN DEFAULT TRUE);";

            String reservationTable = "CREATE TABLE IF NOT EXISTS reservations(" +
                                      "reservation_id SERIAL PRIMARY KEY," +
                                      "customer_id INT REFERENCES customers(customer_id)," +
                                      "room_id INT REFERENCES rooms(room_id)," +
                                      "check_in_date DATE," +
                                      "check_out_date DATE," +
                                      "status VARCHAR(20));";

            String paymentTable = "CREATE TABLE IF NOT EXISTS payments (" +
                                  "payment_id SERIAL PRIMARY KEY," +
                                  "reservation_id INT REFERENCES reservations(reservation_id)," +
                                  "amount DECIMAL(10,2)," +
                                  "payment_date DATE," +
                                  "payment_status VARCHAR(20));";

            statement.executeUpdate(customerTable);
            statement.executeUpdate(roomTable);
            statement.executeUpdate(reservationTable);
            statement.executeUpdate(paymentTable);

            logger.info("✅ Tables Created or Verified");
        } catch (Exception e) {
            logger.severe("❌ Table Creation Error: " + e.getMessage());
        }
    }

    public Customer getCustomerByEmail(Connection conn, String email) throws SQLException {
        String query = "SELECT * FROM customers WHERE email = ?";
        try (PreparedStatement prepstmt = conn.prepareStatement(query)) {
            prepstmt.setString(1, email);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password")
                );
                return customer;
            }
        }
        return null;
    }


    public int insertCustomer(Connection conn, String name, String email, String phone, String password) throws SQLException {
        int generatedId = -1;
        try {
            String query = "INSERT INTO customers (name, email, phone, password) VALUES (?, ?, ?, ?) RETURNING customer_id;";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

            logger.info("✅ Customer Inserted Successfully");
        } catch (Exception e) {
            logger.severe("❌ Insert Customer Error: " + e.getMessage());
        }
        return generatedId;
    }

    public void insertRoom(Connection conn, int roomId, String roomType, double price, boolean availability) {
        try {
            Statement statement = conn.createStatement();
            String query = String.format("INSERT INTO ROOMS (room_id, room_type, price, availability) VALUES (%d, '%s', %.2f, %b) " +
                                        "ON CONFLICT (room_id) DO NOTHING;", roomId, roomType, price, availability);
            statement.executeUpdate(query);
            logger.info("✅ Room Inserted");
        } catch (Exception e) {
            logger.severe("❌ Insert Room Error: " + e.getMessage());
        }
    }

    public void insertPayment(Connection conn, int reservationId, double amount, LocalDate paymentDate, String paymentStatus) {
        try {
            String query = "INSERT INTO payments (reservation_id, amount, payment_date, payment_status) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, reservationId);
            statement.setDouble(2, amount);
            statement.setDate(3, Date.valueOf(paymentDate));
            statement.setString(4, paymentStatus);

            statement.executeUpdate();
            logger.info("Processing payment for reservation ID: " + reservationId);
            logger.info("✅ Payment Inserted");
        } catch (Exception e) {
            logger.severe("❌ Insert Payment Error: " + e.getMessage());
        }
    }


    public int insertReservation(Connection conn, Reservation reservation) {
        int generatedId = -1;
        try {
            String query = "INSERT INTO reservations (customer_id, room_id, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?) RETURNING reservation_id";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1, reservation.getCustomerId());
            statement.setInt(2, reservation.getRoomId());
            statement.setDate(3, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
            statement.setString(5, reservation.getStatus());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
            logger.info("✅ Reservation Inserted");
        } catch (Exception e) {
            logger.severe("❌ Insert Reservation Error: " + e.getMessage());
        }
        return generatedId;
    }

    public List<Room> getAvailableRoomsByType(Connection conn, String roomType) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        try {
            String query = "SELECT * FROM rooms WHERE room_type = ? AND availability = TRUE";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, roomType);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Room room = new Room(rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price"),
                        rs.getBoolean("availability")
                );
                rooms.add(room);
            }
        } catch (Exception e) {
           logger.severe("❌ Database error: " + e.getMessage());
        }
        return rooms;
    }

    public void updateRoomAvailability(Connection conn, int roomId, boolean availability) {
        try {
            String query = "UPDATE rooms SET availability = ? WHERE room_id  = ? ";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setBoolean(1, availability);
            statement.setInt(2, roomId);
            statement.executeUpdate();
            logger.info("✅ Room availability updated");
        } catch (Exception e) {
            logger.severe("❌ Update Room Availability Error: " + e.getMessage());
        }
    }

    public Reservation getReservationById(Connection conn, int reservationId) {
        String query = "SELECT * FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Reservation(
                    rs.getInt("reservation_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getString("status")
                );
            } else {
                logger.warning("No reservation found with ID: " + reservationId);
            }
        } catch (Exception e) {
            logger.severe("Error fetching reservation by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Reservation> getReservationByCustomerId(Connection conn, int customerId) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("room_id"),
                        rs.getDate("check_in_date").toLocalDate(),
                        rs.getDate("check_out_date").toLocalDate(),
                        rs.getString("status")
                ));
            }
        } catch (Exception e) {
            logger.severe("Error fetching reservations: " + e.getMessage());
        }
        return reservations;
    }

    public void updateReservationStatus(Connection conn, int reservationId, String status) {
         String query = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
         try (PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setString(1, status);
             stmt.setInt(2, reservationId);
             stmt.executeUpdate();
             logger.info("✅ Reservation status updated");
         } catch (Exception e) {
             logger.severe("❌ Error updating reservation status: " + e.getMessage());
         }
    }

    public boolean isRoomAvailable(Connection conn, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        // First check if room is marked as available in the rooms table
        String roomQuery = "SELECT availability FROM rooms WHERE room_id = ?";
        try (PreparedStatement roomStmt = conn.prepareStatement(roomQuery)) {
            roomStmt.setInt(1, roomId);
            ResultSet roomRs = roomStmt.executeQuery();

            if (!roomRs.next() || !roomRs.getBoolean("availability")) {
                return false;
            }

            // Then check reservation conflicts
            String reservationQuery = """
                SELECT 1 FROM reservations
                WHERE room_id = ?
                AND status IN ('Booked', 'Paid', 'Confirmed')
                AND (
                    (check_in_date <= ? AND check_out_date >= ?)
                    OR (check_in_date >= ? AND check_in_date < ?)
                    OR (check_out_date > ? AND check_out_date <= ?)
                )
            """;

            PreparedStatement stmt = conn.prepareStatement(reservationQuery);
            stmt.setInt(1, roomId);
            stmt.setDate(2, Date.valueOf(checkOutDate));
            stmt.setDate(3, Date.valueOf(checkInDate));
            stmt.setDate(4, Date.valueOf(checkInDate));
            stmt.setDate(5, Date.valueOf(checkOutDate));
            stmt.setDate(6, Date.valueOf(checkInDate));
            stmt.setDate(7, Date.valueOf(checkOutDate));

            ResultSet rs = stmt.executeQuery();
            return !rs.next();
        } catch (Exception e) {
            logger.severe("❌ Room availability check failed: " + e.getMessage());
            return false;
        }
    }
    public Customer getCustomerById(Connection conn, int customerId) {
        try {
            String query = "SELECT * FROM customers WHERE customer_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password")
                );
            }
        } catch (Exception e) {
            logger.severe("❌ Error fetching customer: " + e.getMessage());
        }
        return null;
    }

    public Room getRoomById(Connection conn, int roomId) {
        try {
            String query = "SELECT * FROM rooms WHERE room_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getDouble("price"),
                        rs.getBoolean("availability")
                );
            }
        } catch (Exception e) {
            logger.severe("❌ Error fetching room: " + e.getMessage());
        }
        return null;
    }

    public String getReceiptByReservationId(Connection conn, int reservationId) {
        String receipt = "";
        try {
            String query = """
            SELECT c.name, r.room_id, ro.room_type, ro.price, p.payment_date, r.status
            FROM reservations r
            JOIN customers c ON r.customer_id = c.customer_id
            JOIN rooms ro ON r.room_id = ro.room_id
            JOIN payments p ON r.reservation_id = p.reservation_id
            WHERE r.reservation_id = ?;
        """;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                int roomId = rs.getInt("room_id");
                String roomType = rs.getString("room_type");
                double price = rs.getDouble("price");
                String paymentDate = rs.getDate("payment_date").toString();
                String status = rs.getString("status");

                receipt = """
                \n📄 === Payment Receipt ===
                🧑 Customer Name: %s
                🏨 Room ID: %d
                🛏️ Room Type: %s
                💰 Amount Paid: PHP %.2f
                📅 Payment Date: %s
                📌 Payment Status: %s
                """.formatted(name, roomId, roomType, price, paymentDate, status);
            } else {
                receipt = "❌ No receipt found for this reservation.";
            }
        } catch (SQLException e) {
            receipt = "❌ Error fetching receipt: " + e.getMessage();
        }
        return receipt;
    }


}