package com.vendoza.ui;

import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class ProductDetailPage {

    private final Product product;
    private int totalReviewCount = 2;
    private Label mainCardReviewCount;

    public ProductDetailPage(Product product) {
        this.product = product;
    }

    public Scene getScene() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        HBox navBar = createNavBar();

        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #F0E8DF; -fx-background: #F0E8DF;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox mainLayout = new VBox(navBar, scrollPane);
        mainLayout.setStyle("-fx-background-color: #F0E8DF;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
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
        backBtn.setOnAction(e -> SceneManager.goBack());

        Label logo = new Label("VENDOZA");
        logo.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Georgia';"
        );

        HBox logoGroup = new HBox(4);
        logoGroup.setAlignment(Pos.CENTER_LEFT);
        logoGroup.getChildren().addAll(backBtn, logo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button cartBtn = new Button("🛒  Cart");
        cartBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;"
        );
        cartBtn.setOnMouseEntered(e -> cartBtn.setStyle(
                "-fx-background-color: #D4A853; -fx-text-fill: #2C1810;" +
                        "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;"
        ));
        cartBtn.setOnMouseExited(e -> cartBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;"
        ));
        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new CartPage().getScene());
            else LoginRequiredDialog.show("Please log in first.");
        });

        navBar.getChildren().addAll(logoGroup, spacer, cartBtn);
        return navBar;
    }

    private VBox createContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40, 80, 60, 80));
        content.setStyle("-fx-background-color: #ebddc3;");

        HBox mainCard = createMainCard();
        VBox detailSection = createDetailSection();

        content.getChildren().addAll(mainCard, detailSection);
        return content;
    }

    private HBox createMainCard() {
        HBox card = new HBox(0);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.09), 18, 0, 0, 4);"
        );

        StackPane imagePane = new StackPane();
        imagePane.setPrefWidth(420);
        imagePane.setPrefHeight(460);
        imagePane.setStyle(
                "-fx-background-color: #F5F0EA;" +
                        "-fx-background-radius: 24 0 0 24;"
        );

        if (product.isOnSale()) {
            Label saleBadge = new Label("  SALE " + (int) product.getDiscountPercent() + "%  ");
            saleBadge.setStyle(
                    "-fx-background-color: #D4A853; -fx-text-fill: #2C1810;" +
                            "-fx-font-size: 12px; -fx-font-weight: 900;" +
                            "-fx-background-radius: 8; -fx-padding: 5 12;"
            );
            StackPane.setAlignment(saleBadge, Pos.TOP_LEFT);
            StackPane.setMargin(saleBadge, new Insets(20, 0, 0, 20));
            imagePane.getChildren().add(saleBadge);
        }

        try {
            Image img = new Image(getClass().getResourceAsStream(product.getImageUrl()));
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(340);
            imageView.setFitHeight(340);
            imageView.setPreserveRatio(true);
            imagePane.getChildren().add(imageView);
        } catch (Exception ex) {
            Label fallback = new Label("🛍️");
            fallback.setStyle("-fx-font-size: 100px;");
            imagePane.getChildren().add(fallback);
        }

        VBox infoPane = new VBox(16);
        infoPane.setPadding(new Insets(40, 40, 40, 40));
        infoPane.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(infoPane, Priority.ALWAYS);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #2C1810; -fx-font-family: 'Georgia';"
        );
        nameLabel.setWrapText(true);

        HBox ratingRow = new HBox(10);
        ratingRow.setAlignment(Pos.CENTER_LEFT);
        Label stars = new Label(product.getStarString());
        stars.setStyle("-fx-font-size: 18px; -fx-text-fill: #D4A853;");
        Label ratingNum = new Label(String.format("%.1f", product.getRating()));
        ratingNum.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        mainCardReviewCount = new Label("(" + totalReviewCount + " reviews)");
        mainCardReviewCount.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");

        ratingRow.getChildren().addAll(stars, ratingNum, mainCardReviewCount);

        Region sep1 = new Region();
        sep1.setPrefHeight(1);
        sep1.setStyle("-fx-background-color: #F0EAE4;");

        HBox priceBox = new HBox(15);
        priceBox.setAlignment(Pos.BOTTOM_LEFT);

        Label currentPrice = new Label("Rp" + Styles.formatPrice(product.getCurrentPrice()));
        currentPrice.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';");
        priceBox.getChildren().add(currentPrice);

        if (product.isOnSale()) {
            HBox originalRow = new HBox(8);
            originalRow.setAlignment(Pos.CENTER_LEFT);
            originalRow.setTranslateY(6);

            Label originalPrice = new Label("Rp" + Styles.formatPrice(product.getPrice()));
            originalPrice.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #BDBDBD;" +
                            "-fx-font-family: 'Playfair Display';"
            );

            Region redStrip = new Region();
            redStrip.setPrefHeight(1.5);
            redStrip.setMaxHeight(1.5);
            redStrip.setStyle("-fx-background-color: #E53935;");

            StackPane strikethroughContainer = new StackPane();
            strikethroughContainer.setAlignment(Pos.CENTER);
            strikethroughContainer.getChildren().addAll(originalPrice, redStrip);

            Label discountBadge = new Label("-" + (int) product.getDiscountPercent() + "%");
            discountBadge.setStyle(
                    "-fx-background-color: #E53935; -fx-text-fill: white;" +
                            "-fx-font-size: 12px; -fx-font-weight: bold;" +
                            "-fx-background-radius: 6; -fx-padding: 3 8;"
            );

            originalRow.getChildren().addAll(strikethroughContainer, discountBadge);
            priceBox.getChildren().add(originalRow);
        }

        Region sep2 = new Region();
        sep2.setPrefHeight(1);
        sep2.setStyle("-fx-background-color: #F0EAE4;");

        GridPane quickInfo = new GridPane();
        quickInfo.setHgap(12);
        quickInfo.setVgap(10);
        addInfoRow(quickInfo, 0, "Category", product.getCategory());
        addInfoRow(quickInfo, 1, "Stock", product.getStockLabel(), product.getStockColor());

        Region sep3 = new Region();
        sep3.setPrefHeight(1);
        sep3.setStyle("-fx-background-color: #F0EAE4;");

        HBox qtyRow = new HBox(12);
        qtyRow.setAlignment(Pos.CENTER_LEFT);
        Label qtyLabel = new Label("Quantity:");
        qtyLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        final int[] qty = {1};
        Button minusBtn = new Button("−");
        minusBtn.setStyle(
                "-fx-background-color: #EFEBE9; -fx-text-fill: #3E2723;" +
                        "-fx-font-size: 18px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-min-width: 38; -fx-min-height: 38; -fx-cursor: hand;"
        );
        Label qtyNum = new Label("1");
        qtyNum.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #3E2723;" +
                        "-fx-min-width: 32; -fx-alignment: center;"
        );
        Button plusBtn = new Button("+");
        plusBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853;" +
                        "-fx-font-size: 18px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-min-width: 38; -fx-min-height: 38; -fx-cursor: hand;"
        );
        minusBtn.setOnAction(e -> {
            if (qty[0] > 1) {
                qty[0]--;
                qtyNum.setText(String.valueOf(qty[0]));
            }
        });
        plusBtn.setOnAction(e -> {
            if (qty[0] < product.getStock()) {
                qty[0]++;
                qtyNum.setText(String.valueOf(qty[0]));
            }
        });
        qtyRow.getChildren().addAll(qtyLabel, minusBtn, qtyNum, plusBtn);

        HBox actionRow = new HBox(12);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        Button cartBtn = new Button("🛒  Add to Cart");
        cartBtn.setPrefHeight(48);
        HBox.setHgrow(cartBtn, Priority.ALWAYS);
        cartBtn.setMaxWidth(Double.MAX_VALUE);
        cartBtn.setStyle("-fx-background-color: #EFEBE9; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;");
        cartBtn.setOnMouseEntered(e -> cartBtn.setStyle("-fx-background-color: #D7CCC8; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;"));
        cartBtn.setOnMouseExited(e -> cartBtn.setStyle("-fx-background-color: #EFEBE9; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;"));

        Button buyBtn = new Button("Buy Now");
        buyBtn.setPrefHeight(48);
        HBox.setHgrow(buyBtn, Priority.ALWAYS);
        buyBtn.setMaxWidth(Double.MAX_VALUE);
        buyBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;");
        buyBtn.setOnMouseEntered(e -> buyBtn.setStyle("-fx-background-color: #D4A853; -fx-text-fill: #2C1810; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;"));
        buyBtn.setOnMouseExited(e -> buyBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 14; -fx-cursor: hand;"));

        cartBtn.setDisable(!product.isAvailable());
        buyBtn.setDisable(!product.isAvailable());

        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.addToCart(product, qty[0]);
                showSuccess(product.getName() + " (x" + qty[0] + ") added to cart!");
            } else {
                LoginRequiredDialog.show("Please log in first.");
            }
        });

        buyBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.clearCart();
                CartService.addToCart(product, qty[0]);
                SceneManager.setScene(new CheckoutPage().getScene());
            } else {
                LoginRequiredDialog.show("Please log in first.");
            }
        });

        actionRow.getChildren().addAll(cartBtn, buyBtn);
        infoPane.getChildren().addAll(nameLabel, ratingRow, sep1, priceBox, sep2, quickInfo, sep3, qtyRow, actionRow);
        card.getChildren().addAll(imagePane, infoPane);

        return card;
    }

    private VBox createDetailSection() {
        VBox section = new VBox(24);

        VBox descCard = new VBox(14);
        descCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 20;" +
                        "-fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);"
        );

        Label descTitle = new Label("Product Description");
        descTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';");

        Region descSep = new Region();
        descSep.setPrefHeight(2);
        descSep.setMaxWidth(140);
        descSep.setStyle("-fx-background-color: #D4A853; -fx-background-radius: 2;");

        Label descText = new Label(product.getDescription());
        descText.setStyle("-fx-font-size: 14px; -fx-text-fill: #5D4037; -fx-line-spacing: 6;");
        descText.setWrapText(true);

        descCard.getChildren().addAll(descTitle, descSep, descText);

        VBox commentCard = new VBox(20);
        commentCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 20;" +
                        "-fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);"
        );

        Label commentTitle = new Label("Customer Reviews  (" + totalReviewCount + ")");
        commentTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';");

        Region commentSep = new Region();
        commentSep.setPrefHeight(2);
        commentSep.setMaxWidth(140);
        commentSep.setStyle("-fx-background-color: #D4A853; -fx-background-radius: 2;");

        HBox ratingSelector = new HBox(8);
        ratingSelector.setAlignment(Pos.CENTER_LEFT);
        Label ratingPrompt = new Label("Your Rating:");
        ratingPrompt.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        final int[] selectedRating = {0};
        Button[] starBtns = new Button[5];

        for (int i = 0; i < 5; i++) {
            final int starIndex = i + 1;
            Button starBtn = new Button("☆");
            starBtn.setStyle(
                    "-fx-background-color: transparent; -fx-text-fill: #BDBDBD;" +
                            "-fx-font-size: 24px; -fx-cursor: hand; -fx-padding: 0 2;"
            );
            starBtn.setOnAction(ev -> {
                selectedRating[0] = starIndex;
                for (int j = 0; j < 5; j++) {
                    starBtns[j].setText(j < starIndex ? "★" : "☆");
                    starBtns[j].setStyle(
                            "-fx-background-color: transparent; -fx-font-size: 24px; -fx-cursor: hand; -fx-padding: 0 2;" +
                                    (j < starIndex ? "-fx-text-fill: #D4A853;" : "-fx-text-fill: #BDBDBD;")
                    );
                }
            });
            starBtns[i] = starBtn;
        }

        ratingSelector.getChildren().add(ratingPrompt);
        for (Button sb : starBtns) ratingSelector.getChildren().add(sb);

        TextArea commentInput = new TextArea();
        commentInput.setPromptText("Share your thoughts about this product...");
        commentInput.setPrefRowCount(3);
        commentInput.setStyle(
                "-fx-background-color: #F5EFE8; -fx-background-radius: 12;" +
                        "-fx-border-color: #C8A96E; -fx-border-radius: 12; -fx-border-width: 1.5;" +
                        "-fx-font-size: 13px; -fx-text-fill: #3E2723; -fx-padding: 10;" +
                        "-fx-control-inner-background: #F5EFE8;"
        );

        Button submitBtn = new Button("Post Review");
        submitBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-padding: 10 28; -fx-cursor: hand;"
        );
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle(
                "-fx-background-color: #D4A853; -fx-text-fill: #2C1810;" +
                        "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-padding: 10 28; -fx-cursor: hand;"
        ));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-padding: 10 28; -fx-cursor: hand;"
        ));

        VBox commentsListBox = new VBox(14);

        commentsListBox.getChildren().addAll(
                buildCommentItem("Sarah M.", "Absolutely love this! Quality is amazing.", "2 days ago", "★★★★★"),
                buildCommentItem("James K.", "Great product, fast shipping. Highly recommend!", "5 days ago", "★★★★☆")
        );

        submitBtn.setOnAction(e -> {
            if (!AuthService.isLoggedIn()) {
                LoginRequiredDialog.show("Please log in first to post a review.");
                return;
            }
            String text = commentInput.getText().trim();
            if (!text.isEmpty()) {
                int r = selectedRating[0] > 0 ? selectedRating[0] : 5;
                String userStars = "★".repeat(r) + "☆".repeat(5 - r);

                commentsListBox.getChildren().add(0, buildCommentItem("You", text, "Just now", userStars));

                commentInput.clear();

                selectedRating[0] = 0;
                for (Button sb : starBtns) {
                    sb.setText("☆");
                    sb.setStyle("-fx-background-color: transparent; -fx-text-fill: #BDBDBD; -fx-font-size: 24px; -fx-cursor: hand; -fx-padding: 0 2;");
                }

                totalReviewCount++;
                commentTitle.setText("Customer Reviews  (" + totalReviewCount + ")");

                updateMainCardReviewCount(totalReviewCount);

                showSuccess("Your review has been posted!");
            }
        });

        HBox submitRow = new HBox();
        submitRow.setAlignment(Pos.CENTER_RIGHT);
        submitRow.getChildren().add(submitBtn);

        VBox inputBox = new VBox(12);
        inputBox.getChildren().addAll(ratingSelector, commentInput, submitRow);

        commentCard.getChildren().addAll(commentTitle, commentSep, inputBox, commentsListBox);
        section.getChildren().addAll(descCard, commentCard);
        return section;
    }

    private VBox buildCommentItem(String author, String text, String time, String stars) {
        VBox item = new VBox(6);
        item.setStyle("-fx-background-color: #FAF6F1; -fx-background-radius: 12; -fx-padding: 14 16;");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label authorLabel = new Label(author);
        authorLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        Label starsLabel = new Label(stars);
        starsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #D4A853;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timeLabel = new Label(time);
        timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #BCAAA4;");

        header.getChildren().addAll(authorLabel, starsLabel, spacer, timeLabel);

        Label body = new Label(text);
        body.setStyle("-fx-font-size: 13px; -fx-text-fill: #5D4037;");
        body.setWrapText(true);

        item.getChildren().addAll(header, body);
        return item;
    }

    private void addInfoRow(GridPane grid, int rowIndex, String label, String value) {
        addInfoRow(grid, rowIndex, label, value, "#3E2723");
    }

    private void addInfoRow(GridPane grid, int rowIndex, String label, String value, String valueColor) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63; -fx-min-width: 90;");
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + valueColor + ";");
        val.setWrapText(true);
        grid.add(lbl, 0, rowIndex);
        grid.add(val, 1, rowIndex);
    }

    private void updateMainCardReviewCount(int newCount) {
        if (mainCardReviewCount != null) {
            mainCardReviewCount.setText("(" + newCount + " reviews)");
        }
    }

    private void showSuccess(String message) {
        CustomDialog.showSuccess("Success", message);
    }
}