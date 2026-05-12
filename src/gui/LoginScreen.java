package gui;

import models.*;
import data.DataStore;
import app.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * LoginScreen - The first screen users see
 *
 * Users enter their username and password to log in.
 * Based on credentials, they are sent to Admin or Customer dashboard.
 *
 * شرح بالعربي:
 * - دي أول شاشة بتظهر للمستخدم.
 * - لو البيانات صح، بنوجّه المستخدم حسب دوره (Admin أو Customer).
 */
public class LoginScreen {

    private Scene scene;

    /**
     * Constructor - builds the entire login screen UI
     * بالعربي: بنبني كل عناصر الواجهة هنا (Labels, Fields, Buttons).
     */
    public LoginScreen() {
    // ===== Create UI Components =====
    // بالعربي: هنا بنعمل كل عناصر الواجهة.

        // Title label
        Label titleLabel = new Label("🚗 Car Rental Management System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #2196F3;");

        // Subtitle
        Label subtitleLabel = new Label("Please log in to continue");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #666666;");

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", 14));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefWidth(280);
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", 14));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(280);
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 8px;");

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(280);
        loginButton.setPrefHeight(40);
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        loginButton.setStyle(
            "-fx-background-color: #2196F3; -fx-text-fill: white; " +
            "-fx-cursor: hand; -fx-background-radius: 5px;"
        );

    // Status label (shows error messages)
    // بالعربي: مكان الرسائل لما يحصل خطأ.
        Label statusLabel = new Label("");
        statusLabel.setFont(Font.font("Arial", 12));
        statusLabel.setStyle("-fx-text-fill: red;");

        // ===== Sample credentials info =====
        Label infoLabel = new Label("Sample Logins:");
        infoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        infoLabel.setStyle("-fx-text-fill: #999999;");

        Label adminInfo = new Label("Admin: admin1 / admin123");
        adminInfo.setStyle("-fx-text-fill: #999999; -fx-font-size: 11px;");

        Label customerInfo = new Label("Customer: customer1 / cust123");
        customerInfo.setStyle("-fx-text-fill: #999999; -fx-font-size: 11px;");

    // ===== EVENT HANDLING =====
    // بالعربي: التعامل مع أحداث المستخدم.

        // What happens when the Login button is clicked
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Check if fields are empty
            // بالعربي: تحقق إن الحقول مش فاضية.
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both username and password!");
                return;
            }

            // Try to find the user in our DataStore
            // بالعربي: نبحث في البيانات عن المستخدم.
            User user = DataStore.findUser(username, password);

            if (user == null) {
                // User not found - show error
                // بالعربي: بيانات غير صحيحة.
                statusLabel.setText("Invalid username or password!");
            } else if (user instanceof Admin) {
                // User is an Admin - go to Admin Dashboard
                // بالعربي: لو أدمن نفتح لوحة الأدمن.
                AdminDashboard adminDashboard = new AdminDashboard((Admin) user);
                Main.switchScene(adminDashboard.getScene());
            } else if (user instanceof Customer) {
                // User is a Customer - go to Customer Dashboard
                // بالعربي: لو عميل نفتح لوحة العميل.
                CustomerDashboard customerDashboard = new CustomerDashboard((Customer) user);
                Main.switchScene(customerDashboard.getScene());
            }
        });

    // Allow pressing Enter to login
    // بالعربي: لو المستخدم ضغط Enter ندخله.
        passwordField.setOnAction(e -> loginButton.fire());

    // ===== Layout =====
    // بالعربي: ترتيب عناصر الواجهة داخل الحاويات.
        VBox formBox = new VBox(12);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            loginButton,
            statusLabel
        );

        // Separator line
        Separator separator = new Separator();
        separator.setPrefWidth(280);

        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getChildren().addAll(infoLabel, adminInfo, customerInfo);

    // Main container
    // بالعربي: الحاوية الرئيسية للشاشة.
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #f5f5f5;");
        root.getChildren().addAll(titleLabel, subtitleLabel, formBox, separator, infoBox);

        // Create the scene
        scene = new Scene(root, 1100, 750);
    }

    /**
     * Returns the scene to be displayed
     * بالعربي: بنرجّع الـ Scene علشان يتم عرضها.
     */
    public Scene getScene() {
        return scene;
    }
}
