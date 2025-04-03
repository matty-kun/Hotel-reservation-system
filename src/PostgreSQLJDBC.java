import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgreSQLJDBC {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/hotel_management";
        String user = "your_username";
        String password = "your_password";

        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to PostgreSQL database!");

            // Execute a test query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");

            while (rs.next()) {
                System.out.println("Customer ID: " + rs.getInt("customer_id") + ", Name: " + rs.getString("name"));
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}