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
 * AdminDashboard - The main screen for Admin users
 *
 * Features:
 * - View all cars in a table
 * - Add, Edit, Delete cars
 * - View all rentals
 * - View all customers
 *
 * شرح بالعربي:
 * - دي لوحة تحكم الأدمن.
 * - الأدمن يقدر يدير السيارات ويشوف الحجوزات والعملاء.
 */
public class AdminDashboard {

    private Scene scene;
    private Admin currentAdmin;
    private BorderPane mainLayout;
    private TableView<Car> carTable;
    private ObservableList<Car> carData;

    /**
     * Constructor - builds the admin dashboard UI
     * بالعربي: بنبني كل عناصر شاشة الأدمن هنا.
     */
    public AdminDashboard(Admin admin) {
        this.currentAdmin = admin;

    // ===== Top Bar =====
    // بالعربي: شريط علوي فيه عنوان وترحيب وخروج.
        HBox topBar = createTopBar();

    // ===== Navigation Buttons =====
    // بالعربي: أزرار التنقل بين الأقسام.
        HBox navBar = createNavBar();

    // ===== Main content area (starts with car table) =====
    // بالعربي: المنطقة الرئيسية وتبدأ بعرض السيارات.
        VBox topSection = new VBox(0);
        topSection.getChildren().addAll(topBar, navBar);

        mainLayout = new BorderPane();
        mainLayout.setTop(topSection);
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Show cars view by default
        showCarsView();

        scene = new Scene(mainLayout, 1100, 750);
    }

    /**
     * Create the top bar with title and logout button
     * بالعربي: إنشاء الشريط العلوي.
     */
    private HBox createTopBar() {
        Label titleLabel = new Label("🔧 Admin Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label welcomeLabel = new Label("Welcome, " + currentAdmin.getName());
        welcomeLabel.setStyle("-fx-text-fill: #E3F2FD; -fx-font-size: 14px;");

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
        topBar.setStyle("-fx-background-color: #1565C0;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(titleBox, spacer, logoutButton);
        return topBar;
    }

    /**
     * Create navigation bar with section buttons
     * بالعربي: إنشاء شريط التنقل للأقسام المختلفة.
     */
    private HBox createNavBar() {
        Button carsBtn = new Button("🚗 Manage Cars");
        Button rentalsBtn = new Button("📋 All Rentals");
        Button customersBtn = new Button("👥 Customers");

        String btnStyle = "-fx-background-color: #2196F3; -fx-text-fill: white; " +
            "-fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 10 20; -fx-background-radius: 0;";

        carsBtn.setStyle(btnStyle);
        rentalsBtn.setStyle(btnStyle);
        customersBtn.setStyle(btnStyle);

        // Button click handlers - switch the center content
        carsBtn.setOnAction(e -> showCarsView());
        rentalsBtn.setOnAction(e -> showRentalsView());
        customersBtn.setOnAction(e -> showCustomersView());

        HBox navBar = new HBox(0);
        navBar.setStyle("-fx-background-color: #1976D2;");
        navBar.getChildren().addAll(carsBtn, rentalsBtn, customersBtn);
        return navBar;
    }

    // ====================================================================
    // CARS VIEW - Show all cars with Add, Edit, Delete buttons
    // بالعربي: عرض جدول السيارات والتحكم فيها.
    // ====================================================================
    private void showCarsView() {
        VBox carsView = new VBox(15);
        carsView.setPadding(new Insets(20));

        Label sectionTitle = new Label("Car Inventory");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

    // Action buttons
    // بالعربي: أزرار إضافة/تعديل/حذف.
        Button addBtn = new Button("➕ Add Car");
        Button editBtn = new Button("✏️ Edit Car");
        Button deleteBtn = new Button("🗑️ Delete Car");

        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 16; -fx-background-radius: 5px;");
        editBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 16; -fx-background-radius: 5px;");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 8 16; -fx-background-radius: 5px;");

        HBox buttonBar = new HBox(10);
        buttonBar.getChildren().addAll(addBtn, editBtn, deleteBtn);

    // Create the car table
    // بالعربي: جدول عرض بيانات السيارات.
        carTable = new TableView<>();
        carData = FXCollections.observableArrayList(DataStore.cars);
        carTable.setItems(carData);

    // Define table columns
    // بالعربي: تعريف أعمدة الجدول.
        TableColumn<Car, String> idCol = new TableColumn<>("Car ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("carId"));
        idCol.setPrefWidth(80);

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

        TableColumn<Car, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));
        statusCol.setPrefWidth(100);

        carTable.getColumns().addAll(idCol, brandCol, modelCol, yearCol, priceCol, typeCol, statusCol);
        VBox.setVgrow(carTable, Priority.ALWAYS);

    // ===== Button Event Handlers =====
    // بالعربي: منطق الأزرار.

    // Add Car button
    // بالعربي: فتح شاشة إضافة سيارة.
        addBtn.setOnAction(e -> {
            AddCarForm addForm = new AddCarForm(null); // null = adding new car
            Main.switchScene(addForm.getScene());
        });

    // Edit Car button
    // بالعربي: تعديل السيارة المختارة.
        editBtn.setOnAction(e -> {
            Car selectedCar = carTable.getSelectionModel().getSelectedItem();
            if (selectedCar == null) {
                showAlert("No Selection", "Please select a car to edit.");
                return;
            }
            AddCarForm editForm = new AddCarForm(selectedCar); // pass car = editing
            Main.switchScene(editForm.getScene());
        });

    // Delete Car button
    // بالعربي: حذف السيارة المختارة بعد تأكيد.
        deleteBtn.setOnAction(e -> {
            Car selectedCar = carTable.getSelectionModel().getSelectedItem();
            if (selectedCar == null) {
                showAlert("No Selection", "Please select a car to delete.");
                return;
            }

            // Confirm deletion
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete " + selectedCar.toString() + "?");
            confirm.setContentText("This action cannot be undone.");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                currentAdmin.removeCar(selectedCar.getCarId());
                refreshCarTable();
            }
        });

        carsView.getChildren().addAll(sectionTitle, buttonBar, carTable);
        mainLayout.setCenter(carsView);
    }

    // ====================================================================
    // RENTALS VIEW - Show all rentals in a table
    // بالعربي: عرض كل الحجوزات التي تمت.
    // ====================================================================
    private void showRentalsView() {
        VBox rentalsView = new VBox(15);
        rentalsView.setPadding(new Insets(20));

        Label sectionTitle = new Label("All Rentals");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Rental> rentalTable = new TableView<>();
        ObservableList<Rental> rentalData = FXCollections.observableArrayList(DataStore.rentals);
        rentalTable.setItems(rentalData);

        TableColumn<Rental, String> ridCol = new TableColumn<>("Rental ID");
        ridCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        ridCol.setPrefWidth(100);

        TableColumn<Rental, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custCol.setPrefWidth(150);

        TableColumn<Rental, String> carCol = new TableColumn<>("Car");
        carCol.setCellValueFactory(new PropertyValueFactory<>("carInfo"));
        carCol.setPrefWidth(180);

        TableColumn<Rental, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDateString"));
        startCol.setPrefWidth(120);

        TableColumn<Rental, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDateString"));
        endCol.setPrefWidth(120);

        TableColumn<Rental, Integer> daysCol = new TableColumn<>("Days");
        daysCol.setCellValueFactory(new PropertyValueFactory<>("numberOfDays"));
        daysCol.setPrefWidth(70);

        TableColumn<Rental, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalPriceFormatted"));
        totalCol.setPrefWidth(100);

        rentalTable.getColumns().addAll(ridCol, custCol, carCol, startCol, endCol, daysCol, totalCol);
        VBox.setVgrow(rentalTable, Priority.ALWAYS);

    // Show message if no rentals yet
    // بالعربي: رسالة لو مفيش حجوزات.
        if (DataStore.rentals.isEmpty()) {
            rentalTable.setPlaceholder(new Label("No rentals yet."));
        }

        rentalsView.getChildren().addAll(sectionTitle, rentalTable);
        mainLayout.setCenter(rentalsView);
    }

    // ====================================================================
    // CUSTOMERS VIEW - Show all customers
    // بالعربي: عرض كل العملاء المسجلين.
    // ====================================================================
    private void showCustomersView() {
        VBox customersView = new VBox(15);
        customersView.setPadding(new Insets(20));

        Label sectionTitle = new Label("Registered Customers");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Customer> customerTable = new TableView<>();
        ArrayList<Customer> customers = DataStore.getAllCustomers();
        ObservableList<Customer> customerData = FXCollections.observableArrayList(customers);
        customerTable.setItems(customerData);

        TableColumn<Customer, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        idCol.setPrefWidth(100);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(220);

        TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(150);

        customerTable.getColumns().addAll(idCol, nameCol, emailCol, usernameCol);
        VBox.setVgrow(customerTable, Priority.ALWAYS);

        customersView.getChildren().addAll(sectionTitle, customerTable);
        mainLayout.setCenter(customersView);
    }

    /**
     * Refresh the car table after add/edit/delete
     * بالعربي: تحديث جدول السيارات بعد أي تغيير.
     */
    public void refreshCarTable() {
        if (carTable != null) {
            carData = FXCollections.observableArrayList(DataStore.cars);
            carTable.setItems(carData);
            carTable.refresh();
        }
    }

    /**
     * Show a simple alert dialog
     * بالعربي: نافذة تنبيه بسيطة.
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
