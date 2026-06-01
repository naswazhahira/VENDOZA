package com.vendoza.ui;

import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ShippingAddressPage {

    public Scene getScene() {
        User user = AuthService.getCurrentUser();

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");
        mainContent.setMaxWidth(Double.MAX_VALUE);

        // Card utama
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        card.setMaxWidth(Double.MAX_VALUE);

        // Icon + info
        Label iconLabel = new Label("🏠");
        iconLabel.setStyle("-fx-font-size: 40px;");

        Label infoTitle = new Label("Main Address");
        infoTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label nameLabel = new Label(user.getUsername());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.TEXT_DARK + "; -fx-font-weight: bold;");

        String phone = (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty())
                ? user.getPhoneNumber() : "(No phone number)";
        Label phoneLabel = new Label("📱 " + phone);
        phoneLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");
        phoneLabel.setMaxWidth(Double.MAX_VALUE);

        // FIX: addressLabel di-uncomment dan ditampilkan
        String address = (user.getAddress() != null && !user.getAddress().isEmpty())
                ? user.getAddress() : "No address saved yet. Please add your shipping address.";
        Label addressLabel = new Label("📍 " + address);
        addressLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        addressLabel.setWrapText(true);
        addressLabel.setMaxWidth(Double.MAX_VALUE);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        Button editBtn = new Button("Edit Address");
        editBtn.setStyle(Styles.buttonStyle());
        editBtn.setOnMouseEntered(e -> editBtn.setStyle(Styles.buttonHoverStyle()));
        editBtn.setOnMouseExited(e -> editBtn.setStyle(Styles.buttonStyle()));
        editBtn.setOnAction(e -> SceneManager.setScene(new EditProfilePage().getScene()));

        // FIX: addressLabel ditambahkan ke card
        card.getChildren().addAll(iconLabel, infoTitle, nameLabel, phoneLabel, addressLabel, sep, editBtn);

        // Info box
        VBox infoBox = new VBox(8);
        infoBox.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background-radius: 10; -fx-padding: 15;");
        infoBox.setMaxWidth(Double.MAX_VALUE);

        Label infoHeader = new Label("About Shipping Address");
        infoHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label infoText = new Label("Your shipping address is used as the default delivery location when you checkout. " +
                "You can change it anytime through Edit Profile.");
        infoText.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-wrap-text: true;");
        infoText.setWrapText(true);
        infoText.setMaxWidth(Double.MAX_VALUE);

        infoBox.getChildren().addAll(infoHeader, infoText);

        mainContent.getChildren().addAll(card, infoBox);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");

        Scene scene = new Scene(root, 1200, 700);
        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Button backBtn = new Button("< Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        Label title = new Label("Shipping Address");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        navBar.getChildren().addAll(backBtn, title);
        return navBar;
    }
}