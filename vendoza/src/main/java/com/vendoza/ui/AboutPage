package com.vendoza.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class AboutPage {

    public Scene getScene() {
        HBox navBar = createNavBar();

        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(30, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Hero card
        VBox heroCard = new VBox(15);
        heroCard.setAlignment(Pos.CENTER);
        heroCard.setStyle("-fx-background-color: " + Styles.BROWN_DARK + ";" +
                "-fx-background-radius: 20; -fx-padding: 40;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 8);");
        heroCard.setMaxWidth(Double.MAX_VALUE);

        // Logo circle
        StackPane logoContainer = new StackPane();
        Circle logoBg = new Circle(50);
        logoBg.setFill(Color.web(Styles.GOLD));
        logoBg.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

        Label logoIcon = new Label("👗");
        logoIcon.setStyle("-fx-font-size: 50px;");

        logoContainer.getChildren().addAll(logoBg, logoIcon);

        Label appName = new Label("VENDOZA");
        appName.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + Styles.WHITE + ";");

        Label tagline = new Label("Fashion aesthetic for the modern generation");
        tagline.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.BROWN_LIGHT + ";");

        Label versionLabel = new Label("Version 1.0.0");
        versionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + ";" +
                "-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 15; -fx-padding: 4 12;");

        heroCard.getChildren().addAll(logoContainer, appName, tagline, versionLabel);

        // About card
        VBox aboutCard = new VBox(15);
        aboutCard.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        aboutCard.setMaxWidth(Double.MAX_VALUE);

        Label aboutTitle = new Label("About Us");
        aboutTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");
        aboutTitle.setMaxWidth(Double.MAX_VALUE);

        Label aboutText = new Label(
                "Vendoza is a modern fashion e-commerce platform designed to bring the latest trends " +
                        "right to your fingertips. We believe fashion should be accessible, comfortable, and " +
                        "expressive of your unique identity.\n\n" +
                        "Our curated collection features pieces for everyday wear, special occasions, and everything in between."
        );
        aboutText.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-wrap-text: true;");
        aboutText.setWrapText(true);
        aboutText.setMaxWidth(Double.MAX_VALUE);

        aboutCard.getChildren().addAll(aboutTitle, aboutText);

        // Stats row
        HBox statsRow = new HBox(15);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.setMaxWidth(Double.MAX_VALUE);

        statsRow.getChildren().addAll(
                createStatBox("500+", "Products"),
                createStatBox("10K+", "Happy Customers"),
                createStatBox("50+", "Brands")
        );

        // Features card
        VBox featuresCard = new VBox(12);
        featuresCard.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        featuresCard.setMaxWidth(Double.MAX_VALUE);

        Label featTitle = new Label("✨ What We Offer");
        featTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");
        featuresCard.getChildren().add(featTitle);

        String[][] features = {
                {"🎯", "Curated Collections", "Handpicked fashion for every style"},
                {"🚚", "Fast Delivery", "Quick and reliable shipping to your door"},
                {"💎", "Premium Quality", "Only the best materials and craftsmanship"},
                {"🔒", "Secure Shopping", "Your data and payments are always safe"},
                {"🔄", "Easy Returns", "Hassle-free return policy within 7 days"}
        };

        for (String[] feature : features) {
            HBox featureRow = new HBox(12);
            featureRow.setAlignment(Pos.CENTER_LEFT);
            featureRow.setStyle("-fx-padding: 5 0;");

            Label featureIcon = new Label(feature[0]);
            featureIcon.setStyle("-fx-font-size: 20px;");
            featureIcon.setMinWidth(30);

            VBox featureInfo = new VBox(2);
            Label featureName = new Label(feature[1]);
            featureName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");
            Label featureDesc = new Label(feature[2]);
            featureDesc.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");
            featureInfo.getChildren().addAll(featureName, featureDesc);

            featureRow.getChildren().addAll(featureIcon, featureInfo);
            featuresCard.getChildren().add(featureRow);
        }

        mainContent.getChildren().addAll(heroCard, statsRow, aboutCard, featuresCard);

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

    private VBox createStatBox(String number, String label) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 12; -fx-padding: 15 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        HBox.setHgrow(box, Priority.ALWAYS);

        Label numLabel = new Label(number);
        numLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        Label lblLabel = new Label(label);
        lblLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        box.getChildren().addAll(numLabel, lblLabel);
        return box;
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

        Label title = new Label("About Vendoza");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        navBar.getChildren().addAll(backBtn, title);
        return navBar;
    }
}
