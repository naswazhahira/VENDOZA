package com.vendoza.ui;

import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import com.vendoza.service.DataService;
import javafx.animation.ScaleTransition;
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
import javafx.util.Duration;

import java.util.List;

public class CategoryPage {

    private final String category;

    public CategoryPage(String category) {
        this.category = category;
    }

    public Scene getScene() {
        double screenWidth  = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        HBox navBar = createNavBar();

        VBox contentArea = new VBox(30);
        contentArea.setPadding(new Insets(35, 60, 60, 60));
        contentArea.setStyle("-fx-background-color: #F0E8DF;");

        VBox header = createCategoryHeader();

        VBox productsSection = createProductsGrid();

        contentArea.getChildren().addAll(header, productsSection);

        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #F0E8DF; -fx-background: #F0E8DF;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox mainLayout = new VBox(navBar, scrollPane);
        mainLayout.setStyle("-fx-background-color: #F0E8DF;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(mainLayout, screenWidth, screenHeight);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(30);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(16, 50, 16, 50));
        navBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );

        Label logo = new Label("👗 VENDOZA");
        logo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Georgia';"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button backBtn = new Button("← Kembali ke Beranda");
        backBtn.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;"
        );
        backBtn.setOnAction(e -> SceneManager.setScene(new HomePage().getScene()));

        Button cartBtn = new Button("🛒  Cart");
        cartBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #5D4037;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;"
        );
        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new CartPage().getScene());
            else LoginRequiredDialog.show("Silakan masuk terlebih dahulu.");
        });

        navBar.getChildren().addAll(logo, spacer, backBtn, cartBtn);
        return navBar;
    }

    private VBox createCategoryHeader() {
        VBox header = new VBox(10);
        header.setStyle(
                "-fx-background-color: linear-gradient(to right, #2C1810, #4E342E, #6D4C41);" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 40 50 40 50;"
        );
        header.setAlignment(Pos.CENTER_LEFT);
        header.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.15)));

        String emoji = getCategoryEmoji();
        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 52px;");

        Label titleLabel = new Label(category.toUpperCase());
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Georgia';"
        );

        Label subLabel = new Label("Temukan koleksi " + category + " terbaik pilihan kami");
        subLabel.setStyle(
                "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 14px;"
        );

        header.getChildren().addAll(emojiLabel, titleLabel, subLabel);
        return header;
    }

    private String getCategoryEmoji() {
        switch (category) {
            case "Women's Fashion": return "👗";
            case "Men's Fashion":   return "👔";
            case "Accessories":     return "👜";
            case "Footwear":        return "👟";
            default:                return "🛍️";
        }
    }

    private VBox createProductsGrid() {
        VBox section = new VBox(20);

        List<Product> products = DataService.getProductsByCategory(category);

        Label countLabel = new Label(products.size() + " produk ditemukan di " + category);
        countLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #8D6E63;"
        );

        Region sep = new Region();
        sep.setPrefHeight(3);
        sep.setStyle("-fx-background-color: linear-gradient(to right, #D4A853, transparent); -fx-background-radius: 3;");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(18);
        productGrid.setVgap(18);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            productGrid.getColumnConstraints().add(cc);
        }

        if (products.isEmpty()) {
            Label emptyLabel = new Label("😔  Belum ada produk di kategori ini.");
            emptyLabel.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-text-fill: #8D6E63;" +
                            "-fx-padding: 40;"
            );
            section.getChildren().addAll(countLabel, sep, emptyLabel);
            return section;
        }

        int col = 0, row = 0;
        for (Product product : products) {
            VBox card = createProductCard(product);
            productGrid.add(card, col++, row);
            if (col > 3) { col = 0; row++; }
        }

        section.getChildren().addAll(countLabel, sep, productGrid);
        return section;
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(0);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-cursor: hand;"
        );
        card.setMaxWidth(Double.MAX_VALUE);
        card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.07)));

        card.setOnMouseEntered(e -> {
            card.setEffect(new DropShadow(22, Color.rgb(62, 39, 35, 0.18)));
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1.025); st.setToY(1.025); st.play();
        });
        card.setOnMouseExited(e -> {
            card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.07)));
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1); st.setToY(1); st.play();
        });
        card.setOnMouseClicked(e -> SceneManager.setScene(new ProductDetailPage(product).getScene()));

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(200);
        imageContainer.setStyle(
                "-fx-background-color: #F5F0EA;" +
                        "-fx-background-radius: 18 18 0 0;"
        );

        if (product.isOnSale()) {
            Label saleBadge = new Label("SALE");
            saleBadge.setStyle(
                    "-fx-background-color: #D4A853;" +
                            "-fx-text-fill: #2C1810;" +
                            "-fx-font-size: 10px;" +
                            "-fx-font-weight: 900;" +
                            "-fx-background-radius: 6;" +
                            "-fx-padding: 4 9;"
            );
            StackPane.setAlignment(saleBadge, Pos.TOP_LEFT);
            StackPane.setMargin(saleBadge, new Insets(12, 0, 0, 12));
            imageContainer.getChildren().add(saleBadge);
        }

        try {
            Image img = new Image(getClass().getResourceAsStream(product.getImageUrl()));
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(170);
            imageView.setFitHeight(170);
            imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);
        } catch (Exception ex) {
            Label fallback = new Label("🛍️");
            fallback.setStyle("-fx-font-size: 65px;");
            imageContainer.getChildren().add(fallback);
        }

        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(14, 16, 16, 16));

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723;"
        );
        nameLabel.setWrapText(true);

        String stars = buildStarString(product.getRating());
        Label ratingLabel = new Label(stars + "  " + String.format("%.1f", product.getRating()));
        ratingLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #D4A853;");

        HBox priceBox = new HBox(8);
        priceBox.setAlignment(Pos.CENTER_LEFT);

        Label priceLabel = new Label("Rp" + Styles.formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723;"
        );
        priceBox.getChildren().add(priceLabel);

        if (product.isOnSale()) {
            Label originalLabel = new Label("Rp" + Styles.formatPrice(product.getPrice()));
            originalLabel.setStyle(
                    "-fx-font-size: 12px;" +
                            "-fx-text-fill: #BDBDBD;" +
                            "-fx-strikethrough: true;"
            );
            int discountPct = (int)(((product.getPrice() - product.getCurrentPrice()) / product.getPrice()) * 100);
            Label discountLabel = new Label("-" + discountPct + "%");
            discountLabel.setStyle(
                    "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: #E53935;" +
                            "-fx-background-radius: 5;" +
                            "-fx-padding: 2 6;"
            );
            priceBox.getChildren().addAll(originalLabel, discountLabel);
        }

        Region lineSep = new Region();
        lineSep.setPrefHeight(1);
        lineSep.setStyle("-fx-background-color: #F0EAE4;");

        HBox buttonBox = new HBox(8);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(4, 0, 0, 0));

        Button addBtn = new Button("🛒  Keranjang");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        addBtn.setStyle(
                "-fx-background-color: #EFEBE9;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 12px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-padding: 9 0; -fx-cursor: hand;"
        );

        Button buyBtn = new Button("Beli Sekarang");
        buyBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buyBtn, Priority.ALWAYS);
        buyBtn.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 12px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-padding: 9 0; -fx-cursor: hand;"
        );

        addBtn.setOnAction(e -> {
            e.consume();
            if (AuthService.isLoggedIn()) {
                CartService.addToCart(product, 1);
            } else { LoginRequiredDialog.show("Silakan masuk terlebih dahulu."); }
        });

        buyBtn.setOnAction(e -> {
            e.consume();
            if (AuthService.isLoggedIn()) {
                CartService.clearCart();
                CartService.addToCart(product, 1);
                SceneManager.setScene(new CheckoutPage().getScene());
            } else { LoginRequiredDialog.show("Silakan masuk terlebih dahulu."); }
        });

        buttonBox.getChildren().addAll(addBtn, buyBtn);
        infoBox.getChildren().addAll(nameLabel, ratingLabel, priceBox, lineSep, buttonBox);
        card.getChildren().addAll(imageContainer, infoBox);
        return card;
    }

    private String buildStarString(double rating) {
        int full  = (int) rating;
        int half  = (rating - full >= 0.5) ? 1 : 0;
        int empty = 5 - full - half;
        return "★".repeat(full) + (half == 1 ? "½" : "") + "☆".repeat(empty);
    }
}