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
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class SearchPage {

    private TextField searchField;
    private VBox recentSearchesBox;
    private FlowPane categoryChipsBox;
    private VBox resultsBox;

    // STATIC - agar data tetap tersimpan antar instance halaman
    private static final List<String> searchHistory = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 5;

    // References to sections for dynamic visibility
    private VBox recentSection;
    private VBox categoriesSection;
    private boolean isManualSearch = false;

    // Category mapping dengan lebih banyak keyword
    private static class CategoryMapping {
        final String displayName;
        final String icon;
        final List<String> keywords;

        CategoryMapping(String displayName, String icon, List<String> keywords) {
            this.displayName = displayName;
            this.icon = icon;
            this.keywords = keywords;
        }
    }

    private final List<CategoryMapping> categoryMappings = List.of(
            new CategoryMapping("Women's Fashion", "👗", List.of("Women", "woman", "dress", "blouse", "skirt", "knit", "blazer", "shirt")),
            new CategoryMapping("Men's Fashion", "👔", List.of("Men", "man", "jacket", "cargo", "denim", "sneakers")),
            new CategoryMapping("Accessories", "👜", List.of("Accessories", "bag", "scarf", "hat", "accessory", "tote")),
            new CategoryMapping("Footwear", "👟", List.of("shoes", "sneakers", "footwear", "shoe", "sneaker", "leather"))
    );

    public Scene getScene() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        HBox navBar = createNavBar();
        VBox mainContent = createMainContent();

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.getChildren().addAll(navBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(root, 1200, 700);
    }

    private VBox createMainContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25, 40, 50, 40));

        // Header Section
        VBox headerSection = createHeaderSection();

        // Search Section
        VBox searchSection = createSearchSection();

        // Recent Searches Section
        recentSection = createRecentSection();

        // Categories Section
        categoriesSection = createCategoriesSection();

        // Results Section
        VBox resultsSection = createResultsSection();

        content.getChildren().addAll(
                headerSection,
                searchSection,
                recentSection,
                categoriesSection,
                resultsSection
        );

        return content;
    }

    private VBox createHeaderSection() {
        VBox headerBox = new VBox(5);

        Text title = new Text("Find Your Style");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        Text subtitle = new Text("Discover the latest fashion trends");
        subtitle.setStyle("-fx-font-size: 14px; -fx-fill: " + Styles.TEXT_LIGHT + ";");

        headerBox.getChildren().addAll(title, subtitle);
        return headerBox;
    }

    private VBox createSearchSection() {
        VBox searchSection = new VBox(10);

        Label searchLabel = new Label("What are you looking for?");
        searchLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        HBox searchBox = new HBox(12);
        searchBox.setAlignment(Pos.CENTER);

        searchField = new TextField();
        searchField.setPromptText("Search for clothing, accessories, shoes...");
        searchField.setStyle(Styles.textFieldStyle());

        Button searchBtn = new Button("🔍 Search");
        searchBtn.setStyle("-fx-background-color: " + Styles.BROWN_DARK + ";" +
                "-fx-text-fill: " + Styles.WHITE + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 10 25;" +
                "-fx-cursor: hand;");
        searchBtn.setOnMouseEntered(e -> searchBtn.setStyle(
                "-fx-background-color: " + Styles.GOLD + ";" +
                        "-fx-text-fill: " + Styles.BROWN_DARK + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-cursor: hand;"));
        searchBtn.setOnMouseExited(e -> searchBtn.setStyle(
                "-fx-background-color: " + Styles.BROWN_DARK + ";" +
                        "-fx-text-fill: " + Styles.WHITE + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-cursor: hand;"));
        searchBtn.setOnAction(e -> performManualSearch());
        searchField.setOnAction(e -> performManualSearch());

        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchField, searchBtn);

        searchSection.getChildren().addAll(searchLabel, searchBox);
        return searchSection;
    }

    private VBox createRecentSection() {
        VBox recentSection = new VBox(12);

        Label recentLabel = new Label("Recent Searches");
        recentLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        recentSearchesBox = new VBox(10);
        recentSearchesBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.75);" +
                "-fx-background-radius: 15; -fx-padding: 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        loadRecentSearches();
        recentSection.getChildren().addAll(recentLabel, recentSearchesBox);
        return recentSection;
    }

    private VBox createCategoriesSection() {
        VBox categoriesSection = new VBox(12);

        Label categoriesLabel = new Label("Shop by Category");
        categoriesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        categoryChipsBox = new FlowPane(15, 15);
        categoryChipsBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.75);" +
                "-fx-background-radius: 15; -fx-padding: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        loadCategoryChips();
        categoriesSection.getChildren().addAll(categoriesLabel, categoryChipsBox);
        return categoriesSection;
    }

    private VBox createResultsSection() {
        VBox resultsSection = new VBox(12);

        Label resultsLabel = new Label("Search Results");
        resultsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        resultsBox = new VBox(15);
        resultsBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.75);" +
                "-fx-background-radius: 15; -fx-padding: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        resultsSection.getChildren().addAll(resultsLabel, resultsBox);
        return resultsSection;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(25);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");

        Label logo = new Label("VENDOZA");
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-letter-spacing: 1px;");

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Button homeBtn = createNavButton("Home", false);
        Button searchBtn = createNavButton("Search", true);
        Button cartBtn = createNavButton("Cart", false);
        Button profileBtn = createNavButton("Profile", false);

        homeBtn.setOnAction(e -> {
            isManualSearch = false;
            showAllSections();
            SceneManager.setScene(new HomePage().getScene());
        });
        searchBtn.setOnAction(e -> {
            isManualSearch = false;
            showAllSections();
            searchField.clear();
            resultsBox.getChildren().clear();
            // Buat instance baru tapi searchHistory tetap ada karena static
            SceneManager.setScene(new SearchPage().getScene());
        });
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
        String baseStyle = "-fx-background-color: transparent; -fx-font-size: 14px; -fx-cursor: hand;";

        if (isActive) {
            btn.setStyle(baseStyle + "-fx-text-fill: " + Styles.GOLD + ";" +
                    "-fx-font-weight: bold; -fx-border-color: " + Styles.GOLD + ";" +
                    "-fx-border-width: 0 0 2 0; -fx-border-style: solid;");
        } else {
            btn.setStyle(baseStyle + "-fx-text-fill: " + Styles.TEXT_DARK + ";");
            btn.setOnMouseEntered(e -> btn.setStyle(baseStyle + "-fx-text-fill: " + Styles.GOLD + ";" +
                    "-fx-font-weight: bold;"));
            btn.setOnMouseExited(e -> btn.setStyle(baseStyle + "-fx-text-fill: " + Styles.TEXT_DARK + ";"));
        }
        return btn;
    }

    private void loadCategoryChips() {
        categoryChipsBox.getChildren().clear();

        for (CategoryMapping mapping : categoryMappings) {
            Button categoryChip = new Button(mapping.icon + "  " + mapping.displayName);
            categoryChip.setStyle("-fx-background-color: rgba(210, 180, 140, 0.15);" +
                    "-fx-text-fill: " + Styles.TEXT_DARK + ";" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-background-radius: 30;" +
                    "-fx-padding: 10 22;" +
                    "-fx-cursor: hand;" +
                    "-fx-border-color: rgba(210, 180, 140, 0.3);" +
                    "-fx-border-radius: 30;");

            categoryChip.setOnAction(e -> {
                isManualSearch = false;
                showAllSections();
                searchField.setText(mapping.displayName);
                performSearchByCategory(mapping);
            });

            categoryChip.setOnMouseEntered(e -> categoryChip.setStyle(
                    "-fx-background-color: " + Styles.GOLD + ";" +
                            "-fx-text-fill: " + Styles.BROWN_DARK + ";" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 30;" +
                            "-fx-padding: 10 22;" +
                            "-fx-cursor: hand;" +
                            "-fx-border-color: transparent;"));

            categoryChip.setOnMouseExited(e -> categoryChip.setStyle(
                    "-fx-background-color: rgba(210, 180, 140, 0.15);" +
                            "-fx-text-fill: " + Styles.TEXT_DARK + ";" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: 600;" +
                            "-fx-background-radius: 30;" +
                            "-fx-padding: 10 22;" +
                            "-fx-cursor: hand;" +
                            "-fx-border-color: rgba(210, 180, 140, 0.3);" +
                            "-fx-border-radius: 30;"));

            categoryChipsBox.getChildren().add(categoryChip);
        }
    }

    private void performManualSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            CustomToast.showWarningToast(searchField.getScene().getWindow(), "Please enter a search keyword");
            return;
        }

        isManualSearch = true;
        hideNonResultSections();

        addToSearchHistory(keyword);
        List<Product> results = searchByCategoryOrKeyword(keyword);
        displayResults(results, keyword);
    }

    private void performSearchByCategory(CategoryMapping mapping) {
        List<Product> allProducts = DataService.getAllProducts();
        List<Product> results = new ArrayList<>();

        for (Product product : allProducts) {
            String productCategory = product.getCategory();
            String productName = product.getName().toLowerCase();

            for (String keyword : mapping.keywords) {
                if ((productCategory != null && productCategory.toLowerCase().contains(keyword.toLowerCase())) ||
                        productName.contains(keyword.toLowerCase())) {
                    results.add(product);
                    break;
                }
            }
        }

        results = new ArrayList<>(new java.util.LinkedHashSet<>(results));
        String searchTerm = mapping.displayName;

        addToSearchHistory(searchTerm);
        displayResults(results, searchTerm);
    }

    private void addToSearchHistory(String term) {
        if (!searchHistory.contains(term)) {
            searchHistory.addFirst(term);
            while (searchHistory.size() > MAX_HISTORY_SIZE) {
                searchHistory.remove(MAX_HISTORY_SIZE);
            }
            saveRecentSearches();
            loadRecentSearches();
        }
    }

    private List<Product> searchByCategoryOrKeyword(String keyword) {
        List<Product> allProducts = DataService.getAllProducts();
        List<Product> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Product product : allProducts) {
            if (matchesSearch(product, lowerKeyword)) {
                results.add(product);
            }
        }
        return results;
    }

    private boolean matchesSearch(Product product, String keyword) {
        String category = product.getCategory();
        return (category != null && category.toLowerCase().contains(keyword)) ||
                product.getName().toLowerCase().contains(keyword) ||
                (product.getDescription() != null && product.getDescription().toLowerCase().contains(keyword));
    }

    private void displayResults(List<Product> results, String keyword) {
        resultsBox.getChildren().clear();

        if (results.isEmpty()) {
            VBox emptyState = createEmptyState(keyword);
            resultsBox.getChildren().add(emptyState);
            return;
        }

        Label resultCount = new Label("Found " + results.size() + " product" + (results.size() > 1 ? "s" : ""));
        resultCount.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 13px;");
        resultsBox.getChildren().add(resultCount);

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        int col = 0, row = 0;
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

    private void hideNonResultSections() {
        if (recentSection != null) {
            recentSection.setVisible(false);
            recentSection.setManaged(false);
        }
        if (categoriesSection != null) {
            categoriesSection.setVisible(false);
            categoriesSection.setManaged(false);
        }
    }

    private void showAllSections() {
        if (recentSection != null) {
            recentSection.setVisible(true);
            recentSection.setManaged(true);
        }
        if (categoriesSection != null) {
            categoriesSection.setVisible(true);
            categoriesSection.setManaged(true);
        }
    }

    private VBox createEmptyState(String keyword) {
        VBox emptyState = new VBox(15);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(40, 20, 40, 20));

        Label emojiLabel = new Label("🔍");
        emojiLabel.setStyle("-fx-font-size: 48px;");

        Label messageLabel = new Label("No products found for \"" + keyword + "\"");
        messageLabel.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 16px; -fx-font-weight: 500;");

        Label suggestionLabel = new Label("Try different keywords or browse our categories");
        suggestionLabel.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 12px;");

        emptyState.getChildren().addAll(emojiLabel, messageLabel, suggestionLabel);
        return emptyState;
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        card.setPrefWidth(280);
        card.setAlignment(Pos.CENTER);

        Label imagePlaceholder = createImagePlaceholder(product);

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);

        Label categoryLabel = new Label(product.getCategory());
        categoryLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                "-fx-background-color: rgba(210, 180, 140, 0.15);" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 3 10;");

        HBox priceBox = new HBox(8);
        priceBox.setAlignment(Pos.CENTER);
        Label priceLabel = new Label(formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        if (product.isOnSale()) {
            Label originalLabel = new Label(formatPrice(product.getPrice()));
            originalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                    "-fx-strikethrough: true;");
            priceBox.getChildren().addAll(priceLabel, originalLabel);
        } else {
            priceBox.getChildren().add(priceLabel);
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addToCartBtn = createAddToCartButton(product);
        Button buyNowBtn = createBuyNowButton(product);

        buttonBox.getChildren().addAll(addToCartBtn, buyNowBtn);

        card.getChildren().addAll(imagePlaceholder, nameLabel, categoryLabel, priceBox, buttonBox);
        return card;
    }

    private Button createAddToCartButton(Product product) {
        Button btn = new Button("+ Add to Cart");
        btn.setStyle("-fx-background-color: rgba(210, 180, 140, 0.2);" +
                "-fx-text-fill: " + Styles.TEXT_DARK + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 8 18;" +
                "-fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + Styles.GOLD + ";" +
                        "-fx-text-fill: " + Styles.BROWN_DARK + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 8 18;" +
                        "-fx-cursor: hand;"));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: rgba(210, 180, 140, 0.2);" +
                        "-fx-text-fill: " + Styles.TEXT_DARK + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 8 18;" +
                        "-fx-cursor: hand;"));

        btn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.addToCart(product, 1);
                CustomToast.showSuccessCartToast(btn.getScene().getWindow(), product.getName());
            } else {
                showLoginAlert();
            }
        });
        return btn;
    }

    private Button createBuyNowButton(Product product) {
        Button btn = new Button("Buy Now");
        btn.setStyle("-fx-background-color: " + Styles.BROWN_DARK + ";" +
                "-fx-text-fill: " + Styles.WHITE + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 8 18;" +
                "-fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + Styles.GOLD + ";" +
                        "-fx-text-fill: " + Styles.BROWN_DARK + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 8 18;" +
                        "-fx-cursor: hand;"));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + Styles.BROWN_DARK + ";" +
                        "-fx-text-fill: " + Styles.WHITE + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 8 18;" +
                        "-fx-cursor: hand;"));

        btn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) {
                CartService.clearCart();
                CartService.addToCart(product, 1);
                SceneManager.setScene(new CheckoutPage().getScene());
            } else {
                showLoginAlert();
            }
        });
        return btn;
    }

    private String formatPrice(double price) {
        return String.format("Rp %,.0f", price).replace(",", ".");
    }

    private Label createImagePlaceholder(Product product) {
        String icon = getProductIcon(product);
        Label imagePlaceholder = new Label(icon);
        imagePlaceholder.setStyle("-fx-font-size: 55px; -fx-alignment: center;");
        imagePlaceholder.setMaxWidth(Double.MAX_VALUE);
        return imagePlaceholder;
    }

    private String getProductIcon(Product product) {
        String icon = product.getImageUrl();
        if (icon != null && !icon.isEmpty()) return icon;

        String category = product.getCategory();
        String name = product.getName().toLowerCase();

        if (name.contains("blazer") || name.contains("blouse") || name.contains("dress")) return "👗";
        if (name.contains("jacket") || name.contains("cargo")) return "👔";
        if (name.contains("bag") || name.contains("scarf") || name.contains("hat")) return "👜";
        if (name.contains("sneakers") || name.contains("shoe")) return "👟";
        if ("Women".equals(category)) return "👗";
        if ("Men".equals(category)) return "👔";
        if ("Accessories".equals(category)) return "👜";
        return "👕";
    }

    private void loadRecentSearches() {
        recentSearchesBox.getChildren().clear();

        if (searchHistory.isEmpty()) {
            Label emptyLabel = new Label("No recent searches");
            emptyLabel.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 12px;");
            recentSearchesBox.getChildren().add(emptyLabel);
            return;
        }

        FlowPane recentChips = new FlowPane(10, 10);
        for (String search : searchHistory) {
            HBox chip = createSearchChip(search);
            recentChips.getChildren().add(chip);
        }
        recentSearchesBox.getChildren().add(recentChips);
    }

    private HBox createSearchChip(String search) {
        HBox chip = new HBox(8);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.setStyle("-fx-background-color: rgba(210, 180, 140, 0.15);" +
                "-fx-background-radius: 20; -fx-padding: 6 12; -fx-cursor: hand;" +
                "-fx-border-color: rgba(210, 180, 140, 0.3); -fx-border-radius: 20;");

        Label searchLabel = new Label(search);
        searchLabel.setStyle("-fx-text-fill: " + Styles.TEXT_DARK + "; -fx-font-size: 12px;");

        Button deleteBtn = new Button("✕");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                "-fx-font-size: 10px; -fx-cursor: hand; -fx-padding: 0 0 0 5;");
        deleteBtn.setOnAction(e -> {
            searchHistory.remove(search);
            saveRecentSearches();
            loadRecentSearches();
        });

        chip.getChildren().addAll(searchLabel, deleteBtn);
        chip.setOnMouseClicked(e -> {
            isManualSearch = false;
            showAllSections();
            searchField.setText(search);
            performManualSearch();
        });

        return chip;
    }

    private void saveRecentSearches() {
        // Data sudah disimpan di static variable,
        // bisa ditambahkan ke file preferences jika perlu persistent storage
    }

    private void showLoginAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please login first");
        alert.setContentText("You need to login to access this feature.");
        ButtonType loginBtn = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(loginBtn, new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.showAndWait().ifPresent(response -> {
            if (response == loginBtn) SceneManager.setScene(new LoginPage().getScene());
        });
    }
}