package com.vendoza.ui;

import com.vendoza.model.Order;
import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;

public class ProfilePage {

    public Scene getScene() {
        // CEK LOGIN: Jika belum login, arahkan ke login page
        if (!AuthService.isLoggedIn()) {
            showLoginRequiredAlert();
            return new LoginPage().getScene();
        }

        User currentUser = AuthService.getCurrentUser();

        // Top Navigation Bar
        HBox navBar = createNavBar();

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        // ===== 1. PROFILE HEADER =====
        HBox profileHeader = createProfileHeader(currentUser);

        // ===== 2. MENU PESANAN =====
        VBox orderMenuSection = createOrderMenuSection();

        // ===== 3. MENU LAINNYA =====
        VBox otherMenuSection = createOtherMenuSection();

        // ===== 4. LOGOUT BUTTON =====
        Button logoutBtn = createLogoutButton();

        mainContent.getChildren().addAll(
                profileHeader,
                orderMenuSection,
                otherMenuSection,
                logoutBtn
        );

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");

        Scene scene = new Scene(root, 1200, 700);
        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label logo = new Label("👤 VENDOZA");
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Button homeBtn = createNavButton("🏠 Home", false);
        Button searchBtn = createNavButton("🔍 Search", false);
        Button cartBtn = createNavButton("🛒 Cart", false);
        Button profileBtn = createNavButton("👤 Profile", true);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
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

    private HBox createProfileHeader(User user) {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        // Profile Icon (KLIK UNTUK EDIT)
        StackPane profileIcon = createProfileIcon();

        // Profile Info
        VBox profileInfo = new VBox(5);

        Label nameLabel = new Label(user.getUsername());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label emailLabel = new Label(user.getEmail());
        emailLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        HBox editBox = new HBox(5);
        editBox.setAlignment(Pos.CENTER_LEFT);
        editBox.setStyle("-fx-cursor: hand;");

        Label editIcon = new Label("✎");
        editIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + ";");

        Label editLabel = new Label("Edit Profile →");
        editLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + ";");

        editBox.getChildren().addAll(editIcon, editLabel);
        editBox.setOnMouseClicked(e -> SceneManager.setScene(new EditProfilePage().getScene()));
        editBox.setOnMouseEntered(e -> {
            editLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.BROWN_DARK + ";");
            editIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.BROWN_DARK + ";");
        });
        editBox.setOnMouseExited(e -> {
            editLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + ";");
            editIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + ";");
        });

        profileInfo.getChildren().addAll(nameLabel, emailLabel, editBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(profileIcon, profileInfo, spacer);
        return header;
    }

    private StackPane createProfileIcon() {
        StackPane iconContainer = new StackPane();
        iconContainer.setStyle("-fx-cursor: hand;");

        Circle circle = new Circle(40);
        circle.setStyle("-fx-fill: " + Styles.BROWN_DARK + ";");
        circle.setEffect(new DropShadow(5, javafx.scene.paint.Color.rgb(0, 0, 0, 0.1)));

        Label iconLabel = new Label("👤");
        iconLabel.setStyle("-fx-font-size: 45px;");

        iconContainer.getChildren().addAll(circle, iconLabel);
        iconContainer.setOnMouseClicked(e -> SceneManager.setScene(new EditProfilePage().getScene()));

        iconContainer.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), iconContainer);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        iconContainer.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), iconContainer);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        return iconContainer;
    }

    private VBox createOrderMenuSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("📋 My Order");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label historyLabel = new Label("View Order History →");
        historyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + "; -fx-cursor: hand;");
        historyLabel.setOnMouseClicked(e -> showOrderHistory());
        historyLabel.setOnMouseEntered(e -> historyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.BROWN_DARK + "; -fx-cursor: hand;"));
        historyLabel.setOnMouseExited(e -> historyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + "; -fx-cursor: hand;"));

        header.getChildren().addAll(titleLabel, spacer, historyLabel);

        // Order Status Menu
        HBox orderMenu = new HBox(30);
        orderMenu.setAlignment(Pos.CENTER);
        orderMenu.setPadding(new Insets(15, 0, 10, 0));

        long belumBayarCount = countOrdersByStatus("Pending");
        long dikemasCount = countOrdersByStatus("Processing");
        long dikirimCount = countOrdersByStatus("Shipped");
        long selesaiCount = countOrdersByStatus("Delivered");

        VBox belumBayar = createOrderMenuItem("⏳", "Pending", String.valueOf(belumBayarCount));
        belumBayar.setOnMouseClicked(e -> filterOrdersByStatus("Pending"));

        VBox dikemas = createOrderMenuItem("📦", "Processing", String.valueOf(dikemasCount));
        dikemas.setOnMouseClicked(e -> filterOrdersByStatus("Processing"));

        VBox dikirim = createOrderMenuItem("🚚", "Shipped", String.valueOf(dikirimCount));
        dikirim.setOnMouseClicked(e -> filterOrdersByStatus("Shipped"));

        VBox beriPenilaian = createOrderMenuItem("⭐", "Rating", String.valueOf(selesaiCount));
        beriPenilaian.setOnMouseClicked(e -> showRatingPage());

        orderMenu.getChildren().addAll(belumBayar, dikemas, dikirim, beriPenilaian);

        section.getChildren().addAll(header, orderMenu);
        return section;
    }

    private VBox createOrderMenuItem(String icon, String text, String count) {
        VBox item = new VBox(5);
        item.setAlignment(Pos.CENTER);
        item.setPrefWidth(120);
        item.setStyle("-fx-cursor: hand; -fx-padding: 10 0; -fx-background-radius: 10;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 28px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        Label countLabel = new Label(count);
        countLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        item.getChildren().addAll(iconLabel, textLabel, countLabel);

        item.setOnMouseEntered(e -> {
            item.setStyle("-fx-cursor: hand; -fx-padding: 10 0; -fx-background-radius: 10; -fx-background-color: " + Styles.BROWN_PALE + ";");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), item);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        item.setOnMouseExited(e -> {
            item.setStyle("-fx-cursor: hand; -fx-padding: 10 0; -fx-background-radius: 10;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), item);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        return item;
    }

    private VBox createOtherMenuSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label titleLabel = new Label("📌 Other Menu");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        VBox menuList = new VBox(5);

        HBox alamatItem = createMenuItem("📍", "My Address");
        alamatItem.setOnMouseClicked(e -> showAddressPage());

        HBox paymentItem = createMenuItem("💳", "Payment Methods");
        paymentItem.setOnMouseClicked(e -> showPaymentPage());

        HBox helpItem = createMenuItem("❓", "Help Center");
        helpItem.setOnMouseClicked(e -> showHelpPage());

        HBox aboutItem = createMenuItem("ℹ️", "About Vendoza");
        aboutItem.setOnMouseClicked(e -> showAboutPage());

        menuList.getChildren().addAll(alamatItem, paymentItem, helpItem, aboutItem);
        section.getChildren().addAll(titleLabel, menuList);

        return section;
    }

    private HBox createMenuItem(String icon, String text) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10, 5, 10, 5));
        item.setStyle("-fx-cursor: hand; -fx-background-radius: 10;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrowLabel = new Label("→");
        arrowLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.GOLD + ";");

        item.getChildren().addAll(iconLabel, textLabel, spacer, arrowLabel);

        item.setOnMouseEntered(e -> {
            item.setStyle("-fx-background-color: #E8DCD0; -fx-cursor: hand; -fx-padding: 10 5 10 5; -fx-background-radius: 10;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), item);
            st.setToX(1.01);
            st.setToY(1.01);
            st.play();
        });
        item.setOnMouseExited(e -> {
            item.setStyle("-fx-cursor: hand; -fx-padding: 10 5 10 5; -fx-background-radius: 10;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), item);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        return item;
    }

    private Button createLogoutButton() {
        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.ERROR_RED + ";" +
                "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                "-fx-padding: 12 0; -fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold;");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);

        logoutBtn.setOnMouseEntered(e -> {
            logoutBtn.setStyle("-fx-background-color: " + Styles.ERROR_RED + "; -fx-text-fill: white;" +
                    "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                    "-fx-padding: 12 0; -fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), logoutBtn);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        logoutBtn.setOnMouseExited(e -> {
            logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.ERROR_RED + ";" +
                    "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                    "-fx-padding: 12 0; -fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold;");
            ScaleTransition st = new ScaleTransition(Duration.millis(150), logoutBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        logoutBtn.setOnAction(e -> {
            AuthService.logout();
            SceneManager.showHomePage();
        });

        return logoutBtn;
    }

    private void showLoginRequiredAlert() {
        LoginRequiredDialog.show("You need to login to access your profile.");
    }

    private void showLoginAlert() {
        LoginRequiredDialog.show("You need to login to access this feature.");
    }

    private long countOrdersByStatus(String status) {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) return 0;
        return currentUser.getOrders().stream()
                .filter(order -> order.getStatus().equals(status))
                .count();
    }

    private void filterOrdersByStatus(String status) {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser == null) return;

        List<Order> filteredOrders = currentUser.getOrders().stream()
                .filter(order -> order.getStatus().equals(status))
                .toList();

        if (filteredOrders.isEmpty()) {
            showAlert("Info", "No orders with status: " + getStatusName(status));
        } else {
            StringBuilder message = new StringBuilder("Orders with status " + getStatusName(status) + ":\n\n");
            for (Order order : filteredOrders) {
                message.append("Order #").append(order.getOrderId())
                        .append("\nTotal: Rp ").append(Styles.formatPrice(order.getTotal()))
                        .append("\n\n");
            }
            showAlert("Orders", message.toString());
        }
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

    private void showOrderHistory() {
        User currentUser = AuthService.getCurrentUser();
        StringBuilder history = new StringBuilder("Order History:\n\n");

        if (currentUser.getOrders().isEmpty()) {
            history.append("No orders yet");
        } else {
            for (Order order : currentUser.getOrders()) {
                history.append("Order #").append(order.getOrderId())
                        .append("\nStatus: ").append(getStatusName(order.getStatus()))
                        .append("\nTotal: Rp ").append(Styles.formatPrice(order.getTotal()))
                        .append("\n\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order History");
        alert.setHeaderText(null);
        alert.setContentText(history.toString());

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void showRatingPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rating");
        alert.setHeaderText("⭐ Rate Products");
        alert.setContentText("Product rating feature coming soon!");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void showAddressPage() {
        User user = AuthService.getCurrentUser();
        String address = user.getAddress() != null && !user.getAddress().isEmpty() ? user.getAddress() : "No address saved yet";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("My Address");
        alert.setHeaderText("📍 Shipping Address");
        alert.setContentText(address);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void showPaymentPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Methods");
        alert.setHeaderText("💳 Available Payment Methods");
        alert.setContentText("1. Bank Transfer (BCA/Mandiri/BNI)\n2. Credit Card\n3. E-Wallet (GoPay/OVO/Dana)\n4. Cash on Delivery (COD)");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void showHelpPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help Center");
        alert.setHeaderText("❓ Customer Support");
        alert.setContentText("Email: support@vendoza.com\nWhatsApp: 0812-3456-7890\nInstagram: @vendoza");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void showAboutPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Vendoza");
        alert.setHeaderText("👗 VENDOZA");
        alert.setContentText("Version 1.0\n\nFashion aesthetic for modern style.\nFashion for today's generation.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }
}
