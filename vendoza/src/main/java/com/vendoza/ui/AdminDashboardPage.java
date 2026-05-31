package com.vendoza.ui;

import com.vendoza.service.AuthService;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class AdminDashboardPage {

    public Scene getScene() {
        // Cek apakah user adalah admin
        System.out.println("AdminDashboardPage: Checking access...");
        System.out.println("Is logged in? " + AuthService.isLoggedIn());
        System.out.println("Is admin? " + AuthService.isAdmin());

        if (!AuthService.isLoggedIn()) {
            System.out.println("Not logged in, redirect to login");
            showAccessDeniedAlert("Please login first!");
            return new LoginPage().getScene();
        }

        if (!AuthService.isAdmin()) {
            System.out.println("Not admin, redirect to home");
            showAccessDeniedAlert("You don't have admin access!");
            return new HomePage().getScene();
        }

        System.out.println("Access granted! Showing Admin Dashboard");

        // Navbar khusus admin
        HBox navBar = createAdminNavBar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 40, 40, 40));
        mainContent.setStyle("-fx-background-color: #E8DCD0;");

        // Welcome Section
        VBox welcomeBox = createWelcomeSection();

        // Stats Cards
        HBox statsBox = createStatsCards();

        // Quick Actions
        GridPane quickActions = createQuickActions();

        mainContent.getChildren().addAll(welcomeBox, statsBox, quickActions);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #E8DCD0;");

        return new Scene(root, 1200, 700);
    }

    private HBox createAdminNavBar() {
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: #2D1B11; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label logo = new Label("👑 ADMIN PANEL");
        logo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        Button homeBtn = createAdminButton("🏠 Home", false);
        Button dashboardBtn = createAdminButton("📊 Dashboard", true);
        Button logoutBtn = createAdminButton("🚪 Logout", false);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
        dashboardBtn.setOnAction(e -> SceneManager.setScene(new AdminDashboardPage().getScene()));
        logoutBtn.setOnAction(e -> {
            AuthService.logout();
            SceneManager.showHomePage();
        });

        navBar.getChildren().addAll(logo, leftSpacer, homeBtn, dashboardBtn, logoutBtn, rightSpacer);
        return navBar;
    }

    private Button createAdminButton(String text, boolean isActive) {
        Button btn = new Button(text);
        if (isActive) {
            btn.setStyle("-fx-background-color: #D4AF37; -fx-text-fill: #2D1B11;" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 25;" +
                    "-fx-padding: 8 20; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;" +
                    "-fx-font-size: 14px; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #D4AF37; -fx-text-fill: #2D1B11;" +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 25;" +
                    "-fx-padding: 8 20; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;" +
                    "-fx-font-size: 14px; -fx-cursor: hand;"));
        }
        return btn;
    }

    private VBox createWelcomeSection() {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);

        Text welcome = new Text("Welcome, " + AuthService.getCurrentUser().getUsername() + "!");
        welcome.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #5D4037;");

        Text role = new Text("Administrator Access • Full Control");
        role.setStyle("-fx-font-size: 14px; -fx-fill: #8D6E63;");

        box.getChildren().addAll(welcome, role);
        return box;
    }

    private HBox createStatsCards() {
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(20, 0, 20, 0));

        VBox productsCard = createStatCard("📦", "Total Products", "12", "#5D4037");
        VBox ordersCard = createStatCard("📋", "Total Orders", "8", "#8D6E63");
        VBox usersCard = createStatCard("👥", "Total Users", "4", "#D4AF37");
        VBox revenueCard = createStatCard("💰", "Revenue", "Rp 4.2M", "#C62828");

        statsBox.getChildren().addAll(productsCard, ordersCard, usersCard, revenueCard);
        return statsBox;
    }

    private VBox createStatCard(String icon, String title, String value, String color) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20;");
        card.setPrefWidth(180);
        card.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.08)));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 32px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(iconLabel, titleLabel, valueLabel);
        return card;
    }

    private GridPane createQuickActions() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 0, 0, 0));

        ActionCard productCard = new ActionCard("📦", "Manage Products", "Add, edit, or delete products", "#5D4037");
        productCard.setOnMouseClicked(e -> showMessage("Product Management", "Product management features coming soon!"));

        ActionCard orderCard = new ActionCard("📋", "Manage Orders", "View and update order status", "#8D6E63");
        orderCard.setOnMouseClicked(e -> showMessage("Order Management", "Order management features coming soon!"));

        ActionCard userCard = new ActionCard("👥", "Manage Users", "View registered users", "#D4AF37");
        userCard.setOnMouseClicked(e -> showMessage("User Management", "User management features coming soon!"));

        ActionCard reportCard = new ActionCard("📊", "View Reports", "Sales and revenue reports", "#C62828");
        reportCard.setOnMouseClicked(e -> showMessage("Reports", "Report features coming soon!"));

        grid.add(productCard, 0, 0);
        grid.add(orderCard, 1, 0);
        grid.add(userCard, 0, 1);
        grid.add(reportCard, 1, 1);

        return grid;
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    private void showAccessDeniedAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Access Denied");
        alert.setHeaderText("⚠️ Admin Access Required");
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20;");
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.setStyle(Styles.buttonStyle());

        alert.showAndWait();
    }

    // Inner class untuk action card
    private static class ActionCard extends VBox {
        public ActionCard(String icon, String title, String description, String color) {
            super(10);
            setAlignment(Pos.CENTER_LEFT);
            setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20;");
            setPrefWidth(350);
            setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.08)));
            setCursor(javafx.scene.Cursor.HAND);

            Label iconLabel = new Label(icon);
            iconLabel.setStyle("-fx-font-size: 36px;");

            Label titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

            Label descLabel = new Label(description);
            descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63;");
            descLabel.setWrapText(true);

            getChildren().addAll(iconLabel, titleLabel, descLabel);

            setOnMouseEntered(e -> {
                setStyle("-fx-background-color: #EFEBE9; -fx-background-radius: 15; -fx-padding: 20;");
                ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
                st.setToX(1.02);
                st.setToY(1.02);
                st.play();
            });
            setOnMouseExited(e -> {
                setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 20;");
                ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
                st.setToX(1);
                st.setToY(1);
                st.play();
            });
        }
    }
}