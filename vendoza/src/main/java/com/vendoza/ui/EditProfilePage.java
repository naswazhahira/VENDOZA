package com.vendoza.ui;

import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class EditProfilePage {

    private TextField nameField;
    private TextField phoneField;
    private TextField emailField;
    private TextArea addressArea;
    private Label messageLabel;

    public Scene getScene() {
        User currentUser = AuthService.getCurrentUser();

        // Top Navigation Bar
        HBox navBar = createNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(30, 300, 50, 300));

        // Title
        Text title = new Text("Ubah Profil");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        // Profile Icon
        StackPane profileIcon = createProfileIcon();

        // Form Container
        VBox formBox = new VBox(15);
        formBox.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;");

        // Nama
        Label nameLabel = new Label("Nama");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        nameField = new TextField(currentUser.getUsername());
        nameField.setStyle(Styles.textFieldStyle());

        // No Handphone
        Label phoneLabel = new Label("No Handphone");
        phoneLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        phoneField = new TextField(currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "");
        phoneField.setStyle(Styles.textFieldStyle());

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        emailField = new TextField(currentUser.getEmail());
        emailField.setStyle(Styles.textFieldStyle());
        emailField.setEditable(false);

        // Alamat
        Label addressLabel = new Label("Alamat");
        addressLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        addressArea = new TextArea(currentUser.getAddress() != null ? currentUser.getAddress() : "");
        addressArea.setPromptText("Masukkan alamat lengkap");
        addressArea.setStyle(Styles.textFieldStyle());
        addressArea.setPrefHeight(80);

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";");

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Simpan");
        saveBtn.setStyle(Styles.buttonStyle());
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle(Styles.buttonHoverStyle()));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle(Styles.buttonStyle()));
        saveBtn.setOnAction(e -> saveProfile());

        Button cancelBtn = new Button("Batal");
        cancelBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                "-fx-border-color: " + Styles.TEXT_LIGHT + "; -fx-border-radius: 25;" +
                "-fx-padding: 8 20; -fx-cursor: hand;");
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
        scrollPane.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        return new Scene(root, 1200, 700);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + ";");

        Button backBtn = new Button("← Kembali");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + "; -fx-cursor: hand;");
        backBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        Label logo = new Label("Ubah Profil");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navBar.getChildren().addAll(backBtn, spacer, logo);
        return navBar;
    }

    private StackPane createProfileIcon() {
        StackPane iconContainer = new StackPane();
        iconContainer.setAlignment(Pos.CENTER);

        Circle circle = new Circle(50);
        circle.setStyle("-fx-fill: " + Styles.BROWN_LIGHT + ";");

        Label iconLabel = new Label("👤");
        iconLabel.setStyle("-fx-font-size: 60px;");

        iconContainer.getChildren().addAll(circle, iconLabel);

        return iconContainer;
    }

    private void saveProfile() {
        String newName = nameField.getText();
        String phone = phoneField.getText();
        String address = addressArea.getText();

        if (newName.isEmpty()) {
            messageLabel.setText("Nama tidak boleh kosong!");
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + ";");
            return;
        }

        // Update profile
        User currentUser = AuthService.getCurrentUser();
        currentUser.setUsername(newName);
        currentUser.setPhoneNumber(phone);
        currentUser.setAddress(address);

        messageLabel.setText("Profil berhasil diubah!");
        messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + ";");

        // Kembali ke Profile setelah 1 detik
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        delay.setOnFinished(e -> SceneManager.setScene(new ProfilePage().getScene()));
        delay.play();
    }
}