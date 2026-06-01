package com.vendoza.ui;

import com.vendoza.model.CartItem;
import com.vendoza.model.Order;
import com.vendoza.model.User;
import com.vendoza.service.AuthService;
import com.vendoza.service.CartService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CheckoutPage {

    private static final Map<String, List<String>> PROVINCE_CITIES = new LinkedHashMap<>();
    private static final Map<String, String>       PROVINCE_PROMO  = new LinkedHashMap<>();

    static {
        PROVINCE_CITIES.put("DKI Jakarta",        List.of("Jakarta Pusat","Jakarta Utara","Jakarta Barat","Jakarta Selatan","Jakarta Timur"));
        PROVINCE_CITIES.put("Jawa Barat",          List.of("Bandung","Bekasi","Depok","Bogor","Cimahi","Tasikmalaya","Sukabumi","Cirebon","Karawang"));
        PROVINCE_CITIES.put("Jawa Tengah",         List.of("Semarang","Solo","Magelang","Purwokerto","Pekalongan","Tegal","Salatiga","Kudus"));
        PROVINCE_CITIES.put("Jawa Timur",          List.of("Surabaya","Malang","Sidoarjo","Gresik","Mojokerto","Kediri","Madiun","Jember","Banyuwangi"));
        PROVINCE_CITIES.put("Banten",              List.of("Tangerang","Tangerang Selatan","Serang","Cilegon"));
        PROVINCE_CITIES.put("D.I. Yogyakarta",     List.of("Kota Yogyakarta","Sleman","Bantul","Kulon Progo","Gunung Kidul"));
        PROVINCE_CITIES.put("Sumatera Utara",      List.of("Medan","Binjai","Pematangsiantar","Tebing Tinggi","Sibolga","Padangsidempuan"));
        PROVINCE_CITIES.put("Sumatera Barat",      List.of("Padang","Bukittinggi","Payakumbuh","Solok","Sawahlunto"));
        PROVINCE_CITIES.put("Sumatera Selatan",    List.of("Palembang","Lubuklinggau","Prabumulih","Pagar Alam","Muara Enim"));
        PROVINCE_CITIES.put("Riau",                List.of("Pekanbaru","Dumai","Bengkalis","Siak","Kampar"));
        PROVINCE_CITIES.put("Kepulauan Riau",      List.of("Batam","Tanjungpinang","Bintan","Karimun","Natuna"));
        PROVINCE_CITIES.put("Lampung",             List.of("Bandar Lampung","Metro","Pringsewu","Kotabumi"));
        PROVINCE_CITIES.put("Kalimantan Barat",    List.of("Pontianak","Singkawang","Sambas","Ketapang"));
        PROVINCE_CITIES.put("Kalimantan Tengah",   List.of("Palangka Raya","Sampit","Buntok","Pangkalan Bun"));
        PROVINCE_CITIES.put("Kalimantan Selatan",  List.of("Banjarmasin","Banjarbaru","Martapura","Kotabaru"));
        PROVINCE_CITIES.put("Kalimantan Timur",    List.of("Samarinda","Balikpapan","Bontang","Tenggarong"));
        PROVINCE_CITIES.put("Kalimantan Utara",    List.of("Tarakan","Nunukan","Tanjung Selor","Malinau"));
        PROVINCE_CITIES.put("Sulawesi Selatan",    List.of("Makassar","Parepare","Palopo","Gowa","Maros"));
        PROVINCE_CITIES.put("Sulawesi Tengah",     List.of("Palu","Luwuk","Poso","Tolitoli"));
        PROVINCE_CITIES.put("Sulawesi Utara",      List.of("Manado","Bitung","Tomohon","Kotamobagu"));
        PROVINCE_CITIES.put("Sulawesi Tenggara",   List.of("Kendari","Bau-Bau","Kolaka","Raha"));
        PROVINCE_CITIES.put("Bali",                List.of("Denpasar","Badung","Gianyar","Tabanan","Singaraja","Klungkung"));
        PROVINCE_CITIES.put("Nusa Tenggara Barat", List.of("Mataram","Bima","Praya","Sumbawa Besar"));
        PROVINCE_CITIES.put("Nusa Tenggara Timur", List.of("Kupang","Ende","Maumere","Labuan Bajo"));
        PROVINCE_CITIES.put("Maluku",              List.of("Ambon","Tual","Namlea","Masohi"));
        PROVINCE_CITIES.put("Maluku Utara",        List.of("Ternate","Tidore","Tobelo","Sofifi"));
        PROVINCE_CITIES.put("Papua",               List.of("Jayapura","Timika","Merauke","Nabire","Wamena"));
        PROVINCE_CITIES.put("Papua Barat",         List.of("Manokwari","Sorong","Fakfak","Kaimana"));
        PROVINCE_CITIES.put("Aceh",                List.of("Banda Aceh","Lhokseumawe","Langsa","Sabang","Meulaboh"));
        PROVINCE_CITIES.put("Jambi",               List.of("Jambi","Sungai Penuh","Muara Bulian","Kuala Tungkal"));
        PROVINCE_CITIES.put("Bengkulu",            List.of("Bengkulu","Curup","Manna","Arga Makmur"));
        PROVINCE_CITIES.put("Bangka Belitung",     List.of("Pangkal Pinang","Sungailiat","Koba","Toboali"));
        PROVINCE_CITIES.put("Gorontalo",           List.of("Gorontalo","Limboto","Marisa","Kwandang"));

        PROVINCE_PROMO.put("DKI Jakarta",        "JKTPREMIUM · Same-Day Delivery Available");
        PROVINCE_PROMO.put("Jawa Barat",         "JABAR10 · 10% Logistics Discount");
        PROVINCE_PROMO.put("Jawa Tengah",        "JATENG10 · 10% Logistics Discount");
        PROVINCE_PROMO.put("Jawa Timur",         "JATIM10 · 10% Logistics Discount");
        PROVINCE_PROMO.put("Banten",             "BANTEN10 · 10% Logistics Discount");
        PROVINCE_PROMO.put("D.I. Yogyakarta",    "JOGJA10 · 10% Logistics Discount");
        PROVINCE_PROMO.put("Sumatera Utara",     "SUMUT15 · 15% Off Shipping");
        PROVINCE_PROMO.put("Sumatera Barat",     "SUMBAR15 · 15% Off Shipping");
        PROVINCE_PROMO.put("Sumatera Selatan",   "SUMSEL15 · 15% Off Shipping");
        PROVINCE_PROMO.put("Riau",               "RIAU15 · 15% Off Shipping");
        PROVINCE_PROMO.put("Kepulauan Riau",     "KEPRI15 · 15% Island Shipping Discount");
        PROVINCE_PROMO.put("Lampung",            "LAMPUNG12 · 12% Logistics Discount");
        PROVINCE_PROMO.put("Kalimantan Barat",   "KALBAR20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Kalimantan Tengah",  "KALTENG20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Kalimantan Selatan", "KALSEL20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Kalimantan Timur",   "KALTIM20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Kalimantan Utara",   "KALUT22 · 22% Remote Area Discount");
        PROVINCE_PROMO.put("Sulawesi Selatan",   "SULSEL20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Sulawesi Tengah",    "SULTENG20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Sulawesi Utara",     "SULUT20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Sulawesi Tenggara",  "SULTRA20 · 20% Remote Area Discount");
        PROVINCE_PROMO.put("Bali",               "BALI12 · 12% Bali Zone Discount");
        PROVINCE_PROMO.put("Nusa Tenggara Barat","NTB22 · 22% Island Shipping Discount");
        PROVINCE_PROMO.put("Nusa Tenggara Timur","NTT22 · 22% Island Shipping Discount");
        PROVINCE_PROMO.put("Maluku",             "MLK25 · 25% Extended Reach Discount");
        PROVINCE_PROMO.put("Maluku Utara",       "MALUT25 · 25% Extended Reach Discount");
        PROVINCE_PROMO.put("Papua",              "PAPUA25 · 25% Extended Reach Discount");
        PROVINCE_PROMO.put("Papua Barat",        "PAPBAR25 · 25% Extended Reach Discount");
        PROVINCE_PROMO.put("Aceh",               "ACEH15 · 15% Off Shipping");
        PROVINCE_PROMO.put("Jambi",              "JAMBI15 · 15% Off Shipping");
        PROVINCE_PROMO.put("Bengkulu",           "BENGKULU18 · 18% Off Shipping");
        PROVINCE_PROMO.put("Bangka Belitung",    "BABEL18 · 18% Island Shipping Discount");
        PROVINCE_PROMO.put("Gorontalo",          "GTLO20 · 20% Remote Area Discount");
    }

    private TextField        recipientField;
    private TextField        phoneField;
    private TextField        streetField;
    private ComboBox<String> provinceCombo;
    private ComboBox<String> cityCombo;
    private TextField        postalField;
    private TextField        labelField;
    private TextField        notesField;
    private Label            addressFormError;
    private Label            locationRecommendationLabel;

    private VBox         addressFormBox;
    private VBox         savedAddressesBox;
    private User.Address selectedAddress;

    private ToggleGroup shippingGroup;
    private ToggleGroup paymentGroup;
    private Label       shippingCostLabel;
    private Label       totalLabel;
    private Label       checkoutPointsLabel;

    private Button placeOrderBtn;

    private final List<CartItem> checkoutItems;
    private double currentSubtotal = 0;
    private Scene  currentScene;

    private boolean forceShowSetupAddress = true;

    //  KONSTRUKTOR
    public CheckoutPage() {
        this(CartService.getCartItems());
    }

    public CheckoutPage(List<CartItem> itemsToCheckout) {
        this.checkoutItems = itemsToCheckout;
        for (CartItem item : itemsToCheckout) currentSubtotal += item.getSubtotal();

        User user = AuthService.getCurrentUser();
        if (user != null && !user.getAddresses().isEmpty()) {
            this.forceShowSetupAddress = false;
        }
    }

    //  ENTRY POINT
    public Scene getScene() {
        if (!AuthService.isLoggedIn()) {
            LoginRequiredDialog.show("Please log in to process checkout.");
            return new LoginPage().getScene();
        }
        if (forceShowSetupAddress) return buildAddressSetupScene();
        currentScene = buildCheckoutScene();
        return currentScene;
    }

    //  SCENE: SETUP ALAMAT PERTAMA KALI
    private Scene buildAddressSetupScene() {
        HBox navBar = createNavBar();
        double screenWidth  = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        VBox content = new VBox(28);
        content.setPadding(new Insets(30, 80, 60, 80));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: #ebddc3;");

        HBox stepBar = buildStepIndicator(2);

        VBox titleBox = new VBox(8);
        titleBox.setAlignment(Pos.CENTER);
        Label title = new Label("Delivery Address Registration");
        title.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );
        Label subtitle = new Label("An address registry is essential to arrange our high-end premium shipping services.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");
        titleBox.getChildren().addAll(title, subtitle);

        VBox formCard = buildAddressFormCard(true);
        formCard.setMaxWidth(Double.MAX_VALUE);

        content.getChildren().addAll(stepBar, titleBox, formCard);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #F0E8DF; -fx-background: #F0E8DF;");
        scroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        scroll.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        return new Scene(root, screenWidth, screenHeight);
    }

    //  SCENE: CHECKOUT UTAMA
    private Scene buildCheckoutScene() {
        HBox navBar = createNavBar();
        double screenWidth  = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        VBox mainContent = new VBox(24);
        mainContent.setPadding(new Insets(30, 50, 50, 50));
        mainContent.setStyle("-fx-background-color: #F0E8DF;");

        HBox stepBar = buildStepIndicator(2);
        mainContent.getChildren().add(stepBar);

        HBox contentLayout = new HBox(26);
        contentLayout.setAlignment(Pos.TOP_LEFT);

        VBox leftPanel = new VBox(20);
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        leftPanel.getChildren().addAll(
                buildAddressSection(),
                buildShippingSection(),
                buildPaymentSection(),
                buildNotesSection(),
                buildItemsPanel()
        );

        VBox rightPanel = buildOrderSummaryWithBenefits();
        rightPanel.setPrefWidth(360);
        rightPanel.setMinWidth(360);

        contentLayout.getChildren().addAll(leftPanel, rightPanel);
        mainContent.getChildren().add(contentLayout);

        ScrollPane pageScroll = new ScrollPane(mainContent);
        pageScroll.setFitToWidth(true);
        pageScroll.setStyle("-fx-background-color: #F0E8DF; -fx-background: #F0E8DF;");
        pageScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
        pageScroll.setBorder(Border.EMPTY);

        VBox root = new VBox(navBar, pageScroll);
        VBox.setVgrow(pageScroll, Priority.ALWAYS);

        updatePlaceOrderBtnState();
        return new Scene(root, screenWidth, screenHeight);
    }

    //  STEP INDICATOR
    private HBox buildStepIndicator(int currentStep) {
        HBox bar = new HBox(0);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(15, 0, 25, 0));
        bar.setStyle("-fx-background-color: transparent;");

        String[] steps = {"Shopping Cart", "Checkout Details", "Order Complete"};

        for (int i = 0; i < steps.length; i++) {
            int     stepNum   = i + 1;
            boolean isDone    = stepNum < currentStep;
            boolean isCurrent = stepNum == currentStep;

            VBox stepCol = new VBox(5);
            stepCol.setAlignment(Pos.CENTER);

            StackPane circle = new StackPane();
            circle.setMinWidth(22); circle.setMinHeight(22);
            circle.setMaxSize(22, 22);

            Label indicator = new Label(isDone ? "✓" : String.valueOf(stepNum));

            if (isCurrent) {
                circle.setStyle(
                        "-fx-background-color: #3E2723; -fx-background-radius: 50; " +
                                "-fx-border-color: #D4A853; -fx-border-width: 1.5; -fx-border-radius: 50;"
                );
                indicator.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #D4A853;");
            } else if (isDone) {
                circle.setStyle("-fx-background-color: #D4A853; -fx-background-radius: 50;");
                indicator.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
            } else {
                circle.setStyle(
                        "-fx-background-color: transparent; -fx-border-color: #D0C0B8; " +
                                "-fx-border-width: 1; -fx-border-radius: 50;"
                );
                indicator.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #D0C0B8;");
            }
            circle.getChildren().add(indicator);

            Label stepLabel = new Label(steps[i]);
            if (isCurrent) {
                stepLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
            } else if (isDone) {
                stepLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #A08060;");
            } else {
                stepLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #BCAAA4;");
            }

            stepCol.getChildren().addAll(circle, stepLabel);
            bar.getChildren().add(stepCol);

            if (i < steps.length - 1) {
                Pane line = new Pane();
                line.setPrefWidth(80);
                line.setPrefHeight(1);
                line.setMaxHeight(1);
                line.setMinHeight(1);
                line.setStyle(stepNum < currentStep
                        ? "-fx-background-color: #D4A853;"
                        : "-fx-background-color: #D0C0B8;"
                );
                HBox.setMargin(line, new Insets(0, 15, 12, 15));
                bar.getChildren().add(line);
            }
        }
        return bar;
    }

    //  SECTION HEADER HELPER
    private HBox buildSectionHeader(String titleText, String rightBtnText, Runnable rightAction) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Pane strip = new Pane();
        strip.setPrefWidth(4);
        strip.setPrefHeight(22);
        strip.setStyle("-fx-background-color: #D4A853; -fx-background-radius: 2;");
        HBox.setMargin(strip, new Insets(0, 12, 0, 0));

        Label title = new Label(titleText);
        title.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );

        header.getChildren().addAll(strip, title);

        if (rightBtnText != null) {
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            Button btn = buildLinkButton(rightBtnText);
            if (rightAction != null) btn.setOnAction(e -> rightAction.run());
            header.getChildren().addAll(sp, btn);
        }
        return header;
    }

    //  SECTION: ALAMAT PENGIRIMAN
    private VBox buildAddressSection() {
        VBox card = new VBox(16);
        card.setStyle(sectionCardStyle());

        HBox header = buildSectionHeader("Delivery Destination", "+ Add New Address", this::toggleAddressForm);

        savedAddressesBox = new VBox(12);
        refreshSavedAddresses();

        addressFormBox = buildAddressFormCard(false);
        addressFormBox.setVisible(false);
        addressFormBox.setManaged(false);

        card.getChildren().addAll(header, new Separator(), savedAddressesBox, addressFormBox);
        return card;
    }

    private void toggleAddressForm() {
        boolean showing = addressFormBox.isVisible();
        addressFormBox.setVisible(!showing);
        addressFormBox.setManaged(!showing);
    }

    private void refreshSavedAddresses() {
        savedAddressesBox.getChildren().clear();
        User user = AuthService.getCurrentUser();
        if (selectedAddress == null && user != null) selectedAddress = user.getPrimaryAddress();
        if (user != null) {
            for (User.Address addr : user.getAddresses()) {
                savedAddressesBox.getChildren().add(buildAddressCard(addr));
            }
        }
        updatePlaceOrderBtnState();
    }

    private VBox buildAddressCard(User.Address addr) {
        boolean isSelected = addr == selectedAddress;

        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setStyle(
                "-fx-background-color: " + (isSelected ? "#FCF9F5" : "white") + "; " +
                        "-fx-border-color: "     + (isSelected ? "#3E2723" : "#F0EAE4") + "; " +
                        "-fx-border-width: "     + (isSelected ? "1.5" : "1") + "; " +
                        "-fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;"
        );

        VBox info = new VBox(6);

        HBox nameRow = new HBox(8);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel  = new Label(addr.getRecipientName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        Label phoneLabel = new Label(addr.getPhone());
        phoneLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");
        nameRow.getChildren().addAll(nameLabel, new Label("·"), phoneLabel);

        HBox badgesRow = new HBox(6);
        Label labelBadge = new Label(addr.getLabel().toUpperCase());
        labelBadge.setStyle(
                "-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #3E2723; " +
                        "-fx-background-color: #F5F0EA; -fx-background-radius: 4; -fx-padding: 2 6;"
        );
        badgesRow.getChildren().add(labelBadge);

        if (addr.isPrimary()) {
            Label primaryBadge = new Label("PRIMARY");
            primaryBadge.setStyle(
                    "-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #D4A853; " +
                            "-fx-border-color: #D4A853; -fx-border-radius: 4; -fx-padding: 1 5;"
            );
            badgesRow.getChildren().add(primaryBadge);
        }

        Label addrText = new Label(
                addr.getStreet() + ", " + addr.getCity() + ", " +
                        addr.getProvince() + " " + addr.getPostalCode()
        );
        addrText.setStyle("-fx-font-size: 13px; -fx-text-fill: #5D4037;");
        addrText.setWrapText(true);

        info.getChildren().addAll(nameRow, badgesRow, addrText);

        HBox footer = new HBox(info);
        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        footer.getChildren().add(space);

        if (!addr.isPrimary()) {
            Button setPrimaryBtn = buildLinkButton("Set Primary");
            setPrimaryBtn.setOnAction(e -> {
                AuthService.getCurrentUser().setPrimaryAddress(addr);
                selectedAddress = addr;
                refreshSavedAddresses();
            });
            footer.getChildren().add(setPrimaryBtn);
        }

        Button deleteBtn = new Button("✕ Delete");
        deleteBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #BDBDBD; " +
                        "-fx-font-size: 12px; -fx-cursor: hand;"
        );
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #E57373; " +
                        "-fx-font-size: 12px; -fx-cursor: hand;"
        ));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #BDBDBD; " +
                        "-fx-font-size: 12px; -fx-cursor: hand;"
        ));
        deleteBtn.setOnAction(e -> {
            User user = AuthService.getCurrentUser();
            if (user.getAddresses().size() == 1) {
                CustomToast.showWarningToast(currentScene.getWindow(), "You must have at least one address.");
                return;
            }
            user.getAddresses().remove(addr);
            if (addr == selectedAddress) {
                selectedAddress = user.getAddresses().isEmpty() ? null : user.getAddresses().get(0);
                if (selectedAddress != null) selectedAddress.setPrimary(true);
            }
            refreshSavedAddresses();
        });
        footer.getChildren().add(deleteBtn);

        card.getChildren().add(footer);
        card.setOnMouseClicked(e -> {
            selectedAddress = addr;
            refreshSavedAddresses();
        });
        return card;
    }

    //  FORM ALAMAT
    private VBox buildAddressFormCard(boolean isSetupMode) {
        VBox form = new VBox(isSetupMode ? 20 : 14);
        form.setStyle(isSetupMode
                ? "-fx-background-color: white; -fx-background-radius: 20; -fx-padding: 35 45 35 45; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 3);"
                : "-fx-background-color: #FAF6F1; -fx-background-radius: 12; -fx-padding: 20;"
        );

        Label formTitle = new Label("Operational Registry Location Details");
        formTitle.setStyle(
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );

        HBox row1 = new HBox(20);
        labelField     = buildTextField("Address Label (e.g., Home, Studio, Office)");
        recipientField = buildTextField("Recipient Full Name *");
        HBox.setHgrow(labelField,     Priority.ALWAYS);
        HBox.setHgrow(recipientField, Priority.ALWAYS);
        row1.getChildren().addAll(labelField, recipientField);

        HBox row2 = new HBox(20);
        phoneField  = buildTextField("Active Contact Number *");
        streetField = buildTextField("Complete Address Lines (Street, Suite, Block) *");
        HBox.setHgrow(phoneField,  Priority.ALWAYS);
        HBox.setHgrow(streetField, Priority.ALWAYS);
        row2.getChildren().addAll(phoneField, streetField);

        HBox row3 = new HBox(20);
        row3.setAlignment(Pos.CENTER_LEFT);

        provinceCombo = new ComboBox<>();
        provinceCombo.getItems().addAll(PROVINCE_CITIES.keySet());
        provinceCombo.setPromptText("Province *");
        provinceCombo.setMaxWidth(Double.MAX_VALUE);
        provinceCombo.setStyle(comboStyle());
        HBox.setHgrow(provinceCombo, Priority.ALWAYS);

        cityCombo = new ComboBox<>();
        cityCombo.setPromptText("City *");
        cityCombo.setDisable(true);
        cityCombo.setMaxWidth(Double.MAX_VALUE);
        cityCombo.setStyle(comboStyle());
        HBox.setHgrow(cityCombo, Priority.ALWAYS);

        postalField = buildTextField("Postal Zip Code *");
        HBox.setHgrow(postalField, Priority.ALWAYS);

        row3.getChildren().addAll(provinceCombo, cityCombo, postalField);

        locationRecommendationLabel = new Label();
        locationRecommendationLabel.setStyle(
                "-fx-font-size: 12px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #D4A853; " +
                        "-fx-background-color: #3E2723; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 9 16;"
        );
        locationRecommendationLabel.setVisible(false);
        locationRecommendationLabel.setManaged(false);

        provinceCombo.setOnAction(e -> {
            String selectedProvince = provinceCombo.getValue();
            cityCombo.getItems().clear();
            cityCombo.setValue(null);

            if (selectedProvince != null) {
                cityCombo.setDisable(false);
                cityCombo.getItems().addAll(
                        PROVINCE_CITIES.getOrDefault(selectedProvince, List.of())
                );
                cityCombo.setPromptText("Select City *");

                String promo = PROVINCE_PROMO.get(selectedProvince);
                if (promo != null) {
                    locationRecommendationLabel.setText("✦  Location Offer Applied:  " + promo);
                    locationRecommendationLabel.setVisible(true);
                    locationRecommendationLabel.setManaged(true);
                } else {
                    locationRecommendationLabel.setVisible(false);
                    locationRecommendationLabel.setManaged(false);
                }
            } else {
                cityCombo.setDisable(true);
                locationRecommendationLabel.setVisible(false);
                locationRecommendationLabel.setManaged(false);
            }
        });

        addressFormError = new Label();
        addressFormError.setStyle("-fx-text-fill: #E57373; -fx-font-size: 12px; -fx-font-weight: bold;");

        HBox btnRow = new HBox(12);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        String buttonText = isSetupMode
                ? "Save & Continue to Checkout Details"
                : "Register Destination Address";
        Button saveBtn = new Button(buttonText);
        saveBtn.setPadding(new Insets(12, 35, 12, 35));
        saveBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 10; -fx-cursor: hand;"
        );
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle(
                "-fx-background-color: #D4A853; -fx-text-fill: #2C1810; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 10; -fx-cursor: hand;"
        ));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 10; -fx-cursor: hand;"
        ));
        saveBtn.setOnAction(e -> {
            if (isSetupMode) saveAddressAndProceed();
            else saveNewAddress();
        });

        if (isSetupMode) {
            btnRow.getChildren().add(saveBtn);
            form.getChildren().addAll(
                    formTitle, new Separator(),
                    row1, row2, row3,
                    locationRecommendationLabel, addressFormError, btnRow
            );
        } else {
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle(cancelBtnStyle());
            cancelBtn.setPadding(new Insets(12, 24, 12, 24));
            cancelBtn.setOnAction(e -> {
                addressFormBox.setVisible(false);
                addressFormBox.setManaged(false);
                clearAddressForm();
            });
            btnRow.getChildren().addAll(cancelBtn, saveBtn);
            form.getChildren().addAll(
                    formTitle,
                    row1, row2, row3,
                    locationRecommendationLabel, addressFormError, btnRow
            );
        }

        return form;
    }

    //  LOGIKA SIMPAN ALAMAT
    private void saveAddressAndProceed() {
        if (!validateAddressForm()) return;
        User user = AuthService.getCurrentUser();
        if (user != null) {
            User.Address addr = buildAddressFromForm();
            addr.setPrimary(true);
            user.getAddresses().add(addr);
            selectedAddress = addr;
        }
        this.forceShowSetupAddress = false;
        SceneManager.setScene(this.getScene());
    }

    private void saveNewAddress() {
        if (!validateAddressForm()) return;
        User user = AuthService.getCurrentUser();
        User.Address addr = buildAddressFromForm();
        if (user != null) {
            if (user.getAddresses().isEmpty()) addr.setPrimary(true);
            user.getAddresses().add(addr);
            selectedAddress = addr;
        }
        clearAddressForm();
        addressFormBox.setVisible(false);
        addressFormBox.setManaged(false);
        refreshSavedAddresses();
    }

    private boolean validateAddressForm() {
        boolean provinceEmpty = provinceCombo.getValue() == null || provinceCombo.getValue().isBlank();
        boolean cityEmpty     = cityCombo.getValue()     == null || cityCombo.getValue().isBlank();

        if (recipientField.getText().trim().isEmpty()
                || phoneField.getText().trim().isEmpty()
                || streetField.getText().trim().isEmpty()
                || provinceEmpty
                || cityEmpty
                || postalField.getText().trim().isEmpty()) {
            addressFormError.setText("Logistics criteria fields require precise inputs.");
            return false;
        }
        addressFormError.setText("");
        return true;
    }

    private User.Address buildAddressFromForm() {
        String lbl = labelField.getText().trim().isEmpty() ? "Location" : labelField.getText().trim();
        return new User.Address(
                lbl,
                recipientField.getText().trim(),
                phoneField.getText().trim(),
                streetField.getText().trim(),
                cityCombo.getValue(),
                provinceCombo.getValue(),
                postalField.getText().trim(),
                false
        );
    }

    private void clearAddressForm() {
        labelField.clear();
        recipientField.clear();
        phoneField.clear();
        streetField.clear();
        postalField.clear();
        provinceCombo.setValue(null);
        cityCombo.getItems().clear();
        cityCombo.setDisable(true);
        if (locationRecommendationLabel != null) {
            locationRecommendationLabel.setVisible(false);
            locationRecommendationLabel.setManaged(false);
        }
        if (addressFormError != null) addressFormError.setText("");
    }
    
    //  SECTION: PENGIRIMAN
    private VBox buildShippingSection() {
        VBox card = new VBox(16);
        card.setStyle(sectionCardStyle());

        HBox header = buildSectionHeader("Logistics Arrangement", null, null);

        shippingGroup = new ToggleGroup();
        VBox options = new VBox(12);
        options.getChildren().addAll(
                buildShippingOptionCard("Standard Carrier",    "3–5 Business Days", 15000, true),
                buildShippingOptionCard("Priority JetExpress", "1–2 Business Days", 35000, false)
        );

        card.getChildren().addAll(header, new Separator(), options);
        return card;
    }

    private HBox buildShippingOptionCard(String name, String duration, double cost, boolean selected) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 18, 16, 18));
        row.setStyle(optionCardStyle(selected));

        RadioButton radio = new RadioButton();
        radio.setToggleGroup(shippingGroup);
        radio.setSelected(selected);
        radio.setStyle("-fx-cursor: hand;");

        Label iconLbl = new Label(cost > 20000 ? "⚡" : "📦");
        iconLbl.setStyle("-fx-font-size: 22px;");

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        HBox durationRow = new HBox(6);
        durationRow.setAlignment(Pos.CENTER_LEFT);
        Label durationLabel = new Label(duration);
        durationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63;");
        durationRow.getChildren().add(durationLabel);

        if (cost > 20000) {
            Label fastBadge = new Label("EXPRESS");
            fastBadge.setStyle(
                    "-fx-font-size: 9px; -fx-font-weight: bold; -fx-text-fill: #7A3A00; " +
                            "-fx-background-color: #FAE8C8; -fx-background-radius: 4; -fx-padding: 2 6;"
            );
            durationRow.getChildren().add(fastBadge);
        }

        info.getChildren().addAll(nameLabel, durationRow);

        Label priceLabel = new Label("Rp " + Styles.formatPrice(cost));
        priceLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");

        row.getChildren().addAll(radio, iconLbl, info, priceLabel);
        row.setOnMouseClicked(e -> {
            radio.setSelected(true);
            updateShippingStyles();
            updateTotal();
        });
        return row;
    }

    private void updateShippingStyles() {
        Toggle sel = shippingGroup.getSelectedToggle();
        for (Toggle t : shippingGroup.getToggles()) {
            HBox row = (HBox) ((RadioButton) t).getParent();
            row.setStyle(optionCardStyle(t == sel));
        }
    }

    //  SECTION: PEMBAYARAN
    private VBox buildPaymentSection() {
        VBox card = new VBox(16);
        card.setStyle(sectionCardStyle());

        HBox header = buildSectionHeader("Settlement Pathway", null, null);

        paymentGroup = new ToggleGroup();
        VBox options = new VBox(12);
        options.getChildren().addAll(
                buildPaymentOptionCard("Direct Bank Transfer",          "BCA, Mandiri Escrow Vault",    true),
                buildPaymentOptionCard("International Card Transaction", "Visa, Mastercard Secured TSL", false)
        );

        card.getChildren().addAll(header, new Separator(), options);
        return card;
    }

    private HBox buildPaymentOptionCard(String name, String detail, boolean selected) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 16, 14, 16));
        row.setStyle(optionCardStyle(selected));

        RadioButton radio = new RadioButton();
        radio.setToggleGroup(paymentGroup);
        radio.setSelected(selected);
        radio.setStyle("-fx-cursor: hand;");

        VBox info = new VBox(4);
        Label nameLabel   = new Label(name);
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        Label detailLabel = new Label(detail);
        detailLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63;");
        info.getChildren().addAll(nameLabel, detailLabel);

        row.getChildren().addAll(radio, info);
        row.setOnMouseClicked(e -> {
            radio.setSelected(true);
            updatePaymentStyles();
        });
        return row;
    }

    private void updatePaymentStyles() {
        Toggle sel = paymentGroup.getSelectedToggle();
        for (Toggle t : paymentGroup.getToggles()) {
            HBox row = (HBox) ((RadioButton) t).getParent();
            row.setStyle(optionCardStyle(t == sel));
        }
    }

    //  SECTION: CATATAN KURIR
    private VBox buildNotesSection() {
        VBox card = new VBox(12);
        card.setStyle(sectionCardStyle());

        HBox header = buildSectionHeader("Notes for Courier (optional)", null, null);

        notesField = new TextField();
        notesField.setPromptText("e.g., Leave at the door, ring once...");
        notesField.setStyle(
                "-fx-background-color: #FAF6F1; -fx-text-fill: #3E2723; " +
                        "-fx-prompt-text-fill: #8D6E63; -fx-background-radius: 12; " +
                        "-fx-padding: 12 15; -fx-border-color: #D7CCC8; -fx-border-radius: 12;"
        );
        notesField.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(header, new Separator(), notesField);
        return card;
    }

    //  SECTION: REVIEW ITEM
    private VBox buildItemsPanel() {
        VBox card = new VBox(14);
        card.setStyle(sectionCardStyle());

        HBox header = buildSectionHeader("Review Selected Wardrobe Items", null, null);

        VBox itemsContainer = new VBox(8);
        for (CartItem item : checkoutItems) {
            HBox row = new HBox(16);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #FAF6F1; -fx-background-radius: 10;");

            Label img = new Label(item.getProduct().getImageUrl());
            img.setStyle("-fx-font-size: 28px;");

            VBox info = new VBox(4);
            HBox.setHgrow(info, Priority.ALWAYS);
            Label nameL = new Label(item.getProduct().getName());
            nameL.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
            Label catQty = new Label("Qty: " + item.getQuantity());
            catQty.setStyle("-fx-font-size: 12px; -fx-text-fill: #8D6E63;");
            info.getChildren().addAll(nameL, catQty);

            Label subT = new Label("Rp " + Styles.formatPrice(item.getSubtotal()));
            subT.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2C1810;");

            row.getChildren().addAll(img, info, subT);
            itemsContainer.getChildren().add(row);
        }

        card.getChildren().addAll(header, new Separator(), itemsContainer);
        return card;
    }

    //  PANEL KANAN: ORDER SUMMARY
    private VBox buildOrderSummaryWithBenefits() {
        VBox card = new VBox(16);
        card.setStyle(sectionCardStyle());

        HBox header = buildSectionHeader("Order Summary", null, null);
        card.getChildren().addAll(header, new Separator());

        VBox financialList = new VBox(10);
        financialList.getChildren().addAll(
                summaryRow("Merchandise Subtotal", "Rp " + Styles.formatPrice(currentSubtotal)),
                summaryRowWithLabel("Logistics Processing",
                        shippingCostLabel = new Label("Rp " + Styles.formatPrice(15000)))
        );
        shippingCostLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723; -fx-font-weight: bold;");
        card.getChildren().add(financialList);

        VBox benefitsContainer = new VBox(10);
        benefitsContainer.setPadding(new Insets(16));
        benefitsContainer.setStyle("-fx-background-color: #3E2723; -fx-background-radius: 12;");

        Label benefitsTitle = new Label("✦  Included Perks");
        benefitsTitle.setStyle(
                "-fx-font-size: 10px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #D4A853; -fx-letter-spacing: 1px;"
        );

        checkoutPointsLabel = new Label("Earn " + (int)(currentSubtotal / 1000) + " Vendoza Loyalty Points");
        checkoutPointsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #FAF6F1; -fx-font-weight: bold;");

        Label boxTxt = new Label("✓  Complimentary Eco-Luxury Box");
        boxTxt.setStyle("-fx-font-size: 12px; -fx-text-fill: #D4A853;");
        Label checkTxt = new Label("✓  Atelier Protection & Anti-Wrinkle Mist");
        checkTxt.setStyle("-fx-font-size: 12px; -fx-text-fill: #D4A853;");

        benefitsContainer.getChildren().addAll(benefitsTitle, checkoutPointsLabel, boxTxt, checkTxt);
        card.getChildren().addAll(benefitsContainer, new Separator());

        totalLabel = new Label("Rp " + Styles.formatPrice(currentSubtotal + 15000));
        totalLabel.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );

        placeOrderBtn = new Button("Authorize Secure Order");
        placeOrderBtn.setMaxWidth(Double.MAX_VALUE);
        placeOrderBtn.setPrefHeight(46);
        placeOrderBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 12; -fx-cursor: hand;"
        );
        placeOrderBtn.setOnMouseEntered(e -> placeOrderBtn.setStyle(
                "-fx-background-color: #D4A853; -fx-text-fill: #2C1810; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 12; -fx-cursor: hand;"
        ));
        placeOrderBtn.setOnMouseExited(e -> placeOrderBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 12; -fx-cursor: hand;"
        ));

        Button backBtn = new Button("Return to Cart Bag");
        backBtn.setMaxWidth(Double.MAX_VALUE);
        backBtn.setStyle(cancelBtnStyle());
        backBtn.setOnAction(e -> SceneManager.showCartPage());

        card.getChildren().addAll(
                summaryRowWithLabel("Total Payable Amount", totalLabel),
                placeOrderBtn,
                backBtn
        );
        return card;
    }

    private void updatePlaceOrderBtnState() {
        if (placeOrderBtn == null) return;
        boolean has = selectedAddress != null;
        placeOrderBtn.setDisable(!has);
        placeOrderBtn.setOnAction(e -> { if (has) placeOrder(); });
    }

    private void placeOrder() {
        User   user     = AuthService.getCurrentUser();
        double shipping = getShippingCost();
        String orderId  = "ORD-" + System.currentTimeMillis();

        if (user != null && selectedAddress != null) {
            Order newOrder = new Order(orderId, user, checkoutItems, shipping, selectedAddress.toDisplayString());
            user.getOrders().add(newOrder);
        }
        for (CartItem item : checkoutItems) CartService.removeFromCart(item.getProduct());

        SceneManager.setScene(buildThankYouScene(
                orderId,
                currentSubtotal + shipping,
                getSelectedShippingName(),
                getSelectedPaymentName()
        ));
    }

    //  SCENE: ORDER COMPLETE
    private Scene buildThankYouScene(String orderId, double total, String shippingName, String paymentName) {
        double screenWidth  = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        VBox rootBox = new VBox(0);
        rootBox.setStyle("-fx-background-color: #F0E8DF;");

        VBox heroSection = new VBox(6);
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(28, 50, 28, 50));
        heroSection.setStyle("-fx-background-color: #3E2723;");

        Label checkmark = new Label("✧");
        checkmark.setStyle("-fx-font-size: 28px; -fx-text-fill: #D4A853;");

        Label heroTitle = new Label("Order Confirmed");
        heroTitle.setStyle(
                "-fx-font-size: 26px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #FAF6F1; -fx-font-family: 'Playfair Display';"
        );

        Label heroSub = new Label("Your curated pieces are being prepared with our signature atelier care.");
        heroSub.setStyle("-fx-font-size: 12px; -fx-text-fill: #BCA898; -fx-font-style: italic;");

        Label orderPill = new Label("ORDER ID: " + orderId);
        orderPill.setStyle(
                "-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #D4A853; " +
                        "-fx-border-color: #D4A853; -fx-border-radius: 20; -fx-padding: 4 14;"
        );

        HBox stepBar = buildStepIndicator(4);
        stepBar.setPadding(new Insets(12, 0, 0, 0));

        heroSection.getChildren().addAll(checkmark, heroTitle, heroSub, orderPill, stepBar);

        VBox contentArea = new VBox(30);
        contentArea.setPadding(new Insets(40, 100, 60, 100));
        contentArea.setAlignment(Pos.TOP_CENTER);

        HBox splitLayout = new HBox(40);
        splitLayout.setAlignment(Pos.TOP_CENTER);

        VBox leftPanel = new VBox(25);
        leftPanel.setPrefWidth(500);

        VBox receiptBox = new VBox(0);
        receiptBox.setStyle(
                "-fx-background-color: white; -fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 15, 0, 0, 5);"
        );

        VBox receiptHeader = new VBox(5);
        receiptHeader.setPadding(new Insets(25, 30, 20, 30));
        receiptHeader.setStyle("-fx-border-color: transparent transparent #F0EAE4 transparent; -fx-border-width: 1;");
        Label rTitle = new Label("Transaction Receipt");
        rTitle.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );
        Label rDate = new Label("Issued " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
        rDate.setStyle("-fx-font-size: 12px; -fx-text-fill: #A09088;");
        receiptHeader.getChildren().addAll(rTitle, rDate);

        VBox rRows = new VBox(0);
        rRows.getChildren().addAll(
                receiptRow("Order Status",    "Confirmed",  false),
                receiptRow("Shipping Method", shippingName, false),
                receiptRow("Payment Method",  paymentName,  false)
        );

        HBox totalRow = new HBox();
        totalRow.setPadding(new Insets(20, 30, 20, 30));
        totalRow.setStyle("-fx-border-color: #F0EAE4 transparent transparent transparent; -fx-border-width: 1;");
        Label tLbl = new Label("Total Charged");
        tLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #8D6E63;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label tVal = new Label("Rp " + Styles.formatPrice(total));
        tVal.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Playfair Display';"
        );
        totalRow.getChildren().addAll(tLbl, spacer, tVal);

        receiptBox.getChildren().addAll(receiptHeader, rRows, totalRow);
        leftPanel.getChildren().add(receiptBox);

        VBox rightPanel = new VBox(25);
        rightPanel.setPrefWidth(350);

        VBox stylingCard = new VBox(15);
        stylingCard.setPadding(new Insets(30));
        stylingCard.setStyle("-fx-background-color: #3E2723; -fx-background-radius: 20;");
        Label sTitle = new Label("Elevate Your New Collection");
        sTitle.setStyle("-fx-font-size: 20px; -fx-text-fill: #FAF6F1; -fx-font-family: 'Playfair Display';");
        Label sDesc = new Label(
                "Connect with a Vendoza Personal Stylist to curate perfect outfits " +
                        "from your new pieces — complimentary."
        );
        sDesc.setWrapText(true);
        sDesc.setStyle("-fx-font-size: 13px; -fx-text-fill: #BCA898; -fx-line-spacing: 5;");
        Button sBtn = new Button("Book Styling Session  →");
        sBtn.setMaxWidth(Double.MAX_VALUE);
        sBtn.setStyle(
                "-fx-background-color: #D4A853; -fx-text-fill: #2C1810; " +
                        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 12; -fx-cursor: hand;"
        );
        stylingCard.getChildren().addAll(sTitle, sDesc, sBtn);

        VBox pointsCard = new VBox(10);
        pointsCard.setPadding(new Insets(20));
        pointsCard.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-border-color: #F0EAE4;");
        Label pVal = new Label("+" + (int)(total / 1000) + " Loyalty Points");
        pVal.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #3E2723;");
        pointsCard.getChildren().add(pVal);

        rightPanel.getChildren().addAll(stylingCard, pointsCard);
        splitLayout.getChildren().addAll(leftPanel, rightPanel);

        HBox ctaRow = new HBox(20);
        ctaRow.setAlignment(Pos.CENTER);
        Button shopBtn = new Button("Continue Shopping");
        shopBtn.setStyle(
                "-fx-background-color: #3E2723; -fx-text-fill: #D4A853; " +
                        "-fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 12 30; -fx-cursor: hand;"
        );
        shopBtn.setOnAction(e -> SceneManager.showHomePage());
        ctaRow.getChildren().add(shopBtn);

        contentArea.getChildren().addAll(splitLayout, ctaRow);
        rootBox.getChildren().addAll(heroSection, contentArea);

        ScrollPane scroll = new ScrollPane(rootBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #F0E8DF; -fx-background: #F0E8DF;");
        scroll.setHbarPolicy(ScrollBarPolicy.NEVER);

        return new Scene(new VBox(createNavBar(), scroll), screenWidth, screenHeight);
    }

    //  HELPER: RECEIPT ROW
    private HBox receiptRow(String label, String value, boolean highlight) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 28, 14, 28));
        row.setStyle("-fx-border-color: transparent transparent #F7F2EE transparent; -fx-border-width: 1;");
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #A09088;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label val = new Label(value);
        val.setStyle(highlight
                ? "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #D4A853;"
                : "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #3E2723;"
        );
        row.getChildren().addAll(lbl, spacer, val);
        return row;
    }
    
    //  HELPER: KALKULASI & STATE
    private double getShippingCost() {
        if (shippingGroup == null) return 15000;
        int idx = shippingGroup.getToggles().indexOf(shippingGroup.getSelectedToggle());
        return idx == 1 ? 35000 : 15000;
    }

    private String getSelectedShippingName() {
        if (shippingGroup == null) return "Standard Carrier";
        int idx = shippingGroup.getToggles().indexOf(shippingGroup.getSelectedToggle());
        return idx == 1 ? "Priority JetExpress" : "Standard Carrier";
    }

    private String getSelectedPaymentName() {
        if (paymentGroup == null) return "Direct Bank Transfer";
        int idx = paymentGroup.getToggles().indexOf(paymentGroup.getSelectedToggle());
        return idx == 1 ? "International Card" : "Direct Bank Transfer";
    }

    private void updateTotal() {
        if (totalLabel == null || shippingCostLabel == null) return;
        double shipping = getShippingCost();
        shippingCostLabel.setText("Rp " + Styles.formatPrice(shipping));
        totalLabel.setText("Rp " + Styles.formatPrice(currentSubtotal + shipping));
        if (checkoutPointsLabel != null) {
            checkoutPointsLabel.setText("Earn " + (int)(currentSubtotal / 1000) + " Vendoza Loyalty Points");
        }
    }

    //  HELPER: STYLE & BUILDER
    private String optionCardStyle(boolean selected) {
        return "-fx-background-color: " + (selected ? "#FCF9F5" : "white") + "; " +
                "-fx-border-color: "     + (selected ? "#3E2723" : "#F0EAE4") + "; " +
                "-fx-border-width: 1; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-cursor: hand;";
    }

    private String sectionCardStyle() {
        return "-fx-background-color: white; -fx-background-radius: 16; -fx-padding: 24; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 10, 0, 0, 2);";
    }

    private String cancelBtnStyle() {
        return "-fx-background-color: transparent; -fx-text-fill: #8D6E63; " +
                "-fx-font-size: 13px; -fx-border-color: #D7CCC8; " +
                "-fx-border-radius: 12; -fx-background-radius: 12; " +
                "-fx-padding: 12 0; -fx-cursor: hand;";
    }

    private String comboStyle() {
        return "-fx-background-color: #FAF6F1; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: #D7CCC8; " +
                "-fx-border-radius: 12; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 2 0;";
    }

    private TextField buildTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(
                "-fx-background-color: #FAF6F1; -fx-text-fill: #3E2723; " +
                        "-fx-prompt-text-fill: #8D6E63; -fx-background-radius: 12; " +
                        "-fx-padding: 12 15; -fx-border-color: #D7CCC8; " +
                        "-fx-border-radius: 12; -fx-font-size: 13px;"
        );
        return tf;
    }

    private Button buildLinkButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-background-color: transparent; -fx-font-size: 13px; " +
                        "-fx-font-weight: bold; -fx-text-fill: #3E2723; -fx-cursor: hand;"
        );
        return btn;
    }

    private HBox summaryRow(String lbl, String val) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label l = new Label(lbl);
        l.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label v = new Label(val);
        v.setStyle("-fx-font-size: 13px; -fx-text-fill: #3E2723; -fx-font-weight: bold;");
        row.getChildren().addAll(l, sp, v);
        return row;
    }

    private HBox summaryRowWithLabel(String lbl, Label valLabel) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        Label l = new Label(lbl);
        l.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        row.getChildren().addAll(l, sp, valLabel);
        return row;
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
        Button cartBtn    = createNavButton("🛒  Cart",    false);
        Button profileBtn = createNavButton("👤  Profile", false);

        homeBtn.setOnAction(e -> SceneManager.showHomePage());
        searchBtn.setOnAction(e -> SceneManager.setScene(new SearchPage().getScene()));
        cartBtn.setOnAction(e -> SceneManager.showCartPage());
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
