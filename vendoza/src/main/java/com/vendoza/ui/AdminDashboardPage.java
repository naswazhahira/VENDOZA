package com.vendoza.ui;

import com.vendoza.model.Product;
import com.vendoza.service.AuthService;
import com.vendoza.service.DataService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.util.List;

public class AdminDashboardPage {

    public Scene getScene() {
        if (!AuthService.isLoggedIn() || !AuthService.isAdmin()) {
            CustomDialog.showError("Access Denied",
                    !AuthService.isLoggedIn() ? "Please login first!" : "Admin access required!");
            return AuthService.isLoggedIn() ? new HomePage().getScene() : new LoginPage().getScene();
        }

        double screenWidth  = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        HBox navBar = createNavBar();

        VBox mainContent = new VBox(28);
        mainContent.setPadding(new Insets(35, 55, 60, 55));
        mainContent.setStyle("-fx-background-color: #ebddc3;");

        mainContent.getChildren().addAll(
                createWelcomeSection(),
                createStatsRow(),
                createSectionTitle("Product Management"),
                createProductTable(),
                createSectionTitle("Order Management"),
                createOrderTable(),
                createSectionTitle("Registered Users"),
                createUserTable()
        );

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #ebddc3; -fx-background: #ebddc3;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scrollPane);
        root.setStyle("-fx-background-color: #ebddc3;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return new Scene(root, screenWidth, screenHeight);
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(0);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(16, 50, 16, 50));
        navBar.setStyle(
                "-fx-background-color: #2C1810;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 3);"
        );

        VBox logoBox = new VBox(1);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        Label logo = new Label("VENDOZA");
        logo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #D4A853; -fx-font-family: 'Georgia';");
        Label adminTag = new Label("ADMIN PANEL");
        adminTag.setStyle("-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: rgba(212,168,83,0.6); -fx-letter-spacing: 2px;");
        logoBox.getChildren().addAll(logo, adminTag);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navButtons = new HBox(4);
        navButtons.setAlignment(Pos.CENTER_RIGHT);

        Button homeBtn      = createAdminNavButton("🏠  Home",      false);
        Button dashboardBtn = createAdminNavButton("📊  Dashboard", true);
        Button logoutBtn    = createAdminNavButton("🚪  Logout",    false);

        homeBtn.setOnAction(e -> SceneManager.setScene(new AdminHomePage().getScene()));
        dashboardBtn.setOnAction(e -> SceneManager.setScene(new AdminDashboardPage().getScene()));
        logoutBtn.setOnAction(e -> { AuthService.logout(); SceneManager.showHomePage(); });

        navButtons.getChildren().addAll(homeBtn, dashboardBtn, logoutBtn);
        navBar.getChildren().addAll(logoBox, spacer, navButtons);
        return navBar;
    }

    private Button createAdminNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        String active  = "-fx-background-color: #D4A853; -fx-text-fill: #2C1810; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;";
        String def     = "-fx-background-color: transparent; -fx-text-fill: rgba(255,255,255,0.75); -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;";
        String hover   = "-fx-background-color: rgba(212,168,83,0.15); -fx-text-fill: #D4A853; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 9 22; -fx-cursor: hand;";
        btn.setStyle(isActive ? active : def);
        if (!isActive) { btn.setOnMouseEntered(e -> btn.setStyle(hover)); btn.setOnMouseExited(e -> btn.setStyle(def)); }
        return btn;
    }

    private VBox createWelcomeSection() {
        VBox box = new VBox(0);
        Label welcome = new Label("Welcome, " + AuthService.getCurrentUser().getUsername());
        welcome.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2C1810; -fx-font-family: 'Georgia';");
        box.getChildren().add(welcome);
        return box;
    }

    private HBox createStatsRow() {
        HBox row = new HBox(16);
        int productCount = DataService.getAllProducts().size();
        int orderCount   = DataService.getAllOrders().size();
        int userCount    = DataService.getAllUsers().size();
        double revenue   = DataService.getAllOrders().stream().mapToDouble(com.vendoza.model.Order::getTotalAmount).sum();
        row.getChildren().addAll(
                statCard("📦", "Total Products", String.valueOf(productCount), "#3E2723"),
                statCard("📋", "Total Orders",   String.valueOf(orderCount),   "#5D4037"),
                statCard("👥", "Total Users",    String.valueOf(userCount),    "#6D4C41"),
                statCard("💰", "Revenue",        "Rp" + Styles.formatPrice(revenue), "#C62828")
        );
        return row;
    }

    private VBox statCard(String icon, String title, String value, String color) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 22 20;");
        card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.07)));
        HBox.setHgrow(card, Priority.ALWAYS);
        Label ic  = new Label(icon);  ic.setStyle("-fx-font-size: 30px;");
        Label ttl = new Label(title); ttl.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63;");
        Label val = new Label(value); val.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        card.getChildren().addAll(ic, ttl, val);
        return card;
    }

    private VBox createSectionTitle(String text) {
        VBox box = new VBox(8);
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #3E2723; -fx-font-family: 'Georgia';");
        Region sep = new Region();
        sep.setPrefHeight(3);
        sep.setStyle("-fx-background-color: linear-gradient(to right, #D4A853, transparent); -fx-background-radius: 3;");
        box.getChildren().addAll(lbl, sep);
        return box;
    }

    private VBox createProductTable() {
        VBox container = new VBox(10);

        Button addBtn = new Button("＋  Add New Product");
        addBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 10 24; -fx-cursor: hand;");
        addBtn.setOnMouseEntered(e -> addBtn.setStyle("-fx-background-color: #D4A853; -fx-text-fill: #2C1810; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 10 24; -fx-cursor: hand;"));
        addBtn.setOnMouseExited(e -> addBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 10 24; -fx-cursor: hand;"));
        addBtn.setOnAction(e -> showProductForm(null));

        HBox topRow = new HBox(addBtn);
        topRow.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(topRow, buildProductTableBox());
        return container;
    }

    private VBox buildProductTableBox() {
        VBox tableBox = new VBox(0);
        tableBox.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);");

        HBox header = new HBox();
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setStyle("-fx-background-color: #2C1810; -fx-background-radius: 18 18 0 0;");
        header.setAlignment(Pos.CENTER_LEFT);

        String[] cols   = {"Image", "Product Name", "Category", "Price", "Stock", "On Sale", "Actions"};
        double[] widths = {70,      0,              150,        130,     70,      100,       150}; // 0 = kolom dinamis

        for (int i = 0; i < cols.length; i++) {
            Label lbl = new Label(cols[i]);
            lbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #D4A853;");

            if (widths[i] == 0) {
                HBox.setHgrow(lbl, Priority.ALWAYS);
                lbl.setMaxWidth(Double.MAX_VALUE);
                lbl.setAlignment(Pos.CENTER_LEFT);
            } else {
                lbl.setPrefWidth(widths[i]);
                lbl.setMinWidth(widths[i]);
                lbl.setMaxWidth(widths[i]);

                if (cols[i].equals("Stock") || cols[i].equals("On Sale")) {
                    lbl.setAlignment(Pos.CENTER);
                } else {
                    lbl.setAlignment(Pos.CENTER_LEFT);
                }
            }
            header.getChildren().add(lbl);
        }
        tableBox.getChildren().add(header);

        List<Product> products = DataService.getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            tableBox.getChildren().add(buildProductRow(products.get(i), i % 2 == 0));
        }
        return tableBox;
    }

    private HBox buildProductRow(Product p, boolean even) {
        HBox row = new HBox();
        row.setPadding(new Insets(10, 24, 10, 24));
        row.setStyle("-fx-background-color: " + (even ? "white" : "#FAF6F1") + ";");
        row.setAlignment(Pos.CENTER_LEFT);

        StackPane imgPane = new StackPane();
        imgPane.setPrefSize(50, 50);
        imgPane.setMinSize(50, 50);
        imgPane.setStyle("-fx-background-color: #F5F0EA; -fx-background-radius: 8;");
        Rectangle clip = new Rectangle(50, 50);
        clip.setArcWidth(16); clip.setArcHeight(16);
        imgPane.setClip(clip);

        try {
            String path = p.getImageUrl();
            InputStream is = getClass().getResourceAsStream(path.startsWith("/") ? path : "/" + path);
            if (is != null) {
                ImageView iv = new ImageView(new Image(is));
                iv.setFitWidth(50); iv.setFitHeight(50);
                iv.setPreserveRatio(true); iv.setSmooth(true);
                imgPane.getChildren().add(iv);
            } else {
                Label fb = new Label("🛍"); fb.setStyle("-fx-font-size: 20px;");
                imgPane.getChildren().add(fb);
            }
        } catch (Exception ex) {
            Label fb = new Label("🛍"); fb.setStyle("-fx-font-size: 20px;");
            imgPane.getChildren().add(fb);
        }

        HBox imgBox = new HBox(imgPane);
        imgBox.setPrefWidth(70);
        imgBox.setMinWidth(70);
        imgBox.setMaxWidth(70);
        imgBox.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(p.getName());
        name.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723;");
        name.setWrapText(false);
        name.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(name, Priority.ALWAYS);
        name.setMaxWidth(Double.MAX_VALUE);

        String categoryName = p.getCategory().replace("'s Fashion", "");  // ← HAPUS "Fashion"
        Label cat = new Label(categoryName);
        cat.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723;");
        cat.setPrefWidth(150);
        cat.setMinWidth(150);
        cat.setMaxWidth(150);
        cat.setAlignment(Pos.CENTER_LEFT);

        Label price = new Label("Rp" + Styles.formatPrice(p.getCurrentPrice()));
        price.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723;");
        price.setPrefWidth(130);
        price.setMinWidth(130);
        price.setMaxWidth(130);
        price.setAlignment(Pos.CENTER_LEFT);

        Label stock = new Label(String.valueOf(p.getStock()));
        stock.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723;");
        stock.setPrefWidth(70);
        stock.setMinWidth(70);
        stock.setMaxWidth(70);
        stock.setAlignment(Pos.CENTER);

        Label sale = new Label(p.isOnSale() ? "✔" : "—");
        sale.setPrefWidth(100);
        sale.setMinWidth(100);
        sale.setMaxWidth(100);
        sale.setAlignment(Pos.CENTER);
        sale.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + (p.isOnSale() ? "#388E3C" : "#BDBDBD") + ";");

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setPrefWidth(150);
        actions.setMinWidth(150);
        actions.setMaxWidth(150);

        Button editBtn = tableBtn("Edit", "#5D4037", "#3E2723");
        Button delBtn  = tableBtn("Delete", "#E53935", "#C62828");
        editBtn.setOnAction(e -> showProductForm(p));
        delBtn.setOnAction(e -> showDeleteConfirm(p));

        actions.getChildren().addAll(editBtn, delBtn);

        row.getChildren().addAll(imgBox, name, cat, price, stock, sale, actions);
        return row;
    }

    private void showDeleteConfirm(Product p) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-border-color: #C4A484; -fx-border-width: 1.5; -fx-border-radius: 20; -fx-padding: 36 44;");
        root.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.18)));

        Label icon  = new Label("🗑");  icon.setStyle("-fx-font-size: 44px;");
        Label title = new Label("Delete Product?");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C1810; -fx-font-family: 'Georgia';");
        Label msg = new Label("\"" + p.getName() + "\"\nwill be permanently removed.");
        msg.setStyle("-fx-font-size: 13px; -fx-text-fill: #5D4037; -fx-text-alignment: center;");
        msg.setAlignment(Pos.CENTER);

        HBox btnRow = new HBox(12);
        btnRow.setAlignment(Pos.CENTER);

        Button cancelBtn = dialogActionBtn("Cancel", "#EFEBE9", "#3E2723", "#DDD0C5", "#2C1810");
        cancelBtn.setOnAction(e -> stage.close());

        Button deleteBtn = dialogActionBtn("Yes, Delete", "#E53935", "white", "#C62828", "white");
        deleteBtn.setOnAction(e -> {
            stage.close();
            DataService.deleteProduct(p.getId());
            SceneManager.setScene(new AdminDashboardPage().getScene());
        });

        btnRow.getChildren().addAll(cancelBtn, deleteBtn);
        root.getChildren().addAll(icon, title, msg, btnRow);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.sizeToScene(); stage.centerOnScreen(); stage.showAndWait();
    }

    private void showProductForm(Product existing) {
        boolean isEdit = existing != null;

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

        // Title bar
        HBox titleBar = new HBox();
        titleBar.setStyle("-fx-background-color: #2C1810; -fx-padding: 16 24; -fx-background-radius: 20 20 0 0;");
        titleBar.setAlignment(Pos.CENTER_LEFT);
        Label titleLbl = new Label(isEdit ? "Edit Product" : "Add New Product");
        titleLbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #D4A853; -fx-font-family: 'Georgia';");
        Region titleSpacer = new Region(); HBox.setHgrow(titleSpacer, Priority.ALWAYS);
        Button closeX = new Button("✕");
        closeX.setStyle("-fx-background-color: transparent; -fx-text-fill: #D4A853; -fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 0;");
        closeX.setOnMouseEntered(e -> closeX.setStyle("-fx-background-color: transparent; -fx-text-fill: #E53935; -fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 0;"));
        closeX.setOnMouseExited(e -> closeX.setStyle("-fx-background-color: transparent; -fx-text-fill: #D4A853; -fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 0;"));
        closeX.setOnAction(e -> stage.close());
        titleBar.getChildren().addAll(titleLbl, titleSpacer, closeX);

        GridPane form = new GridPane();
        form.setHgap(16); form.setVgap(14);
        form.setPadding(new Insets(24, 28, 10, 28));

        TextField nameF     = dialogField(isEdit ? existing.getName() : "");
        TextField priceF    = dialogField(isEdit ? String.valueOf((int) existing.getPrice()) : "");
        TextField discountF = dialogField(isEdit ? String.valueOf((int) existing.getDiscountPrice()) : "0");
        TextField stockF    = dialogField(isEdit ? String.valueOf(existing.getStock()) : "");

        TextField imageF = dialogField(isEdit ? existing.getImageUrl() : "");
        imageF.setPromptText("Click Browse to select image...");
        imageF.setEditable(false);
        imageF.setPrefWidth(295);

        Button browseBtn = new Button("Browse");
        browseBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 9 18; -fx-cursor: hand;");
        browseBtn.setOnMouseEntered(e -> browseBtn.setStyle("-fx-background-color: #D4A853; -fx-text-fill: #2C1810; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 9 18; -fx-cursor: hand;"));
        browseBtn.setOnMouseExited(e -> browseBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 9 18; -fx-cursor: hand;"));
        browseBtn.setOnAction(e -> {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Select Product Image");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            java.io.File file = fc.showOpenDialog(stage);
            if (file != null) {
                try {
                    java.nio.file.Path dest = java.nio.file.Paths.get("src/main/resources/images/" + file.getName());
                    java.nio.file.Files.copy(file.toPath(), dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    imageF.setText("/images/" + file.getName());
                } catch (Exception ex) { imageF.setText(file.toURI().toString()); }
            }
        });

        HBox imageRow = new HBox(8, imageF, browseBtn);
        imageRow.setAlignment(Pos.CENTER_LEFT);

        final String[] selectedCategory = {isEdit ? existing.getCategory() : "Women"};

        Button dropdownToggle = new Button(selectedCategory[0] + "  ▾");
        dropdownToggle.setPrefWidth(400);
        dropdownToggle.setStyle(
                "-fx-background-color: white; -fx-text-fill: #3E2723;" +
                        "-fx-font-size: 13px; -fx-font-weight: bold;" +
                        "-fx-background-radius: 10; -fx-border-color: #DDD0C5;" +
                        "-fx-border-radius: 10; -fx-border-width: 1.5;" +
                        "-fx-padding: 9 14; -fx-cursor: hand; -fx-alignment: CENTER_LIFT;"
        );

        javafx.stage.Popup popup = new javafx.stage.Popup();
        popup.setAutoHide(true);

        VBox popupList = new VBox(0);
        popupList.setStyle(
                "-fx-background-color: white; -fx-border-color: #DDD0C5;" +
                        "-fx-border-radius: 10; -fx-border-width: 1.5; -fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 4);"
        );
        popupList.setPrefWidth(400);

        String[] catOptions = {"Women", "Men", "Accessories", "Footwear", "Jewelry"};
        for (String opt : catOptions) {
            Button optBtn = new Button(opt);
            optBtn.setPrefWidth(400);
            String optDef   = "-fx-background-color: white; -fx-text-fill: #3E2723; -fx-font-size: 13px; -fx-background-radius: 0; -fx-padding: 10 16; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";
            String optHover = "-fx-background-color: #F5EFE8; -fx-text-fill: #2C1810; -fx-font-size: 13px; -fx-background-radius: 0; -fx-padding: 10 16; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";
            optBtn.setStyle(optDef);
            optBtn.setOnMouseEntered(e -> optBtn.setStyle(optHover));
            optBtn.setOnMouseExited(e -> optBtn.setStyle(optDef));
            optBtn.setOnAction(e -> {
                selectedCategory[0] = opt;
                dropdownToggle.setText(opt + "  ▾");
                popup.hide();
            });
            popupList.getChildren().add(optBtn);
        }
        popup.getContent().add(popupList);

        dropdownToggle.setOnAction(e -> {
            if (popup.isShowing()) {
                popup.hide();
                dropdownToggle.setText(selectedCategory[0] + "  ▾");
            } else {
                javafx.geometry.Bounds b = dropdownToggle.localToScreen(dropdownToggle.getBoundsInLocal());
                popup.show(dropdownToggle, b.getMaxX() - popupList.getPrefWidth(), b.getMaxY() + 2);
                dropdownToggle.setText(selectedCategory[0] + "  ▴");
            }
        });
        popup.setOnHidden(e -> dropdownToggle.setText(selectedCategory[0] + "  ▾"));

        TextArea descF = new TextArea(isEdit ? existing.getDescription() : "");
        descF.setPrefRowCount(3); descF.setPrefWidth(400);
        descF.setStyle("-fx-background-radius: 10; -fx-border-color: #DDD0C5; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-font-size: 13px; -fx-control-inner-background: white; -fx-padding: 8;");

        CheckBox saleChk = new CheckBox("Mark as On Sale");
        saleChk.setSelected(isEdit && existing.isOnSale());
        saleChk.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723;");

        form.addRow(0, dialogLabel("Product Name"), nameF);
        form.addRow(1, dialogLabel("Category"),     dropdownToggle);
        form.addRow(2, dialogLabel("Price (Rp)"),   priceF);
        form.addRow(3, dialogLabel("Disc. Price"),  discountF);
        form.addRow(4, dialogLabel("Stock"),        stockF);
        form.addRow(5, dialogLabel("Image"),        imageRow);
        form.addRow(6, dialogLabel("Description"),  descF);
        form.addRow(7, new Label(),                 saleChk);

        Button saveBtn = new Button(isEdit ? "Save Changes" : "Add Product");
        saveBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 11 32; -fx-cursor: hand;");
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #D4A853; -fx-text-fill: #2C1810; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 11 32; -fx-cursor: hand;"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: #3E2723; -fx-text-fill: #D4A853; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 11 32; -fx-cursor: hand;"));

        saveBtn.setOnAction(e -> {
            try {
                String  name     = nameF.getText().trim();
                String  category = selectedCategory[0];
                double  price    = Double.parseDouble(priceF.getText().trim());
                double  discount = Double.parseDouble(discountF.getText().trim());
                int     stock    = Integer.parseInt(stockF.getText().trim());
                String  image    = imageF.getText().trim();
                String  desc     = descF.getText().trim();
                boolean onSale   = saleChk.isSelected();

                if (name.isEmpty()) { CustomDialog.showError("Validation", "Product name cannot be empty."); return; }

                if (isEdit) {
                    DataService.updateProduct(existing.getId().intValue(), name, category, price, discount, image, desc, onSale, stock);
                } else {
                    DataService.addProduct(name, category, price, discount, image, desc, onSale, stock, "VENDOZA", "Premium");
                }
                stage.close();
                SceneManager.setScene(new AdminDashboardPage().getScene());
            } catch (NumberFormatException ex) {
                CustomDialog.showError("Validation", "Price, Discount, and Stock must be valid numbers.");
            }
        });

        HBox btnRow = new HBox(saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        btnRow.setPadding(new Insets(8, 28, 24, 28));

        VBox wrapper = new VBox(0);
        wrapper.setStyle("-fx-background-color: #FAF6F1; -fx-background-radius: 20; -fx-border-color: #C4A484; -fx-border-width: 1.5; -fx-border-radius: 20;");
        wrapper.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.18)));
        wrapper.getChildren().addAll(titleBar, form, btnRow);

        Scene scene = new Scene(wrapper);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.sizeToScene(); stage.centerOnScreen(); stage.showAndWait();
    }

    private VBox createOrderTable() {
        VBox container = new VBox(0);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);");

        HBox header = new HBox();
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setStyle("-fx-background-color: #2C1810; -fx-background-radius: 18 18 0 0;");
        header.setAlignment(Pos.CENTER_LEFT);

        String[] cols   = {"Order ID", "Customer", "Total", "Status", "Update Status"};
        double[] widths = {100, 0, 160, 130, 0};
        for (int i = 0; i < cols.length; i++) {
            Label lbl = new Label(cols[i]);
            lbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #D4A853;");
            if (widths[i] > 0) { lbl.setPrefWidth(widths[i]); }
            else { HBox.setHgrow(lbl, Priority.ALWAYS); lbl.setMaxWidth(Double.MAX_VALUE); }
            header.getChildren().add(lbl);
        }
        container.getChildren().add(header);

        List<com.vendoza.model.Order> orders = DataService.getAllOrders();
        if (orders.isEmpty()) {
            VBox emptyBox = new VBox(10);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            Label emptyIcon = new Label("📋"); emptyIcon.setStyle("-fx-font-size: 36px;");
            Label emptyLbl  = new Label("No orders yet."); emptyLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #8D6E63;");
            emptyBox.getChildren().addAll(emptyIcon, emptyLbl);
            container.getChildren().add(emptyBox);
            return container;
        }

        for (int i = 0; i < orders.size(); i++) {
            com.vendoza.model.Order o = orders.get(i);
            HBox row = new HBox();
            row.setPadding(new Insets(14, 24, 14, 24));
            row.setStyle("-fx-background-color: " + (i % 2 == 0 ? "white" : "#FAF6F1") + ";");
            row.setAlignment(Pos.CENTER_LEFT);

            Label idLbl    = tableLabel("#" + o.getId(), 100);
            Label custLbl  = tableLabel(o.getCustomerName(), 0);
            Label totalLbl = tableLabel("Rp" + Styles.formatPrice(o.getTotalAmount()), 160);

            Label statusLbl = new Label(o.getStatus());
            statusLbl.setPrefWidth(130);
            statusLbl.setStyle(statusBadgeStyle(o.getStatus()));

            HBox statusBtns = new HBox(6);
            statusBtns.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(statusBtns, Priority.ALWAYS);

            String[] statuses = {"Processing", "Shipped", "Delivered", "Cancelled"};
            for (String s : statuses) {
                Button sb = new Button(s);
                sb.setStyle(s.equals(o.getStatus()) ? activeStatusBtnStyle(s) : inactiveStatusBtnStyle());
                sb.setOnMouseEntered(e -> { if (!sb.getText().equals(statusLbl.getText())) sb.setStyle(hoverStatusBtnStyle()); });
                sb.setOnMouseExited(e -> { boolean cur = sb.getText().equals(statusLbl.getText()); sb.setStyle(cur ? activeStatusBtnStyle(sb.getText()) : inactiveStatusBtnStyle()); });
                sb.setOnAction(e -> {
                    String ns = sb.getText();
                    DataService.updateOrderStatus(o.getId(), ns);
                    statusLbl.setText(ns);
                    statusLbl.setStyle(statusBadgeStyle(ns));
                    for (javafx.scene.Node node : statusBtns.getChildren()) {
                        if (node instanceof Button) { Button b = (Button) node; b.setStyle(b.getText().equals(ns) ? activeStatusBtnStyle(ns) : inactiveStatusBtnStyle()); }
                    }
                });
                statusBtns.getChildren().add(sb);
            }

            row.getChildren().addAll(idLbl, custLbl, totalLbl, statusLbl, statusBtns);
            container.getChildren().add(row);
        }
        return container;
    }

    private String statusBadgeStyle(String s) { return "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + statusColor(s) + ";"; }

    private String activeStatusBtnStyle(String s) {
        String bg;
        switch (s) { case "Delivered": bg="#388E3C"; break; case "Shipped": bg="#1976D2"; break; case "Processing": bg="#F57C00"; break; case "Cancelled": bg="#E53935"; break; default: bg="#8D6E63"; }
        return "-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 4 10; -fx-cursor: hand;";
    }
    private String inactiveStatusBtnStyle() { return "-fx-background-color: #EFEBE9; -fx-text-fill: #8D6E63; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 4 10; -fx-cursor: hand;"; }
    private String hoverStatusBtnStyle()    { return "-fx-background-color: #DDD0C5; -fx-text-fill: #3E2723; -fx-font-size: 10px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 4 10; -fx-cursor: hand;"; }
    private String statusColor(String s) {
        if (s == null) return "#8D6E63";
        switch (s) { case "Delivered": return "#388E3C"; case "Shipped": return "#1976D2"; case "Processing": return "#F57C00"; case "Cancelled": return "#E53935"; default: return "#8D6E63"; }
    }

    private VBox createUserTable() {
        VBox container = new VBox(0);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);");

        HBox header = new HBox();
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setStyle("-fx-background-color: #2C1810; -fx-background-radius: 18 18 0 0;");
        header.setAlignment(Pos.CENTER_LEFT);

        String[] cols   = {"", "Username", "Email", "Role"};
        double[] widths = {44, 200, 0, 130};
        for (int i = 0; i < cols.length; i++) {
            Label lbl = new Label(cols[i]);
            lbl.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #D4A853;");
            if (widths[i] > 0) { lbl.setPrefWidth(widths[i]); }
            else { HBox.setHgrow(lbl, Priority.ALWAYS); lbl.setMaxWidth(Double.MAX_VALUE); }
            header.getChildren().add(lbl);
        }
        container.getChildren().add(header);

        List<com.vendoza.model.User> users = DataService.getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            com.vendoza.model.User u = users.get(i);
            HBox row = new HBox();
            row.setPadding(new Insets(12, 24, 12, 24));
            row.setStyle("-fx-background-color: " + (i % 2 == 0 ? "white" : "#FAF6F1") + ";");
            row.setAlignment(Pos.CENTER_LEFT);

            Label avatar = new Label(String.valueOf(u.getUsername().charAt(0)).toUpperCase());
            avatar.setStyle("-fx-background-color: #EFEBE9; -fx-text-fill: #3E2723; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 50; -fx-min-width: 34; -fx-min-height: 34; -fx-alignment: center;");
            HBox avatarBox = new HBox(avatar);
            avatarBox.setPrefWidth(44); avatarBox.setMinWidth(44);
            avatarBox.setAlignment(Pos.CENTER_LEFT);

            Label userLbl  = tableLabel(u.getUsername(), 200);
            Label emailLbl = tableLabel(u.getEmail(), 0);

            boolean isAdm = u.getRole().equalsIgnoreCase("ADMIN");
            Label roleLbl = new Label(u.getRole());
            roleLbl.setPrefWidth(130);
            roleLbl.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: " + (isAdm ? "#2C1810" : "#5D4037") + "; -fx-background-color: " + (isAdm ? "#D4A853" : "#EFEBE9") + "; -fx-background-radius: 20; -fx-padding: 4 12;");

            row.getChildren().addAll(avatarBox, userLbl, emailLbl, roleLbl);
            container.getChildren().add(row);
        }
        return container;
    }

    private Label tableLabel(String text, double width) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723;");
        lbl.setWrapText(false);
        lbl.setAlignment(Pos.CENTER);
        if (width > 0) { lbl.setPrefWidth(width); }
        else { HBox.setHgrow(lbl, Priority.ALWAYS); lbl.setMaxWidth(Double.MAX_VALUE); }
        return lbl;
    }

    private Button tableBtn(String text, String bg, String hoverBg) {
        Button btn = new Button(text);
        String base  = "-fx-background-color: " + bg      + "; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 14; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + hoverBg + "; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 6 14; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return btn;
    }

    private Button dialogActionBtn(String text, String bg, String fg, String hoverBg, String hoverFg) {
        Button btn = new Button(text);
        String base  = "-fx-background-color: " + bg      + "; -fx-text-fill: " + fg      + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 30; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + hoverBg + "; -fx-text-fill: " + hoverFg + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 30; -fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return btn;
    }

    private TextField dialogField(String val) {
        TextField tf = new TextField(val);
        tf.setPrefWidth(400);
        tf.setStyle("-fx-background-radius: 10; -fx-border-color: #DDD0C5; -fx-border-radius: 10; -fx-border-width: 1.5; -fx-padding: 9; -fx-font-size: 13px; -fx-background-color: white;");
        return tf;
    }

    private Label dialogLabel(String text) {
        Label lbl = new Label(text);
        lbl.setMinWidth(110);
        lbl.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #5D4037;");
        return lbl;
    }
}
