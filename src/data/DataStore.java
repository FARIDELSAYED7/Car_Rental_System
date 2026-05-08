package data;

import models.*;
import java.util.ArrayList;

/**
 * DataStore - Acts as a simple in-memory database
 *
 * This class uses STATIC ArrayLists to store all data.
 * Static means all screens share the SAME data - like a global database.
 * When the application closes, all data is lost (no file saving).
 *
 * In a real application, you would use a database instead.
 */
public class DataStore {

    // ===== Static ArrayLists - our "database tables" =====
    // Static = shared by all parts of the program
    public static ArrayList<Car> cars = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Rental> rentals = new ArrayList<>();
    public static ArrayList<Invoice> invoices = new ArrayList<>();

    // ===== ID Counters for generating unique IDs =====
    private static int carIdCounter = 11;     // Starts at 11 because sample data uses C001-C010
    private static int rentalIdCounter = 1;
    private static int invoiceIdCounter = 1;
    private static int paymentIdCounter = 1;

    // ===== ID Generator Methods =====

    /**
     * Generate a unique Car ID like "C011", "C012", etc.
     */
    public static String generateCarId() {
        String id = "C" + String.format("%03d", carIdCounter);
        carIdCounter++;
        return id;
    }

    /**
     * Generate a unique Rental ID like "R001", "R002", etc.
     */
    public static String generateRentalId() {
        String id = "R" + String.format("%03d", rentalIdCounter);
        rentalIdCounter++;
        return id;
    }

    /**
     * Generate a unique Invoice ID like "INV001", "INV002", etc.
     */
    public static String generateInvoiceId() {
        String id = "INV" + String.format("%03d", invoiceIdCounter);
        invoiceIdCounter++;
        return id;
    }

    /**
     * Generate a unique Payment ID like "PAY001", "PAY002", etc.
     */
    public static String generatePaymentId() {
        String id = "PAY" + String.format("%03d", paymentIdCounter);
        paymentIdCounter++;
        return id;
    }

    // ===== Load Sample Data =====

    /**
     * Fill the system with sample data for testing
     * This is called when the application starts
     */
    public static void loadSampleData() {
        // Initialize DB schema and sample data if needed
        DatabaseHelper.initializeDatabase();
        
        // Load everything from the database
        users = DatabaseHelper.loadAllUsers();
        cars = DatabaseHelper.loadAllCars();
        rentals = DatabaseHelper.loadAllRentals();
        invoices = DatabaseHelper.loadAllInvoices();
        
        // Sync counters with database data length to ensure IDs are fresh
        carIdCounter = cars.size() + 1;
        rentalIdCounter = rentals.size() + 1;
        invoiceIdCounter = invoices.size() + 1;
        paymentIdCounter = invoices.size() + 1;
    }

    // ===== Helper Methods =====

    /**
     * Find a user by username and password (for login)
     * Returns the User if found, or null if not found
     */
    public static User findUser(String username, String password) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Found the user!
            }
        }
        return null; // User not found
    }

    /**
     * Find a car by its ID
     * Returns the Car if found, or null if not found
     */
    public static Car findCarById(String carId) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getCarId().equals(carId)) {
                return cars.get(i);
            }
        }
        return null;
    }

    /**
     * Get all available cars (not currently rented)
     */
    public static ArrayList<Car> getAvailableCars() {
        ArrayList<Car> availableCars = new ArrayList<>();
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).isAvailable()) {
                availableCars.add(cars.get(i));
            }
        }
        return availableCars;
    }

    /**
     * Get all customers from the users list
     */
    public static ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i) instanceof Customer) {
                customers.add((Customer) users.get(i));
            }
        }
        return customers;
    }
}
