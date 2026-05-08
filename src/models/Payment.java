package models;

/**
 * Payment - Simulates a payment transaction
 *
 * This is a SIMULATED payment - no real money is involved.
 * It just records the payment method and marks it as successful.
 *
 * OOP Concepts:
 * - ENCAPSULATION: Private fields with getters
 * - METHODS: processPayment() simulates payment logic
 */
public class Payment {

    private String paymentId;
    private double amount;
    private String paymentMethod; // Credit Card, Debit Card, Cash, PayPal
    private boolean successful;

    /**
     * Constructor - creates a payment (not yet processed)
     */
    public Payment(String paymentId, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.successful = false; // Payment starts as not processed
    }

    /**
     * Simulate processing a payment
     * In a real system, this would connect to a payment gateway
     * For our project, it always succeeds
     */
    public boolean processPayment() {
        // Simulate successful payment
        this.successful = true;
        return true;
    }

    // ===== Getters =====
    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
