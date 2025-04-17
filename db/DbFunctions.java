package db;

import models.Reservation;
import models.Room;
import models.Customer;
import models.Payment;

import utils.LoggerConfig;

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
                                   "name VARCHAR(100)," +
                                   "email VARCHAR(100)," +
                                   "phone VARCHAR(15));";

            String roomTable = "CREATE TABLE IF NOT EXISTS rooms (" +
                               "room_id SERIAL PRIMARY KEY," +
                               "room_type VARCHAR(50)," +
                               "price DECIMAL(10,2)," +
                               "availability BOOLEAN);";

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

    public int insertCustomer(Connection conn, String name, String email, String phone) throws SQLException {
        int generatedId = -1;
        try {
            String query = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?) RETURNING customer_id;";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phone);

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
}

