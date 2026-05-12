package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Rental - Represents a car rental transaction
 *
 * This class links a Customer to a Car with rental dates.
 * It automatically calculates the number of days and total price.
 *
 * OOP Concepts:
 * - ENCAPSULATION: Private fields with getters
 * - ASSOCIATION: Rental HAS-A Customer and HAS-A Car
 *
 * شرح بالعربي:
 * - ده الكلاس اللي يمثّل عملية الحجز نفسها.
 * - بيربط العميل بالسيارة والتواريخ ويحسب السعر تلقائيًا.
 */
public class Rental {

    private String rentalId;
    private Customer customer;  // Association - Rental has a Customer
    private Car car;            // Association - Rental has a Car
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfDays;
    private double totalPrice;

    /**
     * Constructor - creates a rental and automatically calculates days and price
     * بالعربي: بمجرد ما نعمل الحجز، نحسب عدد الأيام والسعر.
     */
    public Rental(String rentalId, Customer customer, Car car,
                  LocalDate startDate, LocalDate endDate) {
        this.rentalId = rentalId;
        this.customer = customer;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = calculateDays();
        this.totalPrice = calculateTotalPrice();
    }

    /**
     * Calculate the number of days between start and end date
     * بالعربي: حساب الفرق بين تاريخ البداية والنهاية بالأيام.
     */
    private int calculateDays() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculate total rental price = number of days × price per day
     * بالعربي: السعر الكلي = عدد الأيام × سعر اليوم.
     */
    public double calculateTotalPrice() {
        return numberOfDays * car.getPricePerDay();
    }

    // ===== Getters =====
    public String getRentalId() {
        return rentalId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Car getCar() {
        return car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // ===== Helper getters for TableView display =====
    // بالعربي: دوال مساعدة لعرض البيانات بشكل مرتب في الجداول.
    public String getCustomerName() {
        return customer.getName();
    }

    public String getCarInfo() {
        return car.toString();
    }

    public String getStartDateString() {
        return startDate.toString();
    }

    public String getEndDateString() {
        return endDate.toString();
    }

    public String getTotalPriceFormatted() {
        return String.format("$%.2f", totalPrice);
    }
}
