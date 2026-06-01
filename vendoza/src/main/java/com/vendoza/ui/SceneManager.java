package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Stack;

public class SceneManager {

    private static Stage primaryStage;
    private static final Stack<Scene> history = new Stack<>();

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Vendoza - Fashion Aesthetic");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
    }

    public static void setScene(Scene scene) {
        if (primaryStage.getScene() != null) {
            history.push(primaryStage.getScene());
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void goBack() {
        if (!history.isEmpty()) {
            primaryStage.setScene(history.pop());
            primaryStage.show();
        } else {
            setScene(new HomePage().getScene());
        }
    }

    public static void showHomePage() {
        history.clear();
        if (AuthService.isLoggedIn() && AuthService.isAdmin()) {
            setScene(new AdminHomePage().getScene());
        } else {
            setScene(new HomePage().getScene());
        }
    }

    public static void showLoginPage() {
        setScene(new LoginPage().getScene());
    }

    public static void showRegisterPage() {
        setScene(new RegisterPage().getScene());
    }

    public static void showCartPage() {
        setScene(new CartPage().getScene());
    }

    public static void showProfilePage() {
        setScene(new ProfilePage().getScene());
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
