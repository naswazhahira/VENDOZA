package com.vendoza.ui;

import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EditProfilePage {

    private TextField nameField;
    private TextField phoneField;
    private TextField emailField;
    private TextArea addressArea;
    private Label messageLabel;

    public Scene getScene() {
        User currentUser = AuthService.getCurrentUser();

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(30, 40, 80, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        Text title = new Text("✏️ Edit Profile");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + ";" +
                "-fx-border-width: 0 0 3 0; -fx-padding: 0 0 10 0;");

        StackPane profileIcon = createProfileIcon();

        VBox formBox = new VBox(15);
        formBox.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        formBox.setMaxWidth(500);

        // Nama
        Label nameLabel = new Label("Full Name");
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        nameField = new TextField(currentUser.getUsername());
        nameField.setStyle(Styles.textFieldStyle());

        // No Handphone
        Label phoneLabel = new Label("Phone Number");
        phoneLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        phoneField = new TextField(currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "");
        phoneField.setPromptText("081234567890");
        phoneField.setStyle(Styles.textFieldStyle());

        // Email
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        emailField = new TextField(currentUser.getEmail());
        emailField.setStyle(Styles.textFieldStyle());
        emailField.setEditable(false);
        emailField.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                "-fx-background-radius: 15; -fx-padding: 12 15;");

        // Alamat
        Label addressLabel = new Label("Shipping Address");
        addressLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        addressArea = new TextArea(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        addressArea.setPromptText("Enter your complete shipping address");
        addressArea.setStyle(Styles.textFieldStyle());
        addressArea.setPrefHeight(80);

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";" +
                "-fx-font-size: 12px;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.setStyle(Styles.buttonStyle());
        saveBtn.setPrefWidth(150);
        saveBtn.setOnMouseEntered(e -> {
            saveBtn.setStyle(Styles.buttonHoverStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), saveBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        saveBtn.setOnMouseExited(e -> {
            saveBtn.setStyle(Styles.buttonStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), saveBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        saveBtn.setOnAction(e -> saveProfile());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(Styles.outlineButtonStyle());
        cancelBtn.setPrefWidth(150);
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + ";" +
                "-fx-text-fill: " + Styles.BROWN_DARK + ";-fx-border-color: " + Styles.BROWN_DARK + ";" +
                "-fx-border-radius: 25;-fx-padding: 8 20;-fx-cursor: hand;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(Styles.outlineButtonStyle()));
        cancelBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);

        formBox.getChildren().addAll(
                nameLabel, nameField,
                phoneLabel, phoneField,
                emailLabel, emailField,
                addressLabel, addressArea,
                messageLabel, buttonBox
        );

        mainContent.getChildren().addAll(title, profileIcon, formBox);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label logo = new Label("✏️ VENDOZA");
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

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
        profileBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        navBar.getChildren().addAll(logo, leftSpacer, homeBtn, searchBtn, cartBtn, profileBtn, rightSpacer);
        return navBar;
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
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
                st.setToX(1.05);
                st.setToY(1.05);
                st.play();
            });

            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                        "-fx-font-size: 14px; -fx-cursor: hand;");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
                st.setToX(1);
                st.setToY(1);
                st.play();
            });
        }
        return btn;
    }

    private StackPane createProfileIcon() {
        StackPane iconContainer = new StackPane();
        iconContainer.setAlignment(Pos.CENTER);

        Circle circle = new Circle(50);
        circle.setFill(Color.web(Styles.BROWN_LIGHT));
        circle.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.1)));

        Label iconLabel = new Label("👤");
        iconLabel.setStyle("-fx-font-size: 55px;");

        iconContainer.getChildren().addAll(circle, iconLabel);
        return iconContainer;
    }

    private void saveProfile() {
        String newName = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();

        if (newName.isEmpty()) {
            messageLabel.setText("❌ Name cannot be empty!");
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            return;
        }

        User currentUser = AuthService.getCurrentUser();
        currentUser.setUsername(newName);
        currentUser.setPhoneNumber(phone);
        currentUser.setAddress(address);

        messageLabel.setText("✅ Profile updated successfully!");
        messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";");

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        delay.setOnFinished(e -> SceneManager.setScene(new ProfilePage().getScene()));
        delay.play();
    }

    private void showLoginAlert() {
        LoginRequiredDialog.show("You need to login to access this feature.");
    }
}
