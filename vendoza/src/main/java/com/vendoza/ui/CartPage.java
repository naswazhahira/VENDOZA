package com.vendoza.ui;

import com.vendoza.model.CartItem;
import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartPage {

    private VBox cartItemsContainer;
    private Label totalLabel;
    private Label checkoutCountLabel;
    private CheckBox selectAllCheck;
    private CheckBox bottomSelectAll;

    private Label sidebarItemsLabel;
    private Label sidebarSubtotalLabel;
    private Label sidebarDiscountLabel;
    private Label sidebarSavedLabel;
    private Label sidebarPointsLabel;
    private Label sidebarTotalLabel;

    private Button bottomCheckoutBtn;
    private HBox bottomBar;
    private HBox columnHeader;

    private StackPane rootStackPane;
    private VBox customPopupToast;
    private Label popupMessageLabel;

    private final Map<Product, CheckBox> itemCheckboxes = new HashMap<>();
    private Scene currentScene;

    //  ENTRY POINT
    public Scene getScene() {
        if (!AuthService.isLoggedIn()) {
            LoginRequiredDialog.show("Please login to access your cart.");
            return new LoginPage().getScene();
        }

        double screenWidth  = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        rootStackPane = new StackPane();
        rootStackPane.setStyle("-fx-background-color: #F0E8DF;");

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(0);
        mainContent.setStyle("-fx-background-color: #F0E8DF;");

        HBox pageHeader = new HBox(16);
        pageHeader.setAlignment(Pos.CENTER_LEFT);
        pageHeader.setPadding(new Insets(22, 50, 22, 50));
        pageHeader.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: transparent transparent #F0EAE4 transparent;" +
                        "-fx-border-width: 1;"
        );

        Label cartIcon = new Label("🛒");
        cartIcon.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #F5F0EA;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 10;"
        );

        VBox titleBox = new VBox(4);
        Label title = new Label("Shopping Cart");
        title.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Georgia';"
        );
        int itemCount = CartService.getCartItemCount();
        Label subtitle = new Label(itemCount == 0
                ? "Your cart is empty"
                : itemCount + " item" + (itemCount > 1 ? "s" : "") + " in your collection"
        );
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #A09088;");
        titleBox.getChildren().addAll(title, subtitle);

        pageHeader.getChildren().addAll(cartIcon, titleBox);
        HBox body = new HBox(30);
        body.setPadding(new Insets(40, 50, 120, 50));
        body.setStyle("-fx-background-color: #F0E8DF;");
        body.setAlignment(Pos.TOP_LEFT);

        VBox leftPanel = new VBox(0);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        columnHeader = buildColumnHeader();
        cartItemsContainer = new VBox(0);
        leftPanel.getChildren().addAll(columnHeader, cartItemsContainer);

        VBox sidebar = buildSidebar();
        sidebar.setPrefWidth(340);
        sidebar.setMinWidth(340);

        body.getChildren().addAll(leftPanel, sidebar);
        mainContent.getChildren().addAll(pageHeader, body);

        ScrollPane pageScroll = new ScrollPane(mainContent);
        pageScroll.setFitToWidth(true);
        pageScroll.setStyle("-fx-background-color: #F0E8DF; -fx-background: #F0E8DF;");
        pageScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        pageScroll.setBorder(Border.EMPTY);

        bottomBar = buildBottomBar();

        VBox fullContentLayout = new VBox(navBar, pageScroll, bottomBar);
        VBox.setVgrow(pageScroll, Priority.ALWAYS);

        buildCustomPopupToast();

        rootStackPane.getChildren().addAll(fullContentLayout, customPopupToast);

        currentScene = new Scene(rootStackPane, screenWidth, screenHeight);
        currentScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        refreshCart();
        return currentScene;
    }

    //  CUSTOM DIALOG KONFIRMASI BERGAYA VENDOZA
    private boolean showConfirmDialog(String title, String message, String confirmText) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("");

        DialogPane dp = dialog.getDialogPane();
        dp.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 0;"
        );

        VBox content = new VBox(16);
        content.setPadding(new Insets(36, 36, 28, 36));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        Label iconLabel = new Label("");
        iconLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: #D4A853;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 17px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );

        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63; -fx-text-alignment: center;");
        msgLabel.setWrapText(true);
        msgLabel.setMaxWidth(300);

        content.getChildren().addAll(iconLabel, titleLabel, msgLabel);
        dp.setContent(content);
        dp.setHeader(null);
        dp.setGraphic(null);

        ButtonType confirmBtnType = new ButtonType(confirmText, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtnType  = new ButtonType("Cancel",    ButtonBar.ButtonData.CANCEL_CLOSE);
        dp.getButtonTypes().addAll(confirmBtnType, cancelBtnType);

        Button confirm = (Button) dp.lookupButton(confirmBtnType);
        confirm.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 10; -fx-padding: 10 24; -fx-cursor: hand;"
        );
        confirm.setOnMouseEntered(e -> confirm.setStyle(
                "-fx-background-color: #D4A853; -fx-text-fill: #2C1810; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 10; -fx-padding: 10 24; -fx-cursor: hand;"
        ));
        confirm.setOnMouseExited(e -> confirm.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 10; -fx-padding: 10 24; -fx-cursor: hand;"
        ));

        Button cancel = (Button) dp.lookupButton(cancelBtnType);
        cancel.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #8D6E63; " +
                        "-fx-font-size: 13px; -fx-border-color: #D7CCC8; " +
                        "-fx-border-radius: 10; -fx-background-radius: 10; " +
                        "-fx-padding: 10 24; -fx-cursor: hand;"
        );

        Optional<ButtonType> result = dialog.showAndWait();
        return result.isPresent() && result.get() == confirmBtnType;
    }

    //  CUSTOM POPUP TOAST
    private void buildCustomPopupToast() {
        customPopupToast = new VBox(10);
        customPopupToast.setAlignment(Pos.CENTER);
        customPopupToast.setPadding(new Insets(22, 30, 22, 30));
        customPopupToast.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-background-radius: 16; " +
                        "-fx-border-color: #3E2723; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 16; " +
                        "-fx-effect: dropshadow(gaussian, rgba(62,39,35,0.12), 15, 0, 0, 4);"
        );
        customPopupToast.setMaxSize(440, 120);
        customPopupToast.setVisible(false);
        customPopupToast.setManaged(false);

        Label iconLabel = new Label("");
        iconLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #D4A853; -fx-font-weight: bold;");

        popupMessageLabel = new Label();
        popupMessageLabel.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-text-fill: #3E2723; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-alignment: center; " +
                        "-fx-line-spacing: 4;"
        );
        popupMessageLabel.setWrapText(true);

        customPopupToast.getChildren().addAll(iconLabel, popupMessageLabel);
        StackPane.setAlignment(customPopupToast, Pos.CENTER);
    }

    private void triggerAnimatePopup(String message) {
        popupMessageLabel.setText(message);
        if (!customPopupToast.isVisible()) {
            customPopupToast.setVisible(true);
            customPopupToast.setManaged(true);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), customPopupToast);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            PauseTransition stayOnScreen = new PauseTransition(Duration.seconds(2.8));

            FadeTransition fadeOut = new FadeTransition(Duration.millis(350), customPopupToast);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                customPopupToast.setVisible(false);
                customPopupToast.setManaged(false);
            });

            SequentialTransition sequence = new SequentialTransition(fadeIn, stayOnScreen, fadeOut);
            sequence.play();
        }
    }

    //  CHECKOUT ACTION
    private void handleCheckoutAction() {
        List<CartItem> selected = getSelectedCartItems();
        if (selected.isEmpty()) {
            triggerAnimatePopup("Cannot Proceed: Please select at least one beautiful piece from your collection to checkout.");
        } else {
            SceneManager.setScene(new CheckoutPage(selected).getScene());
        }
    }

    //  COLUMN HEADER
    private HBox buildColumnHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setStyle(
                "-fx-background-color: white; -fx-background-radius: 16 16 0 0; " +
                        "-fx-border-color: #F0EAE4; -fx-border-width: 0 0 1 0;"
        );

        selectAllCheck = new CheckBox("  Product");
        selectAllCheck.setSelected(false);
        selectAllCheck.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-cursor: hand;"
        );
        selectAllCheck.setPrefWidth(380);
        selectAllCheck.setOnAction(e -> {
            boolean sel = selectAllCheck.isSelected();
            itemCheckboxes.values().forEach(cb -> cb.setSelected(sel));
            if (bottomSelectAll != null) bottomSelectAll.setSelected(sel);
            calculateSelectedTotal();
        });

        Label priceCol = new Label("Unit Price");
        priceCol.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63; -fx-font-weight: bold;");
        priceCol.setPrefWidth(160);
        priceCol.setAlignment(Pos.CENTER);

        Label qtyCol = new Label("Quantity");
        qtyCol.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63; -fx-font-weight: bold;");
        qtyCol.setPrefWidth(140);
        qtyCol.setAlignment(Pos.CENTER);

        Label totalCol = new Label("Item Total");
        totalCol.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63; -fx-font-weight: bold;");
        totalCol.setPrefWidth(140);
        totalCol.setAlignment(Pos.CENTER);

        Label actionCol = new Label("Action");
        actionCol.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63; -fx-font-weight: bold;");
        actionCol.setPrefWidth(80);
        actionCol.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(selectAllCheck, spacer, priceCol, qtyCol, totalCol, actionCol);
        return header;
    }

    //  SIDEBAR
    private VBox buildSidebar() {
        VBox sidebar = new VBox(16);
        sidebar.setPadding(new Insets(26));
        sidebar.setStyle(
                "-fx-background-color: white; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 15, 0, 0, 3);"
        );

        Label summaryTitle = new Label("Order Summary");
        summaryTitle.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );

        Region sep1 = new Region();
        sep1.setPrefHeight(1);
        sep1.setStyle("-fx-background-color: #F0EAE4;");
        sidebar.getChildren().addAll(summaryTitle, sep1);

        VBox breakdownBox = new VBox(12);
        sidebarItemsLabel = new Label("Selected Items: 0");
        sidebarItemsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");

        sidebarSubtotalLabel = new Label("Rp 0");
        sidebarSubtotalLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723; -fx-font-weight: bold;");
        HBox subtotalRow = createSummaryRow("Subtotal", sidebarSubtotalLabel);

        sidebarDiscountLabel = new Label("-Rp 0");
        sidebarDiscountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #E53935; -fx-font-weight: bold;");
        HBox discountRow = createSummaryRow("Discount", sidebarDiscountLabel);

        breakdownBox.getChildren().addAll(sidebarItemsLabel, subtotalRow, discountRow);
        sidebar.getChildren().add(breakdownBox);

        Region sep2 = new Region();
        sep2.setPrefHeight(1);
        sep2.setStyle("-fx-background-color: #D7CCC8;");

        VBox perksBox = new VBox(12);
        sidebarSavedLabel = new Label("Rp 0");
        sidebarSavedLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2E7D32; -fx-font-weight: bold;");
        HBox savedRow = createSummaryRow("You Saved", sidebarSavedLabel);

        sidebarPointsLabel = new Label("+0 pts");
        sidebarPointsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #D4A853; -fx-font-weight: bold;");
        HBox rewardsRow = createSummaryRow("Rewards", sidebarPointsLabel);

        perksBox.getChildren().addAll(savedRow, rewardsRow);
        sidebar.getChildren().addAll(sep2, perksBox);

        Region sep3 = new Region();
        sep3.setPrefHeight(1);
        sep3.setStyle("-fx-background-color: #F0EAE4;");

        sidebarTotalLabel = new Label("Rp 0");
        sidebarTotalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        HBox totalRow = createSummaryRow("Total", sidebarTotalLabel);

        Label noteLabel = new Label("Logistics fees and delivery routes will be accurately adjusted during checkout.");
        noteLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #A09088; -fx-line-spacing: 4;");
        noteLabel.setWrapText(true);

        Button continueBtn = new Button("Continue Shopping");
        continueBtn.setMaxWidth(Double.MAX_VALUE);
        continueBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #3E2723; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #D7CCC8; " +
                        "-fx-border-radius: 12; -fx-border-width: 1.5; -fx-background-radius: 12; " +
                        "-fx-padding: 12 0; -fx-cursor: hand;"
        );
        continueBtn.setOnMouseEntered(e -> continueBtn.setStyle(
                "-fx-background-color: #FAF6F1; -fx-text-fill: #3E2723; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #3E2723; " +
                        "-fx-border-radius: 12; -fx-border-width: 1.5; -fx-background-radius: 12; " +
                        "-fx-padding: 12 0; -fx-cursor: hand;"
        ));
        continueBtn.setOnMouseExited(e -> continueBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #3E2723; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; -fx-border-color: #D7CCC8; " +
                        "-fx-border-radius: 12; -fx-border-width: 1.5; -fx-background-radius: 12; " +
                        "-fx-padding: 12 0; -fx-cursor: hand;"
        ));
        continueBtn.setOnAction(e -> SceneManager.showHomePage());

        sidebar.getChildren().addAll(sep3, totalRow, noteLabel, continueBtn);
        return sidebar;
    }

    private HBox createSummaryRow(String titleText, Label valueLabel) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #5D4037;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        row.getChildren().addAll(titleLabel, spacer, valueLabel);
        return row;
    }

    //  BOTTOM BAR
    private HBox buildBottomBar() {
        HBox bar = new HBox(20);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(16, 50, 16, 50));
        bar.setStyle(
                "-fx-background-color: white; -fx-border-color: #F0EAE4; " +
                        "-fx-border-width: 1 0 0 0; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, -3);"
        );

        bottomSelectAll = new CheckBox("Select All");
        bottomSelectAll.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723; -fx-cursor: hand;");
        bottomSelectAll.setSelected(false);
        bottomSelectAll.setOnAction(e -> {
            boolean sel = bottomSelectAll.isSelected();
            if (selectAllCheck != null) selectAllCheck.setSelected(sel);
            itemCheckboxes.values().forEach(cb -> cb.setSelected(sel));
            calculateSelectedTotal();
        });

        Button deleteSelectedBtn = new Button("Delete Selected");
        deleteSelectedBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #8D6E63; " +
                        "-fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #D7CCC8; " +
                        "-fx-border-radius: 8; -fx-padding: 6 16;"
        );
        deleteSelectedBtn.setOnMouseEntered(e -> deleteSelectedBtn.setStyle(
                "-fx-background-color: #FFF5F5; -fx-text-fill: #E57373; " +
                        "-fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #E57373; " +
                        "-fx-border-radius: 8; -fx-padding: 6 16;"
        ));
        deleteSelectedBtn.setOnMouseExited(e -> deleteSelectedBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #8D6E63; " +
                        "-fx-font-size: 13px; -fx-cursor: hand; -fx-border-color: #D7CCC8; " +
                        "-fx-border-radius: 8; -fx-padding: 6 16;"
        ));
        deleteSelectedBtn.setOnAction(e -> {
            List<CartItem> selected = getSelectedCartItems();
            if (selected.isEmpty()) {
                triggerAnimatePopup("Please select items you wish to clear from the list.");
                return;
            }
            boolean ok = showConfirmDialog(
                    "Remove Selected Items",
                    "Remove " + selected.size() + " selected item(s) from your cart?",
                    "Yes, Remove All"
            );
            if (ok) {
                for (CartItem item : selected) CartService.removeFromCart(item.getProduct());
                refreshCart();
                CustomToast.showSuccessToast(currentScene.getWindow(), selected.size() + " item(s) removed.");
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label totalCaption = new Label("Total Selected:");
        totalCaption.setStyle("-fx-font-size: 14px; -fx-text-fill: #5D4037;");

        totalLabel = new Label("Rp 0");
        totalLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        checkoutCountLabel = new Label("(0 items)");
        checkoutCountLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");

        bottomCheckoutBtn = new Button("Checkout");
        bottomCheckoutBtn.setOnAction(e -> handleCheckoutAction());

        bar.getChildren().addAll(
                bottomSelectAll, deleteSelectedBtn, spacer,
                totalCaption, totalLabel, checkoutCountLabel, bottomCheckoutBtn
        );
        return bar;
    }

    //  REFRESH CART
    private void refreshCart() {
        cartItemsContainer.getChildren().clear();
        itemCheckboxes.clear();

        boolean isEmpty = CartService.getCartItemCount() == 0;

        if (columnHeader != null) {
            columnHeader.setVisible(!isEmpty);
            columnHeader.setManaged(!isEmpty);
        }

        if (isEmpty) {
            cartItemsContainer.getChildren().add(buildEmptyCart());
            if (selectAllCheck  != null) selectAllCheck.setSelected(false);
            if (bottomSelectAll != null) bottomSelectAll.setSelected(false);
        } else {
            for (CartItem item : CartService.getCartItems()) {
                cartItemsContainer.getChildren().add(createCartRow(item));
            }
        }
        calculateSelectedTotal();
    }

    private VBox buildEmptyCart() {
        VBox emptyBox = new VBox(16);
        emptyBox.setAlignment(Pos.CENTER);
        emptyBox.setPadding(new Insets(80, 0, 80, 0));
        emptyBox.setStyle(
                "-fx-background-color: white; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 12, 0, 0, 3);"
        );

        Label emptyIcon = new Label("🛒");
        emptyIcon.setStyle("-fx-font-size: 44px;");

        Label msg = new Label("Your cart is empty");
        msg.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );

        Label subMsg = new Label("Discover our curated collections and find your perfect piece.");
        subMsg.setStyle("-fx-font-size: 13px; -fx-text-fill: #A09088;");
        subMsg.setWrapText(true);

        Button shopBtn = new Button("Discover Collections");
        shopBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 20; -fx-padding: 10 26; -fx-cursor: hand;"
        );
        shopBtn.setOnMouseEntered(e -> shopBtn.setStyle(
                "-fx-background-color: #D4A853; -fx-text-fill: #2C1810; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 20; -fx-padding: 10 26; -fx-cursor: hand;"
        ));
        shopBtn.setOnMouseExited(e -> shopBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 20; -fx-padding: 10 26; -fx-cursor: hand;"
        ));
        shopBtn.setOnAction(e -> SceneManager.showHomePage());

        emptyBox.getChildren().addAll(emptyIcon, msg, subMsg, shopBtn);
        return emptyBox;
    }

    //  CART ROW
    private HBox createCartRow(CartItem item) {
        Product product = item.getProduct();

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(18, 24, 18, 24));
        row.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: transparent transparent #F0EAE4 transparent; " +
                        "-fx-border-width: 1;"
        );
        row.setOnMouseEntered(e -> row.setStyle(
                "-fx-background-color: #FAF6F1; " +
                        "-fx-border-color: transparent transparent #F0EAE4 transparent; " +
                        "-fx-border-width: 1;"
        ));
        row.setOnMouseExited(e -> row.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: transparent transparent #F0EAE4 transparent; " +
                        "-fx-border-width: 1;"
        ));

        CheckBox checkBox = new CheckBox();
        checkBox.setStyle("-fx-cursor: hand;");
        checkBox.setOnAction(e -> {
            calculateSelectedTotal();
            syncSelectAllState();
        });
        itemCheckboxes.put(product, checkBox);

        HBox productSection = new HBox(16);
        productSection.setAlignment(Pos.CENTER_LEFT);
        productSection.setPrefWidth(380);

        Label imgLabel = new Label(product.getImageUrl());
        imgLabel.setStyle(
                "-fx-font-size: 36px; -fx-background-color: #F5F0EA; " +
                        "-fx-background-radius: 12; -fx-padding: 8 12;"
        );

        VBox infoBox = new VBox(6);
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2C1810;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(240);

        Label categoryLabel = new Label(product.getCategory());
        categoryLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #8D6E63;");

        if (product.isOnSale()) {
            Label saleBadge = new Label("SALE " + (int) product.getDiscountPercent() + "%");
            saleBadge.setStyle(
                    "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: white; " +
                            "-fx-background-color: #E53935; -fx-background-radius: 4; -fx-padding: 2 6;"
            );
            HBox badges = new HBox(6, categoryLabel, saleBadge);
            infoBox.getChildren().addAll(nameLabel, badges);
        } else {
            infoBox.getChildren().addAll(nameLabel, categoryLabel);
        }
        productSection.getChildren().addAll(imgLabel, infoBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox priceBox = new VBox(3);
        priceBox.setPrefWidth(160);
        priceBox.setAlignment(Pos.CENTER);
        Label priceLabel = new Label("Rp " + Styles.formatPrice(product.getCurrentPrice()));
        priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        priceBox.getChildren().add(priceLabel);

        if (product.isOnSale() && product.getDiscountPrice() > 0) {
            Text origPrice = new Text("Rp " + Styles.formatPrice(product.getPrice()));
            origPrice.setStrikethrough(true);
            origPrice.setStyle("-fx-font-size: 12px; -fx-fill: #E53935;");
            priceBox.getChildren().add(origPrice);
        }

        HBox qtyBox = buildQtyControl(item, product);
        StackPane qtyPane = new StackPane(qtyBox);
        qtyPane.setPrefWidth(140);

        Label subtotalLabel = new Label("Rp " + Styles.formatPrice(item.getSubtotal()));
        subtotalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        StackPane subtotalPane = new StackPane(subtotalLabel);
        subtotalPane.setPrefWidth(140);

        Button removeBtn = new Button("🗑");
        removeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #A1887F; " +
                        "-fx-font-size: 15px; -fx-cursor: hand; -fx-padding: 4 8;"
        );
        removeBtn.setOnMouseEntered(e -> removeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #E53935; " +
                        "-fx-font-size: 15px; -fx-cursor: hand; -fx-padding: 4 8;"
        ));
        removeBtn.setOnMouseExited(e -> removeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #A1887F; " +
                        "-fx-font-size: 15px; -fx-cursor: hand; -fx-padding: 4 8;"
        ));
        removeBtn.setOnAction(e -> {
            boolean ok = showConfirmDialog(
                    "Remove Item",
                    "Remove \"" + product.getName() + "\" from your cart?",
                    "Yes, Remove"
            );
            if (ok) {
                CartService.removeFromCart(product);
                refreshCart();
            }
        });
        StackPane actionPane = new StackPane(removeBtn);
        actionPane.setPrefWidth(80);

        row.getChildren().addAll(
                checkBox, new Label("  "),
                productSection, spacer,
                priceBox, qtyPane, subtotalPane, actionPane
        );
        return row;
    }

    //  QTY CONTROL
    private HBox buildQtyControl(CartItem item, Product product) {
        HBox qtyBox = new HBox(10);
        qtyBox.setAlignment(Pos.CENTER);

        Button minusBtn = new Button("−");
        minusBtn.setStyle(
                "-fx-background-color: #EFEBE9; -fx-text-fill: #3E2723; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; " +
                        "-fx-min-width: 32; -fx-min-height: 32; -fx-cursor: hand;"
        );

        Label qtyNum = new Label(String.valueOf(item.getQuantity()));
        qtyNum.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723; " +
                        "-fx-min-width: 26; -fx-alignment: center;"
        );

        Button plusBtn = new Button("+");
        plusBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; " +
                        "-fx-min-width: 32; -fx-min-height: 32; -fx-cursor: hand;"
        );

        minusBtn.setOnAction(e -> {
            if (item.getQuantity() == 1) {
                boolean ok = showConfirmDialog(
                        "Remove Item",
                        "Remove \"" + product.getName() + "\" from your cart?",
                        "Yes, Remove"
                );
                if (ok) {
                    CartService.removeFromCart(product);
                    refreshCart();
                }
            } else {
                CartService.updateQuantity(product, item.getQuantity() - 1);
                refreshCart();
            }
        });

        plusBtn.setOnAction(e -> {
            if (item.getQuantity() >= product.getStock()) return;
            CartService.updateQuantity(product, item.getQuantity() + 1);
            refreshCart();
        });

        qtyBox.getChildren().addAll(minusBtn, qtyNum, plusBtn);
        return qtyBox;
    }

    //  HELPERS
    private void syncSelectAllState() {
        if (itemCheckboxes.isEmpty()) return;
        boolean allSelected = itemCheckboxes.values().stream().allMatch(CheckBox::isSelected);
        if (selectAllCheck  != null) selectAllCheck.setSelected(allSelected);
        if (bottomSelectAll != null) bottomSelectAll.setSelected(allSelected);
    }

    private void calculateSelectedTotal() {
        double subtotalNormal = 0;
        double totalFinal     = 0;
        int    count          = 0;

        for (CartItem item : CartService.getCartItems()) {
            CheckBox cb = itemCheckboxes.get(item.getProduct());
            if (cb != null && cb.isSelected()) {
                Product product = item.getProduct();
                count          += item.getQuantity();
                totalFinal     += item.getSubtotal();
                subtotalNormal += (product.getPrice() * item.getQuantity());
            }
        }

        double totalDiscountValue = subtotalNormal - totalFinal;

        if (totalLabel         != null) totalLabel.setText("Rp " + Styles.formatPrice(totalFinal));
        if (checkoutCountLabel != null) checkoutCountLabel.setText("(" + count + " items)");

        if (sidebarItemsLabel != null) {
            sidebarItemsLabel.setText("Selected Items: " + count);
            sidebarSubtotalLabel.setText("Rp " + Styles.formatPrice(subtotalNormal));
            sidebarDiscountLabel.setText("-Rp " + Styles.formatPrice(totalDiscountValue));
            sidebarSavedLabel.setText("Rp " + Styles.formatPrice(totalDiscountValue));
            sidebarPointsLabel.setText("+" + (int)(totalFinal / 1000) + " pts");
            sidebarTotalLabel.setText("Rp " + Styles.formatPrice(totalFinal));
        }

        if (bottomBar != null) {
            boolean cartEmpty = CartService.getCartItemCount() == 0;
            bottomBar.setVisible(!cartEmpty);
            bottomBar.setManaged(!cartEmpty);
        }

        updateCheckoutButtonsState(count > 0);
    }

    private void updateCheckoutButtonsState(boolean hasSelection) {
        if (bottomCheckoutBtn == null) return;
        if (hasSelection) {
            bottomCheckoutBtn.setStyle(
                    "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                            "-fx-font-size: 15px; -fx-font-weight: bold; " +
                            "-fx-background-radius: 10; -fx-padding: 12 36; -fx-cursor: hand;"
            );
            bottomCheckoutBtn.setOnMouseEntered(e -> bottomCheckoutBtn.setStyle(
                    "-fx-background-color: #D4A853; -fx-text-fill: #2C1810; " +
                            "-fx-font-size: 15px; -fx-font-weight: bold; " +
                            "-fx-background-radius: 10; -fx-padding: 12 36; -fx-cursor: hand;"
            ));
            bottomCheckoutBtn.setOnMouseExited(e -> bottomCheckoutBtn.setStyle(
                    "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                            "-fx-font-size: 15px; -fx-font-weight: bold; " +
                            "-fx-background-radius: 10; -fx-padding: 12 36; -fx-cursor: hand;"
            ));
        } else {
            bottomCheckoutBtn.setStyle(
                    "-fx-background-color: #D7CCC8; -fx-text-fill: #A1887F; " +
                            "-fx-font-size: 15px; -fx-font-weight: bold; " +
                            "-fx-background-radius: 10; -fx-padding: 12 36; -fx-cursor: default;"
            );
            bottomCheckoutBtn.setOnMouseEntered(null);
            bottomCheckoutBtn.setOnMouseExited(null);
        }
    }

    private List<CartItem> getSelectedCartItems() {
        List<CartItem> selected = new ArrayList<>();
        for (CartItem item : CartService.getCartItems()) {
            CheckBox cb = itemCheckboxes.get(item.getProduct());
            if (cb != null && cb.isSelected()) selected.add(item);
        }
        return selected;
    }

    //  NAV BAR
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

        Button homeBtn    = createNavButton("🏠  Home",    false);
        Button searchBtn  = createNavButton("🔍  Search",  false);
        Button cartBtn    = createNavButton("🛒  Cart",    true);
        Button profileBtn = createNavButton("👤  Profile", false);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));
        cartBtn.setOnAction(e -> { /* already on cart page */ });
        profileBtn.setOnAction(e -> {
            if (AuthService.isLoggedIn()) SceneManager.setScene(new ProfilePage().getScene());
        });

        navButtons.getChildren().addAll(homeBtn, searchBtn, cartBtn, profileBtn);
        navBar.getChildren().addAll(logo, spacer, navButtons);
        return navBar;
    }

    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: #3E2723; -fx-text-fill: #D4A853;" +
                            "-fx-font-size: 13px; -fx-font-weight: bold;" +
                            "-fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: transparent; -fx-text-fill: #3E2723;" +
                            "-fx-font-size: 13px; -fx-font-weight: bold;" +
                            "-fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;"
            );
            btn.setOnMouseEntered(e -> btn.setStyle(
                    "-fx-background-color: #F0E8DF; -fx-text-fill: #3E2723;" +
                            "-fx-font-size: 13px; -fx-font-weight: bold;" +
                            "-fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;"
            ));
            btn.setOnMouseExited(e -> btn.setStyle(
                    "-fx-background-color: transparent; -fx-text-fill: #3E2723;" +
                            "-fx-font-size: 13px; -fx-font-weight: bold;" +
                            "-fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;"
            ));
        }
        return btn;
    }
}
