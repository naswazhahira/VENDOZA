    package com.vendoza.ui;

    import com.vendoza.model.User;
    import com.vendoza.service.AuthService;
    import javafx.animation.ScaleTransition;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.effect.DropShadow;
    import javafx.scene.image.Image;
    import javafx.scene.image.PixelReader;
    import javafx.scene.image.WritableImage;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.scene.paint.ImagePattern;
    import javafx.scene.shape.Circle;
    import javafx.stage.Screen;
    import javafx.util.Duration;
    import com.google.gson.JsonArray;
    import com.google.gson.JsonObject;
    import com.google.gson.JsonParser;
    import java.net.URI;
    import java.net.http.HttpClient;
    import java.net.http.HttpRequest;
    import java.net.http.HttpResponse;
    import java.util.HashMap;
    import java.util.Map;

    // TAMBAHKAN IMPORT UNTUK CLASS YANG MISSING
    import com.vendoza.ui.MyOrderPage;
    import com.vendoza.ui.ShippingAddressPage;
    import com.vendoza.ui.PaymentMethodPage;
    import com.vendoza.ui.HelpCenterPage;
    import com.vendoza.ui.AboutPage;
    import com.vendoza.ui.EditProfilePage;
    import com.vendoza.ui.LoginRequiredDialog;
    import com.vendoza.ui.SearchPage;
    import com.vendoza.ui.CartPage;

    public class ProfilePage {

        public Scene getScene() {
            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();

            if (!AuthService.isLoggedIn()) {
                showLoginRequiredAlert();
                return new LoginPage().getScene();
            }

            User currentUser = AuthService.getCurrentUser();

            HBox navBar = createNavBar();

            VBox mainContent = new VBox(15);
            mainContent.setPadding(new Insets(20, 40, 40, 40));
            mainContent.setStyle("-fx-background-color: #ebddc3;");
            mainContent.setFillWidth(true);

            HBox profileHeader = createProfileHeader(currentUser);
            VBox orderMenuSection = createOrderMenuSection(currentUser);
            VBox otherMenuSection = createOtherMenuSection();
            Button logoutBtn = createLogoutButton();

            mainContent.getChildren().addAll(profileHeader, orderMenuSection, otherMenuSection, logoutBtn);

            ScrollPane scrollPane = new ScrollPane(mainContent);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: #E8DCD0; -fx-background: #E8DCD0;");
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setBorder(Border.EMPTY);

            VBox root = new VBox(navBar, scrollPane);
            root.setStyle("-fx-background-color: #E8DCD0;");

            Scene scene = new Scene(root, screenWidth, screenHeight);
            return scene;
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
            Button searchBtn = createNavButton("🔍  Search", false);
            Button cartBtn = createNavButton("🛒  Cart", false);
            Button profileBtn = createNavButton("👤  Profile", true);

            homeBtn.setOnAction(e -> SceneManager.showHomePage());
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

        private Button createNavButton(String text, boolean isActive) {
            Button btn = new Button(text);
            String activeStyle =
                    "-fx-background-color: #3E2723;" +
                            "-fx-text-fill: #D4A853;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25;" +
                            "-fx-padding: 9 22;" +
                            "-fx-cursor: hand;";
            String defaultStyle =
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: #5D4037;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25;" +
                            "-fx-padding: 9 22;" +
                            "-fx-cursor: hand;";
            String hoverStyle =
                    "-fx-background-color: #EFEBE9;" +
                            "-fx-text-fill: #3E2723;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 25;" +
                            "-fx-padding: 9 22;" +
                            "-fx-cursor: hand;";

            btn.setStyle(isActive ? activeStyle : defaultStyle);

            if (!isActive) {
                btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
                btn.setOnMouseExited(e -> btn.setStyle(defaultStyle));
            }
            return btn;
        }

        private HBox createProfileHeader(User user) {
            HBox header = new HBox(15);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                    "-fx-background-radius: 15; -fx-padding: 15;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

            StackPane profileIcon = createProfileIcon(user);

            VBox profileInfo = new VBox(5);

            Label nameLabel = new Label(user.getUsername());
            nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

            Label emailLabel = new Label(user.getEmail());
            emailLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

            String phone = (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty())
                    ? "📱 " + user.getPhoneNumber()
                    : "📱 No phone number";
            Label phoneLabel = new Label(phone);
            phoneLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

            Button editProfileBtn = new Button("Edit Profile");
            editProfileBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 11px; -fx-padding: 5 12; -fx-cursor: hand;");

            editProfileBtn.setOnMouseEntered(e -> editProfileBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 11px; -fx-padding: 5 12; -fx-cursor: hand;"));
            editProfileBtn.setOnMouseExited(e -> editProfileBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 11px; -fx-padding: 5 12; -fx-cursor: hand;"));

            editProfileBtn.setOnAction(e -> SceneManager.setScene(new EditProfilePage().getScene()));

            profileInfo.getChildren().addAll(nameLabel, emailLabel, phoneLabel, editProfileBtn);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            header.getChildren().addAll(profileIcon, profileInfo, spacer);
            return header;
        }

        private StackPane createProfileIcon(User user) {
            StackPane iconContainer = new StackPane();
            iconContainer.setStyle("-fx-cursor: hand;");

            String photoPath = user.getProfilePhotoPath();
            if (photoPath != null && !photoPath.isEmpty()) {
                try {
                    Image img = new Image("file:" + photoPath);

                    if (!img.isError()) {
                        WritableImage squareImg = cropToSquare(img);

                        Circle photoCircle = new Circle(40);
                        photoCircle.setFill(new ImagePattern(squareImg));
                        photoCircle.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.1)));

                        iconContainer.getChildren().add(photoCircle);
                    } else {
                        javafx.scene.Node[] nodes = createDefaultIcon();
                        iconContainer.getChildren().addAll(nodes[0], nodes[1]);
                    }
                } catch (Exception ex) {
                    javafx.scene.Node[] nodes = createDefaultIcon();
                    iconContainer.getChildren().addAll(nodes[0], nodes[1]);
                }
            } else {
                javafx.scene.Node[] nodes = createDefaultIcon();
                iconContainer.getChildren().addAll(nodes[0], nodes[1]);
            }

            iconContainer.setOnMouseClicked(e -> SceneManager.setScene(new ProfilePage().getScene()));
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

        private WritableImage cropToSquare(Image img) {
            int imgW = (int) img.getWidth();
            int imgH = (int) img.getHeight();
            int size = Math.min(imgW, imgH);

            int offsetX = (imgW - size) / 2;
            int offsetY = (imgH - size) / 2;

            PixelReader reader = img.getPixelReader();
            return new WritableImage(reader, offsetX, offsetY, size, size);
        }

        private javafx.scene.Node[] createDefaultIcon() {
            Circle circle = new Circle(40);
            circle.setStyle("-fx-fill: " + Styles.BROWN_DARK + ";");
            circle.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.1)));

            Label iconLabel = new Label("👤");
            iconLabel.setStyle("-fx-font-size: 45px;");

            return new javafx.scene.Node[]{circle, iconLabel};
        }

        private VBox createOrderMenuSection(User user) {
            VBox section = new VBox(10);
            section.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                    "-fx-background-radius: 15; -fx-padding: 15;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);");

            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER_LEFT);

            Label titleLabel = new Label(" My Order");
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label historyLabel = new Label("View All →");
            historyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + "; -fx-cursor: hand;");
            historyLabel.setOnMouseClicked(e -> SceneManager.setScene(new MyOrderPage().getScene()));
            historyLabel.setOnMouseEntered(e -> historyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.BROWN_DARK + "; -fx-cursor: hand;"));
            historyLabel.setOnMouseExited(e -> historyLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.GOLD + "; -fx-cursor: hand;"));

            header.getChildren().addAll(titleLabel, spacer, historyLabel);

            HBox orderMenu = new HBox(0);
            orderMenu.setAlignment(Pos.CENTER);
            orderMenu.setPadding(new Insets(10, 0, 5, 0));

            // ✅ Fetch dari API dulu
            Map<String, Long> counts = fetchOrderCounts(user);
            long pendingCount = counts.get("Pending");
            long processingCount = counts.get("Processing");
            long shippedCount = counts.get("Shipped");
            long deliveredCount = counts.get("Delivered");

            VBox pending = createOrderMenuItem("⏳", "Pending", String.valueOf(pendingCount));
            pending.setOnMouseClicked(e -> SceneManager.setScene(new MyOrderPage().getSceneWithFilter("Pending")));

            VBox processing = createOrderMenuItem("📦", "Processing", String.valueOf(processingCount));
            processing.setOnMouseClicked(e -> SceneManager.setScene(new MyOrderPage().getSceneWithFilter("Processing")));

            VBox shipped = createOrderMenuItem("🚚", "Shipped", String.valueOf(shippedCount));
            shipped.setOnMouseClicked(e -> SceneManager.setScene(new MyOrderPage().getSceneWithFilter("Shipped")));

            VBox delivered = createOrderMenuItem("✅", "Delivered", String.valueOf(deliveredCount));
            delivered.setOnMouseClicked(e -> SceneManager.setScene(new MyOrderPage().getSceneWithFilter("Delivered")));

            HBox.setHgrow(pending, Priority.ALWAYS);
            HBox.setHgrow(processing, Priority.ALWAYS);
            HBox.setHgrow(shipped, Priority.ALWAYS);
            HBox.setHgrow(delivered, Priority.ALWAYS);

            orderMenu.getChildren().addAll(pending, processing, shipped, delivered);
            section.getChildren().addAll(header, orderMenu);
            return section;
        }

        private void showLoginRequiredAlert() {
            LoginRequiredDialog.show("You need to login to access your profile.");
        }

        private void showLoginAlert() {
            LoginRequiredDialog.show("You need to login to access this feature.");
        }

        private VBox createOrderMenuItem(String icon, String text, String count) {
            VBox item = new VBox(5);
            item.setAlignment(Pos.CENTER);
            item.setPadding(new Insets(10, 5, 10, 5));
            item.setStyle("-fx-cursor: hand; -fx-background-radius: 10;");

            Label iconLabel = new Label(icon);
            iconLabel.setStyle("-fx-font-size: 28px;");

            Label textLabel = new Label(text);
            textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_DARK + ";");

            Label countLabel = new Label(count);
            countLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.GOLD + ";");

            item.getChildren().addAll(iconLabel, textLabel, countLabel);

            item.setOnMouseEntered(e -> {
                item.setStyle("-fx-cursor: hand; -fx-background-radius: 10; -fx-background-color: " + Styles.BROWN_PALE + ";");
                ScaleTransition st = new ScaleTransition(Duration.millis(150), item);
                st.setToX(1.05);
                st.setToY(1.05);
                st.play();
            });
            item.setOnMouseExited(e -> {
                item.setStyle("-fx-cursor: hand; -fx-background-radius: 10;");
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

            section.setMaxWidth(Double.MAX_VALUE);

            Label titleLabel = new Label(" Other Menu");
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

            VBox menuList = new VBox(5);
            menuList.setMaxWidth(Double.MAX_VALUE);

            HBox alamatItem = createMenuItem("📍", "My Address");
            alamatItem.setOnMouseClicked(e -> SceneManager.setScene(new ShippingAddressPage().getScene()));

            HBox paymentItem = createMenuItem("💳", "Payment Methods");
            paymentItem.setOnMouseClicked(e -> SceneManager.setScene(new PaymentMethodPage().getScene()));

            HBox helpItem = createMenuItem("📞", "Help Center");
            helpItem.setOnMouseClicked(e -> SceneManager.setScene(new HelpCenterPage().getScene()));

            HBox aboutItem = createMenuItem("\u24d8", "About Vendoza");
            aboutItem.setOnMouseClicked(e -> SceneManager.setScene(new AboutPage().getScene()));

            HBox.setHgrow(alamatItem, Priority.ALWAYS);
            HBox.setHgrow(paymentItem, Priority.ALWAYS);
            HBox.setHgrow(helpItem, Priority.ALWAYS);
            HBox.setHgrow(aboutItem, Priority.ALWAYS);

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

            logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: " + Styles.ERROR_RED + "; -fx-text-fill: white;" +
                    "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                    "-fx-padding: 12 0; -fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold;"));
            logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.ERROR_RED + ";" +
                    "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                    "-fx-padding: 12 0; -fx-cursor: hand; -fx-font-size: 14px; -fx-font-weight: bold;"));

            logoutBtn.setOnAction(e -> {
                AuthService.logout();
                SceneManager.showHomePage();
            });

            return logoutBtn;
        }

        private Map<String, Long> fetchOrderCounts(User user) {
            Map<String, Long> counts = new HashMap<>();
            counts.put("Pending", 0L);
            counts.put("Processing", 0L);
            counts.put("Shipped", 0L);
            counts.put("Delivered", 0L);

            if (user == null) return counts;

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:9191/api/orders/" + user.getId()))
                        .GET()
                        .build();

                HttpResponse<String> response = HttpClient.newHttpClient()
                        .send(request, HttpResponse.BodyHandlers.ofString());

                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
                for (com.google.gson.JsonElement el : jsonArray) {
                    JsonObject obj = el.getAsJsonObject();
                    String status = obj.get("status").getAsString();
                    counts.put(status, counts.getOrDefault(status, 0L) + 1);
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to fetch order counts: " + e.getMessage());
            }
            return counts;
        }
    }
