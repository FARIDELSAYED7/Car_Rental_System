package models;

/**
 * User - Abstract base class for all users in the system
 *
 * OOP Concepts Demonstrated:
 * - ABSTRACTION: This class is abstract (you cannot create a User object directly)
 * - ENCAPSULATION: All fields are private, accessed through getters/setters
 * - CONSTRUCTORS: Used to initialize user data when creating objects
 */
public abstract class User {

    // Private fields - ENCAPSULATION (data hiding)
    private String userId;
    private String username;
    private String name;
    private String email;
    private String password;
    private String role;

    // Constructor - initializes all fields when a User is created
    public User(String userId, String username, String name, String email, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Abstract method - ABSTRACTION
     * Every subclass (Admin, Customer) MUST provide its own version of this method.
     * This is what makes polymorphism possible.
     */
    public abstract String displayInfo();

    // ===== Getters (allow reading private fields) =====
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // ===== Setters (allow modifying private fields) =====
    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
