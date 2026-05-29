package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RegisterPage {

    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;

    public Scene getScene() {
        // Title
        Text title = new Text("Create Account");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        // Form Fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle(Styles.textFieldStyle());

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(Styles.textFieldStyle());

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle(Styles.textFieldStyle());

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setStyle(Styles.textFieldStyle());

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");

        // Buttons
        Button registerBtn = new Button("REGISTER");
        registerBtn.setStyle(Styles.buttonStyle());
        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle(Styles.buttonHoverStyle()));
        registerBtn.setOnMouseExited(e -> registerBtn.setStyle(Styles.buttonStyle()));
        registerBtn.setOnAction(e -> handleRegister());

        Button backBtn = new Button("← Back to Login");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_MEDIUM + ";" +
                "-fx-font-size: 14px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> SceneManager.setScene(new LoginPage().getScene()));

        // Layout
        VBox formBox = new VBox(15, usernameField, emailField, passwordField, confirmPasswordField,
                registerBtn, backBtn);
        formBox.setAlignment(Pos.CENTER);
        formBox.setMaxWidth(350);

        VBox mainLayout = new VBox(30, title, formBox, messageLabel);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(40));
        mainLayout.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        return new Scene(mainLayout, 1200, 700);
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill all fields!");
            return;
        }

        if (!password.equals(confirm)) {
            messageLabel.setText("Passwords do not match!");
            return;
        }

        if (password.length() < 4) {
            messageLabel.setText("Password must be at least 4 characters!");
            return;
        }

        if (AuthService.register(username, password, email)) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";");
            messageLabel.setText("Registration successful! Please login.");

            // Kembali ke login setelah 1.5 detik
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            delay.setOnFinished(e -> SceneManager.setScene(new LoginPage().getScene()));
            delay.play();
        } else {
            messageLabel.setText("Username already exists!");
        }
    }
}
