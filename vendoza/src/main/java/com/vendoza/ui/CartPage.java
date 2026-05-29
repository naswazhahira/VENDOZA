package com.vendoza.ui;

import com.vendoza.model.CartItem;
import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class CartPage {

    private VBox cartItemsContainer;
    private Label totalLabel;

    public Scene getScene() {
        // CEK LOGIN: Jika belum login, arahkan ke login page
        if (!AuthService.isLoggedIn()) {
            showLoginRequiredAlert();
            return new LoginPage().getScene();
        }

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 40, 40, 40));

        Text title = new Text("🛒 Your Shopping Cart");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        // Cart Items Container
        cartItemsContainer = new VBox(10);
        cartItemsContainer.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");

        // Order Summary
        VBox summaryBox = new VBox(15);
        summaryBox.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        summaryBox.setPrefWidth(300);

        Label summaryTitle = new Label("Order Summary");
        summaryTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        totalLabel = new Label("Total: Rp 0");
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        Button checkoutBtn = new Button("Proceed to Checkout →");
        checkoutBtn.setStyle(Styles.buttonStyle());
        checkoutBtn.setOnMouseEntered(e -> checkoutBtn.setStyle(Styles.buttonHoverStyle()));
        checkoutBtn.setOnMouseExited(e -> checkoutBtn.setStyle(Styles.buttonStyle()));
        checkoutBtn.setOnAction(e -> {
            if (CartService.getCartItemCount() > 0) {
                SceneManager.setScene(new CheckoutPage().getScene());
            } else {
                showAlert("Cart Empty", "Please add items to your cart first.");
            }
        });

        Button continueBtn = new Button("Continue Shopping");
        continueBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_MEDIUM + ";" +
                "-fx-underline: true; -fx-cursor: hand;");
        continueBtn.setOnAction(e -> SceneManager.setScene(new HomePage().getScene()));

        summaryBox.getChildren().addAll(summaryTitle, new Separator(), totalLabel, checkoutBtn, continueBtn);

        // Layout
        HBox contentLayout = new HBox(30);
        contentLayout.setAlignment(Pos.TOP_LEFT);

        ScrollPane cartScroll = new ScrollPane(cartItemsContainer);
        cartScroll.setFitToWidth(true);
        cartScroll.setPrefHeight(500);
        cartScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        cartScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        contentLayout.getChildren().addAll(cartScroll, summaryBox);
        HBox.setHgrow(cartScroll, Priority.ALWAYS);

        mainContent.getChildren().addAll(title, contentLayout);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        Scene scene = new Scene(root, 1200, 700);

        // Refresh cart display
        refreshCart();

        return scene;
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
        Button homeBtn = createNavButton("Home", false);
        Button searchBtn = createNavButton("Search", false);
        Button cartBtn = createNavButton("Cart 🛒", true);
        Button profileBtn = createNavButton("Profile 👤", false);

        // Actions
        homeBtn.setOnAction(e -> SceneManager.setScene(new HomePage().getScene()));
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));
        cartBtn.setOnAction(e -> SceneManager.setScene(new CartPage().getScene()));
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

    private void refreshCart() {
        cartItemsContainer.getChildren().clear();

        if (CartService.getCartItemCount() == 0) {
            Label emptyLabel = new Label("Your cart is empty");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");
            cartItemsContainer.getChildren().add(emptyLabel);
            totalLabel.setText("Total: Rp 0");
            return;
        }

        for (CartItem item : CartService.getCartItems()) {
            VBox itemCard = createCartItemCard(item);
            cartItemsContainer.getChildren().add(itemCard);
        }

        totalLabel.setText("Total: Rp " + Styles.formatPrice(CartService.getCartTotal()));
    }

    private VBox createCartItemCard(CartItem item) {
        Product product = item.getProduct();

        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: " + Styles.BROWN_LIGHT + ";" +
                "-fx-border-radius: 10; -fx-padding: 15;");

        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        Label imagePlaceholder = new Label(product.getImageUrl());
        imagePlaceholder.setStyle("-fx-font-size: 40px;");

        VBox details = new VBox(5);
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        Label priceLabel = new Label("Rp " + Styles.formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.GOLD + ";");

        details.getChildren().addAll(nameLabel, priceLabel);

        // Quantity controls
        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER);

        Button minusBtn = new Button("-");
        minusBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 30; -fx-cursor: hand;");
        minusBtn.setOnAction(e -> {
            int newQty = item.getQuantity() - 1;
            if (newQty > 0) {
                CartService.updateQuantity(product, newQty);
            } else {
                CartService.removeFromCart(product);
            }
            refreshCart();
        });

        Label qtyLabel = new Label(String.valueOf(item.getQuantity()));
        qtyLabel.setStyle("-fx-font-size: 14px; -fx-min-width: 30; -fx-alignment: center;");

        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 30; -fx-cursor: hand;");
        plusBtn.setOnAction(e -> {
            CartService.updateQuantity(product, item.getQuantity() + 1);
            refreshCart();
        });

        quantityBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

        Label subtotalLabel = new Label("Subtotal: Rp " + Styles.formatPrice(item.getSubtotal()));
        subtotalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        HBox actionBox = new HBox(10);

        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.ERROR_RED + ";" +
                "-fx-underline: true; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> {
            CartService.removeFromCart(product);
            refreshCart();
        });

        // Buy Now button untuk item ini (checkout item ini saja)
        Button buyNowBtn = new Button("Buy Now");
        buyNowBtn.setStyle("-fx-background-color: " + Styles.GOLD + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 5 15; -fx-cursor: hand;");
        buyNowBtn.setOnAction(e -> {
            // Kosongkan keranjang, tambah item ini dengan quantity yang sama, lalu checkout
            int currentQty = item.getQuantity();
            CartService.clearCart();
            CartService.addToCart(product, currentQty);
            SceneManager.setScene(new CheckoutPage().getScene());
        });

        actionBox.getChildren().addAll(removeBtn, buyNowBtn);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        content.getChildren().addAll(imagePlaceholder, details, quantityBox, subtotalLabel, spacer, actionBox);
        card.getChildren().add(content);

        return card;
    }

    private void showLoginRequiredAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please login first");
        alert.setContentText("You need to login to access your cart.");
        ButtonType loginBtn = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(loginBtn, new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.showAndWait().ifPresent(response -> {
            if (response == loginBtn) {
                SceneManager.setScene(new LoginPage().getScene());
            }
        });
    }

    private void showLoginAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please login first");
        alert.setContentText("You need to login to access this feature.");
        ButtonType loginBtn = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(loginBtn, new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.showAndWait().ifPresent(response -> {
            if (response == loginBtn) {
                SceneManager.setScene(new LoginPage().getScene());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}