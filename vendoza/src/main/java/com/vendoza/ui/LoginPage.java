package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class LoginPage {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public Scene getScene() {
        // Root Layout dengan BorderPane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        // === HEADER DENGAN BACK BUTTON ===
        HBox header = createHeader();
        root.setTop(header);

        // === MAIN CONTENT (FORM LOGIN) ===
        VBox centerContent = createLoginForm();
        root.setCenter(centerContent);

        return new Scene(root, 1200, 700);
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(20, 40, 20, 40));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        // BACK BUTTON - Kembali ke HomePage
        Button backBtn = new Button("← Back to Home");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 14px; -fx-cursor: hand; -fx-font-weight: bold;");

        backBtn.setOnMouseEntered(e -> {
            backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                    "-fx-font-size: 14px; -fx-cursor: hand; -fx-font-weight: bold;");
            backBtn.setScaleX(1.05);
            backBtn.setScaleY(1.05);
        });

        backBtn.setOnMouseExited(e -> {
            backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                    "-fx-font-size: 14px; -fx-cursor: hand; -fx-font-weight: bold;");
            backBtn.setScaleX(1);
            backBtn.setScaleY(1);
        });

        backBtn.setOnAction(e -> SceneManager.showHomePage());

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Logo
        Label logo = new Label("👗 VENDOZA");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        header.getChildren().addAll(backBtn, spacer, logo);
        return header;
    }

    private VBox createLoginForm() {
        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(50));

        // Card untuk form
        VBox card = new VBox(20);
        card.setStyle(Styles.cardStyle());
        card.setMaxWidth(450);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));

        // Icon dan Title
        Label iconLabel = new Label("👋");
        iconLabel.setStyle("-fx-font-size: 60px;");

        Text title = new Text("Welcome Back!");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        Text subtitle = new Text("Please login to your account");
        subtitle.setStyle("-fx-font-size: 14px; -fx-fill: " + Styles.TEXT_LIGHT + ";");

        // Username Field
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle(Styles.textFieldStyle());
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password Field
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(Styles.textFieldStyle());
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Message Label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";" +
                "-fx-font-size: 12px;");

        // Login Button
        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle(Styles.buttonStyle());
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setStyle(Styles.buttonHoverStyle());
            loginBtn.setScaleX(1.02);
            loginBtn.setScaleY(1.02);
        });
        loginBtn.setOnMouseExited(e -> {
            loginBtn.setStyle(Styles.buttonStyle());
            loginBtn.setScaleX(1);
            loginBtn.setScaleY(1);
        });
        loginBtn.setOnAction(e -> handleLogin());

        // Register Link
        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        Hyperlink registerLink = new Hyperlink("Register here");
        registerLink.setStyle("-fx-text-fill: " + Styles.GOLD + "; -fx-font-size: 13px; -fx-font-weight: bold;");
        registerLink.setOnAction(e -> goToRegister());

        registerBox.getChildren().addAll(noAccountLabel, registerLink);

        // Tambahkan semua ke card
        card.getChildren().addAll(iconLabel, title, subtitle,
                usernameBox, passwordBox, loginBtn, messageLabel, registerBox);

        formContainer.getChildren().add(card);
        return formContainer;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("❌ Please fill all fields!");
            return;
        }

        if (AuthService.login(username, password)) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";");

            // Cek apakah user adalah admin
            if (AuthService.isAdmin()) {
                messageLabel.setText("✅ Welcome Admin! Redirecting to Admin Panel...");
            } else {
                messageLabel.setText("✅ Login successful! Redirecting...");
            }

            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
            delay.setOnFinished(e -> {
                // Redirect ke halaman yang sesuai
                if (AuthService.isAdmin()) {
                    SceneManager.setScene(new AdminDashboardPage().getScene());
                } else {
                    SceneManager.showHomePage();
                }
            });
            delay.play();
        } else {
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            messageLabel.setText("❌ Invalid username or password!");
        }
    }

    private void goToRegister() {
        SceneManager.setScene(new RegisterPage().getScene());
    }
}
