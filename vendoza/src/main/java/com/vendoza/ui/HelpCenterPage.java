package com.vendoza.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class HelpCenterPage {

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

        Label logo = new Label("Help Center");
        logo.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Georgia';"
        );

        // Gabungkan Back dan Logo dalam satu HBox
        HBox logoGroup = new HBox(4);
        logoGroup.setAlignment(Pos.CENTER_LEFT);
        logoGroup.getChildren().addAll(backBtn, logo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Tidak ada tombol Cart atau navigasi lain
        navBar.getChildren().addAll(logoGroup, spacer);
        return navBar;
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(40, 60, 60, 60));
        content.setStyle("-fx-background-color: #ebddc3;");
        content.setAlignment(Pos.TOP_CENTER);

        // Contact card - FULL WIDTH
        VBox contactCard = new VBox(15);
        contactCard.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        contactCard.setMaxWidth(Double.MAX_VALUE);  // FULL WIDTH
        HBox.setHgrow(contactCard, Priority.ALWAYS);

        Label contactTitle = new Label("📞 Contact Us");
        contactTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        HBox emailRow = createContactRow("📧", "Email", "support@vendoza.com", "Available 24/7");
        HBox waRow = createContactRow("💬", "WhatsApp", "0812-3456-7890", "Mon–Sat, 08:00–21:00");
        HBox igRow = createContactRow("📸", "Instagram", "@vendoza.id", "DM for quick response");
        HBox twitterRow = createContactRow("🐦", "Twitter / X", "@vendoza_id", "For updates and support");

        contactCard.getChildren().addAll(contactTitle, emailRow, waRow, igRow, twitterRow);

        // FAQ Card - FULL WIDTH
        VBox faqCard = new VBox(12);
        faqCard.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        faqCard.setMaxWidth(Double.MAX_VALUE);  // FULL WIDTH
        HBox.setHgrow(faqCard, Priority.ALWAYS);
        faqCard.setAlignment(Pos.CENTER);

        Label faqTitle = new Label("🙋 Frequently Asked Questions");
        faqTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");
        faqCard.getChildren().add(faqTitle);

        String[][] faqs = {
                {"How to track my order?", "Go to My Order → select your order → see the status update."},
                {"Can I cancel my order?", "Orders can be cancelled if status is still Pending. Contact us for help."},
                {"What is the return policy?", "Returns accepted within 7 days if item is unused and in original packaging."},
                {"How long does shipping take?", "Estimated 2–5 business days depending on your location."},
                {"How to change shipping address?", "Go to Profile → Edit Profile → update your address before checkout."}
        };

        for (String[] faq : faqs) {
            TitledPane faqItem = createFaqItem(faq[0], faq[1]);
            faqItem.setMaxWidth(Double.MAX_VALUE);
            faqCard.getChildren().add(faqItem);
        }

        content.getChildren().addAll(contactCard, faqCard);
        return content;
    }

    private HBox createContactRow(String icon, String channel, String contact, String hours) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background-radius: 10; -fx-padding: 12 15;");
        HBox.setHgrow(row, Priority.ALWAYS);
        row.setMaxWidth(Double.MAX_VALUE);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 22px;");
        iconLabel.setMinWidth(40);

        VBox info = new VBox(2);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label channelLabel = new Label(channel);
        channelLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label contactLabel = new Label(contact);
        contactLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        Label hoursLabel = new Label(hours);
        hoursLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        info.getChildren().addAll(channelLabel, contactLabel, hoursLabel);

        row.getChildren().addAll(iconLabel, info);
        return row;
    }

    private TitledPane createFaqItem(String question, String answer) {
        Label answerLabel = new Label(answer);
        answerLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + "; -fx-wrap-text: true;");
        answerLabel.setWrapText(true);
        answerLabel.setPadding(new Insets(5, 10, 10, 10));
        answerLabel.setAlignment(Pos.CENTER_LEFT);
        answerLabel.setMaxWidth(Double.MAX_VALUE);

        VBox contentBox = new VBox(answerLabel);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setStyle("-fx-background-color: transparent;");
        contentBox.setMaxWidth(Double.MAX_VALUE);

        TitledPane pane = new TitledPane(question, contentBox);
        pane.setExpanded(false);
        pane.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-background-color: " + Styles.BROWN_PALE + "; -fx-background-radius: 8;");
        pane.setMaxWidth(Double.MAX_VALUE);
        return pane;
    }
}
