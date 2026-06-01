package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class LoginPage {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public Scene getScene() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        HBox navBar = createNavBar();

        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ebddc3; -fx-background: #ebddc3;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox mainLayout = new VBox(navBar, scrollPane);
        mainLayout.setStyle("-fx-background-color: #ebddc3;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, screenWidth, screenHeight);

        // CSS dengan pengecekan null
        java.net.URL cssUrl = getClass().getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(30);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(16, 50, 16, 22));
        navBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );

        Button backBtn = new Button("❮");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 8 0 0;");

        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #D4A853;" +
                        "-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Georgia'; " +
                        "-fx-padding: 0 10 0 0; -fx-cursor: hand;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #3E2723;" +
                        "-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Georgia'; " +
                        "-fx-padding: 0 10 0 0; -fx-cursor: hand;"
        ));
        backBtn.setOnAction(e -> SceneManager.showHomePage());

        Label logo = new Label("VENDOZA");
        logo.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Georgia';"
        );
        
        HBox logoGroup = new HBox(4);
        logoGroup.setAlignment(Pos.CENTER_LEFT);
        logoGroup.getChildren().addAll(backBtn, logo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        navBar.getChildren().addAll(logoGroup, spacer);
        return navBar;
    }

    private VBox createContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(60, 80, 60, 80));
        content.setStyle("-fx-background-color: #ebddc3;");
        content.setAlignment(Pos.CENTER);

        VBox loginForm = createLoginForm();
        content.getChildren().add(loginForm);
        return content;
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
        card.setPadding(new Insets(60, 40, 40, 40));

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

        // Memicu fungsi login otomatis saat menekan tombol Enter di keyboard
        loginBtn.setDefaultButton(true);

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
        card.getChildren().addAll(title, subtitle,
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
