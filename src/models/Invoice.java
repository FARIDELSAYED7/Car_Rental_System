package models;

/**
 * Invoice - Generates an invoice for a completed rental
 *
 * An invoice combines information from a Rental and a Payment
 * to create a complete billing record.
 *
 * OOP Concepts:
 * - ASSOCIATION: Invoice HAS-A Rental and HAS-A Payment
 * - ENCAPSULATION: Private fields with getters
 *
 * شرح بالعربي:
 * - الفاتورة بتجمع تفاصيل الحجز والدفع.
 * - الهدف منها عرض المعلومات بشكل مرتب للمستخدم.
 */
public class Invoice {

    private String invoiceId;
    private Rental rental;   // Association - Invoice has a Rental
    private Payment payment; // Association - Invoice has a Payment

    /**
     * Constructor - creates an invoice linking a rental to its payment
     * بالعربي: ربط الفاتورة بالحجز والدفع المرتبطين بها.
     */
    public Invoice(String invoiceId, Rental rental, Payment payment) {
        this.invoiceId = invoiceId;
        this.rental = rental;
        this.payment = payment;
    }

    /**
     * Generate a formatted invoice text for display
     * This creates a nice readable invoice string
     * بالعربي: إنشاء نص منسّق للفاتورة لسهولة طباعته أو حفظه.
     */
    public String generateInvoiceText() {
        String text = "";
        text += "==========================================\n";
        text += "              INVOICE                     \n";
        text += "==========================================\n";
        text += "Invoice #:      " + invoiceId + "\n";
        text += "------------------------------------------\n";
        text += "Customer:       " + rental.getCustomerName() + "\n";
        text += "Car:            " + rental.getCarInfo() + "\n";
        text += "Car ID:         " + rental.getCar().getCarId() + "\n";
        text += "------------------------------------------\n";
        text += "Start Date:     " + rental.getStartDate() + "\n";
        text += "End Date:       " + rental.getEndDate() + "\n";
        text += "Number of Days: " + rental.getNumberOfDays() + "\n";
        text += "Price Per Day:  $" + String.format("%.2f", rental.getCar().getPricePerDay()) + "\n";
        text += "------------------------------------------\n";
        text += "TOTAL PRICE:    $" + String.format("%.2f", rental.getTotalPrice()) + "\n";
        text += "Payment Method: " + payment.getPaymentMethod() + "\n";
        text += "Payment Status: " + (payment.isSuccessful() ? "PAID" : "PENDING") + "\n";
        text += "==========================================\n";
        return text;
    }

    // ===== Getters =====
    public String getInvoiceId() {
        return invoiceId;
    }

    public Rental getRental() {
        return rental;
    }

    public Payment getPayment() {
        return payment;
    }

    // ===== Helper getters for TableView display =====
    // بالعربي: دوال مساعدة للعرض داخل الجداول.
    public String getCustomerName() {
        return rental.getCustomerName();
    }

    public String getCarInfo() {
        return rental.getCarInfo();
    }

    public String getTotalPriceFormatted() {
        return String.format("$%.2f", rental.getTotalPrice());
    }

    public String getPaymentMethod() {
        return payment.getPaymentMethod();
    }

    public String getPaymentStatus() {
        if (payment.isSuccessful()) {
            return "Paid";
        } else {
            return "Pending";
        }
    }

    public String getDatesInfo() {
        return rental.getStartDate() + " to " + rental.getEndDate();
    }
}
