package app;

import data.DataStore;
import gui.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main - The entry point of the Car Rental Management System
 *
 * This class starts the JavaFX application and shows the Login screen.
 * It holds a static reference to the primary Stage so all screens can switch scenes.
 */
public class Main extends Application {

    // Static reference to the main window - all screens use this to switch scenes
    public static Stage primaryStage;

    /**
     * start() is called automatically by JavaFX when the application launches
     */
    @Override
    public void start(Stage stage) {
        // Save the stage reference so other screens can use it
        primaryStage = stage;
        primaryStage.setTitle("Car Rental Management System");

        // Load sample data into our "database"
        DataStore.loadSampleData();

        // Create and show the login screen
        LoginScreen loginScreen = new LoginScreen();
        primaryStage.setScene(loginScreen.getScene());
        primaryStage.setResizable(true);
        primaryStage.setWidth(1100);
        primaryStage.setHeight(750);
        primaryStage.show();
    }

    /**
     * Helper method to switch to a different scene
     * Any screen can call: Main.switchScene(newScene)
     */
    public static void switchScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    /**
     * main() - the very first method that runs
     * launch() starts the JavaFX application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
