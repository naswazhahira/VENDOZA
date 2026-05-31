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
import javafx.util.Duration;

import java.util.List;

public class HomePage {

    private VBox contentArea;
    private ScrollPane scrollPane;

    public Scene getScene() {
        // Top Navigation Bar
        HBox navBar = createNavBar();

        // Content Area (Scrollable)
        contentArea = new VBox(35);
        contentArea.setPadding(new Insets(30, 50, 50, 50));
        contentArea.setStyle("-fx-background-color: #E8DCD0;");

        // Banner Sale
        VBox saleBanner = createSaleBanner();

        // Flash Sale Section
        VBox flashSaleSection = createFlashSaleSection();

        // Recommended Products Section (Sekarang memuat +2 produk baru)
        VBox recommendedSection = createRecommendedSection();

        // Categories Section
        VBox categoriesSection = createCategoriesSection();

        // Menyusun komponen (Fitur AI dan Mix & Match sudah dihapus total dari sini)
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

        return new Scene(mainLayout, 1200, 750);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

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
            if (AuthService.isLoggedIn()) SceneManager.setScene(new CartPage().getScene());
            else showLoginAlert();
        });

        profileBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new ProfilePage().getScene());
            else showLoginAlert();
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
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + "; -fx-font-size: 14px; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + "; -fx-font-size: 14px; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_DARK + "; -fx-font-size: 14px; -fx-cursor: hand;"));
        }
        return btn;
    }

    private VBox createSaleBanner() {
        VBox banner = new VBox(15);
        banner.setStyle("-fx-background-color: linear-gradient(to right, #3E2723, #5D4037, #8D6E63);" +
                "-fx-background-radius: 20; -fx-padding: 50;");
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.15)));

        Label saleLabel = new Label("【 ⚡ LIMITED FLASH OFFER ⚡ 】");
        saleLabel.setStyle("-fx-text-fill: " + Styles.GOLD + "; -fx-font-size: 15px; -fx-font-weight: 900;");

        Label titleLabel = new Label("ELEVATE YOUR STYLE\nUP TO 50% OFF");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 38px; -fx-font-weight: bold; -fx-line-spacing: 5px;");

        Button shopBtn = new Button("EXPLORE COLLECTIONS");
        shopBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 30; -fx-padding: 12 35; -fx-cursor: hand;");

        shopBtn.setOnAction(e -> scrollToFlashSale());

        banner.getChildren().addAll(saleLabel, titleLabel, shopBtn);
        return banner;
    }

    private VBox createFlashSaleSection() {
        VBox section = new VBox(20);
        section.setStyle("-fx-background-color: transparent;");

        Label title = new Label("⚡ FLASH SALE");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + "; -fx-border-width: 0 0 3 0; -fx-padding: 0 0 8 0;");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);

        // Grid 4 kolom untuk Flash Sale
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            productGrid.getColumnConstraints().add(cc);
        }

        int col = 0, row = 0;
        for (Product product : DataService.getOnSaleProducts()) {
            VBox productCard = createProductCard(product);
            productGrid.add(productCard, col++, row);
            if (col > 3) { col = 0; row++; }
        }

        section.getChildren().addAll(title, productGrid);
        return section;
    }

    // SECTION RECOMMENDED: Ditambah 2 card baru sehingga total memuat 6 produk
    private VBox createRecommendedSection() {
        VBox section = new VBox(20);
        section.setStyle("-fx-background-color: transparent;");

        Label title = new Label("🌟 RECOMMENDED FOR YOU");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + "; -fx-border-width: 0 0 3 0; -fx-padding: 0 0 8 0;");

        GridPane productGrid = new GridPane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);

        // Diatur menjadi 3 kolom agar layout 6 kartu terbagi rata secara estetik (2 baris x 3 kolom)
        for (int i = 0; i < 3; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(33.33);
            productGrid.getColumnConstraints().add(cc);
        }

        List<Product> recommendedProducts = DataService.getRecommendedProducts();

        int col = 0, row = 0;
        for (Product product : recommendedProducts) {
            VBox productCard = createProductCard(product);
            productGrid.add(productCard, col++, row);
            if (col > 2) { // Pindah baris setelah kolom ke-3 penuh
                col = 0;
                row++;
            }
        }

        section.getChildren().addAll(title, productGrid);
        return section;
    }

    private VBox createCategoriesSection() {
        VBox section = new VBox(20);
        section.setStyle("-fx-background-color: transparent;");

        Label title = new Label("📁 SHOP BY CATEGORY");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + "; -fx-border-width: 0 0 3 0; -fx-padding: 0 0 8 0;");

        GridPane categoriesGrid = new GridPane();
        categoriesGrid.setHgap(15);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(25);
            categoriesGrid.getColumnConstraints().add(cc);
        }

        String[][] categories = {
                {"👗", "Women's Fashion"}, {"👔", "Men's Fashion"},
                {"👜", "Accessories"}, {"👟", "Footwear"}
        };

        int index = 0;
        for (String[] cat : categories) {
            VBox categoryCard = new VBox(12);
            categoryCard.setAlignment(Pos.CENTER);
            categoryCard.setStyle(Styles.cardStyle() + "-fx-padding: 25 15 25 15;");
            categoryCard.setMaxWidth(Double.MAX_VALUE);
            categoryCard.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.06)));

            categoryCard.setOnMouseEntered(e -> {
                categoryCard.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-background-radius: 15; -fx-padding: 25 15 25 15;");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), categoryCard);
                st.setToX(1.03); st.setToY(1.03); st.play();
            });

            categoryCard.setOnMouseExited(e -> {
                categoryCard.setStyle(Styles.cardStyle() + "-fx-padding: 25 15 25 15;");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), categoryCard);
                st.setToX(1); st.setToY(1); st.play();
            });

            categoryCard.setOnMouseClicked(e -> filterByCategory(cat[1]));

            Label emoji = new Label(cat[0]);
            emoji.setStyle("-fx-font-size: 44px;");
            Label name = new Label(cat[1]);
            name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

            categoryCard.getChildren().addAll(emoji, name);
            categoriesGrid.add(categoryCard, index++, 0);
        }

        section.getChildren().addAll(title, categoriesGrid);
        return section;
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(12);
        card.setStyle(Styles.cardStyle() + "-fx-padding: 15;");
        card.setMaxWidth(Double.MAX_VALUE);
        card.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.05)));

        card.setOnMouseEntered(e -> {
            card.setEffect(new DropShadow(18, Color.rgb(62, 39, 35, 0.15)));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.02); st.setToY(1.02); st.play();
        });
        card.setOnMouseExited(e -> {
            card.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.05)));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1); st.setToY(1); st.play();
        });

        // AREA REDIRECT GAMBAR RESOURCING LANGGUNG
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(180);
        imageContainer.setStyle("-fx-background-color: #F5F0EA; -fx-background-radius: 10;");

        try {
            // Memasang gambar langsung dari direktori: src/main/resources/images/
            Image img = new Image(getClass().getResourceAsStream(product.getImageUrl()));
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(160);
            imageView.setFitHeight(160);
            imageView.setPreserveRatio(true);
            imageContainer.getChildren().add(imageView);
        } catch (Exception ex) {
            // Gambar alternatif jika file resource belum disinkronisasi oleh IntelliJ
            Label fallbackLabel = new Label("🛍️");
            fallbackLabel.setStyle("-fx-font-size: 60px;");
            imageContainer.getChildren().add(fallbackLabel);
        }

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        nameLabel.setWrapText(true);
        nameLabel.setPrefHeight(40);

        HBox priceBox = new HBox(8);
        priceBox.setAlignment(Pos.CENTER_LEFT);

        Label priceLabel = new Label("Rp" + Styles.formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        priceBox.getChildren().add(priceLabel);
        if (product.isOnSale()) {
            Label originalLabel = new Label("Rp" + Styles.formatPrice(product.getPrice()));
            originalLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-strikethrough: true;");
            priceBox.getChildren().add(originalLabel);
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addToCartBtn = new Button("🛒 Add");
        addToCartBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addToCartBtn, Priority.ALWAYS);
        addToCartBtn.setStyle("-fx-background-color: #EFEBE9; -fx-text-fill: #3E2723; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 0; -fx-cursor: hand;");

        Button buyNowBtn = new Button("Buy");
        buyNowBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buyNowBtn, Priority.ALWAYS);
        buyNowBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + "; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 0; -fx-cursor: hand;");

        addToCartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.addToCart(product, 1);
                showSuccessMessage("✅ " + product.getName() + " added to cart!");
            } else { showLoginAlert(); }
        });

        buyNowBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.clearCart();
                CartService.addToCart(product, 1);
                SceneManager.setScene(new CheckoutPage().getScene());
            } else { showLoginAlert(); }
        });

        buttonBox.getChildren().addAll(addToCartBtn, buyNowBtn);
        card.getChildren().addAll(imageContainer, nameLabel, priceBox, buttonBox);
        return card;
    }

    private void filterByCategory(String category) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Category");
        alert.setHeaderText("📁 " + category);
        alert.setContentText("Menampilkan seluruh item koleksi premium " + category);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + "; -fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());
        alert.showAndWait();
    }

    private void refreshHome() {
        SceneManager.setScene(new HomePage().getScene());
    }

    private void scrollToFlashSale() {
        if (scrollPane != null) {
            scrollPane.setVvalue(0.28);
        }
    }

    private void showLoginAlert() {
        LoginRequiredDialog.show("Silakan masuk log terlebih dahulu untuk menggunakan fitur ini.");
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success ✨");
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + "; -fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());
        alert.showAndWait();
    }
}
