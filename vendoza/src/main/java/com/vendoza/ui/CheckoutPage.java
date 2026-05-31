package com.vendoza.ui;

import com.vendoza.model.CartItem;
import com.vendoza.model.Order;
import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final List<CartItem> checkoutItems;
    private double currentSubtotal = 0;

    public CheckoutPage() {
        this(CartService.getCartItems());
    }

    public CheckoutPage(List<CartItem> itemsToCheckout) {
        this.checkoutItems = itemsToCheckout;
        for (CartItem item : itemsToCheckout) {
            this.currentSubtotal += item.getSubtotal();
        }
    }

    public Scene getScene() {
        if (!AuthService.isLoggedIn()) {
            showLoginRequiredAlert();
            return new LoginPage().getScene();
        }

        HBox navBar = createNavBar();
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        Text title = new Text("📦 Checkout");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #5D4037; -fx-border-color: #D4AF37; -fx-border-width: 0 0 3 0; -fx-padding: 0 0 10 0;");

        HBox contentLayout = new HBox(30);
        contentLayout.setAlignment(Pos.TOP_LEFT);

        // Left panel - items
        VBox leftPanel = new VBox(15);
        leftPanel.setPrefWidth(600);
        Label itemsLabel = new Label("📦 Your Items");
        itemsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");

        itemsContainer = new VBox(10);
        itemsContainer.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        refreshItemsContainer();
        leftPanel.getChildren().addAll(itemsLabel, itemsContainer);

        // Right panel - shipping & payment
        VBox rightPanel = new VBox(15);
        rightPanel.setPrefWidth(400);

        Label shippingLabel = new Label("🚚 Shipping Information");
        shippingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");

        addressArea = new TextArea();
        addressArea.setPromptText("Enter your shipping address");
        addressArea.setStyle(Styles.textFieldStyle());
        addressArea.setPrefHeight(80);

        User currentUser = AuthService.getCurrentUser();
        if (currentUser != null && currentUser.getAddress() != null && !currentUser.getAddress().isEmpty()) {
            addressArea.setText(currentUser.getAddress());
        }

        addressError = new Label();
        addressError.setStyle("-fx-text-fill: #E57373; -fx-font-size: 12px;");

        Label methodLabel = new Label("📩 Shipping Method");
        methodLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        shippingCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Regular (3-5 days) - Rp 15.000",
                "Express (1-2 days) - Rp 35.000",
                "Same Day - Rp 75.000"
        ));
        shippingCombo.setStyle(Styles.textFieldStyle());
        shippingCombo.setPrefWidth(Double.MAX_VALUE);
        shippingCombo.getSelectionModel().selectFirst();
        shippingCombo.setOnAction(e -> updateTotal());

        Label paymentLabel = new Label("💳 Payment Method");
        paymentLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        paymentCombo = new ComboBox<>(FXCollections.observableArrayList(
                "Bank Transfer (BCA/Mandiri/BNI)",
                "E-Wallet (GoPay/OVO/Dana)",
                "Credit Card (Visa/Mastercard)",
                "Cash on Delivery (COD)"
        ));
        paymentCombo.setStyle(Styles.textFieldStyle());
        paymentCombo.setPrefWidth(Double.MAX_VALUE);
        paymentCombo.getSelectionModel().selectFirst();

        VBox summaryBox = new VBox(10);
        summaryBox.setStyle("-fx-background-color: #F5F0EB; -fx-background-radius: 10; -fx-padding: 15;");

        Label subtotalText = new Label("Subtotal: Rp " + Styles.formatPrice(currentSubtotal));
        subtotalText.setStyle("-fx-font-size: 14px;");

        shippingCostLabel = new Label("Shipping: Rp 15.000");
        shippingCostLabel.setStyle("-fx-font-size: 14px;");

        totalLabel = new Label("Total: Rp " + Styles.formatPrice(currentSubtotal + 15000));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        summaryBox.getChildren().addAll(subtotalText, shippingCostLabel, new Separator(), totalLabel);

        Button placeOrderBtn = new Button("✅ Place Order");
        placeOrderBtn.setStyle(Styles.buttonStyle());
        placeOrderBtn.setMaxWidth(Double.MAX_VALUE);
        placeOrderBtn.setOnMouseEntered(e -> {
            placeOrderBtn.setStyle(Styles.buttonHoverStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), placeOrderBtn);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        placeOrderBtn.setOnMouseExited(e -> {
            placeOrderBtn.setStyle(Styles.buttonStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), placeOrderBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        placeOrderBtn.setOnAction(e -> placeOrder());

        rightPanel.getChildren().addAll(shippingLabel, addressArea, addressError, methodLabel, shippingCombo,
                paymentLabel, paymentCombo, summaryBox, placeOrderBtn);

        contentLayout.getChildren().addAll(leftPanel, rightPanel);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        mainContent.getChildren().addAll(title, contentLayout);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");
        return new Scene(root, 1200, 700);
    }

    private void refreshItemsContainer() {
        if (itemsContainer == null) return;
        itemsContainer.getChildren().clear();
        if (checkoutItems.isEmpty()) {
            Label emptyLabel = new Label("🛒 Checkout container is empty");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #8D6E63; -fx-padding: 30 0;");
            emptyLabel.setAlignment(Pos.CENTER);
            emptyLabel.setMaxWidth(Double.MAX_VALUE);
            itemsContainer.getChildren().add(emptyLabel);
        } else {
            for (CartItem item : checkoutItems) {
                VBox itemRow = new VBox(8);
                itemRow.setStyle("-fx-border-color: #D7CCC8; -fx-border-radius: 8; -fx-padding: 10;");

                HBox itemHeader = new HBox(10);
                itemHeader.setAlignment(Pos.CENTER_LEFT);
                Label imageLabel = new Label(item.getProduct().getImageUrl());
                imageLabel.setStyle("-fx-font-size: 40px;");

                Label nameLabel = new Label(item.getProduct().getName());
                nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
                nameLabel.setPrefWidth(250);

                Region spacer1 = new Region();
                HBox.setHgrow(spacer1, Priority.ALWAYS);
                itemHeader.getChildren().addAll(imageLabel, nameLabel, spacer1);

                HBox itemFooter = new HBox(20);
                itemFooter.setAlignment(Pos.CENTER_LEFT);
                Label priceLabel = new Label("Rp " + Styles.formatPrice(item.getProduct().getCurrentPrice()));
                priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #D4AF37; -fx-font-weight: bold;");
                Label qtyLabel = new Label("Qty: " + item.getQuantity());
                qtyLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
                Label subtotalLabel = new Label("Rp " + Styles.formatPrice(item.getSubtotal()));
                subtotalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

                Region spacer2 = new Region();
                HBox.setHgrow(spacer2, Priority.ALWAYS);
                itemFooter.getChildren().addAll(priceLabel, qtyLabel, spacer2, subtotalLabel);
                itemRow.getChildren().addAll(itemHeader, itemFooter);
                itemsContainer.getChildren().add(itemRow);
            }
        }
    }

    private void updateTotal() {
        double shippingCost = 15000;
        int idx = shippingCombo.getSelectionModel().getSelectedIndex();
        if (idx == 1) shippingCost = 35000;
        else if (idx == 2) shippingCost = 75000;

        shippingCostLabel.setText("Shipping: Rp " + Styles.formatPrice(shippingCost));
        totalLabel.setText("Total: Rp " + Styles.formatPrice(currentSubtotal + shippingCost));
    }

    private void placeOrder() {
        try {
            String address = addressArea.getText().trim();
            if (address.isEmpty()) {
                addressError.setText("Address cannot be empty!");
                return;
            }
            addressError.setText("");

            double shippingCost = 15000;
            int idx = shippingCombo.getSelectionModel().getSelectedIndex();
            if (idx == 1) shippingCost = 35000;
            else if (idx == 2) shippingCost = 75000;
            double finalTotal = currentSubtotal + shippingCost;

            String orderId = "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
            String status = "Pending";
            String shippingMethod = shippingCombo.getValue();
            String paymentMethod = paymentCombo.getValue();

            User currentUser = AuthService.getCurrentUser();
            if (currentUser == null) {
                showAlert("Error", "User not logged in.");
                return;
            }

            // Buat order dengan konstruktor yang benar
            Order newOrder = new Order(orderId, currentUser, checkoutItems, finalTotal, status);
            currentUser.getOrders().add(newOrder);

            // Hapus item dari cart utama
            for (CartItem item : checkoutItems) {
                CartService.removeFromCart(item.getProduct());
            }

            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Order Placed!");
            successAlert.setHeaderText("✅ Thank you for shopping at Vendoza");
            successAlert.setContentText("Order ID: " + orderId +
                    "\nTotal: Rp " + Styles.formatPrice(finalTotal) +
                    "\nShipping: " + shippingMethod +
                    "\nPayment: " + paymentMethod +
                    "\nAddress: " + address);
            DialogPane dialogPane = successAlert.getDialogPane();
            dialogPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-padding: 20;");
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setStyle(Styles.buttonStyle());
            successAlert.showAndWait();

            SceneManager.showHomePage();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to place order: " + ex.getMessage());
        }
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        Label logo = new Label("🛒 VENDOZA");
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Button homeBtn = createNavButton("🏠 Home", false);
        Button searchBtn = createNavButton("🔍 Search", false);
        Button cartBtn = createNavButton("🛒 Cart", false);
        Button profileBtn = createNavButton("👤 Profile", false);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));
        cartBtn.setOnAction(e -> SceneManager.setScene(new CartPage().getScene()));
        profileBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        navBar.getChildren().addAll(logo, leftSpacer, homeBtn, searchBtn, cartBtn, profileBtn, rightSpacer);
        return navBar;
    }

    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-cursor: hand;");
        return btn;
    }

    private void showLoginRequiredAlert() { LoginRequiredDialog.show("You need to login to process checkout."); }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());
        alert.showAndWait();
    }
}
