package com.vendoza.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PaymentMethodPage {

    public Scene getScene() {
        HBox navBar = createNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        //Label pageTitle = new Label("💳 Payment Methods");
        //pageTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        // Bank Transfer
        VBox bankCard = createPaymentGroup("🏦 Bank Transfer",
                new String[][]{
                        {"BCA", "1234-5678-9012", "a/n Vendoza Official"},
                        {"Mandiri", "0987-6543-2100", "a/n Vendoza Official"},
                        {"BNI", "4567-8901-2345", "a/n Vendoza Official"},
                        {"BRI", "6789-0123-4567", "a/n Vendoza Official"}
                });
        bankCard.setMaxWidth(Double.MAX_VALUE);

        // E-Wallet
        VBox ewalletCard = createPaymentGroup("📱 E-Wallet",
                new String[][]{
                        {"GoPay", "0812-3456-7890", "Vendoza Store"},
                        {"OVO", "0812-3456-7890", "Vendoza Store"},
                        {"Dana", "0812-3456-7890", "Vendoza Store"},
                        {"ShopeePay", "0812-3456-7890", "Vendoza Store"}
                });
        ewalletCard.setMaxWidth(Double.MAX_VALUE);

        // COD & Credit Card
        VBox otherCard = new VBox(12);
        otherCard.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        otherCard.setMaxWidth(Double.MAX_VALUE);

        Label otherTitle = new Label("💰 Other Payment Options");
        otherTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        HBox codRow = createSimplePaymentRow("🚚 COD (Cash on Delivery)", "Pay when your order arrives");
        //HBox ccRow = createSimplePaymentRow("💳 Credit / Debit Card", "Visa, Mastercard, and others");
        //HBox qrisRow = createSimplePaymentRow("📷 QRIS", "Scan QR code for payment");

        otherCard.getChildren().addAll(otherTitle, codRow);

        // Info box
        VBox infoBox = new VBox(8);
        infoBox.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background-radius: 10; -fx-padding: 15;");
        infoBox.setMaxWidth(Double.MAX_VALUE);

        Label infoHeader = new Label(" Payment Information");
        infoHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label infoText = new Label("Transfer confirmation is processed automatically within 1x24 hours. " +
                "If you have questions, please contact our customer service.");
        infoText.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");
        infoText.setWrapText(true);
        infoText.setMaxWidth(Double.MAX_VALUE);

        infoBox.getChildren().addAll(infoHeader, infoText);

        mainContent.getChildren().addAll(bankCard, ewalletCard, otherCard, infoBox);

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

    private VBox createPaymentGroup(String groupTitle, String[][] items) {
        VBox card = new VBox(12);
        card.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        card.setMaxWidth(600);

        Label title = new Label(groupTitle);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");
        card.getChildren().add(title);

        for (String[] item : items) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                    "-fx-background-radius: 10; -fx-padding: 12 15;");

            VBox info = new VBox(3);
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

        VBox info = new VBox(3);
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

        Label title = new Label("💳 Payment Methods");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        navBar.getChildren().addAll(backBtn, title);
        return navBar;
    }
}
