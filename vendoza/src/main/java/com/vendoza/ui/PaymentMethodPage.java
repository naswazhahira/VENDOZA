package com.vendoza.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class PaymentMethodPage {

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
        backBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        Label logo = new Label("Payment Methods");
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
        VBox content = new VBox(20);
        content.setPadding(new Insets(40, 60, 60, 60));
        content.setStyle("-fx-background-color: #ebddc3;");
        content.setAlignment(Pos.TOP_CENTER);

        // Bank Transfer - FULL WIDTH
        VBox bankCard = createPaymentGroup("🏦 Bank Transfer",
                new String[][]{
                        {"BCA", "1234-5678-9012", "a/n Vendoza Official"},
                        {"Mandiri", "0987-6543-2100", "a/n Vendoza Official"},
                        {"BNI", "4567-8901-2345", "a/n Vendoza Official"},
                        {"BRI", "6789-0123-4567", "a/n Vendoza Official"}
                });
        bankCard.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(bankCard, Priority.ALWAYS);

        // E-Wallet - FULL WIDTH
        VBox ewalletCard = createPaymentGroup("📱 E-Wallet",
                new String[][]{
                        {"GoPay", "0812-3456-7890", "Vendoza Store"},
                        {"OVO", "0812-3456-7890", "Vendoza Store"},
                        {"Dana", "0812-3456-7890", "Vendoza Store"},
                        {"ShopeePay", "0812-3456-7890", "Vendoza Store"}
                });
        ewalletCard.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(ewalletCard, Priority.ALWAYS);

        // COD & Other Options - FULL WIDTH
        VBox otherCard = new VBox(12);
        otherCard.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        otherCard.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(otherCard, Priority.ALWAYS);

        Label otherTitle = new Label("💰 Other Payment Options");
        otherTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        HBox codRow = createSimplePaymentRow("🚚 COD (Cash on Delivery)", "Pay when your order arrives");

        otherCard.getChildren().addAll(otherTitle, codRow);

        // Info box - FULL WIDTH
        VBox infoBox = new VBox(8);
        infoBox.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background-radius: 10; -fx-padding: 15;");
        infoBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label infoHeader = new Label("Payment Information");
        infoHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label infoText = new Label("Transfer confirmation is processed automatically within 1x24 hours. " +
                "If you have questions, please contact our customer service.");
        infoText.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");
        infoText.setWrapText(true);
        infoText.setMaxWidth(Double.MAX_VALUE);

        infoBox.getChildren().addAll(infoHeader, infoText);

        content.getChildren().addAll(bankCard, ewalletCard, otherCard, infoBox);
        return content;
    }

    private VBox createPaymentGroup(String groupTitle, String[][] items) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        card.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label title = new Label(groupTitle);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");
        card.getChildren().add(title);

        for (String[] item : items) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                    "-fx-background-radius: 10; -fx-padding: 12 15;");
            row.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(row, Priority.ALWAYS);

            VBox info = new VBox(3);
            HBox.setHgrow(info, Priority.ALWAYS);

            Label nameLabel = new Label(item[0]);
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

            Label numberLabel = new Label(item[1]);
            numberLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

            Label holderLabel = new Label(item[2]);
            holderLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

            info.getChildren().addAll(nameLabel, numberLabel, holderLabel);

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Button copyBtn = new Button("Copy");
            copyBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: white;" +
                    "-fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 5 12; -fx-cursor: hand;");
            copyBtn.setOnAction(e -> {
                javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                content.putString(item[1]);
                clipboard.setContent(content);
                copyBtn.setText("Copied!");
                javafx.animation.PauseTransition pt = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
                pt.setOnFinished(ev -> copyBtn.setText("Copy"));
                pt.play();
            });

            row.getChildren().addAll(info, sp, copyBtn);
            card.getChildren().add(row);
        }

        return card;
    }

    private HBox createSimplePaymentRow(String name, String desc) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background-radius: 10; -fx-padding: 12 15;");
        row.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(row, Priority.ALWAYS);

        VBox info = new VBox(3);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label descLabel = new Label(desc);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        info.getChildren().addAll(nameLabel, descLabel);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        row.getChildren().addAll(info, sp);
        return row;
    }
}
