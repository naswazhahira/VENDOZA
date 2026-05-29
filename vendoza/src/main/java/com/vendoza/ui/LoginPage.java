package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginPage {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public Scene getScene() {
        // Title
        Text title = new Text("VENDOZA");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        Text subtitle = new Text("Login to Your Account");
        subtitle.setStyle("-fx-font-size: 16px; -fx-fill: " + Styles.TEXT_LIGHT + ";");

        // Form Fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle(Styles.textFieldStyle());

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(Styles.textFieldStyle());

        // Message Label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");

        // Buttons
        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle(Styles.buttonStyle());
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(Styles.buttonHoverStyle()));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(Styles.buttonStyle()));
        loginBtn.setOnAction(e -> handleLogin());

        Button registerBtn = new Button("Create New Account");
        registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_MEDIUM + ";" +
                "-fx-font-size: 14px; -fx-underline: true; -fx-cursor: hand;");
        registerBtn.setOnAction(e -> goToRegister());

        // Layout Form
        VBox formBox = new VBox(15, usernameField, passwordField, loginBtn, registerBtn);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(350);

        // Main Layout
        VBox mainLayout = new VBox(30, title, subtitle, formBox, messageLabel);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        return new Scene(mainLayout, 1200, 700);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill all fields!");
            return;
        }

        if (AuthService.login(username, password)) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";");
            messageLabel.setText("Login successful! Redirecting...");

            // Pindah ke Home Page setelah 1 detik
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
            delay.setOnFinished(e -> SceneManager.setScene(new HomePage().getScene()));
            delay.play();
        } else {
            messageLabel.setText("Invalid username or password!");
        }
    }

    private void goToRegister() {
        SceneManager.setScene(new RegisterPage().getScene());
    }
}
