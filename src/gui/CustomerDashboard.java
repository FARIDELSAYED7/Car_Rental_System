package gui;

import models.*;
import data.DataStore;
import app.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;

/**
 * CustomerDashboard - The main screen for Customer users
 *
 * Features:
 * - View available cars
 * - Search cars by brand, model, or type
 * - Filter cars by price range
 * - Book a car
 * - View personal rental history
 */
public class CustomerDashboard {

    private Scene scene;
    private Customer currentCustomer;
    private BorderPane mainLayout;
    private TableView<Car> carTable;

    /**
     * Constructor - builds the customer dashboard UI
     */
    public CustomerDashboard(Customer customer) {
        this.currentCustomer = customer;

        // ===== Top Bar =====
        HBox topBar = createTopBar();

        // ===== Navigation =====
        HBox navBar = createNavBar();

        VBox topSection = new VBox(0);
        topSection.getChildren().addAll(topBar, navBar);

        mainLayout = new BorderPane();
        mainLayout.setTop(topSection);
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Show available cars by default
        showAvailableCarsView();

        scene = new Scene(mainLayout, 1100, 750);
    }

    /**
     * Create the top bar with welcome message and logout
     */
    private HBox createTopBar() {
        Label titleLabel = new Label("🚗 Customer Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label welcomeLabel = new Label("Welcome, " + currentCustomer.getName() + "!");
        welcomeLabel.setStyle("-fx-text-fill: #E8F5E9; -fx-font-size: 14px;");

        VBox titleBox = new VBox(2);
        titleBox.getChildren().addAll(titleLabel, welcomeLabel);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(
            "-fx-background-color: #f44336; -fx-text-fill: white; " +
            "-fx-font-size: 13px; -fx-cursor: hand; -fx-background-radius: 5px; -fx-padding: 8 16;"
        );
        logoutButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            Main.switchScene(loginScreen.getScene());
        });

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15, 25, 15, 25));
        topBar.setStyle("-fx-background-color: #2E7D32;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(titleBox, spacer, logoutButton);
        return topBar;
    }

    /**
     * Create navigation bar
     */
    private HBox createNavBar() {
        Button carsBtn = new Button("🚗 Available Cars");
        Button historyBtn = new Button("📋 My Rentals");
        Button invoicesBtn = new Button("🧾 My Invoices");

        String btnStyle = "-fx-background-color: #43A047; -fx-text-fill: white; " +
            "-fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 0;";
        carsBtn.setStyle(btnStyle);
        historyBtn.setStyle(btnStyle);
        invoicesBtn.setStyle(btnStyle);

        carsBtn.setOnAction(e -> showAvailableCarsView());
        historyBtn.setOnAction(e -> showRentalHistoryView());
        invoicesBtn.setOnAction(e -> showInvoicesView());

        HBox navBar = new HBox(0);
        navBar.setStyle("-fx-background-color: #388E3C;");
        navBar.getChildren().addAll(carsBtn, historyBtn, invoicesBtn);
        return navBar;
    }

    // ====================================================================
    // AVAILABLE CARS VIEW - Search, Filter, and Book cars
    // ====================================================================
    private void showAvailableCarsView() {
        VBox carsView = new VBox(12);
        carsView.setPadding(new Insets(20));

        Label sectionTitle = new Label("Available Cars");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // ----- Search Bar -----
        Label searchLabel = new Label("Search:");
        searchLabel.setFont(Font.font("Arial", 13));
        TextField searchField = new TextField();
        searchField.setPromptText("Search by brand or model...");
        searchField.setPrefWidth(200);

        Label typeLabel = new Label("Type:");
        typeLabel.setFont(Font.font("Arial", 13));
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("All", "Sedan", "SUV", "Sports", "Luxury", "Economy");
        typeCombo.setValue("All");

        Button searchBtn = new Button("🔍 Search");
        searchBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 14; -fx-background-radius: 5px;");

        HBox searchBar = new HBox(10);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.getChildren().addAll(searchLabel, searchField, typeLabel, typeCombo, searchBtn);

        // ----- Price Filter -----
        Label priceLabel = new Label("Price Range:");
        priceLabel.setFont(Font.font("Arial", 13));
        TextField minPrice = new TextField();
        minPrice.setPromptText("Min $");
        minPrice.setPrefWidth(80);
        TextField maxPrice = new TextField();
        maxPrice.setPromptText("Max $");
        maxPrice.setPrefWidth(80);

        Button filterBtn = new Button("Filter");
        filterBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 14; -fx-background-radius: 5px;");

        Button resetBtn = new Button("Reset");
        resetBtn.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 14; -fx-background-radius: 5px;");

        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.getChildren().addAll(priceLabel, minPrice, new Label("-"), maxPrice, filterBtn, resetBtn);

        // ----- Car Table -----
        carTable = new TableView<>();
        refreshAvailableCars(carTable);

        TableColumn<Car, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        idCol.setPrefWidth(70);

        TableColumn<Car, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandCol.setPrefWidth(120);

        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(120);

        TableColumn<Car, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);

        TableColumn<Car, Double> priceCol = new TableColumn<>("Price/Day ($)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        priceCol.setPrefWidth(110);

        TableColumn<Car, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("carType"));
        typeCol.setPrefWidth(100);

        carTable.getColumns().addAll(idCol, brandCol, modelCol, yearCol, priceCol, typeCol);
        VBox.setVgrow(carTable, Priority.ALWAYS);

        // ----- Book Button -----
        Button bookBtn = new Button("📅 Book Selected Car");
        bookBtn.setStyle(
            "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; " +
            "-fx-cursor: hand; -fx-padding: 10 30; -fx-background-radius: 5px;"
        );

        HBox bookBar = new HBox();
        bookBar.setAlignment(Pos.CENTER);
        bookBar.setPadding(new Insets(5));
        bookBar.getChildren().add(bookBtn);

        // ===== Event Handlers =====

        // Search button
        searchBtn.setOnAction(e -> {
            String query = searchField.getText().trim().toLowerCase();
            String type = typeCombo.getValue();
            ArrayList<Car> results = new ArrayList<>();

            for (int i = 0; i < DataStore.cars.size(); i++) {
                Car car = DataStore.cars.get(i);
                if (!car.isAvailable()) {
                    continue; // Skip rented cars
                }

                boolean matchesQuery = query.isEmpty() ||
                    car.getBrand().toLowerCase().contains(query) ||
                    car.getModel().toLowerCase().contains(query);

                boolean matchesType = type.equals("All") || car.getCarType().equals(type);

                if (matchesQuery && matchesType) {
                    results.add(car);
                }
            }

            carTable.setItems(FXCollections.observableArrayList(results));
        });

        // Filter by price
        filterBtn.setOnAction(e -> {
            try {
                double min = minPrice.getText().isEmpty() ? 0 : Double.parseDouble(minPrice.getText());
                double max = maxPrice.getText().isEmpty() ? 99999 : Double.parseDouble(maxPrice.getText());

                ArrayList<Car> results = new ArrayList<>();
                for (int i = 0; i < DataStore.cars.size(); i++) {
                    Car car = DataStore.cars.get(i);
                    if (car.isAvailable() && car.getPricePerDay() >= min && car.getPricePerDay() <= max) {
                        results.add(car);
                    }
                }
                carTable.setItems(FXCollections.observableArrayList(results));
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for price range.");
            }
        });

        // Reset filters
        resetBtn.setOnAction(e -> {
            searchField.clear();
            typeCombo.setValue("All");
            minPrice.clear();
            maxPrice.clear();
            refreshAvailableCars(carTable);
        });

        // Book button
        bookBtn.setOnAction(e -> {
            Car selectedCar = carTable.getSelectionModel().getSelectedItem();
            if (selectedCar == null) {
                showAlert("No Selection", "Please select a car to book.");
                return;
            }
            if (!selectedCar.isAvailable()) {
                showAlert("Not Available", "This car is currently rented.");
                return;
            }
            BookingForm bookingForm = new BookingForm(currentCustomer, selectedCar);
            Main.switchScene(bookingForm.getScene());
        });

        carsView.getChildren().addAll(sectionTitle, searchBar, filterBar, carTable, bookBar);
        mainLayout.setCenter(carsView);
    }

    // ====================================================================
    // RENTAL HISTORY VIEW - Show customer's past rentals
    // ====================================================================
    private void showRentalHistoryView() {
        VBox historyView = new VBox(15);
        historyView.setPadding(new Insets(20));

        Label sectionTitle = new Label("My Rental History");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Rental> historyTable = new TableView<>();
        ObservableList<Rental> historyData = FXCollections.observableArrayList(currentCustomer.getRentalHistory());
        historyTable.setItems(historyData);

        TableColumn<Rental, String> ridCol = new TableColumn<>("Rental ID");
        ridCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        ridCol.setPrefWidth(100);

        TableColumn<Rental, String> carCol = new TableColumn<>("Car");
        carCol.setCellValueFactory(new PropertyValueFactory<>("carInfo"));
        carCol.setPrefWidth(200);

        TableColumn<Rental, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDateString"));
        startCol.setPrefWidth(130);

        TableColumn<Rental, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDateString"));
        endCol.setPrefWidth(130);

        TableColumn<Rental, Integer> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));
        daysCol.setPrefWidth(70);

        TableColumn<Rental, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPriceFormatted"));
        totalCol.setPrefWidth(100);

        historyTable.getColumns().addAll(ridCol, carCol, startCol, endCol, daysCol, totalCol);
        VBox.setVgrow(historyTable, Priority.ALWAYS);

        if (currentCustomer.getRentalHistory().isEmpty()) {
            historyTable.setPlaceholder(new Label("You have no rental history yet."));
        }

        historyView.getChildren().addAll(sectionTitle, historyTable);
        mainLayout.setCenter(historyView);
    }

    // ====================================================================
    // INVOICES VIEW - Show customer's invoices
    // ====================================================================
    private void showInvoicesView() {
        VBox invoicesView = new VBox(15);
        invoicesView.setPadding(new Insets(20));

        Label sectionTitle = new Label("My Invoices");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Invoice> invoiceTable = new TableView<>();

        // Filter invoices for this customer
        ArrayList<Invoice> myInvoices = new ArrayList<>();
        for (int i = 0; i < DataStore.invoices.size(); i++) {
            Invoice inv = DataStore.invoices.get(i);
            if (inv.getRental().getCustomer().getUserId().equals(currentCustomer.getUserId())) {
                myInvoices.add(inv);
            }
        }

        ObservableList<Invoice> invoiceData = FXCollections.observableArrayList(myInvoices);
        invoiceTable.setItems(invoiceData);

        TableColumn<Invoice, String> invIdCol = new TableColumn<>("Invoice ID");
        invIdCol.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        invIdCol.setPrefWidth(120);

        TableColumn<Invoice, String> carCol = new TableColumn<>("Car");
        carCol.setCellValueFactory(new PropertyValueFactory<>("carInfo"));
        carCol.setPrefWidth(200);

        TableColumn<Invoice, String> datesCol = new TableColumn<>("Dates");
        datesCol.setCellValueFactory(new PropertyValueFactory<>("datesInfo"));
        datesCol.setPrefWidth(200);

        TableColumn<Invoice, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPriceFormatted"));
        totalCol.setPrefWidth(100);

        TableColumn<Invoice, String> methodCol = new TableColumn<>("Payment");
        methodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        methodCol.setPrefWidth(120);

        TableColumn<Invoice, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        statusCol.setPrefWidth(80);

        invoiceTable.getColumns().addAll(invIdCol, carCol, datesCol, totalCol, methodCol, statusCol);
        VBox.setVgrow(invoiceTable, Priority.ALWAYS);

        // View invoice detail button
        Button viewBtn = new Button("📄 View Invoice Details");
        viewBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 20; -fx-background-radius: 5px;");

        viewBtn.setOnAction(e -> {
            Invoice selected = invoiceTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select an invoice to view.");
                return;
            }
            InvoiceScreen invoiceScreen = new InvoiceScreen(selected, currentCustomer);
            Main.switchScene(invoiceScreen.getScene());
        });

        if (myInvoices.isEmpty()) {
            invoiceTable.setPlaceholder(new Label("No invoices yet."));
        }

        HBox btnBox = new HBox();
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().add(viewBtn);

        invoicesView.getChildren().addAll(sectionTitle, invoiceTable, btnBox);
        mainLayout.setCenter(invoicesView);
    }

    /**
     * Refresh the available cars table
     */
    private void refreshAvailableCars(TableView<Car> table) {
        ArrayList<Car> available = DataStore.getAvailableCars();
        table.setItems(FXCollections.observableArrayList(available));
    }

    /**
     * Show a simple alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}
