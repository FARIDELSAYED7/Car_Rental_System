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

/**
 * AddCarForm - Form screen for adding a new car or editing an existing car
 *
 passed, the form is empty (Add mode).
 *kd sakdjisanjidjsadbnsajbid
 * شرح بالعربي:
 * - نفس الشاشة تستخدم للإضافة والتعديل.
 * - لو دخلناها بـ Car موجودة -> تعديل.
 * - لو دخلناها بـ null -> إضافة جديدة.
 */
public class AddCarForm {

    private Scene scene;
    private Car existingCar; // null = adding new, not null = editing

    /**
     * Constructor - builds the add/edit car form
     * @param car - null for new car, or existing car to edit
     * بالعربي: هنا بنبني كل عناصر الفورم.
     */
    public AddCarForm(Car car) {
        this.existingCar = car;
        boolean isEditing = (car != null);

    // ===== Title =====
    // بالعربي: العنوان يتغير حسب وضع الإضافة أو التعديل.
        Label titleLabel = new Label(isEditing ? "✏️ Edit Car" : "➕ Add New Car");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setStyle("-fx-text-fill: #1565C0;");

    // ===== Form Fields =====
    // بالعربي: خانات الإدخال الخاصة بالسيارة.
        Label brandLabel = new Label("Brand:");
        brandLabel.setFont(Font.font("Arial", 14));
        TextField brandField = new TextField();
        brandField.setPromptText("e.g., Toyota");
        brandField.setPrefWidth(300);
        brandField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Label modelLabel = new Label("Model:");
        modelLabel.setFont(Font.font("Arial", 14));
        TextField modelField = new TextField();
        modelField.setPromptText("e.g., Camry");
        modelField.setPrefWidth(300);
        modelField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Label yearLabel = new Label("Year:");
        yearLabel.setFont(Font.font("Arial", 14));
        TextField yearField = new TextField();
        yearField.setPromptText("e.g., 2024");
        yearField.setPrefWidth(300);
        yearField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Label priceLabel = new Label("Price Per Day ($):");
        priceLabel.setFont(Font.font("Arial", 14));
        TextField priceField = new TextField();
        priceField.setPromptText("e.g., 50.00");
        priceField.setPrefWidth(300);
        priceField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        Label typeLabel = new Label("Car Type:");
        typeLabel.setFont(Font.font("Arial", 14));
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Sedan", "SUV", "Sports", "Luxury", "Economy");
        typeCombo.setValue("Sedan");
        typeCombo.setPrefWidth(300);
        typeCombo.setStyle("-fx-font-size: 14px;");

    // If editing, fill in existing data
    // بالعربي: لو تعديل، نملأ الحقول بالبيانات القديمة.
        if (isEditing) {
            brandField.setText(car.getBrand());
            modelField.setText(car.getModel());
            yearField.setText(String.valueOf(car.getYear()));
            priceField.setText(String.valueOf(car.getPricePerDay()));
            typeCombo.setValue(car.getCarType());
        }

    // ===== Buttons =====
    // بالعربي: أزرار الحفظ والإلغاء.
        Button saveButton = new Button(isEditing ? "Save Changes" : "Add Car");
        saveButton.setPrefWidth(140);
        saveButton.setPrefHeight(40);
        saveButton.setStyle(
            "-fx-background-color: #4CAF50; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5px;"
        );

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(140);
        cancelButton.setPrefHeight(40);
        cancelButton.setStyle(
            "-fx-background-color: #9E9E9E; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5px;"
        );

    // Status label for validation errors
    // بالعربي: يظهر رسائل الأخطاء للمستخدم.
        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

    // ===== Event Handlers =====
    // بالعربي: منطق الضغط على الأزرار.

        // Save button
        saveButton.setOnAction(e -> {
            // Validate inputs
            // بالعربي: التحقق من المدخلات.
            String brand = brandField.getText().trim();
            String model = modelField.getText().trim();
            String yearText = yearField.getText().trim();
            String priceText = priceField.getText().trim();
            String type = typeCombo.getValue();

            if (brand.isEmpty() || model.isEmpty() || yearText.isEmpty() || priceText.isEmpty()) {
                statusLabel.setText("Please fill in all fields!");
                return;
            }

            // Validate year
            // بالعربي: السنة لازم تكون رقم وفي نطاق منطقي.
            int year;
            try {
                year = Integer.parseInt(yearText);
                if (year < 1990 || year > 2030) {
                    statusLabel.setText("Year must be between 1990 and 2030.");
                    return;
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Please enter a valid year (number).");
                return;
            }

            // Validate price
            // بالعربي: السعر لازم يكون رقم أكبر من صفر.
            double price;
            try {
                price = Double.parseDouble(priceText);
                if (price <= 0) {
                    statusLabel.setText("Price must be greater than 0.");
                    return;
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Please enter a valid price (number).");
                return;
            }

            if (isEditing) {
                // Update existing car
                // بالعربي: تعديل السيارة الموجودة.
                existingCar.setBrand(brand);
                existingCar.setModel(model);
                existingCar.setYear(year);
                existingCar.setPricePerDay(price);
                existingCar.setCarType(type);
                
                DatabaseHelper.updateCar(existingCar);

                showSuccessAlert("Car Updated", "Car details updated successfully!");
            } else {
                // Create new car
                // بالعربي: إنشاء سيارة جديدة وإضافتها.
                String carId = DataStore.generateCarId();
                Car newCar = new Car(carId, brand, model, year, price, true, type);
                DataStore.cars.add(newCar);
                
                DatabaseHelper.insertCar(newCar);

                showSuccessAlert("Car Added", "New car '" + brand + " " + model + "' added successfully!");
            }

            // Go back to admin dashboard (find current admin from DataStore)
            // بالعربي: نرجع للوحة الأدمن.
            goBackToAdmin();
        });

    // Cancel button
    // بالعربي: إلغاء بدون حفظ.
        cancelButton.setOnAction(e -> goBackToAdmin());

    // ===== Layout =====
    // بالعربي: ترتيب العناصر على الشاشة.
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(12);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(brandLabel, 0, 0);
        formGrid.add(brandField, 1, 0);
        formGrid.add(modelLabel, 0, 1);
        formGrid.add(modelField, 1, 1);
        formGrid.add(yearLabel, 0, 2);
        formGrid.add(yearField, 1, 2);
        formGrid.add(priceLabel, 0, 3);
        formGrid.add(priceField, 1, 3);
        formGrid.add(typeLabel, 0, 4);
        formGrid.add(typeCombo, 1, 4);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(titleLabel, formGrid, statusLabel, buttonBox);

        scene = new Scene(root, 1100, 750);
    }

    /**
     * Navigate back to Admin Dashboard
     * بالعربي: العودة للوحة الأدمن.
     */
    private void goBackToAdmin() {
        // Find the first admin in DataStore (simplest approach)
        for (int i = 0; i < DataStore.users.size(); i++) {
            if (DataStore.users.get(i) instanceof Admin) {
                Admin admin = (Admin) DataStore.users.get(i);
                AdminDashboard dashboard = new AdminDashboard(admin);
                Main.switchScene(dashboard.getScene());
                return;
            }
        }
    }

    /**
     * Show a success alert
     * بالعربي: رسالة نجاح للمستخدم.
     */
    private void showSuccessAlert(String title, String message) {
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
