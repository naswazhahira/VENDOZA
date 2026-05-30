package com.vendoza.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static Scene currentScene;

    private static HomePage homePage;
    private static LoginPage loginPage;
    private static RegisterPage registerPage;

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Vendoza - Fashion Aesthetic");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);

        // Inisialisasi semua halaman
        homePage = new HomePage();
        loginPage = new LoginPage();
        registerPage = new RegisterPage();
    }

    public static void setScene(Scene scene) {
        currentScene = scene;
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showHomePage() {
        HomePage homePage = new HomePage();
        setScene(homePage.getScene());
    }

    public static void showLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        setScene(loginPage.getScene());
    }

    public static void showRegisterPage() {
        if (registerPage == null) {
            registerPage = new RegisterPage();
        }
        setScene(registerPage.getScene());
    }

    public static void showCartPage() {
        CartPage cartPage = new CartPage();
        setScene(cartPage.getScene());
    }

    public static void showProfilePage() {
        ProfilePage profilePage = new ProfilePage();
        setScene(profilePage.getScene());
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
