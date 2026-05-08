package models;

import data.*;

/**
 * Admin - Represents an administrator user
 *
 * OOP Concepts Demonstrated:
 * - INHERITANCE: Admin extends (inherits from) User
 * - POLYMORPHISM: displayInfo() is overridden to behave differently than Customer
 * - METHOD OVERRIDING: We provide a new version of the abstract displayInfo() method
 */
public class Admin extends User {

    /**
     * Constructor - uses super() to call the parent (User) constructor
     * The role is automatically set to "Admin"
     */
    public Admin(String userId, String username, String name, String email, String password) {
        // super() calls the User constructor - INHERITANCE in action
        super(userId, username, name, email, password, "Admin");
    }

    /**
     * METHOD OVERRIDING - This method was abstract in User,
     * so we MUST provide an implementation here.
     * This version is specific to Admin users.
     */
    @Override
    public String displayInfo() {
        return "Admin: " + getName() + " (ID: " + getUserId() + ") | Email: " + getEmail();
    }

    /**
     * Admin-specific method: Add a new car to the system
     */
    public void addCar(Car car) {
        DataStore.cars.add(car);
        DatabaseHelper.insertCar(car);
    }

    /**
     * Admin-specific method: Remove a car from the system by its ID
     */
    public void removeCar(String carId) {
        // Loop through cars and remove the one with matching ID
        for (int i = 0; i < DataStore.cars.size(); i++) {
            if (DataStore.cars.get(i).getCarId().equals(carId)) {
                DataStore.cars.remove(i);
                DatabaseHelper.deleteCar(carId);
                break; // Stop after finding and removing the car
            }
        }
    }
}
