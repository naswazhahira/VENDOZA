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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HomePage {

    private VBox contentArea;
    private ScrollPane scrollPane;

    public Scene getScene() {
        // Top Navigation Bar
        HBox navBar = createNavBar();

        // Content Area (Scrollable)
        contentArea = new VBox(25);
        contentArea.setPadding(new Insets(20, 40, 40, 40));
        // GUNAKAN WARNA BACKGROUND YANG LEBIH JELAS
        contentArea.setStyle("-fx-background-color: #E8DCD0;");  // Warna coklat cream yang jelas

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
        scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        // Main Layout
        VBox mainLayout = new VBox(navBar, scrollPane);
        mainLayout.setStyle("-fx-background-color: #E8DCD0;");

        Scene scene = new Scene(mainLayout, 1200, 700);
        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label logo = new Label("👗 VENDOZA");
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Button homeBtn = createNavButton("🏠 Home", true);
        Button searchBtn = createNavButton("🔍 Search", false);
        Button cartBtn = createNavButton("🛒 Cart", false);
        Button profileBtn = createNavButton("👤 Profile", false);

        homeBtn.setOnAction(e -> refreshHome());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));

        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                SceneManager.setScene(new CartPage().getScene());
            } else {
                showLoginAlert();
            }
        });

        profileBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                SceneManager.setScene(new ProfilePage().getScene());
            } else {
                showLoginAlert();
            }
        });

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

            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                        "-fx-font-size: 14px; -fx-cursor: hand;");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
                st.setToX(1.05);
                st.setToY(1.05);
                st.play();
            });

            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                        "-fx-font-size: 14px; -fx-cursor: hand;");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
                st.setToX(1);
                st.setToY(1);
                st.play();
            });
        }
        return btn;
    }

    private VBox createSaleBanner() {
        VBox banner = new VBox(15);

        // PERBAIKAN: Gunakan format gradient yang benar untuk JavaFX
        banner.setStyle("-fx-background-color: linear-gradient(to right, #5D4037, #8D6E63);" +
                "-fx-background-radius: 20; -fx-padding: 40;");

        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.1)));

        Label saleLabel = new Label("⚡ LIMITED TIME OFFER ⚡");
        saleLabel.setStyle("-fx-text-fill: " + Styles.GOLD + "; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label titleLabel = new Label("UP TO 50% OFF");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 42px; -fx-font-weight: bold;");

        Label descLabel = new Label("Season's hottest fashion picks. Limited time only!");
        descLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.85); -fx-font-size: 16px;");

        Button shopBtn = new Button("SHOP NOW →");
        shopBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 30; -fx-padding: 12 30;" +
                "-fx-cursor: hand;");

        shopBtn.setOnMouseEntered(e -> {
            shopBtn.setStyle("-fx-background-color: " + Styles.WHITE + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 30; -fx-padding: 12 30;" +
                    "-fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), shopBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        shopBtn.setOnMouseExited(e -> {
            shopBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 30; -fx-padding: 12 30;" +
                    "-fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), shopBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        shopBtn.setOnAction(e -> scrollToFlashSale());

        banner.getChildren().addAll(saleLabel, titleLabel, descLabel, shopBtn);
        return banner;
    }

    private VBox createFlashSaleSection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: transparent;");

        Label title = new Label("⚡ FLASH SALE");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + ";" +
                "-fx-border-width: 0 0 3 0; -fx-padding: 0 0 10 0;");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);
        productGrid.setStyle("-fx-background-color: transparent;");

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
        section.setStyle("-fx-background-color: transparent;");

        Label title = new Label("🌟 RECOMMENDED FOR YOU");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + ";" +
                "-fx-border-width: 0 0 3 0; -fx-padding: 0 0 10 0;");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);
        productGrid.setStyle("-fx-background-color: transparent;");

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
        section.setStyle("-fx-background-color: transparent;");

        Label title = new Label("📁 SHOP BY CATEGORY");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + ";" +
                "-fx-border-width: 0 0 3 0; -fx-padding: 0 0 10 0;");

        HBox categoriesBox = new HBox(20);
        categoriesBox.setAlignment(Pos.CENTER);
        categoriesBox.setStyle("-fx-background-color: transparent;");

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
            categoryCard.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.1)));

            categoryCard.setOnMouseEntered(e -> {
                categoryCard.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + ";" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 5);" +
                        "-fx-padding: 15;");
                ScaleTransition st = new ScaleTransition(Duration.millis(200), categoryCard);
                st.setToX(1.05);
                st.setToY(1.05);
                st.play();
            });

            categoryCard.setOnMouseExited(e -> {
                categoryCard.setStyle(Styles.cardStyle());
                ScaleTransition st = new ScaleTransition(Duration.millis(200), categoryCard);
                st.setToX(1);
                st.setToY(1);
                st.play();
            });

            categoryCard.setOnMouseClicked(e -> filterByCategory(cat[1]));

            Label emoji = new Label(cat[0]);
            emoji.setStyle("-fx-font-size: 48px;");
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
        card.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.08)));

        card.setOnMouseEntered(e -> {
            card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.15)));
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });

        card.setOnMouseExited(e -> {
            card.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.08)));
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        Label imagePlaceholder = new Label(product.getImageUrl());
        imagePlaceholder.setStyle("-fx-font-size: 70px; -fx-alignment: center; -fx-padding: 20 0 10 0;");
        imagePlaceholder.setMaxWidth(Double.MAX_VALUE);
        imagePlaceholder.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        HBox priceBox = new HBox(10);
        priceBox.setAlignment(Pos.CENTER);

        Label priceLabel = new Label("Rp " + Styles.formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

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

        Button addToCartBtn = new Button("🛒 Add");
        addToCartBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 20;" +
                "-fx-cursor: hand;");

        addToCartBtn.setOnMouseEntered(e -> {
            addToCartBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                    "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 20;" +
                    "-fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), addToCartBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        addToCartBtn.setOnMouseExited(e -> {
            addToCartBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                    "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 20;" +
                    "-fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), addToCartBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        addToCartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.addToCart(product, 1);
                showSuccessMessage("✅ " + product.getName() + " added to cart!");
            } else {
                showLoginAlert();
            }
        });

        Button buyNowBtn = new Button("Buy Now →");
        buyNowBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 20;" +
                "-fx-cursor: hand;");

        buyNowBtn.setOnMouseEntered(e -> {
            buyNowBtn.setStyle("-fx-background-color: " + Styles.BROWN_DARK + "; -fx-text-fill: " + Styles.WHITE + ";" +
                    "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 20;" +
                    "-fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), buyNowBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        buyNowBtn.setOnMouseExited(e -> {
            buyNowBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                    "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 8 20;" +
                    "-fx-cursor: hand;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), buyNowBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

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
        alert.setHeaderText("📁 " + category);
        alert.setContentText("Menampilkan produk untuk kategori " + category);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 20;");

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void refreshHome() {
        SceneManager.setScene(new HomePage().getScene());
    }

    private void scrollToFlashSale() {
        if (scrollPane != null) {
            scrollPane.setVvalue(0.35);
        }
    }

    private void showLoginAlert() {
        LoginRequiredDialog.show("You need to login to access this feature.");
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success ✨");
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 20;");

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }
}
