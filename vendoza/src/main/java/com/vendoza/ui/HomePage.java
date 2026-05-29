package com.vendoza.ui;

import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import com.vendoza.service.DataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class HomePage {

    private VBox contentArea;

    public Scene getScene() {
        // Top Navigation Bar
        HBox navBar = createNavBar();

        // Content Area (Scrollable)
        contentArea = new VBox(25);
        contentArea.setPadding(new Insets(20, 40, 40, 40));

        // Banner Sale
        VBox saleBanner = createSaleBanner();

        // Flash Sale Section
        VBox flashSaleSection = createFlashSaleSection();

        // Recommended Products Section
        VBox recommendedSection = createRecommendedSection();

        // Categories Section
        VBox categoriesSection = createCategoriesSection();

        contentArea.getChildren().addAll(saleBanner, flashSaleSection, recommendedSection, categoriesSection);

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + Styles.BROWN_PALE + "; -fx-background: " + Styles.BROWN_PALE + ";");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Main Layout
        VBox mainLayout = new VBox(navBar, scrollPane);
        mainLayout.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        return new Scene(mainLayout, 1200, 700);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        // Logo VENDOZA dengan icon
        Label logo = new Label("VENDOZA");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        // Spacer kiri dan kanan (biar menu di tengah)
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // Navigation Buttons
        Button homeBtn = createNavButton("Home", true);
        Button searchBtn = createNavButton("Search", false);
        Button cartBtn = createNavButton("Cart", false);
        Button profileBtn = createNavButton("Profile", false);

        // Actions
        homeBtn.setOnAction(e -> refreshHome());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));

        // Cart Button - Cek login
        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                SceneManager.setScene(new CartPage().getScene());
            } else {
                showLoginAlert();
            }
        });

        // Profile Button - Cek login
        profileBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                SceneManager.setScene(new ProfilePage().getScene());
            } else {
                showLoginAlert();
            }
        });

        // Susunan: Logo - Spacer Kiri - Menu - Spacer Kanan
        navBar.getChildren().addAll(logo, leftSpacer, homeBtn, searchBtn, cartBtn, profileBtn, rightSpacer);
        return navBar;
    }

    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        if (isActive) {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: " + Styles.GOLD + ";" +
                    "-fx-border-width: 0 0 2 0; -fx-border-style: solid; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                    "-fx-font-size: 14px; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                    "-fx-font-size: 14px; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                    "-fx-font-size: 14px; -fx-cursor: hand;"));
        }
        return btn;
    }

    private VBox createSaleBanner() {
        VBox banner = new VBox(15);
        banner.setStyle("-fx-background: linear-gradient(to right, " + Styles.BROWN_DARK + ", " + Styles.BROWN_MEDIUM + ");" +
                "-fx-background-color: linear-gradient(to right, " + Styles.BROWN_DARK + ", " + Styles.BROWN_MEDIUM + ");" +
                "-fx-background-radius: 20; -fx-padding: 40;");
        banner.setAlignment(Pos.CENTER_LEFT);

        Label saleLabel = new Label("✨ FLASH SALE ✨");
        saleLabel.setStyle("-fx-text-fill: " + Styles.GOLD + "; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label titleLabel = new Label("Up to 50% OFF");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        Label descLabel = new Label("Season's hottest fashion picks. Limited time only!");
        descLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 16px;");

        Button shopBtn = new Button("Shop Now →");
        shopBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 25;");
        shopBtn.setOnAction(e -> scrollToFlashSale());

        banner.getChildren().addAll(saleLabel, titleLabel, descLabel, shopBtn);
        return banner;
    }

    private VBox createFlashSaleSection() {
        VBox section = new VBox(15);

        Label title = new Label("⚡ Flash Sale");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);

        int col = 0;
        int row = 0;
        for (Product product : DataService.getOnSaleProducts()) {
            VBox productCard = createProductCard(product);
            productGrid.add(productCard, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        section.getChildren().addAll(title, productGrid);
        return section;
    }

    private VBox createRecommendedSection() {
        VBox section = new VBox(15);

        Label title = new Label("🌟 Recommended For You");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);

        int col = 0;
        int row = 0;
        for (Product product : DataService.getRecommendedProducts()) {
            VBox productCard = createProductCard(product);
            productGrid.add(productCard, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        section.getChildren().addAll(title, productGrid);
        return section;
    }

    private VBox createCategoriesSection() {
        VBox section = new VBox(15);

        Label title = new Label("📁 Shop by Category");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        HBox categoriesBox = new HBox(20);
        categoriesBox.setAlignment(Pos.CENTER);

        String[][] categories = {
                {"👗", "Women's Fashion"},
                {"👔", "Men's Fashion"},
                {"👜", "Accessories"},
                {"👟", "Footwear"}
        };

        for (String[] cat : categories) {
            VBox categoryCard = new VBox(10);
            categoryCard.setAlignment(Pos.CENTER);
            categoryCard.setStyle(Styles.cardStyle());
            categoryCard.setPrefWidth(200);
            categoryCard.setOnMouseClicked(e -> filterByCategory(cat[1]));

            Label emoji = new Label(cat[0]);
            emoji.setStyle("-fx-font-size: 40px;");
            Label name = new Label(cat[1]);
            name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

            categoryCard.getChildren().addAll(emoji, name);
            categoriesBox.getChildren().add(categoryCard);
        }

        section.getChildren().addAll(title, categoriesBox);
        return section;
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(8);
        card.setStyle(Styles.cardStyle());
        card.setPrefWidth(250);

        Label imagePlaceholder = new Label(product.getImageUrl());
        imagePlaceholder.setStyle("-fx-font-size: 60px; -fx-alignment: center;");
        imagePlaceholder.setMaxWidth(Double.MAX_VALUE);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        nameLabel.setWrapText(true);

        HBox priceBox = new HBox(10);
        Label priceLabel = new Label("Rp " + Styles.formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        if (product.isOnSale()) {
            Label originalLabel = new Label("Rp " + Styles.formatPrice(product.getPrice()));
            originalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                    "-fx-strikethrough: true;");
            priceBox.getChildren().addAll(priceLabel, originalLabel);
        } else {
            priceBox.getChildren().add(priceLabel);
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        // Add to Cart Button - Cek login
        Button addToCartBtn = new Button("+ add");
        addToCartBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                "-fx-font-size: 12px; -fx-background-radius: 20; -fx-padding: 5 15; -fx-cursor: hand;");
        addToCartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.addToCart(product, 1);
                showSuccessMessage("Added to cart!");
            } else {
                showLoginAlert();
            }
        });

        // Checkout Button - Cek login
        Button buyNowBtn = new Button("Checkout");
        buyNowBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 5 15; -fx-cursor: hand;");
        buyNowBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.clearCart();
                CartService.addToCart(product, 1);
                SceneManager.setScene(new CheckoutPage().getScene());
            } else {
                showLoginAlert();
            }
        });

        buttonBox.getChildren().addAll(addToCartBtn, buyNowBtn);
        card.getChildren().addAll(imagePlaceholder, nameLabel, priceBox, buttonBox);
        return card;
    }

    private void filterByCategory(String category) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category");
        alert.setHeaderText(category);
        alert.setContentText("Menampilkan produk untuk kategori " + category);
        alert.showAndWait();
    }

    private void refreshHome() {
        SceneManager.setScene(new HomePage().getScene());
    }

    private void scrollToFlashSale() {
        contentArea.requestLayout();
    }

    private void showLoginAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please login first");
        alert.setContentText("You need to login to access this feature.");
        ButtonType loginBtn = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(loginBtn, cancelBtn);

        alert.showAndWait().ifPresent(response -> {
            if (response == loginBtn) {
                SceneManager.setScene(new LoginPage().getScene());
            }
        });
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}