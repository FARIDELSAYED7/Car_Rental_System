package models;

/**
 * Car - Represents a car in the rental system
 *
 * OOP Concepts Demonstrated:
 * - ENCAPSULATION: All fields are private with getters and setters
 * - CONSTRUCTORS: Constructor initializes all car data
 * - METHOD OVERRIDING: toString() is overridden for easy display
 *
 * شرح بالعربي:
 * - ده كلاس بيحمل بيانات السيارة.
 * - فيه كل المعلومات اللي المستخدم بيشوفها في الواجهة.
 */
public class Car {

    // Private fields - ENCAPSULATION
    // بالعربي: كل خصائص السيارة محفوظة بشكل خاص.
    private String carId;
    private String brand;
    private String model;
    private int year;
    private double pricePerDay;
    private boolean available;
    private String carType; // Sedan, SUV, Sports, Luxury, Economy

    /**
     * Constructor - creates a new Car with all required information
     * بالعربي: بنملأ كل بيانات السيارة عند الإنشاء.
     */
    public Car(String carId, String brand, String model, int year,
               double pricePerDay, boolean available, String carType) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
        this.available = available;
        this.carType = carType;
    }

    // ===== Getters =====
    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getCarType() {
        return carType;
    }

    // ===== Setters =====
    public void setCarId(String carId) {
        this.carId = carId;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    /**
     * Helper method to display availability as text
     * Used in the TableView to show "Available" or "Rented"
     * بالعربي: بنرجّع حالة السيارة بشكل مقروء للواجهة.
     */
    public String getAvailabilityStatus() {
        if (available) {
            return "Available";
        } else {
            return "Rented";
        }
    }

    /**
     * METHOD OVERRIDING - Override toString() from Object class
     * This gives a nice text representation of the car
     * بالعربي: بنرجّع وصف مختصر للسيارة.
     */
    @Override
    public String toString() {
        return brand + " " + model + " (" + year + ")";
    }
}
