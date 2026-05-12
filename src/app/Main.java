package app;
// Hello SUT
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
 *
 * شرح بالعربي (مبسّط جدًا):
 * - هذه هي نقطة البداية للتطبيق بالكامل.
 * - هنا بنجهّز نافذة البرنامج الأساسية (Stage) ونعرض أول شاشة.
 * - أي شاشة أخرى لو عايزة تغيّر الواجهة، بتستخدم الدالة switchScene().
 */
public class Main extends Application {

    // Static reference to the main window - all screens use this to switch scenes
    // بالعربي: ده مرجع ثابت للنافذة الرئيسية علشان كل الشاشات تقدر تغيّر المشهد بسهولة.
    public static Stage primaryStage;

    /**
     * start() is called automatically by JavaFX when the application launches
     * بالعربي: JavaFX بتنادي الدالة دي تلقائيًا عند تشغيل البرنامج.
     */
    @Override
    public void start(Stage stage) {
    // Save the stage reference so other screens can use it
    // حفظ النافذة الرئيسية علشان باقي الشاشات تستخدمها.
        primaryStage = stage;
        primaryStage.setTitle("Car Rental Management System");

    // Load sample data into our "database"
    // تحميل بيانات تجريبية من قاعدة البيانات.
        DataStore.loadSampleData();

    // Create and show the login screen
    // عرض شاشة تسجيل الدخول.
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
     * بالعربي: دالة مساعدة لتبديل الشاشة الحالية بشاشة جديدة.
     */
    public static void switchScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    /**
     * main() - the very first method that runs
     * launch() starts the JavaFX application
     * بالعربي: أول دالة بيتنفّذها البرنامج، وهي اللي بتشغّل JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
