package models;

import java.util.ArrayList;

/**
 * Customer - Represents a customer user who can rent cars
 *
 * OOP Concepts Demonstrated:
 * - INHERITANCE: Customer extends (inherits from) User
 * - POLYMORPHISM: displayInfo() is overridden differently than Admin
 * - ENCAPSULATION: rentalHistory is private, accessed through getter
 *
 * شرح بالعربي:
 * - العميل يقدر يحجز سيارات.
 * - عنده تاريخ حجوزات (rentalHistory).
 */
public class Customer extends User {

    // Private field - only Customer has rental history
    // بالعربي: تاريخ الحجوزات خاص بالعميل فقط.
    private ArrayList<Rental> rentalHistory;

    /**
     * Constructor - initializes customer with an empty rental history
     * بالعربي: بنبدأ تاريخ الحجوزات بقائمة فاضية.
     */
    public Customer(String userId, String username, String name, String email, String password) {
        // Call parent constructor with role "Customer"
        super(userId, username, name, email, password, "Customer");
        this.rentalHistory = new ArrayList<>(); // Start with empty history
    }

    /**
     * METHOD OVERRIDING - Different implementation than Admin's displayInfo()
     * This demonstrates POLYMORPHISM - same method name, different behavior
     * بالعربي: نفس اسم الدالة لكن سلوك مختلف عن الأدمن.
     */
    @Override
    public String displayInfo() {
        return "Customer: " + getName() + " (ID: " + getUserId() + ") | Rentals: " + rentalHistory.size();
    }

    /**
     * Get the customer's rental history
     * بالعربي: إرجاع قائمة الحجوزات.
     */
    public ArrayList<Rental> getRentalHistory() {
        return rentalHistory;
    }

    /**
     * Add a new rental to this customer's history
     * بالعربي: إضافة حجز جديد لقائمة العميل.
     */
    public void addRental(Rental rental) {
        rentalHistory.add(rental);
    }
}
