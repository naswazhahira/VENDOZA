package com.vendoza.ui;

import com.vendoza.model.CartItem;
import com.vendoza.model.Order;
import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MyOrderPage {

    private String currentFilter = "All";
    private VBox orderListContainer;

    public Scene getScene() {
        return getSceneWithFilter("All");
    }

    public Scene getSceneWithFilter(String filter) {
        this.currentFilter = filter;

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(0);
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        // Tab Filter (ala Shopee)
        HBox tabBar = createTabBar();

        // Order List
        orderListContainer = new VBox(12);
        orderListContainer.setPadding(new Insets(15, 40, 40, 40));
        orderListContainer.setAlignment(Pos.CENTER);
        orderListContainer.setMaxWidth(Double.MAX_VALUE);

        loadOrders();

        ScrollPane scrollPane = new ScrollPane(orderListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        mainContent.getChildren().addAll(tabBar, scrollPane);

        VBox root = new VBox(navBar, mainContent);
        VBox.setVgrow(mainContent, Priority.ALWAYS);
        root.setStyle("-fx-background-color: #E8DCD0;");

        Scene scene = new Scene(root, 1200, 700);
        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Button backBtn = new Button("< Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        backBtn.setOnMouseExited(e -> backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        backBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        Label title = new Label("My Order");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navBar.getChildren().addAll(backBtn, title, spacer);
        return navBar;
    }

    private HBox createTabBar() {
        HBox tabBar = new HBox(0);
        tabBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 2);");

        String[] tabs = {"All", "Pending", "Processing", "Shipped", "Delivered"};
        String[] tabIcons = {"📋", "⏳", "📦", "🚚", "✅"};

        for (int i = 0; i < tabs.length; i++) {
            final String tabName = tabs[i];
            Button tabBtn = new Button(tabIcons[i] + " " + tabName);
            HBox.setHgrow(tabBtn, Priority.ALWAYS);
            tabBtn.setMaxWidth(Double.MAX_VALUE);

            // Tambahkan baris ini untuk membuat teks dan ikon rata tengah
            tabBtn.setAlignment(Pos.CENTER);

            boolean isActive = tabName.equals(currentFilter);
            styleTabButton(tabBtn, isActive);

            tabBtn.setOnAction(e -> {
                SceneManager.setScene(new MyOrderPage().getSceneWithFilter(tabName));
            });

            tabBar.getChildren().add(tabBtn);
        }

        return tabBar;
    }

    private void styleTabButton(Button btn, boolean isActive) {
        if (isActive) {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.GOLD + ";" +
                    "-fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand;" +
                    "-fx-border-color: transparent transparent " + Styles.GOLD + " transparent;" +
                    "-fx-border-width: 0 0 3 0; -fx-padding: 14 10;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                    "-fx-font-size: 13px; -fx-cursor: hand; -fx-padding: 14 10;");
        }
    }

    private void loadOrders() {
        orderListContainer.getChildren().clear();
        User user = AuthService.getCurrentUser();
        if (user == null) return;

        List<Order> orders = user.getOrders();
        if (!currentFilter.equals("All")) {
            orders = orders.stream()
                    .filter(o -> o.getStatus().equals(currentFilter))
                    .collect(Collectors.toList());
        }

        if (orders.isEmpty()) {
            VBox emptyBox = new VBox(15);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(80, 0, 0, 0));

            Label emptyIcon = new Label("🛍️");
            emptyIcon.setStyle("-fx-font-size: 60px;");

            Label emptyLabel = new Label("No orders yet");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

            Label emptySubLabel = new Label("Start shopping to see your orders here!");
            emptySubLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

            Button shopBtn = new Button("🛒 Shop Now");
            shopBtn.setStyle(Styles.buttonStyle());
            shopBtn.setOnAction(e -> SceneManager.showHomePage());

            emptyBox.getChildren().addAll(emptyIcon, emptyLabel, emptySubLabel, shopBtn);
            orderListContainer.getChildren().add(emptyBox);
        } else {
            for (Order order : orders) {
                orderListContainer.getChildren().add(createOrderCard(order));
            }
        }
    }

    private VBox createOrderCard(Order order) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 12; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        // Order Header
        HBox headerRow = new HBox(10);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label orderIdLabel = new Label("Order #" + order.getOrderId());
        orderIdLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Status Badge
        Label statusLabel = new Label(getStatusIcon(order.getStatus()) + " " + getStatusName(order.getStatus()));
        statusLabel.setStyle("-fx-background-color: " + getStatusColor(order.getStatus()) + ";" +
                "-fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold;" +
                "-fx-background-radius: 20; -fx-padding: 4 12;");

        // Date
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        Label dateLabel = new Label(order.getOrderDate().format(fmt));
        dateLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        headerRow.getChildren().addAll(orderIdLabel, spacer, dateLabel, statusLabel);

        // Divider
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        // Items
        VBox itemsBox = new VBox(6);
        for (CartItem item : order.getItems()) {
            HBox itemRow = new HBox(10);
            itemRow.setAlignment(Pos.CENTER_LEFT);

            Label itemName = new Label("• " + item.getProduct().getName());
            itemName.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

            Region itemSpacer = new Region();
            HBox.setHgrow(itemSpacer, Priority.ALWAYS);

            Label itemQty = new Label("x" + item.getQuantity());
            itemQty.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

            Label itemPrice = new Label("Rp " + Styles.formatPrice(item.getSubtotal()));
            itemPrice.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

            itemRow.getChildren().addAll(itemName, itemSpacer, itemQty, itemPrice);
            itemsBox.getChildren().add(itemRow);
        }

        // Footer: total + actions
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(8, 0, 0, 0));
        footer.setStyle("-fx-border-color: " + Styles.BROWN_PALE + " transparent transparent transparent;" +
                "-fx-border-width: 1 0 0 0;");

        Label totalTextLabel = new Label("Total:");
        totalTextLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        Label totalLabel = new Label("Rp " + Styles.formatPrice(order.getTotal()));
        totalLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);

        Button detailBtn = new Button("Detail");
        detailBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 12px; -fx-padding: 6 15;");
        detailBtn.setOnMouseEntered(e -> detailBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 12px; -fx-padding: 6 15;"));
        detailBtn.setOnMouseExited(e -> detailBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 12px; -fx-padding: 6 15;"));
        detailBtn.setOnAction(e -> showOrderDetail(order));

        footer.getChildren().addAll(totalTextLabel, totalLabel, footerSpacer, detailBtn);

        // Address row
        HBox addressRow = new HBox(5);
        addressRow.setAlignment(Pos.CENTER_LEFT);
        Label addressIcon = new Label("📍");
        addressIcon.setStyle("-fx-font-size: 11px;");
        Label addressLabel = new Label(order.getShippingAddress() != null ? order.getShippingAddress() : "No address");
        addressLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");
        addressLabel.setWrapText(false);
        addressRow.getChildren().addAll(addressIcon, addressLabel);

        card.getChildren().addAll(headerRow, sep, itemsBox, addressRow, footer);
        return card;
    }

    private void showOrderDetail(Order order) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Order Detail");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + "; -fx-background-radius: 15;");
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        Button closeBtn = (Button) dialogPane.lookupButton(ButtonType.CLOSE);
        closeBtn.setStyle(Styles.buttonStyle());

        VBox content = new VBox(12);
        content.setPadding(new Insets(20));
        content.setMinWidth(450);

        // Title
        Label title = new Label("📋 Order #" + order.getOrderId());
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        // Status badge
        Label statusBadge = new Label(getStatusIcon(order.getStatus()) + " " + getStatusName(order.getStatus()));
        statusBadge.setStyle("-fx-background-color: " + getStatusColor(order.getStatus()) + ";" +
                "-fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;" +
                "-fx-background-radius: 20; -fx-padding: 5 15;");

        // Date
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        Label dateLabel = new Label("🕐 " + order.getOrderDate().format(fmt));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        Separator sep1 = new Separator();

        // Items header
        Label itemsTitle = new Label("Items Ordered:");
        itemsTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        VBox itemsList = new VBox(8);
        for (CartItem item : order.getItems()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-background-color: " + Styles.BROWN_PALE + "; -fx-background-radius: 8; -fx-padding: 8 10;");

            Label nameLabel = new Label(item.getProduct().getName());
            nameLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Label qtyLabel = new Label("x" + item.getQuantity());
            qtyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

            Label priceLabel = new Label("Rp " + Styles.formatPrice(item.getSubtotal()));
            priceLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

            row.getChildren().addAll(nameLabel, sp, qtyLabel, priceLabel);
            itemsList.getChildren().add(row);
        }

        Separator sep2 = new Separator();

        // Price summary
        VBox priceSummary = new VBox(5);
        priceSummary.setStyle("-fx-background-color: " + Styles.BROWN_PALE + "; -fx-background-radius: 8; -fx-padding: 10;");

        HBox subtotalRow = createPriceRow("Subtotal:", "Rp " + Styles.formatPrice(order.getSubtotal()), false);
        HBox shippingRow = createPriceRow("Shipping:", "Rp " + Styles.formatPrice(order.getShippingCost()), false);
        HBox totalRow = createPriceRow("Total:", "Rp " + Styles.formatPrice(order.getTotal()), true);

        priceSummary.getChildren().addAll(subtotalRow, shippingRow, new Separator(), totalRow);

        // Shipping Address
        Label addressTitle = new Label("📍 Shipping Address:");
        addressTitle.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label addressText = new Label(order.getShippingAddress());
        addressText.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-wrap-text: true;");
        addressText.setWrapText(true);
        addressText.setMaxWidth(400);

        content.getChildren().addAll(title, statusBadge, dateLabel, sep1,
                itemsTitle, itemsList, sep2, priceSummary, addressTitle, addressText);

        dialogPane.setContent(content);
        dialog.showAndWait();
    }

    private HBox createPriceRow(String label, String value, boolean bold) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);

        Label labelNode = new Label(label);
        String baseStyle = "-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + ";";
        labelNode.setStyle(bold ? baseStyle + " -fx-font-weight: bold;" : baseStyle);

        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);

        Label valueNode = new Label(value);
        String valStyle = "-fx-font-size: 13px; -fx-text-fill: " + (bold ? Styles.GOLD : Styles.TEXT_DARK) + ";";
        valueNode.setStyle(bold ? valStyle + " -fx-font-weight: bold;" : valStyle);

        row.getChildren().addAll(labelNode, sp, valueNode);
        return row;
    }

    private String getStatusName(String status) {
        return switch (status) {
            case "Pending" -> "Pending Payment";
            case "Processing" -> "Processing";
            case "Shipped" -> "Shipped";
            case "Delivered" -> "Delivered";
            default -> status;
        };
    }

    private String getStatusIcon(String status) {
        return switch (status) {
            case "Pending" -> "⏳";
            case "Processing" -> "📦";
            case "Shipped" -> "🚚";
            case "Delivered" -> "✅";
            default -> "❓";
        };
    }

    private String getStatusColor(String status) {
        return switch (status) {
            case "Pending" -> "#F0A500";
            case "Processing" -> "#2196F3";
            case "Shipped" -> "#9C27B0";
            case "Delivered" -> "#4CAF50";
            default -> Styles.BROWN_MEDIUM;
        };
    }
}
