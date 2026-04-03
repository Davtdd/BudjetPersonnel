package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;
    private Scene loginScene;
    private Scene dashboardScene;

    private DashboardView dashboardView;
    private LoginView loginView;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Create views and pass MainApp reference
        dashboardView = new DashboardView(this);
        loginView = new LoginView(this, dashboardView);

        loginScene = new Scene(loginView, 550, 600);
        dashboardScene = new Scene(dashboardView, 550, 600);

        showLogin();

        stage.setTitle("Gestion Budget avec MySQL");
        stage.show();
    }

    public void showLogin() {
        primaryStage.setScene(loginScene);
    }

    public void showDashboard() {
        primaryStage.setScene(dashboardScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}