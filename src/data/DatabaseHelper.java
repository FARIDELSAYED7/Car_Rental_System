package data;

import models.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * DatabaseHelper - Handles all MySQL database operations.
 *
 * شرح بالعربي (مبسّط):
 * - الكلاس ده مسؤول عن أي عملية قراءة/إضافة/تعديل/حذف في قاعدة البيانات.
 * - كل دالة هنا بتتعامل مع جدول معين (users, cars, rentals, invoices).
 * - ده ليس جزء ذكاء اصطناعي؛ هو مجرد اتصال عادي بقاعدة البيانات.
 */
public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://localhost:3306/car_rental";
    private static final String USER = "root"; // Default username
    private static final String PASSWORD = "FARID2007@"; // Default empty password
    // بالعربي: عدّل بيانات الاتصال حسب بيئتك المحلية.

    /**
     * Connect to the database
     * بالعربي: بنرجّع اتصال جاهز بقاعدة البيانات.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ================== USERS ==================
    // بالعربي: التعامل مع جدول المستخدمين.

    public static ArrayList<User> loadAllUsers() {
        ArrayList<User> usersList = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("user_id");
                String username = rs.getString("username");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String role = rs.getString("role");

                if (role.equals("Admin")) {
                    usersList.add(new Admin(id, username, name, email, password));
                } else {
                    usersList.add(new Customer(id, username, name, email, password));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    // ================== CARS ==================
    // بالعربي: التعامل مع جدول السيارات.

    public static ArrayList<Car> loadAllCars() {
        ArrayList<Car> carsList = new ArrayList<>();
        String query = "SELECT * FROM cars";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("car_id");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                double price = rs.getDouble("price_per_day");
                boolean available = rs.getBoolean("available");
                String type = rs.getString("car_type");

                carsList.add(new Car(id, brand, model, year, price, available, type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carsList;
    }

    public static void insertCar(Car car) {
        String query = "INSERT INTO cars (car_id, brand, model, year, price_per_day, available, car_type) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, car.getCarId());
            pstmt.setString(2, car.getBrand());
            pstmt.setString(3, car.getModel());
            pstmt.setInt(4, car.getYear());
            pstmt.setDouble(5, car.getPricePerDay());
            pstmt.setBoolean(6, car.isAvailable());
            pstmt.setString(7, car.getCarType());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCar(Car car) {
        String query = "UPDATE cars SET brand=?, model=?, year=?, price_per_day=?, available=?, car_type=? WHERE car_id=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, car.getBrand());
            pstmt.setString(2, car.getModel());
            pstmt.setInt(3, car.getYear());
            pstmt.setDouble(4, car.getPricePerDay());
            pstmt.setBoolean(5, car.isAvailable());
            pstmt.setString(6, car.getCarType());
            pstmt.setString(7, car.getCarId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCar(String carId) {
        String query = "DELETE FROM cars WHERE car_id=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, carId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateCarAvailability(String carId, boolean available) {
        String query = "UPDATE cars SET available=? WHERE car_id=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBoolean(1, available);
            pstmt.setString(2, carId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================== RENTALS & INVOICES ==================
    // بالعربي: بيانات الحجز والفواتير.

    public static ArrayList<Rental> loadAllRentals() {
        ArrayList<Rental> rentalsList = new ArrayList<>();
        String query = "SELECT * FROM rentals";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("rental_id");
                String customerId = rs.getString("customer_id");
                String carId = rs.getString("car_id");
                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end = rs.getDate("end_date").toLocalDate();

                // Find customer and car from DataStore lists (assuming they are loaded first)
                // بالعربي: بنربط الحجز بالعميل والسيارة الموجودة في الذاكرة.
                Customer customer = null;
                for (User u : DataStore.users) {
                    if (u.getUserId().equals(customerId) && u instanceof Customer) {
                        customer = (Customer) u;
                        break;
                    }
                }
                
                Car car = DataStore.findCarById(carId);

                if (customer != null && car != null) {
                    Rental rental = new Rental(id, customer, car, start, end);
                    rentalsList.add(rental);
                    customer.addRental(rental); // add to customer history
                    // بالعربي: نضيف الحجز لسجل العميل.
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentalsList;
    }

    public static void insertRental(Rental rental) {
        String query = "INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, number_of_days, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rental.getRentalId());
            pstmt.setString(2, rental.getCustomer().getUserId());
            pstmt.setString(3, rental.getCar().getCarId());
            pstmt.setDate(4, Date.valueOf(rental.getStartDate()));
            pstmt.setDate(5, Date.valueOf(rental.getEndDate()));
            pstmt.setInt(6, rental.getNumberOfDays());
            pstmt.setDouble(7, rental.getTotalPrice());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Invoice> loadAllInvoices() {
        ArrayList<Invoice> invoicesList = new ArrayList<>();
        String query = "SELECT * FROM invoices";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("invoice_id");
                String rentalId = rs.getString("rental_id");
                String method = rs.getString("payment_method");
                String status = rs.getString("payment_status");

                // Find rental
                Rental rental = null;
                for (Rental r : DataStore.rentals) {
                    if (r.getRentalId().equals(rentalId)) {
                        rental = r;
                        break;
                    }
                }

                if (rental != null) {
                    Payment payment = new Payment("PAY_" + id, rental.getTotalPrice(), method);
                    if (status.equals("Paid")) { payment.processPayment(); }
                    invoicesList.add(new Invoice(id, rental, payment));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoicesList;
    }

    public static void insertInvoice(Invoice invoice) {
        String query = "INSERT INTO invoices (invoice_id, rental_id, payment_method, payment_status) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, invoice.getInvoiceId());
            pstmt.setString(2, invoice.getRental().getRentalId());
            pstmt.setString(3, invoice.getPayment().getPaymentMethod());
            pstmt.setString(4, invoice.getPayment().isSuccessful() ? "Paid" : "Pending");
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // ================== INIT DB ==================
    // بالعربي: تجهيز قاعدة البيانات والجداول أول مرة.
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS car_rental");
            stmt.executeUpdate("USE car_rental");
            
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id VARCHAR(10) PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100) NOT NULL," +
                    "password VARCHAR(100) NOT NULL," +
                    "role VARCHAR(20) NOT NULL)");
                    
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cars (" +
                    "car_id VARCHAR(10) PRIMARY KEY," +
                    "brand VARCHAR(50) NOT NULL," +
                    "model VARCHAR(50) NOT NULL," +
                    "year INT NOT NULL," +
                    "price_per_day DOUBLE NOT NULL," +
                    "available BOOLEAN DEFAULT TRUE," +
                    "car_type VARCHAR(30) NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rentals (" +
                    "rental_id VARCHAR(10) PRIMARY KEY," +
                    "customer_id VARCHAR(10) NOT NULL," +
                    "car_id VARCHAR(10) NOT NULL," +
                    "start_date DATE NOT NULL," +
                    "end_date DATE NOT NULL," +
                    "number_of_days INT NOT NULL," +
                    "total_price DOUBLE NOT NULL," +
                    "FOREIGN KEY (customer_id) REFERENCES users(user_id)," +
                    "FOREIGN KEY (car_id) REFERENCES cars(car_id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS invoices (" +
                    "invoice_id VARCHAR(10) PRIMARY KEY," +
                    "rental_id VARCHAR(10) NOT NULL," +
                    "payment_method VARCHAR(30) NOT NULL," +
                    "payment_status VARCHAR(20) DEFAULT 'Paid'," +
                    "FOREIGN KEY (rental_id) REFERENCES rentals(rental_id))");

            // Insert sample data ONLY if users table is empty
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                insertSampleData(conn);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not initialize database. Is MySQL running?");
        }
    }
    
    private static void insertSampleData(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO users VALUES " +
                    "('U001', 'admin1', 'Ahmed Hassan', 'ahmed@email.com', 'admin123', 'Admin')," +
                    "('U002', 'admin2', 'Sara Ali', 'sara@email.com', 'admin456', 'Admin')," +
                    "('U003', 'customer1', 'Farid Elsayed', 'farid@email.com', 'cust123', 'Customer')," +
                    "('U004', 'customer2', 'Nour Mohamed', 'nour@email.com', 'cust456', 'Customer')," +
                    "('U005', 'customer3', 'Omar Khaled', 'omar@email.com', 'cust789', 'Customer')");

            stmt.executeUpdate("INSERT INTO cars VALUES " +
                    "('C001', 'Toyota', 'Camry', 2023, 50.0, TRUE, 'Sedan')," +
                    "('C002', 'BMW', 'X5', 2024, 120.0, TRUE, 'SUV')," +
                    "('C003', 'Honda', 'Civic', 2022, 40.0, TRUE, 'Sedan')," +
                    "('C004', 'Mercedes', 'C-Class', 2024, 100.0, TRUE, 'Luxury')," +
                    "('C005', 'Ford', 'Mustang', 2023, 90.0, TRUE, 'Sports')," +
                    "('C006', 'Toyota', 'RAV4', 2023, 65.0, TRUE, 'SUV')," +
                    "('C007', 'Hyundai', 'Elantra', 2022, 35.0, TRUE, 'Economy')," +
                    "('C008', 'Kia', 'Sportage', 2024, 55.0, TRUE, 'SUV')," +
                    "('C009', 'Nissan', 'Altima', 2023, 45.0, TRUE, 'Sedan')," +
                    "('C010', 'Chevrolet', 'Tahoe', 2024, 110.0, TRUE, 'SUV')");
        }
    }
}
