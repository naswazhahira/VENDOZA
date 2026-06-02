package com.vendoza.ui;

import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import com.vendoza.service.DataService;
import com.vendoza.service.ProductUpdateNotifier;
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
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.stage.Screen;

import java.io.InputStream;
import java.util.List;

public class HomePage {

    private VBox contentArea;
    private ScrollPane scrollPane;
    private ProductUpdateNotifier.ProductUpdateListener productUpdateListener;

    public HomePage() {
        // Setup listener untuk update rating
        productUpdateListener = updatedProduct -> {
            javafx.application.Platform.runLater(() -> {
                System.out.println("Product updated: " + updatedProduct.getName());
            });
        };
        ProductUpdateNotifier.getInstance().addListener(productUpdateListener);
    }

    public Scene getScene() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        HBox navBar = createNavBar();
        contentArea = new VBox(35);
        contentArea.setPadding(new Insets(30, 60, 60, 60));
        contentArea.setStyle("-fx-background-color: #ebddc3;");

        VBox saleBanner = createSaleBanner();
        VBox flashSaleSection = createFlashSaleSection();
        VBox recommendedSection = createRecommendedSection();

        contentArea.getChildren().addAll(saleBanner, flashSaleSection, recommendedSection);

        scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ebddc3; -fx-background: #ebddc3;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox mainLayout = new VBox(navBar, scrollPane);
        mainLayout.setStyle("-fx-background-color: #ebddc3;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css") != null ?
                getClass().getResource("/styles.css").toExternalForm() : "");
        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(30);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(16, 50, 16, 50));
        navBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );

        Label logo = new Label("VENDOZA");
        logo.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Georgia';"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navButtons = new HBox(8);
        navButtons.setAlignment(Pos.CENTER_RIGHT);

        Button homeBtn = createNavButton("🏠  Home", true);
        Button searchBtn = createNavButton("🔍  Search", false);
        Button cartBtn = createNavButton("🛒  Cart", false);
        Button profileBtn = createNavButton("👤  Profile", false);

        homeBtn.setOnAction(e -> refreshHome());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));

        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new CartPage().getScene());
            else showLoginAlert();
        });

        profileBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new ProfilePage().getScene());
            else showLoginAlert();
        });

        if (AuthService.isLoggedIn() && AuthService.isAdmin()) {
            Button adminBtn = createNavButton("📊  Dashboard", false);
            adminBtn.setOnAction(e -> SceneManager.setScene(new AdminDashboardPage().getScene()));
            navButtons.getChildren().addAll(homeBtn, adminBtn);
        } else {
            navButtons.getChildren().addAll(homeBtn, searchBtn, cartBtn, profileBtn);
        }
        navBar.getChildren().addAll(logo, spacer, navButtons);
        return navBar;
    }

    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        String activeStyle =
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;";
        String defaultStyle =
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #5D4037;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;";
        String hoverStyle =
                "-fx-background-color: #EFEBE9;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 9 22;" +
                        "-fx-cursor: hand;";

        btn.setStyle(isActive ? activeStyle : defaultStyle);

        if (!isActive) {
            btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));
        }
        return btn;
    }

    private void navigateToCategoryPage(String category) {
        SceneManager.setScene(new CategoryPage(category).getScene());
    }

    private VBox createSaleBanner() {
        VBox banner = new VBox(16);
        banner.setStyle(
                "-fx-background-color: linear-gradient(to right, #2C1810, #4E342E, #6D4C41);" +
                        "-fx-background-radius: 24;" +
                        "-fx-padding: 55 60 55 60;"
        );
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.2)));

        Label titleLabel = new Label("ELEVATE YOUR STYLE\nUP TO 50% OFF");
        titleLabel.getStyleClass().add("banner-title");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 42px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Georgia';" +
                        "-fx-line-spacing: 6px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 1);"
        );

        Label subLabel = new Label("A premium fashion hub for personalized style and timeless elegance");
        subLabel.setStyle(
                "-fx-text-fill: rgba(255,255,255,0.8);" +
                        "-fx-font-size: 14px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 3, 0, 0, 1);"
        );
        subLabel.setPadding(new Insets(0, 0, 20, 0));

        Button shopBtn = new Button("✦  EXPLORE COLLECTIONS  ✦");
        shopBtn.setStyle(
                "-fx-background-color: #D4A853;" +
                        "-fx-text-fill: #2C1810;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 13 38;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
        shopBtn.setOnMouseEntered(e -> shopBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 13 38;" +
                        "-fx-cursor: hand;"
        ));
        shopBtn.setOnMouseExited(e -> shopBtn.setStyle(
                "-fx-background-color: #D4A853;" +
                        "-fx-text-fill: #2C1810;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 13 38;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        ));
        shopBtn.setOnAction(e -> scrollToFlashSale());

        banner.getChildren().addAll(titleLabel, subLabel, shopBtn);
        return banner;
    }

    private VBox createFlashSaleSection() {
        VBox section = new VBox(20);

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label badge = new Label("  FLASH SALE ");
        badge.setStyle(
                "-fx-background-color: linear-gradient(to right, #D4A853, #E8C87A, #B8860B);" +
                        "-fx-text-fill: #2C1810;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 5 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 2, 0, 0, 1);"
        );

        Label title = new Label("Today's Limited Offers");
        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Georgia';"
        );

        titleRow.getChildren().addAll(badge, title);

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

        int col = 0, row = 0;
        for (Product product : DataService.getOnSaleProducts()) {
            VBox productCard = createProductCard(product, true);
            productGrid.add(productCard, col++, row);
            if (col > 3) { col = 0; row++; }
        }

        section.getChildren().addAll(titleRow, sep, productGrid);
        return section;
    }

    private VBox createRecommendedSection() {
        VBox section = new VBox(20);

        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label badge = new Label(" 🌟 PICKS ");
        badge.setStyle(
                "-fx-background-color: linear-gradient(to right, #D4A853, #E8C87A, #B8860B);" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 4 10;"
        );

        Label title = new Label("Recommended For You");
        title.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: 900;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Georgia';"
        );

        titleRow.getChildren().addAll(badge, title);

        Region sep = new Region();
        sep.setPrefHeight(3);
        sep.setStyle("-fx-background-color: linear-gradient(to right, #D4A853, transparent); -fx-background-radius: 3;");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(18);
        productGrid.setVgap(18);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(33.33);
            productGrid.getColumnConstraints().add(cc);
        }

        List<Product> recommendedProducts = DataService.getRecommendedProducts();
        int col = 0, row = 0;
        int mulaiDariIndeks = 4;
        if (recommendedProducts != null && recommendedProducts.size() > mulaiDariIndeks) {
            List<Product> produkDipotong = recommendedProducts.subList(mulaiDariIndeks, recommendedProducts.size());
            for (Product product : produkDipotong) {
                VBox productCard = createProductCard(product, false);
                productGrid.add(productCard, col++, row);
                if (col > 2) { col = 0; row++; }
            }
        } else {
            for (Product product : recommendedProducts) {
                VBox productCard = createProductCard(product, false);
                productGrid.add(productCard, col++, row);
                if (col > 2) { col = 0; row++; }
            }
        }
        section.getChildren().addAll(titleRow, sep, productGrid);
        return section;
    }

    private VBox createProductCard(Product product, boolean isFlashSale) {
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
            st.setToX(1.025);
            st.setToY(1.025);
            st.play();
        });
        card.setOnMouseExited(e -> {
            card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.07)));
            ScaleTransition st = new ScaleTransition(Duration.millis(180), card);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        card.setOnMouseClicked(e -> SceneManager.setScene(new ProductDetailPage(product).getScene()));
        
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(220);
        imageContainer.setMinHeight(220);
        imageContainer.setMaxHeight(220);
        imageContainer.setStyle(
                "-fx-background-color: #F5F0EA;" +
                        "-fx-background-radius: 18 18 0 0;"
        );

        Rectangle clip = new Rectangle();
        clip.setArcWidth(36);
        clip.setArcHeight(36);
        clip.widthProperty().bind(imageContainer.widthProperty());
        clip.heightProperty().bind(imageContainer.heightProperty());
        imageContainer.setClip(clip);

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
            String rawPath = product.getImageUrl();
            String imagePath = null;

            if (rawPath == null || rawPath.trim().isEmpty()) {
                throw new IllegalArgumentException("Image URL is empty for product: " + product.getName());
            }

            if (rawPath.startsWith("/")) {
                imagePath = rawPath;
            } else if (rawPath.startsWith("images/")) {
                imagePath = "/" + rawPath;
            } else {
                imagePath = "/images/" + rawPath;
            }

            InputStream imgStream = getClass().getResourceAsStream(imagePath);
            if (imgStream == null && !imagePath.equals(rawPath)) {
                imgStream = getClass().getResourceAsStream(rawPath.startsWith("/") ? rawPath : "/" + rawPath);
            }
            if (imgStream == null) {
                imgStream = getClass().getResourceAsStream("/" + rawPath.substring(rawPath.lastIndexOf("/") + 1));
            }

            if (imgStream == null) {
                throw new RuntimeException("Stream null untuk path: " + imagePath + " (raw: " + rawPath + ")");
            }

            Image img = new Image(imgStream);
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageContainer.getChildren().add(imageView);

        } catch (Exception ex) {
            Label fallbackLabel = new Label("🛍️");
            fallbackLabel.setStyle("-fx-font-size: 65px;");
            imageContainer.getChildren().add(fallbackLabel);
            System.err.println("GAGAL memuat gambar untuk " + product.getName() + ": " + ex.getMessage());
        }
        
        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(14, 16, 16, 16));

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Georgia';"
        );
        nameLabel.setWrapText(true);
        nameLabel.setPrefHeight(40);

        Label categoryTag = new Label("✦ " + product.getCategory());
        categoryTag.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-text-fill: #8D6E63;" +
                        "-fx-background-color: #F5F0EA;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 3 10;"
        );

        double averageRating = product.getAverageRating();

        int roundedRating = (int) Math.round(averageRating);
        int fullStars = Math.min(roundedRating, 5);
        int emptyStars = 5 - fullStars;
        String stars = "★".repeat(fullStars) + "☆".repeat(emptyStars);

        Label ratingLabel = new Label(stars + "  " + String.format("%.1f", averageRating));
        ratingLabel.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #D4A853;"
        );

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
                            "-fx-text-fill: #9E9E9E;" +
                            "-fx-font-family: 'Georgia' !important;"
            );

            Region customLine = new Region();
            customLine.setPrefHeight(1.2);
            customLine.setMaxHeight(1.2);
            customLine.setMinWidth(5);
            customLine.setStyle("-fx-background-color: #E53935;");

            StackPane strikethroughContainer = new StackPane();
            strikethroughContainer.setAlignment(Pos.CENTER);
            strikethroughContainer.getChildren().addAll(originalLabel, customLine);

            int discountPct = (int) (((product.getPrice() - product.getCurrentPrice()) / product.getPrice()) * 100);
            Label discountLabel = new Label("-" + discountPct + "%");
            discountLabel.setStyle(
                    "-fx-font-size: 11px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-color: #E53935;" +
                            "-fx-background-radius: 5;" +
                            "-fx-padding: 2 6;"
            );

            priceBox.getChildren().addAll(strikethroughContainer, discountLabel);
        }

        Region lineSep = new Region();
        lineSep.setPrefHeight(1);
        lineSep.setStyle("-fx-background-color: #F0EAE4;");

        HBox buttonBox = new HBox(8);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(4, 0, 0, 0));

        Button addToCartBtn = new Button("🛒  Add to Cart");
        addToCartBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addToCartBtn, Priority.ALWAYS);
        addToCartBtn.setStyle(
                "-fx-background-color: #EFEBE9;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 9 0;" +
                        "-fx-cursor: hand;"
        );
        addToCartBtn.setOnMouseEntered(e2 -> addToCartBtn.setStyle(
                "-fx-background-color: #D7CCC8;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 9 0;" +
                        "-fx-cursor: hand;"
        ));
        addToCartBtn.setOnMouseExited(e2 -> addToCartBtn.setStyle(
                "-fx-background-color: #EFEBE9;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 9 0;" +
                        "-fx-cursor: hand;"
        ));

        Button buyNowBtn = new Button("Buy Now");
        buyNowBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buyNowBtn, Priority.ALWAYS);
        buyNowBtn.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 9 0;" +
                        "-fx-cursor: hand;"
        );
        buyNowBtn.setOnMouseEntered(e2 -> buyNowBtn.setStyle(
                "-fx-background-color: #D4A853;" +
                        "-fx-text-fill: #2C1810;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 9 0;" +
                        "-fx-cursor: hand;"
        ));
        buyNowBtn.setOnMouseExited(e2 -> buyNowBtn.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 9 0;" +
                        "-fx-cursor: hand;"
        ));

        addToCartBtn.setOnAction(e -> {
            e.consume();
            if (AuthService.isLoggedIn()) {
                CartService.addToCart(product, 1);
                showSuccessMessage(product.getName() + " added to cart!");
            } else {
                showLoginAlert();
            }
        });

        buyNowBtn.setOnAction(e -> {
            e.consume();
            if (AuthService.isLoggedIn()) {
                CartService.clearCart();
                CartService.addToCart(product, 1);
                SceneManager.setScene(new CheckoutPage().getScene());
            } else {
                showLoginAlert();
            }
        });

        buttonBox.getChildren().addAll(addToCartBtn, buyNowBtn);
        infoBox.getChildren().addAll(nameLabel, categoryTag, ratingLabel, priceBox, lineSep, buttonBox);
        card.getChildren().addAll(imageContainer, infoBox);
        return card;
    }

    private void refreshHome() {
        SceneManager.setScene(new HomePage().getScene());
    }

    private void scrollToFlashSale() {
        if (scrollPane != null) {
            scrollPane.setVvalue(0.18);
        }
    }

    private void showLoginAlert() {
        LoginRequiredDialog.show("Please log in first to use this feature.");
    }

    private void showSuccessMessage(String message) {
        CustomDialog.showSuccess("Success", message);
    }
}
