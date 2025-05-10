// Responsible for processing customer inputs
//

package services;

import db.DbFunctions;
import models.Customer;
import utils.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class CustomerService {
    private static final DbFunctions db = new DbFunctions();

    public static Customer registerCustomer(Scanner scanner, DbFunctions db, Connection conn) throws SQLException {
        // Check if email already exists
        String email = InputValidator.getValidEmail(scanner);
        if (db.getCustomerByEmail(conn, email) != null) {
            System.out.println("❌ Email is already registered. Please log in.");
            return null;
        }
        Customer customer = getCustomerDetails(scanner, email);

        // Save customer to the database
        saveCustomerToDatabase(customer, db, conn);
        System.out.println("✅ Registration successful! Welcome, " + customer.getName() + " !");

        return customer;
    }

    private static Customer getCustomerDetails (Scanner scanner, String email){
        String name = InputValidator.getValidName(scanner);
        String phone = InputValidator.getValidPhone(scanner);
        String password = InputValidator.getValidPassword(scanner);
        return new Customer(name, email, phone, password);
    }

    private static void saveCustomerToDatabase (Customer customer, DbFunctions db, Connection conn) throws
    SQLException {
        int customerId = db.insertCustomer(conn, customer.getName(), customer.getEmail(), customer.getPhone(), customer.getPassword());
        customer.setCustomerId(customerId);
    }

    // Logging in
    public static Customer loginCustomer (Scanner scanner, DbFunctions db, Connection conn) throws SQLException {
        String email = InputValidator.getValidEmail(scanner);

        Customer customer = db.getCustomerByEmail(conn, email);

        if (customer == null) {
            System.out.println("❌ No customer found with the provided email.");
            return null;
        }

        String password = InputValidator.getValidPassword(scanner);

        if (!customer.getPassword().equals(password)) {
            System.out.println("❌ Incorrect password.");
            return null;
        }

        System.out.println("\n ✅ Login Successful. \n Welcome back, " + customer.getName() + " !");
        return customer;
    }

    public boolean checkRoomAvailability(Connection conn, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return db.isRoomAvailable(conn, roomId, checkInDate, checkOutDate);
    }

}
