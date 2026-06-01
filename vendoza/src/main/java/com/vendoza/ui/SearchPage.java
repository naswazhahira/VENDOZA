package com.vendoza.ui;

import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import com.vendoza.service.DataService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchPage {

    private TextField searchField;
    private VBox recentSearchesBox;
    private FlowPane categoryChipsBox;
    private VBox resultsBox;
    private ComboBox<String> sortComboBox;
    private HBox sortSection;

    // Auto-suggest components
    private ListView<String> suggestionListView;
    private Popup suggestionPopup;
    private VBox suggestionContainer;
    private boolean isSuggestionShowing = false;

    // Result card navigation
    private final List<VBox> productCards = new ArrayList<>();
    private int currentSelectedCardIndex = -1;
    private ScrollPane mainScrollPane;

    // STATIC - agar data tetap tersimpan antar instance halaman
    private static final List<String> searchHistory = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 5;
    private List<Product> currentResults = new ArrayList<>();
    private String currentKeyword = "";

    // Cache for suggestions to improve performance
    private List<String> allProductNames = new ArrayList<>();
    private List<String> allCategories = new ArrayList<>();
    private List<Product> allProductsCache = new ArrayList<>();

    // References to sections for dynamic visibility
    private VBox headerSection;
    private VBox recentSection;
    private VBox categoriesSection;
    private VBox resultsSection;
    private boolean isManualSearch = false;
    private boolean isFromSuggestion = false;

    // Reference to the main content VBox for adjusting spacing
    private VBox mainContent;

    // Category mapping dengan lebih banyak keyword
    private static class CategoryMapping {
        final String displayName;
        final String icon;
        final List<String> keywords;
        final String bgColor;

        CategoryMapping(String displayName, String icon, List<String> keywords) {
            this.displayName = displayName;
            this.icon = icon;
            this.keywords = keywords;
            this.bgColor = "rgba(210, 180, 140, 0.15)";
        }

        CategoryMapping(String displayName, String icon, List<String> keywords, String bgColor) {
            this.displayName = displayName;
            this.icon = icon;
            this.keywords = keywords;
            this.bgColor = bgColor;
        }
    }

    private final List<CategoryMapping> categoryMappings = List.of(
            new CategoryMapping("Women's Fashion", "👗", List.of("Women", "woman", "dress", "blouse", "skirt", "knit", "blazer", "shirt")),
            new CategoryMapping("Men's Fashion", "👔", List.of("Men", "man", "jacket", "cargo", "denim", "sneakers")),
            new CategoryMapping("Accessories", "👜", List.of("Accessories", "bag", "scarf", "hat", "accessory", "tote")),
            new CategoryMapping("Footwear", "👟", List.of("shoes", "sneakers", "footwear", "shoe", "sneaker", "leather")),
            new CategoryMapping("Jewelry", "💍", List.of("jewelry", "necklace", "ring", "earring", "bracelet", "pendant"), "#F3E5F5")
    );

    public Scene getScene() {
        loadSuggestionData();

        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        HBox navBar = createNavBar();
        mainContent = createMainContent();

        mainScrollPane = new ScrollPane(mainContent);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        root.getChildren().addAll(navBar, mainScrollPane);
        VBox.setVgrow(mainScrollPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 1200, 700);

        // Keyboard handler untuk navigasi suggestion popup dan hasil pencarian
        scene.setOnKeyPressed(event -> {
            // Handle suggestion popup navigation (only when popup is showing)
            if (isSuggestionShowing && suggestionListView != null) {
                KeyCode code = event.getCode();
                int currentIndex = suggestionListView.getSelectionModel().getSelectedIndex();
                int lastIndex = suggestionListView.getItems().size() - 1;

                switch (code) {
                    case DOWN:
                        if (currentIndex < lastIndex) {
                            suggestionListView.getSelectionModel().select(currentIndex + 1);
                            suggestionListView.scrollTo(currentIndex + 1);
                        } else if (currentIndex == -1 && lastIndex >= 0) {
                            suggestionListView.getSelectionModel().select(0);
                            suggestionListView.scrollTo(0);
                        }
                        event.consume();
                        break;
                    case UP:
                        if (currentIndex > 0) {
                            suggestionListView.getSelectionModel().select(currentIndex - 1);
                            suggestionListView.scrollTo(currentIndex - 1);
                        } else if (currentIndex == 0) {
                            suggestionListView.getSelectionModel().select(-1);
                            searchField.requestFocus();
                        }
                        event.consume();
                        break;
                    case ENTER:
                        // ENTER ditangani oleh searchField.setOnKeyPressed
                        event.consume();
                        break;
                    case ESCAPE:
                        hideSuggestions();
                        event.consume();
                        break;
                    default:
                        break;
                }
            }
            // Handle result cards navigation (setelah pencarian)
            else if (resultsSection != null && resultsSection.isVisible() && !productCards.isEmpty()) {
                KeyCode code = event.getCode();

                switch (code) {
                    case DOWN:
                        selectNextProduct();
                        event.consume();
                        break;
                    case UP:
                        selectPreviousProduct();
                        event.consume();
                        break;
                    case PAGE_DOWN:
                        selectNextPage();
                        event.consume();
                        break;
                    case PAGE_UP:
                        selectPreviousPage();
                        event.consume();
                        break;
                    case ENTER:
                        if (currentSelectedCardIndex >= 0 && currentSelectedCardIndex < productCards.size()) {
                            Product selectedProduct = currentResults.get(currentSelectedCardIndex);
                            if (AuthService.isLoggedIn()) {
                                CartService.clearCart();
                                CartService.addToCart(selectedProduct, 1);
                                SceneManager.setScene(new CheckoutPage().getScene());
                            } else {
                                showLoginAlert();
                            }
                        }
                        event.consume();
                        break;
                    default:
                        break;
                }
            }
        });

        return scene;
    }

    private void selectNextProduct() {
        if (productCards.isEmpty()) return;

        // Remove previous selection style
        if (currentSelectedCardIndex >= 0) {
            productCards.get(currentSelectedCardIndex).setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 18;"
            );
        }

        currentSelectedCardIndex = Math.min(currentSelectedCardIndex + 1, productCards.size() - 1);
        applyCardSelectionStyle();
        scrollToSelectedCard();
    }

    private void selectPreviousProduct() {
        if (productCards.isEmpty()) return;

        // Remove previous selection style
        if (currentSelectedCardIndex >= 0) {
            productCards.get(currentSelectedCardIndex).setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 18;"
            );
        }

        currentSelectedCardIndex = Math.max(currentSelectedCardIndex - 1, 0);
        applyCardSelectionStyle();
        scrollToSelectedCard();
    }

    private void selectNextPage() {
        if (productCards.isEmpty()) return;
        int itemsPerPage = 6;
        int newIndex = Math.min(currentSelectedCardIndex + itemsPerPage, productCards.size() - 1);

        if (currentSelectedCardIndex >= 0) {
            productCards.get(currentSelectedCardIndex).setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 18;"
            );
        }

        currentSelectedCardIndex = newIndex;
        applyCardSelectionStyle();
        scrollToSelectedCard();
    }

    private void selectPreviousPage() {
        if (productCards.isEmpty()) return;
        int itemsPerPage = 6;
        int newIndex = Math.max(currentSelectedCardIndex - itemsPerPage, 0);

        if (currentSelectedCardIndex >= 0) {
            productCards.get(currentSelectedCardIndex).setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 18;"
            );
        }

        currentSelectedCardIndex = newIndex;
        applyCardSelectionStyle();
        scrollToSelectedCard();
    }

    private void applyCardSelectionStyle() {
        if (currentSelectedCardIndex >= 0 && currentSelectedCardIndex < productCards.size()) {
            productCards.get(currentSelectedCardIndex).setStyle(
                    "-fx-background-color: rgba(210, 180, 140, 0.3);" +
                            "-fx-background-radius: 15;" +
                            "-fx-padding: 18;" +
                            "-fx-border-color: " + Styles.GOLD + ";" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 15;"
            );
        }
    }

    private void scrollToSelectedCard() {
        if (currentSelectedCardIndex >= 0 && currentSelectedCardIndex < productCards.size()) {
            VBox selectedCard = productCards.get(currentSelectedCardIndex);
            double y = selectedCard.localToScene(selectedCard.getBoundsInLocal()).getMinY();
            double scrollPaneY = mainScrollPane.localToScene(mainScrollPane.getBoundsInLocal()).getMinY();
            double targetScroll = y - scrollPaneY - 50;

            double maxScroll = mainScrollPane.getContent().getBoundsInLocal().getHeight() - mainScrollPane.getHeight();
            if (maxScroll > 0) {
                double scrollValue = Math.max(0, Math.min(1, targetScroll / maxScroll));
                mainScrollPane.setVvalue(scrollValue);
            }
        }
    }

    private void loadSuggestionData() {
        allProductsCache = DataService.getAllProducts();
        allProductNames = allProductsCache.stream()
                .map(Product::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        allCategories = allProductsCache.stream()
                .map(Product::getCategory)
                .filter(cat -> cat != null && !cat.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> getSuggestions(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String lowerInput = input.toLowerCase().trim();
        List<String> suggestions = new ArrayList<>();

        for (String category : allCategories) {
            if (category.toLowerCase().contains(lowerInput)) {
                suggestions.add(category);
            }
        }

        for (String productName : allProductNames) {
            if (productName.toLowerCase().contains(lowerInput)) {
                suggestions.add(productName);
            }
        }

        suggestions = suggestions.stream().distinct().collect(Collectors.toList());

        if (suggestions.size() > 10) {
            suggestions = suggestions.subList(0, 10);
        }

        return suggestions;
    }

    private void showSuggestions(String input) {
        if (isManualSearch || isFromSuggestion) {
            return;
        }

        List<String> suggestions = getSuggestions(input);

        if (suggestions.isEmpty() || input.trim().isEmpty()) {
            hideSuggestions();
            return;
        }

        if (suggestionListView == null) {
            createSuggestionPopup();
        }

        suggestionListView.getItems().clear();
        suggestionListView.getItems().addAll(suggestions);
        suggestionListView.getSelectionModel().clearSelection();

        if (!isSuggestionShowing) {
            javafx.geometry.Bounds bounds = searchField.localToScreen(searchField.getBoundsInLocal());

            if (bounds != null) {
                double popupX = bounds.getMinX();
                double popupY = bounds.getMaxY();
                double popupWidth = searchField.getWidth();

                suggestionContainer.setPrefWidth(popupWidth);
                suggestionContainer.setMinWidth(popupWidth);
                suggestionListView.setPrefWidth(popupWidth);

                suggestionPopup.show(searchField.getScene().getWindow(), popupX, popupY);
                isSuggestionShowing = true;
                // Fokus tetap di searchField, tidak dipindah ke suggestionListView
                Platform.runLater(() -> searchField.requestFocus());
            }
        }
    }

    private void createSuggestionPopup() {
        suggestionContainer = new VBox();
        suggestionContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5);" +
                        "-fx-border-color: rgba(210, 180, 140, 0.5);" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-width: 1;"
        );

        suggestionListView = new ListView<>();
        suggestionListView.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 16;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 0;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-text-fill: " + Styles.TEXT_DARK + ";" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-background-insets: 0;"
        );
        suggestionListView.setPrefHeight(250);
        suggestionListView.setMaxHeight(300);

        suggestionListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equals(suggestionListView.getSelectionModel().getSelectedItem())) {
                        setStyle(
                                "-fx-padding: 12 15;" +
                                        "-fx-background-color: " + Styles.GOLD + ";" +
                                        "-fx-cursor: hand;" +
                                        "-fx-font-family: 'Segoe UI';" +
                                        "-fx-font-size: 14px;" +
                                        "-fx-text-fill: " + Styles.WHITE + ";"
                        );
                    } else {
                        setStyle(
                                "-fx-padding: 12 15;" +
                                        "-fx-background-color: transparent;" +
                                        "-fx-cursor: hand;" +
                                        "-fx-font-family: 'Segoe UI';" +
                                        "-fx-font-size: 14px;" +
                                        "-fx-text-fill: " + Styles.TEXT_DARK + ";"
                        );
                    }
                }
            }
        });

        suggestionListView.setOnMouseClicked(e -> {
            String selected = suggestionListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                searchField.setText(selected);
                Platform.runLater(() -> {
                    searchField.positionCaret(selected.length());
                    searchField.deselect();
                });
                hideSuggestions();
                isFromSuggestion = true;
                isManualSearch = true;
                performSearchFromSuggestion(selected);
            }
        });

        suggestionContainer.getChildren().add(suggestionListView);

        suggestionPopup = new Popup();
        suggestionPopup.getContent().add(suggestionContainer);
        suggestionPopup.setAutoHide(true);
        suggestionPopup.setOnAutoHide(event -> {
            isSuggestionShowing = false;
        });
    }

    private void hideSuggestions() {
        if (suggestionPopup != null && isSuggestionShowing) {
            suggestionPopup.hide();
            isSuggestionShowing = false;
        }
    }

    public VBox createMainContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(25, 40, 50, 40));

        headerSection = createHeaderSection();
        VBox searchSection = createSearchSection();
        sortSection = createSortSection();
        sortSection.setVisible(false);
        sortSection.setManaged(false);
        recentSection = createRecentSection();
        categoriesSection = createCategoriesSection();
        resultsSection = createResultsSection();
        resultsSection.setVisible(false);
        resultsSection.setManaged(false);

        content.getChildren().addAll(
                headerSection,
                searchSection,
                sortSection,
                recentSection,
                categoriesSection,
                resultsSection
        );

        return content;
    }

    private HBox createSortSection() {
        HBox sortBox = new HBox(15);
        sortBox.setAlignment(Pos.CENTER_RIGHT);
        sortBox.setPadding(new Insets(5, 0, 10, 0));

        Label sortLabel = new Label("Sort by:");
        sortLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll(
                "Relevance",
                "Price: Low to High",
                "Price: High to Low",
                "Name: A to Z"
        );
        sortComboBox.setValue("Relevance");
        sortComboBox.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-background-color: " + Styles.WHITE + ";" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-color: rgba(210, 180, 140, 0.5);" +
                        "-fx-border-radius: 25;" +
                        "-fx-padding: 6 15;" +
                        "-fx-min-width: 150px;"
        );

        sortComboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });

        sortComboBox.setOnAction(e -> sortAndDisplayResults());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        sortBox.getChildren().addAll(spacer, sortLabel, sortComboBox);
        return sortBox;
    }

    private void sortAndDisplayResults() {
        if (currentResults.isEmpty()) return;

        List<Product> sortedResults = new ArrayList<>(currentResults);
        String sortBy = sortComboBox.getValue();

        switch (sortBy) {
            case "Price: Low to High":
                sortedResults.sort(Comparator.comparingDouble(Product::getCurrentPrice));
                break;
            case "Price: High to Low":
                sortedResults.sort((a, b) -> Double.compare(b.getCurrentPrice(), a.getCurrentPrice()));
                break;
            case "Name: A to Z":
                sortedResults.sort(Comparator.comparing(Product::getName));
                break;
            default:
                break;
        }

        displayResultsDirect(sortedResults);
    }

    private VBox createHeaderSection() {
        VBox headerBox = new VBox(5);

        Text title = new Text("Find Your Style");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        headerBox.getChildren().addAll(title);
        return headerBox;
    }

    private VBox createSearchSection() {
        VBox searchSection = new VBox(10);
        searchSection.setAlignment(Pos.CENTER);

        Label searchLabel = new Label("What are you looking for?");
        searchLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        searchLabel.setAlignment(Pos.CENTER_LEFT);
        searchLabel.setMaxWidth(Double.MAX_VALUE);

        HBox searchBox = new HBox(12);
        searchBox.setAlignment(Pos.CENTER);

        Button backBtn = new Button("❮");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 8 0 0;");
        backBtn.setVisible(false);
        backBtn.setManaged(false);
        backBtn.setOnAction(e -> resetToInitialState());

        searchField = new TextField();
        searchField.setPromptText("Search for clothing, accessories, shoes...");
        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: rgba(210,180,140,0.5);" +
                        "-fx-border-radius: 30;" +
                        "-fx-border-width: 1.5;" +
                        "-fx-padding: 12 20;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-text-fill: " + Styles.TEXT_DARK + ";" +
                        "-fx-prompt-text-fill: #aaa;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.trim().isEmpty()) {
                if (!isManualSearch && !isFromSuggestion) {
                    Platform.runLater(() -> showSuggestions(newVal));
                }
            } else {
                hideSuggestions();
            }

            // Reset flags when user types new text
            if (isManualSearch || isFromSuggestion) {
                isManualSearch = false;
                isFromSuggestion = false;
            }
        });

        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                String currentText = searchField.getText();
                if (currentText != null && !currentText.trim().isEmpty()) {
                    if (!isManualSearch && !isFromSuggestion) {
                        Platform.runLater(() -> showSuggestions(currentText));
                    }
                }
            }
        });

        Button searchBtn = new Button("🔍 Search");
        searchBtn.setStyle(
                "-fx-background-color: " + Styles.BROWN_DARK + ";" +
                        "-fx-text-fill: " + Styles.WHITE + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 12 28;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);"
        );
        searchBtn.setOnMouseEntered(e -> searchBtn.setStyle(
                "-fx-background-color: " + Styles.GOLD + ";" +
                        "-fx-text-fill: " + Styles.BROWN_DARK + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 12 28;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);"
        ));
        searchBtn.setOnMouseExited(e -> searchBtn.setStyle(
                "-fx-background-color: " + Styles.BROWN_DARK + ";" +
                        "-fx-text-fill: " + Styles.WHITE + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 30;" +
                        "-fx-padding: 12 28;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 3);"
        ));
        searchBtn.setOnAction(e -> {
            performManualSearch();
            e.consume();
        });

        // Handler untuk tombol ENTER di search field menggunakan setOnKeyPressed
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (isSuggestionShowing) {
                    String selected = suggestionListView != null
                            ? suggestionListView.getSelectionModel().getSelectedItem()
                            : null;
                    if (selected != null) {
                        isFromSuggestion = true;
                        isManualSearch = true;
                        hideSuggestions();
                        searchField.setText(selected);
                        Platform.runLater(() -> {
                            searchField.positionCaret(selected.length());
                            searchField.deselect();
                        });
                        hideHeaderAndAdjustSpacing();
                        addToSearchHistory(selected);
                        List<Product> results = searchProductsExact(selected);
                        currentResults = results;
                        currentKeyword = selected;
                        displayResults(results, selected);
                    } else {
                        hideSuggestions();
                        performManualSearch();
                    }
                } else {
                    performManualSearch();
                }
                event.consume();
            }
        });

        HBox.setHgrow(searchField, Priority.ALWAYS);
        searchBox.getChildren().addAll(backBtn, searchField, searchBtn);

        searchSection.getProperties().put("searchLabel", searchLabel);
        searchSection.getProperties().put("backBtn", backBtn);

        searchSection.getChildren().addAll(searchLabel, searchBox);
        return searchSection;
    }

    private VBox createRecentSection() {
        VBox recentSection = new VBox(12);

        HBox recentHeader = new HBox(10);
        recentHeader.setAlignment(Pos.CENTER_LEFT);
        recentHeader.setPadding(new Insets(0, 0, 5, 0));

        Label recentLabel = new Label("Recent Searches");
        recentLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button clearAllBtn = new Button("Clear All");
        clearAllBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                "-fx-font-size: 12px; -fx-cursor: hand; -fx-underline: true; -fx-padding: 2 5;");
        clearAllBtn.setOnMouseEntered(e -> clearAllBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                        "-fx-font-size: 12px; -fx-cursor: hand; -fx-underline: true; -fx-padding: 2 5;"));
        clearAllBtn.setOnMouseExited(e -> clearAllBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                        "-fx-font-size: 12px; -fx-cursor: hand; -fx-underline: true; -fx-padding: 2 5;"));
        clearAllBtn.setOnAction(e -> {
            searchHistory.clear();
            loadRecentSearches();
        });

        recentHeader.getChildren().addAll(recentLabel, spacer, clearAllBtn);

        recentSearchesBox = new VBox(10);
        recentSearchesBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.75);" +
                "-fx-background-radius: 15; -fx-padding: 18;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        loadRecentSearches();
        recentSection.getChildren().addAll(recentHeader, recentSearchesBox);
        return recentSection;
    }

    private VBox createCategoriesSection() {
        VBox categoriesSection = new VBox(15);

        Label categoriesLabel = new Label("Shop by Category");
        categoriesLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        HBox cardsBox = new HBox();
        cardsBox.setAlignment(Pos.CENTER);
        cardsBox.setSpacing(0);
        cardsBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cardsBox, Priority.ALWAYS);

        // Simpan referensi ke cardsBox untuk dipakai loadCategoryChips
        categoryChipsBox = new FlowPane();
        categoryChipsBox.setVisible(false);
        categoryChipsBox.setManaged(false);

        // Build kartu langsung ke HBox dengan spacer antar kartu
        String[][] categoryImages = {
                {"Women's Fashion",  getClass().getResource("/images/women-fashion.jpeg").toExternalForm()},
                {"Men's Fashion",    getClass().getResource("/images/men.jpeg").toExternalForm()},
                {"Accessories",      getClass().getResource("/images/accessories.jpeg").toExternalForm()},
                {"Footwear",         getClass().getResource("/images/footware.jpeg").toExternalForm()},
                {"Jewelry",          getClass().getResource("/images/jawalery.jpeg").toExternalForm()}
        };

        for (int i = 0; i < categoryMappings.size(); i++) {
            CategoryMapping mapping = categoryMappings.get(i);
            String imageUrl = i < categoryImages.length ? categoryImages[i][1] : "";

            StackPane card = createCategoryCard(mapping, imageUrl);
            HBox.setHgrow(card, Priority.ALWAYS);
            card.setMaxWidth(Double.MAX_VALUE);

            cardsBox.getChildren().add(card);

            if (i < categoryMappings.size() - 1) {
                Region spacer = new Region();
                spacer.setPrefWidth(15);
                spacer.setMinWidth(15);
                spacer.setMaxWidth(15);
                cardsBox.getChildren().add(spacer);
            }
        }

        categoriesSection.getChildren().addAll(categoriesLabel, cardsBox);
        return categoriesSection;
    }

    private void loadCategoryChips() {
        categoryChipsBox.getChildren().clear();

        // Background image URLs per category (menggunakan warna gradient sebagai fallback)
        String[][] categoryImages = {
                {"Women's Fashion",  "https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=300&q=80"},
                {"Men's Fashion",    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&q=80"},
                {"Accessories",      "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=300&q=80"},
                {"Footwear",         "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&q=80"},
                {"Jewelry",          "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=300&q=80"}
        };

        for (int i = 0; i < categoryMappings.size(); i++) {
            CategoryMapping mapping = categoryMappings.get(i);
            String imageUrl = i < categoryImages.length ? categoryImages[i][1] : "";

            StackPane card = createCategoryCard(mapping, imageUrl);
            categoryChipsBox.getChildren().add(card);
        }
    }

    private StackPane createCategoryCard(CategoryMapping mapping, String imageUrl) {
        StackPane card = new StackPane();
        card.setPrefWidth(185);
        card.setPrefHeight(200);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-radius: 18;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.13), 10, 0, 0, 3);"
        );

        // Clip rounded
        javafx.scene.shape.Rectangle cardClip = new javafx.scene.shape.Rectangle();
        cardClip.setArcWidth(36);
        cardClip.setArcHeight(36);
        card.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            cardClip.setWidth(newVal.getWidth());
            cardClip.setHeight(newVal.getHeight());
        });
        card.setClip(cardClip);

        // Gambar full card dengan blur
        javafx.scene.image.ImageView imgView = new javafx.scene.image.ImageView();
        imgView.setPreserveRatio(false);
        imgView.setEffect(new javafx.scene.effect.GaussianBlur(4));
        card.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            imgView.setFitWidth(newVal.getWidth());
            imgView.setFitHeight(newVal.getHeight());
        });
        javafx.scene.image.Image img = new javafx.scene.image.Image(imageUrl, 300, 300, false, true, true);
        imgView.setImage(img);

        // Dark overlay
        javafx.scene.shape.Rectangle overlay = new javafx.scene.shape.Rectangle();
        overlay.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.30));
        card.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            overlay.setWidth(newVal.getWidth());
            overlay.setHeight(newVal.getHeight());
        });

        // Konten: icon + teks di tengah
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);

        Label iconLabel = new Label(mapping.icon);
        iconLabel.setStyle("-fx-font-size: 36px;");

        Label nameLabel = new Label(mapping.displayName.replace(" ", "\n"));
        nameLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-text-alignment: center;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 4, 0, 0, 1);"
        );
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(160);

        content.getChildren().addAll(iconLabel, nameLabel);
        card.getChildren().addAll(imgView, overlay, content);

        // Hover
        card.setOnMouseEntered(e -> {
            imgView.setEffect(new javafx.scene.effect.GaussianBlur(2));
            overlay.setFill(javafx.scene.paint.Color.rgb(139, 90, 43, 0.45));
            card.setStyle(
                    "-fx-background-radius: 18;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 16, 0, 0, 5);" +
                            "-fx-scale-x: 1.03;" +
                            "-fx-scale-y: 1.03;"
            );
        });
        card.setOnMouseExited(e -> {
            imgView.setEffect(new javafx.scene.effect.GaussianBlur(4));
            overlay.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.30));
            card.setStyle(
                    "-fx-background-radius: 18;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.13), 10, 0, 0, 3);"
            );
        });

        card.setOnMouseClicked(e -> {
            isManualSearch = true;
            isFromSuggestion = true;
            hideSuggestions();
            hideHeaderAndAdjustSpacing();
            searchField.setText(mapping.displayName);
            performSearchByCategory(mapping);
        });

        return card;
    }

    private VBox createResultsSection() {
        VBox resultsSection = new VBox(12);

        Label resultsLabel = new Label("Search Results");
        resultsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");
        resultsLabel.setPadding(new Insets(0, 0, 10, 0));

        resultsBox = new VBox(15);
        resultsBox.setStyle("-fx-background-color: transparent;" +
                "-fx-padding: 0;" +
                "-fx-spacing: 0;");
        resultsBox.setMaxWidth(Double.MAX_VALUE);

        resultsSection.getChildren().addAll(resultsLabel, resultsBox);
        return resultsSection;
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

        Button homeBtn = createNavButton("🏠  Home", false);
        Button searchBtn = createNavButton("🔍  Search", true);
        Button cartBtn = createNavButton("🛒  Cart", false);
        Button profileBtn = createNavButton("👤  Profile", false);

        homeBtn.setOnAction(e -> {
            isManualSearch = false;
            isFromSuggestion = false;
            resetToInitialState();
            SceneManager.setScene(new HomePage().getScene());
        });
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));

        cartBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new CartPage().getScene());
            else showLoginAlert();
        });

        profileBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new ProfilePage().getScene());
            else showLoginAlert();
        });

        navButtons.getChildren().addAll(homeBtn, searchBtn, cartBtn, profileBtn);
        navBar.getChildren().addAll(logo, spacer, navButtons);
        return navBar;
    }

    // ✅ HANYA SATU method createNavButton (yang sudah digabung)
    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);

        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: #3E2723;" +
                            "-fx-text-fill: #D4A853;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25;" +
                            "-fx-padding: 9 22;" +
                            "-fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #5D4037;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25;" +
                            "-fx-padding: 9 22;" +
                            "-fx-cursor: hand;"
            );

            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: #EFEBE9;" +
                            "-fx-text-fill: #3E2723;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25;" +
                            "-fx-padding: 9 22;" +
                            "-fx-cursor: hand;"
            ));

            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #5D4037;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25;" +
                            "-fx-padding: 9 22;" +
                            "-fx-cursor: hand;"
            ));
        }

        return btn;
    }

    private void performSearchFromSuggestion(String keyword) {
        if (keyword.isEmpty()) {
            resetToInitialState();
            isFromSuggestion = false;
            isManualSearch = false;
            return;
        }

        hideHeaderAndAdjustSpacing();
        addToSearchHistory(keyword);
        List<Product> results = searchProductsExact(keyword);
        currentResults = results;
        currentKeyword = keyword;
        displayResults(results, keyword);

        isFromSuggestion = true;
        isManualSearch = true;
    }

    private void performManualSearch() {
        System.out.println("🔍 Pencarian dimulai untuk: " + searchField.getText());
        hideSuggestions();
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            resetToInitialState();
            return;
        }

        isManualSearch = true;
        isFromSuggestion = true;
        hideHeaderAndAdjustSpacing();

        addToSearchHistory(keyword);
        List<Product> results = searchProductsExact(keyword);
        currentResults = results;
        currentKeyword = keyword;
        displayResults(results, keyword);
        System.out.println("✅ Pencarian selesai. Ditemukan " + results.size() + " produk.");
    }

    private List<Product> searchProductsExact(String keyword) {
        List<Product> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase().trim();

        String[] keywords = lowerKeyword.split("\\s+");

        for (Product product : allProductsCache) {
            String name = product.getName().toLowerCase();
            String category = product.getCategory() != null ? product.getCategory().toLowerCase() : "";
            String description = product.getDescription() != null ? product.getDescription().toLowerCase() : "";

            if (keywords.length == 1) {
                if (name.contains(lowerKeyword) || category.contains(lowerKeyword) || description.contains(lowerKeyword)) {
                    results.add(product);
                }
            } else {
                boolean matchesAll = true;
                for (String kw : keywords) {
                    if (kw.length() < 2) continue;
                    if (!name.contains(kw) && !category.contains(kw) && !description.contains(kw)) {
                        matchesAll = false;
                        break;
                    }
                }
                if (matchesAll) {
                    results.add(product);
                }
            }
        }

        if (results.isEmpty() && keywords.length > 1) {
            for (Product product : allProductsCache) {
                String name = product.getName().toLowerCase();
                for (String kw : keywords) {
                    if (kw.length() < 2) continue;
                    if (name.contains(kw)) {
                        results.add(product);
                        break;
                    }
                }
            }
        }

        results = results.stream().distinct().collect(Collectors.toList());

        return results;
    }

    private void performSearchByCategory(CategoryMapping mapping) {
        List<Product> results = new ArrayList<>();

        String targetCategory;
        switch (mapping.displayName) {
            case "Women's Fashion":
                targetCategory = "Women";
                break;
            case "Men's Fashion":
                targetCategory = "Men";
                break;
            case "Accessories":
                targetCategory = "Accessories";
                break;
            case "Footwear":
                targetCategory = "Footwear";
                break;
            case "Jewelry":
                targetCategory = "Jewelry";
                break;
            default:
                targetCategory = mapping.displayName;
        }

        for (Product product : allProductsCache) {
            String productCategory = product.getCategory();
            String productName = product.getName().toLowerCase();
            boolean added = false;

            if (productCategory != null && productCategory.equalsIgnoreCase(targetCategory)) {
                results.add(product);
                added = true;
            }
            else if (!added) {
                for (String keyword : mapping.keywords) {
                    if ((productCategory != null && productCategory.toLowerCase().contains(keyword.toLowerCase())) ||
                            productName.contains(keyword.toLowerCase())) {
                        results.add(product);
                        break;
                    }
                }
            }
        }

        results = new ArrayList<>(new java.util.LinkedHashSet<>(results));

        String searchTerm = mapping.displayName;
        currentResults = results;
        currentKeyword = searchTerm;

        addToSearchHistory(searchTerm);
        displayResults(results, searchTerm);
    }

    private void addToSearchHistory(String term) {
        if (!searchHistory.contains(term)) {
            searchHistory.addFirst(term);
            while (searchHistory.size() > MAX_HISTORY_SIZE) {
                searchHistory.remove(MAX_HISTORY_SIZE);
            }
            loadRecentSearches();
        }
    }

    private void displayResults(List<Product> results, String keyword) {
        resultsBox.getChildren().clear();
        productCards.clear();
        currentSelectedCardIndex = -1;

        if (resultsSection != null) {
            resultsSection.setVisible(true);
            resultsSection.setManaged(true);
        }

        if (sortSection != null) {
            sortSection.setVisible(true);
            sortSection.setManaged(true);
            sortComboBox.setValue("Relevance");
        }

        if (results.isEmpty()) {
            VBox emptyState = createEmptyState(keyword);
            resultsBox.getChildren().add(emptyState);
            return;
        }

        Label resultCount = new Label("Found " + results.size() + " product" + (results.size() > 1 ? "s" : ""));
        resultCount.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 13px;" +
                "-fx-padding: 0 0 10 0;");
        resultsBox.getChildren().add(resultCount);

        displayResultsDirect(results);
    }

    private void displayResultsDirect(List<Product> results) {
        if (resultsBox.getChildren().size() > 1) {
            resultsBox.getChildren().remove(1, resultsBox.getChildren().size());
        }

        if (results.isEmpty()) return;

        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(0));
        grid.setStyle("-fx-background-color: transparent;");

        int col = 0, row = 0;
        for (Product product : results) {
            VBox card = createProductCard(product);
            grid.add(card, col, row);
            productCards.add(card);

            // Add click handler to select card
            final int index = productCards.size() - 1;
            card.setOnMouseClicked(e -> {
                if (currentSelectedCardIndex >= 0 && currentSelectedCardIndex < productCards.size()) {
                    productCards.get(currentSelectedCardIndex).setStyle(
                            "-fx-background-color: white;" +
                                    "-fx-background-radius: 15;" +
                                    "-fx-padding: 18;"
                    );
                }
                currentSelectedCardIndex = index;
                applyCardSelectionStyle();
            });

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        resultsBox.getChildren().add(grid);

        // Auto-select first product
        if (!productCards.isEmpty()) {
            currentSelectedCardIndex = 0;
            applyCardSelectionStyle();
        }
    }

    private void hideHeaderAndAdjustSpacing() {
        if (headerSection != null) {
            headerSection.setVisible(false);
            headerSection.setManaged(false);
        }
        if (recentSection != null) {
            recentSection.setVisible(false);
            recentSection.setManaged(false);
        }
        if (categoriesSection != null) {
            categoriesSection.setVisible(false);
            categoriesSection.setManaged(false);
        }

        if (mainContent != null && mainContent.getChildren().size() > 1) {
            VBox searchSection = (VBox) mainContent.getChildren().get(1);
            if (searchSection.getProperties().containsKey("backBtn")) {
                Button backBtn = (Button) searchSection.getProperties().get("backBtn");
                backBtn.setVisible(true);
                backBtn.setManaged(true);
            }
            if (searchSection.getProperties().containsKey("searchLabel")) {
                Label searchLabel = (Label) searchSection.getProperties().get("searchLabel");
                searchLabel.setVisible(false);
                searchLabel.setManaged(false);
            }
        }

        if (mainContent != null) {
            mainContent.setSpacing(10);
        }
    }

    private void resetToInitialState() {
        hideSuggestions();
        productCards.clear();
        currentSelectedCardIndex = -1;

        if (headerSection != null) {
            headerSection.setVisible(true);
            headerSection.setManaged(true);
        }
        if (recentSection != null) {
            recentSection.setVisible(true);
            recentSection.setManaged(true);
        }
        if (categoriesSection != null) {
            categoriesSection.setVisible(true);
            categoriesSection.setManaged(true);
        }

        if (resultsSection != null) {
            resultsSection.setVisible(false);
            resultsSection.setManaged(false);
        }

        if (sortSection != null) {
            sortSection.setVisible(false);
            sortSection.setManaged(false);
        }

        if (mainContent != null && mainContent.getChildren().size() > 1) {
            VBox searchSection = (VBox) mainContent.getChildren().get(1);
            if (searchSection.getProperties().containsKey("backBtn")) {
                Button backBtn = (Button) searchSection.getProperties().get("backBtn");
                backBtn.setVisible(false);
                backBtn.setManaged(false);
            }
            if (searchSection.getProperties().containsKey("searchLabel")) {
                Label searchLabel = (Label) searchSection.getProperties().get("searchLabel");
                searchLabel.setVisible(true);
                searchLabel.setManaged(true);
            }
        }

        if (mainContent != null) {
            mainContent.setSpacing(15);
        }

        if (searchField != null) {
            searchField.clear();
        }
        if (resultsBox != null) {
            resultsBox.getChildren().clear();
        }

        currentResults.clear();
        currentKeyword = "";
        isManualSearch = false;
        isFromSuggestion = false;
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
                "-fx-padding: 18;");
        card.setPrefWidth(280);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(280);

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

        HBox priceBox = new HBox(10);
        priceBox.setAlignment(Pos.CENTER);

        if (product.getCurrentPrice() < product.getPrice() && product.getCurrentPrice() > 0) {
            Text discountedPriceText = new Text(formatPrice(product.getCurrentPrice()));
            discountedPriceText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: " + Styles.GOLD + ";");

            Text originalPriceText = new Text(formatPrice(product.getPrice()));
            originalPriceText.setStyle("-fx-font-size: 13px; -fx-fill: " + Styles.TEXT_LIGHT + ";");
            originalPriceText.setStrikethrough(true);

            priceBox.getChildren().addAll(discountedPriceText, originalPriceText);
        } else {
            double displayPrice = product.getCurrentPrice() > 0 ? product.getCurrentPrice() : product.getPrice();
            Text priceText = new Text(formatPrice(displayPrice));
            priceText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: " + Styles.GOLD + ";");
            priceBox.getChildren().add(priceText);
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
        if (price <= 0) return "Rp 0";
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
        if (name.contains("necklace") || name.contains("ring") || name.contains("earring")) return "💍";
        if ("Women".equals(category)) return "👗";
        if ("Men".equals(category)) return "👔";
        if ("Accessories".equals(category)) return "👜";
        if ("Footwear".equals(category)) return "👟";
        if ("Jewelry".equals(category)) return "💍";
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

        FlowPane recentChipsFlow = new FlowPane(10, 10);
        recentChipsFlow.setAlignment(Pos.CENTER_LEFT);

        for (String search : searchHistory) {
            HBox chip = createSearchChip(search);
            recentChipsFlow.getChildren().add(chip);
        }

        recentSearchesBox.getChildren().add(recentChipsFlow);
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
            loadRecentSearches();
        });

        chip.getChildren().addAll(searchLabel, deleteBtn);
        chip.setOnMouseClicked(e -> {
            isManualSearch = false;
            isFromSuggestion = false;
            hideSuggestions();
            hideHeaderAndAdjustSpacing();
            searchField.setText(search);
            performManualSearch();
        });

        return chip;
    }

    private void showLoginAlert() {
        hideSuggestions();
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
