package com.vendoza.ui;

import com.vendoza.model.CartItem;
import com.vendoza.model.Order;
import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.List;
import java.util.UUID;

public class CheckoutPage {

    private ComboBox<String> shippingCombo;
    private ComboBox<String> paymentCombo;
    private Label shippingCostLabel;
    private Label totalLabel;
    private TextArea addressArea;
    private Label addressError;
    private VBox itemsContainer;

    public Scene getScene() {
        // CEK LOGIN: Jika belum login, arahkan ke login page
        if (!AuthService.isLoggedIn()) {
            showLoginRequiredAlert();
            return new LoginPage().getScene();
        }

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 40, 40, 40));

        Text title = new Text("Checkout");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK + ";");

        HBox contentLayout = new HBox(30);
        contentLayout.setAlignment(Pos.TOP_LEFT);

        // Left Panel - Order Items
        VBox leftPanel = new VBox(15);
        leftPanel.setPrefWidth(600);

        Label itemsLabel = new Label("Your Items");
        itemsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        itemsContainer = new VBox(10);
        itemsContainer.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;");

        refreshItemsContainer();

        leftPanel.getChildren().addAll(itemsLabel, itemsContainer);

        // Right Panel - Shipping & Payment
        VBox rightPanel = new VBox(15);
        rightPanel.setPrefWidth(400);

        Label shippingLabel = new Label("Shipping Information");
        shippingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        // Shipping Address
        addressArea = new TextArea();
        addressArea.setPromptText("Enter your shipping address");
        addressArea.setStyle(Styles.textFieldStyle());
        addressArea.setPrefHeight(80);

        // Pre-fill address if available
        User currentUser = AuthService.getCurrentUser();
        if (currentUser != null && currentUser.getAddress() != null && !currentUser.getAddress().isEmpty()) {
            addressArea.setText(currentUser.getAddress());
        }

        addressError = new Label();
        addressError.setStyle("-fx-text-fill: " + Styles.ERROR_RED + "; -fx-font-size: 12px;");

        // Shipping Method
        Label methodLabel = new Label("Shipping Method");
        methodLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        shippingCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Regular (3-5 days) - Rp 15.000",
                "Express (1-2 days) - Rp 35.000",
                "Same Day - Rp 75.000"
        ));
        shippingCombo.setStyle(Styles.textFieldStyle());
        shippingCombo.setPrefWidth(Double.MAX_VALUE);
        shippingCombo.getSelectionModel().selectFirst();
        shippingCombo.setOnAction(e -> updateTotal());

        // Payment Method
        Label paymentLabel = new Label("💳 Payment Method");
        paymentLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        paymentCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Bank Transfer (BCA/Mandiri/BNI)",
                "E-Wallet (GoPay/OVO/Dana)",
                "Credit Card (Visa/Mastercard)",
                "Cash on Delivery (COD)"
        ));
        paymentCombo.setStyle(Styles.textFieldStyle());
        paymentCombo.setPrefWidth(Double.MAX_VALUE);
        paymentCombo.getSelectionModel().selectFirst();

        // Order Summary
        VBox summaryBox = new VBox(10);
        summaryBox.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background-radius: 10; -fx-padding: 15;");

        Label subtotalText = new Label("Subtotal: Rp " + Styles.formatPrice(CartService.getCartTotal()));
        subtotalText.setStyle("-fx-font-size: 14px;");

        shippingCostLabel = new Label("Shipping: Rp 15.000");
        shippingCostLabel.setStyle("-fx-font-size: 14px;");

        totalLabel = new Label("Total: Rp " + Styles.formatPrice(CartService.getCartTotal() + 15000));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        summaryBox.getChildren().addAll(subtotalText, shippingCostLabel, new Separator(), totalLabel);

        // Buttons
        Button placeOrderBtn = new Button("Place Order ✓");
        placeOrderBtn.setStyle(Styles.buttonStyle());
        placeOrderBtn.setOnMouseEntered(e -> placeOrderBtn.setStyle(Styles.buttonHoverStyle()));
        placeOrderBtn.setOnMouseExited(e -> placeOrderBtn.setStyle(Styles.buttonStyle()));
        placeOrderBtn.setOnAction(e -> placeOrder());

        Button backBtn = new Button("← Back to Cart");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_MEDIUM + ";" +
                "-fx-underline: true; -fx-cursor: hand;");
        backBtn.setOnAction(e -> SceneManager.setScene(new CartPage().getScene()));

        rightPanel.getChildren().addAll(shippingLabel, addressArea, addressError, methodLabel, shippingCombo,
                paymentLabel, paymentCombo, summaryBox, placeOrderBtn, backBtn);

        contentLayout.getChildren().addAll(leftPanel, rightPanel);
        mainContent.getChildren().addAll(title, contentLayout);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        return new Scene(root, 1200, 700);
    }

    private void refreshItemsContainer() {
        if (itemsContainer == null) return;

        itemsContainer.getChildren().clear();

        if (CartService.getCartItemCount() == 0) {
            Label emptyLabel = new Label("Your cart is empty");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");
            itemsContainer.getChildren().add(emptyLabel);
            return;
        }

        for (CartItem item : CartService.getCartItems()) {
            VBox itemRow = new VBox(8);
            itemRow.setStyle("-fx-border-color: " + Styles.BROWN_LIGHT + ";" +
                    "-fx-border-radius: 8; -fx-padding: 10;");

            HBox itemHeader = new HBox(10);
            itemHeader.setAlignment(Pos.CENTER_LEFT);

            Label imageLabel = new Label(item.getProduct().getImageUrl());
            imageLabel.setStyle("-fx-font-size: 30px;");

            Label nameLabel = new Label(item.getProduct().getName());
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
            nameLabel.setPrefWidth(250);

            Region spacer1 = new Region();
            HBox.setHgrow(spacer1, Priority.ALWAYS);

            itemHeader.getChildren().addAll(imageLabel, nameLabel, spacer1);

            // Quantity and price controls
            HBox itemFooter = new HBox(20);
            itemFooter.setAlignment(Pos.CENTER_LEFT);

            Label priceLabel = new Label("Rp " + Styles.formatPrice(item.getProduct().getCurrentPrice()));
            priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.GOLD + ";");

            // Quantity controls di checkout
            HBox quantityBox = new HBox(10);
            quantityBox.setAlignment(Pos.CENTER);

            IntegerProperty qty = new SimpleIntegerProperty(item.getQuantity());

            Button minusBtn = new Button("-");
            minusBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 30; -fx-cursor: hand;");
            minusBtn.setOnAction(e -> {
                if (qty.get() > 1) {
                    qty.set(qty.get() - 1);
                    CartService.updateQuantity(item.getProduct(), qty.get());
                    refreshCheckout();
                }
            });

            Label qtyLabel = new Label();
            qtyLabel.textProperty().bind(qty.asString());
            qtyLabel.setStyle("-fx-font-size: 14px; -fx-min-width: 30; -fx-alignment: center;");

            Button plusBtn = new Button("+");
            plusBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.TEXT_DARK + ";" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 30; -fx-cursor: hand;");
            plusBtn.setOnAction(e -> {
                qty.set(qty.get() + 1);
                CartService.updateQuantity(item.getProduct(), qty.get());
                refreshCheckout();
            });

            quantityBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

            Label subtotalLabel = new Label();
            subtotalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

            // Bind subtotal ke quantity
            subtotalLabel.textProperty().bind(Bindings.createStringBinding(() ->
                    "Rp " + Styles.formatPrice(item.getProduct().getCurrentPrice() * qty.get()), qty));

            Region spacer2 = new Region();
            HBox.setHgrow(spacer2, Priority.ALWAYS);

            itemFooter.getChildren().addAll(priceLabel, quantityBox, spacer2, subtotalLabel);

            itemRow.getChildren().addAll(itemHeader, itemFooter);
            itemsContainer.getChildren().add(itemRow);
        }
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
        Button cartBtn = createNavButton("Cart", false);
        Button profileBtn = createNavButton("Profile", false);

        // Actions
        homeBtn.setOnAction(e -> SceneManager.setScene(new HomePage().getScene()));
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

    private void updateTotal() {
        String selected = shippingCombo.getValue();
        double shippingCost = 15000;
        if (selected != null) {
            if (selected.contains("Express")) shippingCost = 35000;
            if (selected.contains("Same Day")) shippingCost = 75000;
        }

        shippingCostLabel.setText("Shipping: Rp " + Styles.formatPrice(shippingCost));
        double total = CartService.getCartTotal() + shippingCost;
        totalLabel.setText("Total: Rp " + Styles.formatPrice(total));
    }

    private void refreshCheckout() {
        refreshItemsContainer();
        updateTotal();
    }

    private void placeOrder() {
        String address = addressArea.getText().trim();
        if (address.isEmpty()) {
            addressError.setText("Please enter shipping address");
            return;
        }

        if (CartService.getCartItemCount() == 0) {
            addressError.setText("Your cart is empty");
            return;
        }

        String selected = shippingCombo.getValue();
        double shippingCost = 15000;
        if (selected != null) {
            if (selected.contains("Express")) shippingCost = 35000;
            if (selected.contains("Same Day")) shippingCost = 75000;
        }

        // ===== LOGIKA STATUS BERDASARKAN METODE PEMBAYARAN =====
        String paymentMethod = paymentCombo.getValue();
        String orderStatus;

        if (paymentMethod != null && paymentMethod.equals("Cash on Delivery (COD)")) {
            orderStatus = "Pending";  // Belum Bayar (COD)
        } else {
            orderStatus = "Processing";  // Dikemas (sudah bayar via Bank/E-Wallet/Credit Card)
        }

        // Create order
        String orderId = "VEN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        List<CartItem> items = CartService.getCartItems();
        Order order = new Order(orderId, AuthService.getCurrentUser(), items, shippingCost, address);
        order.setStatus(orderStatus);
        AuthService.getCurrentUser().getOrders().add(order);

        // Clear cart
        CartService.clearCart();

        // Status message
        String statusMessage = (orderStatus.equals("Pending")) ?
                "📌 Pembayaran COD akan dilakukan saat barang diterima.\nPesanan akan segera diproses." :
                "✅ Pembayaran berhasil! Pesanan sedang diproses.";

        // Show success and go to profile
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Placed!");
        alert.setHeaderText("Order #" + orderId);
        alert.setContentText("Your order has been placed successfully!\n" +
                "Total: Rp " + Styles.formatPrice(order.getTotal()) + "\n\n" +
                statusMessage);
        alert.showAndWait();

        SceneManager.setScene(new ProfilePage().getScene());
    }

    private void showLoginRequiredAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please login first");
        alert.setContentText("You need to login to checkout.");
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
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(loginBtn, cancelBtn);
        alert.showAndWait().ifPresent(response -> {
            if (response == loginBtn) SceneManager.setScene(new LoginPage().getScene());
        });
    }
}