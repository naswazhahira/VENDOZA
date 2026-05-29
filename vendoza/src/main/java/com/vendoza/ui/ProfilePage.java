package com.vendoza.ui;

import com.vendoza.model.Order;
import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import java.util.List;

public class ProfilePage {

    public Scene getScene() {
        // CEK LOGIN: Jika belum login, arahkan ke login page
        if (!AuthService.isLoggedIn()) {
            showLoginRequiredAlert();
            return new LoginPage().getScene();
        }

        User currentUser = AuthService.getCurrentUser();

        // Top Navigation Bar (sudah disesuaikan)
        HBox navBar = createNavBar();

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(15, 20, 30, 20));

        // ===== 1. PROFILE HEADER (dengan icon profile) =====
        HBox profileHeader = createProfileHeader(currentUser);

        // ===== 2. MENU PESANAN (Belum Bayar, Dikemas, Dikirim, Rating) =====
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
        scrollPane.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-background: " + Styles.BROWN_PALE + ";");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";");

        return new Scene(root, 1200, 700);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: " + Styles.WHITE + "; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        // Logo VENDOZA dengan icon
        Label logo = new Label("🛍️ VENDOZA");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        // Spacer kiri dan kanan (biar menu di tengah)
        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // Navigation Buttons
        Button homeBtn = createNavButton("Home", false);
        Button searchBtn = createNavButton("Search", false);
        Button cartBtn = createNavButton("Cart 🛒", false);
        Button profileBtn = createNavButton("Profile 👤", true);

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
        profileBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

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

    private HBox createProfileHeader(User user) {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;");

        // Profile Icon (Circle dengan gambar) - KLIK UNTUK EDIT
        StackPane profileIcon = createProfileIcon();

        // Profile Info
        VBox profileInfo = new VBox(5);

        Label nameLabel = new Label(user.getUsername());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Label emailLabel = new Label(user.getEmail());
        emailLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        Label editLabel = new Label("✎ Ubah Profil →");
        editLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + "; -fx-cursor: hand;");
        editLabel.setOnMouseClicked(e -> SceneManager.setScene(new EditProfilePage().getScene()));

        profileInfo.getChildren().addAll(nameLabel, emailLabel, editLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(profileIcon, profileInfo, spacer);
        return header;
    }

    private StackPane createProfileIcon() {
        StackPane iconContainer = new StackPane();
        iconContainer.setStyle("-fx-cursor: hand;");

        // Circle background
        Circle circle = new Circle(40);
        circle.setStyle("-fx-fill: " + Styles.BROWN_DARK + ";");

        // Profile icon (default)
        Label iconLabel = new Label("👤");
        iconLabel.setStyle("-fx-font-size: 45px;");

        iconContainer.getChildren().addAll(circle, iconLabel);

        // Klik untuk buka halaman edit profil
        iconContainer.setOnMouseClicked(e -> SceneManager.setScene(new EditProfilePage().getScene()));

        return iconContainer;
    }

    private VBox createOrderMenuSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;");

        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("My Order");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label historyLabel = new Label("Lihat Riwayat Pesanan →");
        historyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-cursor: hand;");
        historyLabel.setOnMouseClicked(e -> showOrderHistory());

        header.getChildren().addAll(titleLabel, spacer, historyLabel);

        // Order Status Menu
        HBox orderMenu = new HBox(30);
        orderMenu.setAlignment(Pos.CENTER);
        orderMenu.setPadding(new Insets(15, 0, 10, 0));

        // Hitung jumlah pesanan per status
        long belumBayarCount = countOrdersByStatus("Pending");
        long dikemasCount = countOrdersByStatus("Processing");
        long dikirimCount = countOrdersByStatus("Shipped");
        long selesaiCount = countOrdersByStatus("Delivered");

        // Belum Bayar
        VBox belumBayar = createOrderMenuItem("⏳", "Pending", String.valueOf(belumBayarCount));
        belumBayar.setOnMouseClicked(e -> filterOrdersByStatus("Pending"));

        // Dikemas
        VBox dikemas = createOrderMenuItem("📦", "Processing", String.valueOf(dikemasCount));
        dikemas.setOnMouseClicked(e -> filterOrdersByStatus("Processing"));

        // Dikirim
        VBox dikirim = createOrderMenuItem("🚚", "Shipped", String.valueOf(dikirimCount));
        dikirim.setOnMouseClicked(e -> filterOrdersByStatus("Shipped"));

        // Beri Penilaian
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
        item.setStyle("-fx-cursor: hand;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 28px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        Label countLabel = new Label(count);
        countLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

        item.getChildren().addAll(iconLabel, textLabel, countLabel);
        return item;
    }

    private VBox createOtherMenuSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 15;");

        Label titleLabel = new Label("Menu Lainnya");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        VBox menuList = new VBox(5);

        // Alamat Saya
        HBox alamatItem = createMenuItem("📍", "My Address");
        alamatItem.setOnMouseClicked(e -> showAddressPage());

        // Metode Pembayaran
        HBox paymentItem = createMenuItem("💳", "Payment");
        paymentItem.setOnMouseClicked(e -> showPaymentPage());

        // Bantuan
        HBox helpItem = createMenuItem("❓", "Help");
        helpItem.setOnMouseClicked(e -> showHelpPage());

        // Tentang Aplikasi
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
        item.setStyle("-fx-cursor: hand;");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrowLabel = new Label("→");
        arrowLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        item.getChildren().addAll(iconLabel, textLabel, spacer, arrowLabel);

        // Hover effect
        item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: " + Styles.BROWN_PALE + "; -fx-cursor: hand; -fx-padding: 10 5 10 5;"));
        item.setOnMouseExited(e -> item.setStyle("-fx-cursor: hand; -fx-padding: 10 5 10 5;"));

        return item;
    }

    private Button createLogoutButton() {
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.ERROR_RED + ";" +
                "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                "-fx-padding: 10 0; -fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold;");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            AuthService.logout();
            SceneManager.setScene(new LoginPage().getScene());
        });

        return logoutBtn;
    }

    private void showLoginRequiredAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login Required");
        alert.setHeaderText("Please login first");
        alert.setContentText("You need to login to access your profile.");
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

    private long countOrdersByStatus(String status) {
        return AuthService.getCurrentUser().getOrders().stream()
                .filter(order -> order.getStatus().equals(status))
                .count();
    }

    private void filterOrdersByStatus(String status) {
        List<Order> filteredOrders = AuthService.getCurrentUser().getOrders().stream()
                .filter(order -> order.getStatus().equals(status))
                .toList();

        if (filteredOrders.isEmpty()) {
            showAlert("Info", "Tidak ada pesanan dengan status: " + getStatusName(status));
        } else {
            StringBuilder message = new StringBuilder("Pesanan dengan status " + getStatusName(status) + ":\n\n");
            for (Order order : filteredOrders) {
                message.append("Order #").append(order.getOrderId())
                        .append("\nTotal: Rp ").append(Styles.formatPrice(order.getTotal()))
                        .append("\n\n");
            }
            showAlert("Pesanan", message.toString());
        }
    }

    private String getStatusName(String status) {
        return switch (status) {
            case "Pending" -> "Belum Bayar";
            case "Processing" -> "Dikemas";
            case "Shipped" -> "Dikirim";
            case "Delivered" -> "Selesai";
            default -> status;
        };
    }

    private void showOrderHistory() {
        StringBuilder history = new StringBuilder("Riwayat Pesanan:\n\n");
        for (Order order : AuthService.getCurrentUser().getOrders()) {
            history.append("Order #").append(order.getOrderId())
                    .append("\nStatus: ").append(getStatusName(order.getStatus()))
                    .append("\nTotal: Rp ").append(Styles.formatPrice(order.getTotal()))
                    .append("\nTanggal: ").append(order.getOrderDate())
                    .append("\n\n");
        }

        if (AuthService.getCurrentUser().getOrders().isEmpty()) {
            history.append("Belum ada pesanan");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Riwayat Pesanan");
        alert.setHeaderText(null);
        alert.setContentText(history.toString());
        alert.showAndWait();
    }

    private void showRatingPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rating");
        alert.setHeaderText("⭐ Beri Penilaian");
        alert.setContentText("Fitur penilaian produk akan segera hadir!");
        alert.showAndWait();
    }

    private void showAddressPage() {
        User user = AuthService.getCurrentUser();
        String address = user.getAddress() != null ? user.getAddress() : "Belum diisi";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("My Address");
        alert.setHeaderText("📍 Alamat Pengiriman");
        alert.setContentText(address);
        alert.showAndWait();
    }

    private void showPaymentPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Metode Pembayaran");
        alert.setHeaderText("💳 Metode Pembayaran");
        alert.setContentText("1. Bank Transfer (BCA/Mandiri/BNI)\n2. Credit Card\n3. E-Wallet\n4. Cash on Delivery (COD)");
        alert.showAndWait();
    }

    private void showHelpPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bantuan");
        alert.setHeaderText("❓ Pusat Bantuan");
        alert.setContentText("Hubungi customer service:\nEmail: support@vendoza.com\nWhatsApp: 0812-3456-7890");
        alert.showAndWait();
    }

    private void showAboutPage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Vendoza");
        alert.setHeaderText("VENDOZA");
        alert.setContentText("Version 1.0\n\nAplikasi fashion aesthetic.\nFashion untuk masa kini.");
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}