package com.vendoza.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelpCenterPage {

    private static final String BORDER_COLOR = "#C4A484";

    // ========== METHOD UNTUK HALAMAN HELP CENTER UTAMA ==========

    public Scene getScene() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setPadding(new Insets(40, 50, 80, 50));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        Text title = new Text("Help Center");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #3E2723;");

        VBox faqBox = new VBox(15);
        faqBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 25;");
        faqBox.setMaxWidth(800);

        // FAQ Items
        Label faq1 = new Label("❓ How do I track my order?");
        faq1.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Label ans1 = new Label("You can track your order from the 'My Orders' page in your profile. Click on the order to see real-time tracking status.");
        ans1.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63; -fx-wrap-text: true;");

        Label faq2 = new Label("❓ What is your return policy?");
        faq2.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Label ans2 = new Label("We accept returns within 14 days of delivery. Items must be unworn, unwashed, and with original tags attached.");
        ans2.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63; -fx-wrap-text: true;");

        Label faq3 = new Label("❓ How can I contact support?");
        faq3.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Label ans3 = new Label("Email us at support@vendoza.com or call +62 21 1234 5678 (Mon-Fri, 9 AM - 6 PM).");
        ans3.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63; -fx-wrap-text: true;");

        Label faq4 = new Label("❓ How long does shipping take?");
        faq4.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Label ans4 = new Label("Standard shipping takes 3-5 business days. Express shipping takes 1-2 business days.");
        ans4.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63; -fx-wrap-text: true;");

        Label faq5 = new Label("❓ Are the products authentic?");
        faq5.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Label ans5 = new Label("Yes, all products sold on Vendoza are 100% authentic and sourced directly from official brands.");
        ans5.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63; -fx-wrap-text: true;");

        faqBox.getChildren().addAll(
                faq1, ans1, new Separator(),
                faq2, ans2, new Separator(),
                faq3, ans3, new Separator(),
                faq4, ans4, new Separator(),
                faq5, ans5
        );

        mainContent.getChildren().addAll(title, faqBox);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");

        return new Scene(root, screenWidth, screenHeight);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Button backBtn = new Button("< Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3E2723; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #D4A853; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3E2723; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        Label logo = new Label("Help Center");
        logo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navBar.getChildren().addAll(backBtn, logo, spacer);
        return navBar;
    }

    // ========== METHOD DIALOG UNTUK NOTIFICATION (YANG SUDAH ADA) ==========

    public static void showSuccess(String title, String message) {
        showDialog(title, message, "✓", "#4CAF50");
    }

    public static void showError(String title, String message) {
        showDialog(title, message, "⚠", "#E53935");
    }

    public static void showInfo(String title, String message) {
        showDialog(title, message, "ℹ", "#D4A853");
    }

    private static void showDialog(String title, String message, String icon, String iconColor) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 30 40 30 40;"
        );
        root.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.15)));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
                "-fx-font-size: 48px;" +
                        "-fx-text-fill: " + iconColor + ";"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Georgia';"
        );

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #5D4037;" +
                        "-fx-wrap-text: true;"
        );
        messageLabel.setMaxWidth(300);
        messageLabel.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK");
        okButton.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 35;" +
                        "-fx-cursor: hand;"
        );
        okButton.setOnMouseEntered(e -> okButton.setStyle(
                "-fx-background-color: #D4A853;" +
                        "-fx-text-fill: #2C1810;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 35;" +
                        "-fx-cursor: hand;"
        ));
        okButton.setOnMouseExited(e -> okButton.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 35;" +
                        "-fx-cursor: hand;"
        ));
        okButton.setOnAction(e -> dialogStage.close());

        root.getChildren().addAll(iconLabel, titleLabel, messageLabel, okButton);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);

        dialogStage.sizeToScene();
        dialogStage.centerOnScreen();

        dialogStage.showAndWait();
    }
}
