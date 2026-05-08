package models;

import java.util.ArrayList;

/**
 * Customer - Represents a customer user who can rent cars
 *
 * OOP Concepts Demonstrated:
 * - INHERITANCE: Customer extends (inherits from) User
 * - POLYMORPHISM: displayInfo() is overridden differently than Admin
 * - ENCAPSULATION: rentalHistory is private, accessed through getter
 */
public class Customer extends User {

    // Private field - only Customer has rental history
    private ArrayList<Rental> rentalHistory;

    /**
     * Constructor - initializes customer with an empty rental history
     */
    public Customer(String userId, String username, String name, String email, String password) {
        // Call parent constructor with role "Customer"
        super(userId, username, name, email, password, "Customer");
        this.rentalHistory = new ArrayList<>(); // Start with empty history
    }

    /**
     * METHOD OVERRIDING - Different implementation than Admin's displayInfo()
     * This demonstrates POLYMORPHISM - same method name, different behavior
     */
    @Override
    public String displayInfo() {
        return "Customer: " + getName() + " (ID: " + getUserId() + ") | Rentals: " + rentalHistory.size();
    }

    /**
     * Get the customer's rental history
     */
    public ArrayList<Rental> getRentalHistory() {
        return rentalHistory;
    }

    /**
     * Add a new rental to this customer's history
     */
    public void addRental(Rental rental) {
        rentalHistory.add(rental);
    }
}
