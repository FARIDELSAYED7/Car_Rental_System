package gui;

import models.*;
import data.*;
import app.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;

/**
 * BookingForm - Form for booking/renting a car
 *
 * Shows car details, lets customer pick dates and payment method,
 * then creates a Rental, Payment, and Invoice.
 */
public class BookingForm {

    private Scene scene;
    private Customer currentCustomer;
    private Car selectedCar;

    /**
     * Constructor - builds the booking form
     */
    public BookingForm(Customer customer, Car car) {
        this.currentCustomer = customer;
        this.selectedCar = car;

        // ===== Title =====
        Label titleLabel = new Label("📅 Book a Car");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: #2E7D32;");

        // ===== Car Info Section =====
        Label carInfoTitle = new Label("Selected Car:");
        carInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label carDetails = new Label(
            "🚗 " + car.toString() + "\n" +
            "Type: " + car.getCarType() + "  |  " +
            "Price: $" + String.format("%.2f", car.getPricePerDay()) + " per day"
        );
        carDetails.setFont(Font.font("Arial", 14));
        carDetails.setStyle("-fx-background-color: #E8F5E9; -fx-padding: 15; -fx-background-radius: 8;");

        // ===== Date Pickers =====
        Label startLabel = new Label("Start Date:");
        startLabel.setFont(Font.font("Arial", 14));
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Select start date");
        startDatePicker.setPrefWidth(250);

        Label endLabel = new Label("End Date:");
        endLabel.setFont(Font.font("Arial", 14));
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Select end date");
        endDatePicker.setPrefWidth(250);

        // ===== Calculated Fields =====
        Label daysLabel = new Label("Number of Days: -");
        daysLabel.setFont(Font.font("Arial", 14));

        Label totalLabel = new Label("Total Price: -");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalLabel.setStyle("-fx-text-fill: #2E7D32;");

        // ===== Payment Method =====
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setFont(Font.font("Arial", 14));
        ComboBox<String> paymentCombo = new ComboBox<>();
        paymentCombo.getItems().addAll("Credit Card", "Debit Card", "Cash", "PayPal");
        paymentCombo.setValue("Credit Card");
        paymentCombo.setPrefWidth(250);

        // ===== Buttons =====
        Button confirmButton = new Button("✅ Confirm Booking");
        confirmButton.setPrefWidth(200);
        confirmButton.setPrefHeight(45);
        confirmButton.setStyle(
            "-fx-background-color: #4CAF50; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5px;"
        );

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(120);
        cancelButton.setPrefHeight(45);
        cancelButton.setStyle(
            "-fx-background-color: #9E9E9E; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5px;"
        );

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // ===== Auto-calculate when dates change =====

        // When start date changes
        startDatePicker.setOnAction(e -> {
            updateCalculation(startDatePicker, endDatePicker, daysLabel, totalLabel);
        });

        // When end date changes
        endDatePicker.setOnAction(e -> {
            updateCalculation(startDatePicker, endDatePicker, daysLabel, totalLabel);
        });

        // ===== Confirm Booking =====
        confirmButton.setOnAction(e -> {
            // Validate dates
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null || endDate == null) {
                statusLabel.setText("Please select both start and end dates!");
                return;
            }

            if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
                statusLabel.setText("End date must be after start date!");
                return;
            }

            if (startDate.isBefore(LocalDate.now())) {
                statusLabel.setText("Start date cannot be in the past!");
                return;
            }

            // Create Rental
            String rentalId = DataStore.generateRentalId();
            Rental rental = new Rental(rentalId, currentCustomer, selectedCar, startDate, endDate);

            // Create Payment
            String paymentId = DataStore.generatePaymentId();
            Payment payment = new Payment(paymentId, rental.getTotalPrice(), paymentCombo.getValue());
            payment.processPayment(); // Simulate payment

            // Create Invoice
            String invoiceId = DataStore.generateInvoiceId();
            Invoice invoice = new Invoice(invoiceId, rental, payment);

            // Save everything to DataStore
            DataStore.rentals.add(rental);
            DataStore.invoices.add(invoice);
            currentCustomer.addRental(rental);

            // Mark car as rented (not available)
            selectedCar.setAvailable(false);
            
            // Save to database
            DatabaseHelper.insertRental(rental);
            DatabaseHelper.insertInvoice(invoice);
            DatabaseHelper.updateCarAvailability(selectedCar.getCarId(), false);

            // Show the invoice screen
            InvoiceScreen invoiceScreen = new InvoiceScreen(invoice, currentCustomer);
            Main.switchScene(invoiceScreen.getScene());
        });

        // Cancel button
        cancelButton.setOnAction(e -> {
            CustomerDashboard dashboard = new CustomerDashboard(currentCustomer);
            Main.switchScene(dashboard.getScene());
        });

        // ===== Layout =====
        GridPane dateGrid = new GridPane();
        dateGrid.setHgap(15);
        dateGrid.setVgap(12);
        dateGrid.setAlignment(Pos.CENTER);
        dateGrid.add(startLabel, 0, 0);
        dateGrid.add(startDatePicker, 1, 0);
        dateGrid.add(endLabel, 0, 1);
        dateGrid.add(endDatePicker, 1, 1);
        dateGrid.add(paymentLabel, 0, 2);
        dateGrid.add(paymentCombo, 1, 2);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(confirmButton, cancelButton);

        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(
            titleLabel, carInfoTitle, carDetails,
            new Separator(),
            dateGrid, daysLabel, totalLabel, statusLabel,
            new Separator(),
            buttonBox
        );

        scene = new Scene(root, 1100, 750);
    }

    /**
     * Update the days and total price calculation when dates change
     */
    private void updateCalculation(DatePicker start, DatePicker end, Label daysLabel, Label totalLabel) {
        if (start.getValue() != null && end.getValue() != null) {
            LocalDate startDate = start.getValue();
            LocalDate endDate = end.getValue();

            if (endDate.isAfter(startDate)) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
                double total = days * selectedCar.getPricePerDay();
                daysLabel.setText("Number of Days: " + days);
                totalLabel.setText("Total Price: $" + String.format("%.2f", total));
            } else {
                daysLabel.setText("Number of Days: Invalid dates");
                totalLabel.setText("Total Price: -");
            }
        }
    }

    public Scene getScene() {
        return scene;
    }
}
