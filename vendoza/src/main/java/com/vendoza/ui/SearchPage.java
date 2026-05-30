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
import java.util.ArrayList;
import java.util.List;

public class SearchPage {

    private TextField searchField;
    private VBox recentSearchesBox;
    private VBox resultsBox;
    private List<String> searchHistory = new ArrayList<>();

    public Scene getScene() {
        // Top Navigation Bar
        HBox navBar = createNavBar();

        // Main Content
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;"); // Ganti warna

        // Search Header
        Label title = new Label("🔍 Find Your Style");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-border-color: " + Styles.GOLD + ";" +
                "-fx-border-width: 0 0 3 0; -fx-padding: 0 0 10 0;");

        // Search Field
        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Search for clothing, accessories...");
        searchField.setStyle(Styles.textFieldStyle());
        searchField.setPrefWidth(500);

        Button searchBtn = new Button("🔍 Search");
        searchBtn.setStyle(Styles.buttonStyle());
        searchBtn.setOnMouseEntered(e -> {
            searchBtn.setStyle(Styles.buttonHoverStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), searchBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        searchBtn.setOnMouseExited(e -> {
            searchBtn.setStyle(Styles.buttonStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), searchBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        searchBtn.setOnAction(e -> performSearch());
        searchField.setOnAction(e -> performSearch());

        searchBox.getChildren().addAll(searchField, searchBtn);
        searchBox.setAlignment(Pos.CENTER);

        // Recent Searches Section
        VBox recentSection = new VBox(10);
        Label recentLabel = new Label("📜 Recent Searches");
        recentLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        recentSearchesBox = new VBox(8);
        recentSearchesBox.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        loadRecentSearches();
        recentSection.getChildren().addAll(recentLabel, recentSearchesBox);

        // Search Results Section
        VBox resultsSection = new VBox(10);
        Label resultsLabel = new Label("📦 Search Results");
        resultsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        resultsBox = new VBox(15);
        resultsBox.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        resultsSection.getChildren().addAll(resultsLabel, resultsBox);

        // Split layout
        HBox contentLayout = new HBox(30);
        contentLayout.setAlignment(Pos.TOP_LEFT);

        VBox leftPanel = new VBox(15, recentSection);
        leftPanel.setPrefWidth(300);

        VBox rightPanel = new VBox(15, resultsSection);
        rightPanel.setPrefWidth(700);

        contentLayout.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(title, searchBox, contentLayout);

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");

        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label logo = new Label("🔍 VENDOZA");
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Button homeBtn = createNavButton("🏠 Home", false);
        Button searchBtn = createNavButton("🔍 Search", true);
        Button cartBtn = createNavButton("🛒 Cart", false);
        Button profileBtn = createNavButton("👤 Profile", false);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
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

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) return;

        if (!searchHistory.contains(keyword)) {
            searchHistory.add(0, keyword);
            if (searchHistory.size() > 5) searchHistory.remove(5);
            loadRecentSearches();
        }

        List<Product> results = DataService.searchProducts(keyword);
        displayResults(results, keyword);
    }

    private void displayResults(List<Product> results, String keyword) {
        resultsBox.getChildren().clear();

        if (results.isEmpty()) {
            Label noResult = new Label("😔 No products found for \"" + keyword + "\"");
            noResult.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 14px; -fx-padding: 30 0;");
            noResult.setAlignment(Pos.CENTER);
            noResult.setMaxWidth(Double.MAX_VALUE);
            resultsBox.getChildren().add(noResult);
            return;
        }

        Label resultCount = new Label("📊 Found " + results.size() + " product(s)");
        resultCount.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 13px;");
        resultsBox.getChildren().add(resultCount);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        int col = 0;
        int row = 0;
        for (Product product : results) {
            VBox card = createProductCard(product);
            grid.add(card, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        resultsBox.getChildren().add(grid);
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(8);
        card.setStyle(Styles.cardStyle());
        card.setPrefWidth(300);
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
        imagePlaceholder.setStyle("-fx-font-size: 60px; -fx-alignment: center; -fx-padding: 20 0 10 0;");
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

    private void loadRecentSearches() {
        recentSearchesBox.getChildren().clear();
        if (searchHistory.isEmpty()) {
            Label emptyLabel = new Label("No recent searches");
            emptyLabel.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 12px;");
            recentSearchesBox.getChildren().add(emptyLabel);
            return;
        }

        for (String search : searchHistory) {
            HBox itemBox = new HBox(10);
            itemBox.setAlignment(Pos.CENTER_LEFT);
            itemBox.setPadding(new Insets(8, 12, 8, 12));
            itemBox.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                    "-fx-background-radius: 10; -fx-cursor: hand;");

            Label searchLabel = new Label("🔍 " + search);
            searchLabel.setStyle("-fx-text-fill: " + Styles.TEXT_DARK + ";");

            Button deleteBtn = new Button("✖");
            deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                    "-fx-cursor: hand;");
            deleteBtn.setOnAction(e -> {
                searchHistory.remove(search);
                loadRecentSearches();
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            itemBox.getChildren().addAll(searchLabel, spacer, deleteBtn);
            itemBox.setOnMouseClicked(e -> {
                searchField.setText(search);
                performSearch();
            });

            recentSearchesBox.getChildren().add(itemBox);
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
