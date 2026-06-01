package com.vendoza.ui;

import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class ShippingAddressPage {

    public Scene getScene() {

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        User user = AuthService.getCurrentUser();

        HBox navBar = createNavBar();

        ScrollPane scrollPane = new ScrollPane(createContent(user));
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
        backBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        Label logo = new Label("Shipping Address");
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

    private VBox createContent(User user) {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40, 60, 60, 60));
        content.setStyle("-fx-background-color: #ebddc3;");
        content.setAlignment(Pos.CENTER);

        // Card utama - FULL WIDTH
        VBox card = new VBox(15);
        card.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 35;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        card.setMaxWidth(Double.MAX_VALUE);  // FULL WIDTH
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setAlignment(Pos.CENTER);

        // Icon
        Label iconLabel = new Label("🏠");
        iconLabel.setStyle("-fx-font-size: 50px;");

        // Title
        Label infoTitle = new Label("Shipping Address");
        infoTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK +
                "; -fx-font-family: 'Georgia';");

        // Separator
        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        // Nama User
        Label nameLabel = new Label(user.getUsername());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " + Styles.TEXT_DARK + "; -fx-font-weight: bold;");

        // Phone
        String phone = (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty())
                ? user.getPhoneNumber() : "(No phone number)";
        Label phoneLabel = new Label("📱 " + phone);
        phoneLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        // Address
        String address = (user.getAddress() != null && !user.getAddress().isEmpty())
                ? user.getAddress() : "No address saved yet. Please add your shipping address.";
        Label addressLabel = new Label("📍 " + address);
        addressLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        addressLabel.setWrapText(true);
        addressLabel.setMaxWidth(Double.MAX_VALUE);  // FULL WIDTH

        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        // Edit Button
        Button editBtn = new Button("Edit Address");
        editBtn.setStyle(Styles.buttonStyle());
        editBtn.setPrefWidth(200);
        editBtn.setOnMouseEntered(e -> editBtn.setStyle(Styles.buttonHoverStyle()));
        editBtn.setOnMouseExited(e -> editBtn.setStyle(Styles.buttonStyle()));
        editBtn.setOnAction(e -> SceneManager.setScene(new EditProfilePage().getScene()));

        card.getChildren().addAll(iconLabel, infoTitle, sep1, nameLabel, phoneLabel, addressLabel, sep2, editBtn);

        // Info box bawah - FULL WIDTH
        VBox infoBox = new VBox(8);
        infoBox.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background-radius: 10; -fx-padding: 15;");
        infoBox.setMaxWidth(Double.MAX_VALUE);  // FULL WIDTH
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        infoBox.setAlignment(Pos.CENTER);

        Label infoHeader = new Label("About Shipping Address");
        infoHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label infoText = new Label("Your shipping address is used as the default delivery location when you checkout. " +
                "You can change it anytime through Edit Profile.");
        infoText.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-wrap-text: true;");
        infoText.setWrapText(true);
        infoText.setMaxWidth(Double.MAX_VALUE);  // FULL WIDTH
        infoText.setAlignment(Pos.CENTER);

        infoBox.getChildren().addAll(infoHeader, infoText);

        content.getChildren().addAll(card, infoBox);
        return content;
    }
}
