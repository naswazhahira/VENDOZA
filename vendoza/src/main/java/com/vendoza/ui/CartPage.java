package com.vendoza.ui;

import com.vendoza.model.CartItem;
import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartPage {
    private VBox cartItemsContainer;
    private Label totalLabel;
    private final Map<Product, CheckBox> itemCheckboxes = new HashMap<>();

    public Scene getScene() {
        if (!AuthService.isLoggedIn()) {
            showLoginRequiredAlert();
            return new LoginPage().getScene();
        }

        HBox navBar = createNavBar();
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        Text title = new Text("🛒 Your Shopping Cart");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #5D4037; -fx-border-color: #D4AF37; -fx-border-width: 0 0 3 0; -fx-padding: 0 0 10 0;");

        cartItemsContainer = new VBox(10);
        cartItemsContainer.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        VBox summaryBox = new VBox(15);
        summaryBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 15; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");
        summaryBox.setPrefWidth(300);

        Label summaryTitle = new Label("📋 Order Summary");
        summaryTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");

        totalLabel = new Label("Total: Rp 0");
        totalLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Button checkoutBtn = new Button("✅ Proceed to Checkout");
        checkoutBtn.setStyle(Styles.buttonStyle());
        checkoutBtn.setMaxWidth(Double.MAX_VALUE);
        checkoutBtn.setOnMouseEntered(e -> {
            checkoutBtn.setStyle(Styles.buttonHoverStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), checkoutBtn);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        checkoutBtn.setOnMouseExited(e -> {
            checkoutBtn.setStyle(Styles.buttonStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), checkoutBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        checkoutBtn.setOnAction(e -> {
            List<CartItem> selected = getSelectedCartItems();
            if (!selected.isEmpty()) {
                SceneManager.setScene(new CheckoutPage(selected).getScene());
            } else {
                showAlert("No Items Selected", "Please select at least one item to checkout.");
            }
        });

        Button continueBtn = new Button("🛍️ Continue Shopping");
        continueBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #8D6E63; -fx-font-size: 14px; -fx-cursor: hand; -fx-font-weight: bold;");
        continueBtn.setOnMouseEntered(e -> {
            continueBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #D4AF37; -fx-font-size: 14px; -fx-cursor: hand; -fx-font-weight: bold;");
            continueBtn.setScaleX(1.05);
            continueBtn.setScaleY(1.05);
        });
        continueBtn.setOnMouseExited(e -> {
            continueBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #8D6E63; -fx-font-size: 14px; -fx-cursor: hand; -fx-font-weight: bold;");
            continueBtn.setScaleX(1);
            continueBtn.setScaleY(1);
        });
        continueBtn.setOnAction(e -> SceneManager.showHomePage());

        summaryBox.getChildren().addAll(summaryTitle, new Separator(), totalLabel, checkoutBtn, continueBtn);

        HBox contentLayout = new HBox(30);
        contentLayout.setAlignment(Pos.TOP_LEFT);

        ScrollPane cartScroll = new ScrollPane(cartItemsContainer);
        cartScroll.setFitToWidth(true);
        cartScroll.setPrefHeight(500);
        cartScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        cartScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        cartScroll.setBorder(Border.EMPTY);

        contentLayout.getChildren().addAll(cartScroll, summaryBox);
        HBox.setHgrow(cartScroll, Priority.ALWAYS);

        mainContent.getChildren().addAll(title, contentLayout);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");

        refreshCart();
        return new Scene(root, 1200, 700);
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
        Button cartBtn = createNavButton("🛒 Cart", true);
        Button profileBtn = createNavButton("👤 Profile", false);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));
        cartBtn.setOnAction(e -> {});
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
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #D4AF37; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-color: #D4AF37; -fx-border-width: 0 0 2 0; -fx-border-style: solid; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #D4AF37; -fx-font-size: 14px; -fx-cursor: hand;");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
                st.setToX(1.05);
                st.setToY(1.05);
                st.play();
            });
            btn.setOnMouseExited(e -> {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-cursor: hand;");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
                st.setToX(1);
                st.setToY(1);
                st.play();
            });
        }
        return btn;
    }

    // Menyimpan state checkbox
    private Map<Product, Boolean> getCurrentSelectedStates() {
        Map<Product, Boolean> states = new HashMap<>();
        for (Map.Entry<Product, CheckBox> entry : itemCheckboxes.entrySet()) {
            states.put(entry.getKey(), entry.getValue().isSelected());
        }
        return states;
    }

    private void applySelectedStates(Map<Product, Boolean> states) {
        for (Map.Entry<Product, Boolean> entry : states.entrySet()) {
            CheckBox cb = itemCheckboxes.get(entry.getKey());
            if (cb != null) {
                cb.setSelected(entry.getValue());
            }
        }
        calculateSelectedTotal();
    }

    private void refreshCart() {
        Map<Product, Boolean> savedStates = getCurrentSelectedStates();
        cartItemsContainer.getChildren().clear();
        itemCheckboxes.clear();

        if (CartService.getCartItemCount() == 0) {
            VBox emptyBox = new VBox(15);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50, 0, 50, 0));
            Label emptyIcon = new Label("🛒");
            emptyIcon.setStyle("-fx-font-size: 64px;");
            Label emptyLabel = new Label("Your cart is empty");
            emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #8D6E63;");
            Label emptySubLabel = new Label("Add some items to get started!");
            emptySubLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #8D6E63;");
            emptyBox.getChildren().addAll(emptyIcon, emptyLabel, emptySubLabel);
            cartItemsContainer.getChildren().add(emptyBox);
            totalLabel.setText("Total: Rp 0");
        } else {
            int index = 1;
            for (CartItem item : CartService.getCartItems()) {
                VBox itemCard = createCartItemCard(item, index);
                cartItemsContainer.getChildren().add(itemCard);
                index++;
            }
            applySelectedStates(savedStates);
        }
    }

    private VBox createCartItemCard(CartItem item, int itemNumber) {
        Product product = item.getProduct();
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #D7CCC8; -fx-border-radius: 12; -fx-padding: 15; -fx-background-radius: 12; -fx-background-color: #FFFFFF;");
        card.setOnMouseEntered(e -> card.setStyle("-fx-border-color: #D4AF37; -fx-border-radius: 12; -fx-padding: 15; -fx-background-radius: 12; -fx-background-color: #FFFFFF;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-border-color: #D7CCC8; -fx-border-radius: 12; -fx-padding: 15; -fx-background-radius: 12; -fx-background-color: #FFFFFF;"));

        HBox content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        // Nomor urut item
        Label numberLabel = new Label(itemNumber + ".");
        numberLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #D4AF37; -fx-min-width: 35;");

        CheckBox selectItemCheck = new CheckBox();
        selectItemCheck.setStyle("-fx-cursor: hand;");
        selectItemCheck.setSelected(true);
        selectItemCheck.setOnAction(e -> calculateSelectedTotal());
        itemCheckboxes.put(product, selectItemCheck);

        Label imagePlaceholder = new Label(product.getImageUrl());
        imagePlaceholder.setStyle("-fx-font-size: 50px;");

        VBox details = new VBox(5);
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        Label variantLabel = new Label("Size: M | Color: Classic Beige");
        variantLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63; -fx-font-style: italic;");
        Label priceLabel = new Label("Rp " + Styles.formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #D4AF37; -fx-font-weight: bold;");
        details.getChildren().addAll(nameLabel, variantLabel, priceLabel);

        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER);
        Button minusBtn = new Button("-");
        minusBtn.setStyle("-fx-background-color: #D7CCC8; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 32; -fx-cursor: hand; -fx-background-radius: 16;");
        minusBtn.setOnAction(e -> {
            int newQty = item.getQuantity() - 1;
            if (newQty > 0) CartService.updateQuantity(product, newQty);
            else CartService.removeFromCart(product);
            refreshCart();
        });

        Label qtyLabel = new Label(String.valueOf(item.getQuantity()));
        qtyLabel.setStyle("-fx-font-size: 14px; -fx-min-width: 30; -fx-alignment: center; -fx-font-weight: bold;");

        Button plusBtn = new Button("+");
        plusBtn.setStyle("-fx-background-color: #D7CCC8; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 32; -fx-cursor: hand; -fx-background-radius: 16;");
        plusBtn.setOnAction(e -> {
            CartService.updateQuantity(product, item.getQuantity() + 1);
            refreshCart();
        });
        quantityBox.getChildren().addAll(minusBtn, qtyLabel, plusBtn);

        Label subtotalLabel = new Label("Subtotal: Rp " + Styles.formatPrice(item.getSubtotal()));
        subtotalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");

        HBox actionBox = new HBox(10);
        Button removeBtn = new Button("🗑️ Remove");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #E57373; -fx-font-size: 12px; -fx-cursor: hand; -fx-font-weight: bold;");
        removeBtn.setOnAction(e -> {
            CartService.removeFromCart(product);
            refreshCart();
        });

        Button buyNowBtn = new Button("Buy Now →");
        buyNowBtn.setStyle("-fx-background-color: #D4AF37; -fx-text-fill: #5D4037; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 6 18; -fx-cursor: hand;");
        buyNowBtn.setOnAction(e -> {
            CartItem instantItem = new CartItem(product, item.getQuantity());
            List<CartItem> instantList = new ArrayList<>();
            instantList.add(instantItem);
            SceneManager.setScene(new CheckoutPage(instantList).getScene());
        });

        actionBox.getChildren().addAll(removeBtn, buyNowBtn);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        content.getChildren().addAll(numberLabel, selectItemCheck, imagePlaceholder, details, quantityBox, subtotalLabel, spacer, actionBox);
        card.getChildren().add(content);
        return card;
    }

    private void calculateSelectedTotal() {
        double total = 0;
        for (CartItem item : CartService.getCartItems()) {
            CheckBox cb = itemCheckboxes.get(item.getProduct());
            if (cb != null && cb.isSelected()) {
                total += item.getSubtotal();
            }
        }
        totalLabel.setText("Total: Rp " + Styles.formatPrice(total));
    }

    private List<CartItem> getSelectedCartItems() {
        List<CartItem> selected = new ArrayList<>();
        for (CartItem item : CartService.getCartItems()) {
            CheckBox cb = itemCheckboxes.get(item.getProduct());
            if (cb != null && cb.isSelected()) {
                selected.add(item);
            }
        }
        return selected;
    }

    private void showLoginRequiredAlert() { LoginRequiredDialog.show("You need to login to access your cart."); }
    private void showLoginAlert() { LoginRequiredDialog.show("You need to login to access this feature."); }
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
