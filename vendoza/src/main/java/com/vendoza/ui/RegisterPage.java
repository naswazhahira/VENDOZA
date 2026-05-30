package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class RegisterPage {

    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label messageLabel;

    public Scene getScene() {
        // Root Layout dengan BorderPane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #E8DCD0;");

        // === HEADER DENGAN BACK BUTTON ===
        HBox header = createHeader();
        root.setTop(header);

        // === MAIN CONTENT (FORM REGISTER) DENGAN SCROLLPANE ===
        VBox centerContent = createRegisterForm();

        // TAMBAHKAN SCROLLPANE AGAR BISA DISCROLL
        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(15, 40, 15, 40));
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

        // Navigation Buttons (seperti di HomePage)
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        Button homeBtn = createNavButton("🏠 Home", false);
        Button searchBtn = createNavButton("🔍 Search", false);
        Button cartBtn = createNavButton("🛒 Cart", false);
        Button profileBtn = createNavButton("👤 Profile", false);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));
        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                SceneManager.setScene(new CartPage().getScene());
            } else {
                showLoginAlert();
            }
        });
        profileBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                SceneManager.setScene(new ProfilePage().getScene());
            } else {
                showLoginAlert();
            }
        });

        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Label logo = new Label("👗 VENDOZA");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        header.getChildren().addAll(backBtn, leftSpacer, homeBtn, searchBtn, cartBtn, profileBtn, rightSpacer, logo);
        return header;
    }

    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        if (isActive) {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: " + Styles.GOLD + ";" +
                    "-fx-border-width: 0 0 2 0; -fx-border-style: solid; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                    "-fx-font-size: 14px; -fx-cursor: hand;");

            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                        "-fx-font-size: 14px; -fx-cursor: hand;");
                btn.setScaleX(1.05);
                btn.setScaleY(1.05);
            });

            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                        "-fx-font-size: 14px; -fx-cursor: hand;");
                btn.setScaleX(1);
                btn.setScaleY(1);
            });
        }
        return btn;
    }

    private VBox createRegisterForm() {
        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(50, 40, 80, 40)); // Tambah padding bottom

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

    private void showLoginAlert() {
        LoginRequiredDialog.show("You need to login to access this feature.");
    }
}
