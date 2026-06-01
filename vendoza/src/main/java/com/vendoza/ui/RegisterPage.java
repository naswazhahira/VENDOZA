package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class RegisterPage {

    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
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

        // TEKAN ENTER UNTUK REGISTER
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleRegister();
            }
        });

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
        content.setPadding(new Insets(40, 80, 60, 80));
        content.setStyle("-fx-background-color: #ebddc3;");
        content.setAlignment(Pos.CENTER);

        VBox registerForm = createRegisterForm();
        content.getChildren().add(registerForm);
        return content;
    }

    private VBox createRegisterForm() {
        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(50, 40, 80, 40));

        // Card untuk form
        VBox card = new VBox(20);
        card.setStyle(Styles.cardStyle());
        card.setMaxWidth(450);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));

        // Icon dan Title
        Label iconLabel = new Label("📝");
        iconLabel.setStyle("-fx-font-size: 60px;");

        Text title = new Text("Create Account");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        Text subtitle = new Text("Join us and start shopping!");
        subtitle.setStyle("-fx-font-size: 14px; -fx-fill: " + Styles.TEXT_LIGHT + ";");

        // Username Field
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.setStyle(Styles.textFieldStyle());
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Email Field
        VBox emailBox = new VBox(5);
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        emailField = new TextField();
        emailField.setPromptText("your@email.com");
        emailField.setStyle(Styles.textFieldStyle());
        emailBox.getChildren().addAll(emailLabel, emailField);

        // Password Field
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        passwordField = new PasswordField();
        passwordField.setPromptText("Minimum 4 characters");
        passwordField.setStyle(Styles.textFieldStyle());
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Confirm Password Field
        VBox confirmBox = new VBox(5);
        Label confirmLabel = new Label("Confirm Password");
        confirmLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter your password");
        confirmPasswordField.setStyle(Styles.textFieldStyle());
        confirmBox.getChildren().addAll(confirmLabel, confirmPasswordField);

        // Message Label
        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";" +
                "-fx-font-size: 12px;");

        // Register Button
        Button registerBtn = new Button("REGISTER");
        registerBtn.setStyle(Styles.buttonStyle());
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setOnMouseEntered(e -> {
            registerBtn.setStyle(Styles.buttonHoverStyle());
            registerBtn.setScaleX(1.02);
            registerBtn.setScaleY(1.02);
        });
        registerBtn.setOnMouseExited(e -> {
            registerBtn.setStyle(Styles.buttonStyle());
            registerBtn.setScaleX(1);
            registerBtn.setScaleY(1);
        });
        registerBtn.setOnAction(e -> handleRegister());

        // Login Link
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        Label haveAccountLabel = new Label("Already have an account?");
        haveAccountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        Hyperlink loginLink = new Hyperlink("Login here");
        loginLink.setStyle("-fx-text-fill: " + Styles.GOLD + "; -fx-font-size: 13px; -fx-font-weight: bold;");
        loginLink.setOnAction(e -> SceneManager.setScene(new LoginPage().getScene()));

        loginBox.getChildren().addAll(haveAccountLabel, loginLink);

        // Tambahkan semua ke card
        card.getChildren().addAll(iconLabel, title, subtitle,
                usernameBox, emailBox, passwordBox, confirmBox,
                registerBtn, messageLabel, loginBox);

        formContainer.getChildren().add(card);
        return formContainer;
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        // Validasi
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            messageLabel.setText("❌ Please fill all fields!");
            return;
        }

        if (!password.equals(confirm)) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            messageLabel.setText("❌ Passwords do not match!");
            return;
        }

        if (password.length() < 4) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            messageLabel.setText("❌ Password must be at least 4 characters!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            messageLabel.setText("❌ Please enter a valid email address!");
            return;
        }

        if (AuthService.register(username, password, email)) {
            messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";");
            messageLabel.setText("✅ Registration successful! Please login.");

            // Kembali ke login setelah 1.5 detik
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            delay.setOnFinished(e -> SceneManager.setScene(new LoginPage().getScene()));
            delay.play();
        } else {
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            messageLabel.setText("❌ Username already exists!");
        }
    }
}
