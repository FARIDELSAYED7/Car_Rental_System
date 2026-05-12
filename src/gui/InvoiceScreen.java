package gui;

import models.*;
import data.DataStore;
import app.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * InvoiceScreen - Displays a generated invoice after a successful booking
 *
 * Shows all rental details, payment info, and total cost in a
 * formatted invoice layout.
 *
 * شرح بالعربي:
 * - الشاشة دي بتعرض الفاتورة بعد الحجز.
 * - فيها كل البيانات المهمة بشكل مرتب.
 */
public class InvoiceScreen {

    private Scene scene;

    /**
     * Constructor - builds the invoice display screen
     * بالعربي: تجهيز واجهة الفاتورة بالكامل.
     */
    public InvoiceScreen(Invoice invoice, Customer customer) {

    // ===== Header =====
    // بالعربي: عنوان الفاتورة ورسالة النجاح.
        Label headerLabel = new Label("🧾 INVOICE");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        headerLabel.setStyle("-fx-text-fill: #1565C0;");

        Label successLabel = new Label("✅ Booking Confirmed & Payment Successful!");
        successLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        successLabel.setStyle("-fx-text-fill: #4CAF50;");

    // ===== Invoice Details =====
    // بالعربي: تفاصيل الحجز والدفع.
        Rental rental = invoice.getRental();
        Payment payment = invoice.getPayment();

    // Create formatted labels for each invoice line
    // بالعربي: سطور الفاتورة بشكل منسق.
        VBox invoiceBox = new VBox(8);
        invoiceBox.setPadding(new Insets(25));
        invoiceBox.setStyle(
            "-fx-background-color: white; -fx-border-color: #E0E0E0; " +
            "-fx-border-radius: 8; -fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        invoiceBox.setMaxWidth(500);

        // Invoice ID
        Label invIdLabel = createDetailLabel("Invoice #:", invoice.getInvoiceId());

        // Separator
        Separator sep1 = new Separator();

        // Customer info
        Label custLabel = createDetailLabel("Customer:", rental.getCustomerName());

        // Car info
        Label carLabel = createDetailLabel("Car:", rental.getCarInfo());
        Label carIdLabel = createDetailLabel("Car ID:", rental.getCar().getCarId());
        Label carTypeLabel = createDetailLabel("Car Type:", rental.getCar().getCarType());

        Separator sep2 = new Separator();

        // Date info
        Label startLabel = createDetailLabel("Start Date:", rental.getStartDate().toString());
        Label endLabel = createDetailLabel("End Date:", rental.getEndDate().toString());
        Label daysLabel = createDetailLabel("Number of Days:", String.valueOf(rental.getNumberOfDays()));
        Label ppd = createDetailLabel("Price Per Day:", "$" + String.format("%.2f", rental.getCar().getPricePerDay()));

        Separator sep3 = new Separator();

        // Total and payment
        Label totalLabel = new Label("TOTAL: $" + String.format("%.2f", rental.getTotalPrice()));
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalLabel.setStyle("-fx-text-fill: #2E7D32;");

        Label methodLabel = createDetailLabel("Payment Method:", payment.getPaymentMethod());
        Label statusLabel = createDetailLabel("Payment Status:", payment.isSuccessful() ? "✅ PAID" : "⏳ PENDING");

        invoiceBox.getChildren().addAll(
            invIdLabel, sep1,
            custLabel, carLabel, carIdLabel, carTypeLabel, sep2,
            startLabel, endLabel, daysLabel, ppd, sep3,
            totalLabel, methodLabel, statusLabel
        );

    // ===== Buttons =====
    // بالعربي: أزرار الرجوع والطباعة.
        Button backButton = new Button("🏠 Back to Dashboard");
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(40);
        backButton.setStyle(
            "-fx-background-color: #2196F3; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5px;"
        );

        backButton.setOnAction(e -> {
            CustomerDashboard dashboard = new CustomerDashboard(customer);
            Main.switchScene(dashboard.getScene());
        });

        Button printButton = new Button("🖨️ Print Invoice");
        printButton.setPrefWidth(200);
        printButton.setPrefHeight(40);
        printButton.setStyle(
            "-fx-background-color: #FF9800; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5px;"
        );

        printButton.setOnAction(e -> {
            javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
            if (job != null) {
                // Printer exists, show native print dialog
                if (job.showPrintDialog(scene.getWindow())) {
                    boolean success = job.printPage(invoiceBox);
                    if (success) {
                        job.endJob();
                    }
                }
            } else {
                // No printer found on the computer, fallback to saving as a text file
                javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
                fileChooser.setTitle("Save Invoice As Text");
                fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Text Files", "*.txt"));
                fileChooser.setInitialFileName("Invoice_" + invoice.getInvoiceId() + ".txt");
                java.io.File file = fileChooser.showSaveDialog(scene.getWindow());
                
                if (file != null) {
                    try (java.io.PrintWriter writer = new java.io.PrintWriter(file)) {
                        writer.println(invoice.generateInvoiceText());
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Saved Successfully");
                        a.setHeaderText("No Printer Detected");
                        a.setContentText("Your invoice was saved as a text file instead.");
                        a.showAndWait();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, printButton);

        // ===== Main Layout =====
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(headerLabel, successLabel, invoiceBox, buttonBox);

        scene = new Scene(root, 1100, 750);
    }

    /**
     * Helper method to create a formatted detail label
     * Shows label and value side by side
     * بالعربي: دالة مساعدة لعرض السطر بشكل مرتب.
     */
    private Label createDetailLabel(String labelText, String value) {
        Label label = new Label(labelText + "  " + value);
        label.setFont(Font.font("Arial", 14));
        return label;
    }

    public Scene getScene() {
        return scene;
    }
}
