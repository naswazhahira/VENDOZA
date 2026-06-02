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
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.File;

public class EditProfilePage {

    private TextField nameField;
    private TextField phoneField;
    private TextField emailField;
    private TextArea addressArea;
    private Label messageLabel;
    private String selectedPhotoPath = null;

    private ImageView photoImageView;
    private Circle defaultCircleBg;
    private Label defaultPhotoIcon;

    public Scene getScene() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        User currentUser = AuthService.getCurrentUser();

        if (currentUser.getProfilePhotoPath() != null) {
            selectedPhotoPath = currentUser.getProfilePhotoPath();
        }

        HBox navBar = createNavBar();

        ScrollPane scrollPane = new ScrollPane(createContent(currentUser));
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(" -fx-background: #ebddc3;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBorder(Border.EMPTY);

        VBox mainLayout = new VBox(navBar, scrollPane);
        mainLayout.setStyle("-fx-background-color: #ebddc3;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, screenWidth, screenHeight);

        // CSS dengan pengecekan null
        java.net.URL cssUrl = getClass().getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        return scene;
    }

    private HBox createNavBar() {
        HBox navBar = new HBox(30);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(16, 50, 16, 22));
        navBar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);"
        );

        Button backBtn = new Button("❮");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                "-fx-font-size: 24px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0 8 0 0;");

        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #D4A853;" +
                        "-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Georgia'; " +
                        "-fx-padding: 0 10 0 0; -fx-cursor: hand;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #3E2723;" +
                        "-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Georgia'; " +
                        "-fx-padding: 0 10 0 0; -fx-cursor: hand;"
        ));
        backBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        Label logo = new Label("Edit Profile");
        logo.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723; -fx-font-family: 'Georgia';"
        );

        // Gabungkan Back dan Logo dalam satu HBox
        HBox logoGroup = new HBox(4);
        logoGroup.setAlignment(Pos.CENTER_LEFT);
        logoGroup.getChildren().addAll(backBtn, logo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Tidak ada tombol tambahan
        navBar.getChildren().addAll(logoGroup, spacer);
        return navBar;
    }

    private VBox createContent(User currentUser) {
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40, 60, 60, 60));
        content.setStyle("-fx-background-color: #ebddc3;");

        Text title = new Text("Edit Profile");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-fill: " + Styles.BROWN_DARK +
                "; -fx-font-family: 'Georgia';");

        VBox photoSection = createPhotoSection(currentUser);

        VBox formBox = new VBox(15);
        formBox.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 15; -fx-padding: 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        formBox.setMaxWidth(500);
        formBox.setAlignment(Pos.TOP_LEFT);

        Label nameLabel = new Label("Full Name");
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        nameField = new TextField(currentUser.getUsername());
        nameField.setStyle(Styles.textFieldStyle());

        Label phoneLabel = new Label("Phone Number");
        phoneLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        String phoneValue = (currentUser.getPhoneNumber() != null) ? currentUser.getPhoneNumber() : "";
        phoneField = new TextField(phoneValue);
        phoneField.setPromptText("e.g. 081234567890 (optional)");
        phoneField.setStyle(Styles.textFieldStyle());
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[0-9+\\-\\s]*")) phoneField.setText(oldVal);
        });

        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        emailField = new TextField(currentUser.getEmail());
        emailField.setEditable(false);
        emailField.setStyle("-fx-background-color: " + Styles.BROWN_PALE + ";" +
                "-fx-text-fill: " + Styles.TEXT_LIGHT + ";" +
                "-fx-background-radius: 15; -fx-padding: 12 15;");

        Label emailNote = new Label("Email cannot be changed");
        emailNote.setStyle("-fx-font-size: 11px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        Label addressLabel = new Label("Shipping Address");
        addressLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        String addressValue = (currentUser.getAddress() != null) ? currentUser.getAddress() : "";
        addressArea = new TextArea(addressValue);
        addressArea.setPromptText("Enter your complete shipping address");
        addressArea.setStyle(Styles.textFieldStyle());
        addressArea.setPrefHeight(80);
        addressArea.setWrapText(true);

        messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + "; -fx-font-size: 12px;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle(Styles.buttonStyle());
        saveBtn.setPrefWidth(160);
        saveBtn.setOnMouseEntered(e -> {
            saveBtn.setStyle(Styles.buttonHoverStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), saveBtn);
            st.setToX(1.05); st.setToY(1.05); st.play();
        });
        saveBtn.setOnMouseExited(e -> {
            saveBtn.setStyle(Styles.buttonStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(150), saveBtn);
            st.setToX(1); st.setToY(1); st.play();
        });
        saveBtn.setOnAction(e -> saveProfile());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(Styles.outlineButtonStyle());
        cancelBtn.setPrefWidth(150);
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(
                "-fx-background-color: " + Styles.BROWN_LIGHT + ";" +
                        "-fx-text-fill: " + Styles.BROWN_DARK + "; -fx-border-color: " + Styles.BROWN_DARK + ";" +
                        "-fx-border-radius: 25; -fx-padding: 8 20; -fx-cursor: hand;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(Styles.outlineButtonStyle()));
        cancelBtn.setOnAction(e -> SceneManager.setScene(new ProfilePage().getScene()));

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);

        formBox.getChildren().addAll(
                nameLabel, nameField,
                phoneLabel, phoneField,
                emailLabel, emailField, emailNote,
                addressLabel, addressArea,
                messageLabel, buttonBox
        );

        content.getChildren().addAll(title, photoSection, formBox);
        return content;
    }

    private VBox createPhotoSection(User user) {
        VBox section = new VBox(12);
        section.setAlignment(Pos.CENTER);

        StackPane photoStack = new StackPane();
        photoStack.setAlignment(Pos.CENTER);
        photoStack.setPrefSize(110, 110);
        photoStack.setMaxSize(110, 110);

        defaultCircleBg = new Circle(55);
        defaultCircleBg.setFill(Color.web(Styles.BROWN_DARK));
        defaultCircleBg.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.2)));

        defaultPhotoIcon = new Label("👤");
        defaultPhotoIcon.setStyle("-fx-font-size: 55px;");

        photoImageView = new ImageView();
        photoImageView.setVisible(false);

        photoStack.getChildren().addAll(defaultCircleBg, defaultPhotoIcon, photoImageView);

        String existingPhoto = user.getProfilePhotoPath();
        if (existingPhoto != null && !existingPhoto.isEmpty()) {
            loadPhotoIntoView(existingPhoto);
        }

        photoStack.setOnMouseClicked(e -> openGallery());
        photoStack.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), photoStack);
            st.setToX(1.05); st.setToY(1.05); st.play();
        });
        photoStack.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), photoStack);
            st.setToX(1); st.setToY(1); st.play();
        });

        Button galleryBtn = new Button("Choose from Gallery");
        galleryBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 12px; -fx-padding: 8 15;");
        galleryBtn.setOnMouseEntered(e -> galleryBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 12px; -fx-padding: 8 15;"));
        galleryBtn.setOnMouseExited(e -> galleryBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 12px; -fx-padding: 8 15;"));
        galleryBtn.setOnAction(e -> openGallery());

        Button cameraBtn = new Button("Take Photo");
        cameraBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 12px; -fx-padding: 8 15;");
        cameraBtn.setOnMouseEntered(e -> cameraBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 12px; -fx-padding: 8 15;"));
        cameraBtn.setOnMouseExited(e -> cameraBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 12px; -fx-padding: 8 15;"));
        cameraBtn.setOnAction(e -> openCamera());

        Button removeBtn = new Button("Delete Profile");
        removeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Styles.ERROR_RED + ";" +
                        "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                        "-fx-font-size: 12px; -fx-padding: 8 15; -fx-cursor: hand;");
        removeBtn.setOnMouseEntered(e -> removeBtn.setStyle(
                "-fx-background-color: " + Styles.ERROR_RED + "; -fx-text-fill: white;" +
                        "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                        "-fx-font-size: 12px; -fx-padding: 8 15; -fx-cursor: hand;"));
        removeBtn.setOnMouseExited(e -> removeBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + Styles.ERROR_RED + ";" +
                        "-fx-border-color: " + Styles.ERROR_RED + "; -fx-border-radius: 25;" +
                        "-fx-font-size: 12px; -fx-padding: 8 15; -fx-cursor: hand;"));
        removeBtn.setOnAction(e -> {
            selectedPhotoPath = "";
            showDefaultPhoto();
            if (messageLabel != null) {
                messageLabel.setText("Photo removed.");
                messageLabel.setStyle("-fx-text-fill: " + Styles.TEXT_LIGHT + "; -fx-font-size: 12px;");
            }
        });

        HBox btnRow = new HBox(10);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.getChildren().addAll(galleryBtn, cameraBtn, removeBtn);

        section.getChildren().addAll(photoStack, btnRow);
        return section;
    }

    private void openCamera() {
        new CameraCapturePage().show(path -> {
            System.out.println("Photo path received: " + path);
            if (path != null && !path.isEmpty()) {
                selectedPhotoPath = path;
                javafx.application.Platform.runLater(() -> {
                    loadPhotoIntoView(path);
                    if (messageLabel != null) {
                        messageLabel.setText("Photo captured!");
                        messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + "; -fx-font-size: 12px;");
                    }
                });
            }
        });
    }

    private void loadPhotoIntoView(String path) {
        try {
            File imgFile = new File(path);
            if (!imgFile.exists()) {
                showDefaultPhoto();
                return;
            }

            Image img = new Image(imgFile.toURI().toString());

            if (!img.isError()) {
                WritableImage squareImg = cropToSquare(img);
                ImagePattern pattern = new ImagePattern(squareImg);
                defaultCircleBg.setFill(pattern);
                defaultPhotoIcon.setVisible(false);
                photoImageView.setVisible(false);
                defaultCircleBg.setVisible(true);
            } else {
                showDefaultPhoto();
            }
        } catch (Exception ex) {
            showDefaultPhoto();
        }
    }

    private WritableImage cropToSquare(Image img) {
        int imgW = (int) img.getWidth();
        int imgH = (int) img.getHeight();
        int size  = Math.min(imgW, imgH);
        int offsetX = (imgW - size) / 2;
        int offsetY = (imgH - size) / 2;
        PixelReader reader = img.getPixelReader();
        return new WritableImage(reader, offsetX, offsetY, size, size);
    }

    private void showDefaultPhoto() {
        defaultCircleBg.setFill(Color.web(Styles.BROWN_DARK));
        defaultPhotoIcon.setVisible(true);
        photoImageView.setVisible(false);
        defaultCircleBg.setVisible(true);
    }

    private void openGallery() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            if (file.length() > 5 * 1024 * 1024) {
                if (messageLabel != null) {
                    messageLabel.setText("File too large! Maximum 5MB.");
                    messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + "; -fx-font-size: 12px;");
                }
                return;
            }
            selectedPhotoPath = file.getAbsolutePath();
            loadPhotoIntoView(selectedPhotoPath);
            if (messageLabel != null) {
                messageLabel.setText("Photo selected: " + file.getName());
                messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + "; -fx-font-size: 12px;");
            }
        }
    }

    private void saveProfile() {
        String newName = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();

        if (newName.isEmpty()) {
            messageLabel.setText("Name cannot be empty!");
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + "; -fx-font-size: 12px;");
            return;
        }

        if (!phone.isEmpty() && !phone.matches("[0-9+\\-\\s]{8,15}")) {
            messageLabel.setText("Phone number is invalid! (8-15 digits)");
            messageLabel.setStyle("-fx-text-fill: " + Styles.ERROR_RED + "; -fx-font-size: 12px;");
            return;
        }

        User currentUser = AuthService.getCurrentUser();

        // Simpan semua data ke Preferences via AuthService
        AuthService.updateProfile(newName, phone, address);

        // Simpan foto jika ada perubahan
        if (selectedPhotoPath != null) {
            if (selectedPhotoPath.isEmpty()) {
                // User menghapus foto
                AuthService.updateUserProfilePhoto(currentUser.getEmail(), "");
                currentUser.setProfilePhotoPath(null);
            } else {
                // User mengganti foto
                AuthService.updateUserProfilePhoto(currentUser.getEmail(), selectedPhotoPath);
                currentUser.setProfilePhotoPath(selectedPhotoPath);
            }
        }

        messageLabel.setText("Profile updated successfully!");
        messageLabel.setStyle("-fx-text-fill: " + Styles.SUCCESS_GREEN + "; -fx-font-size: 12px;");

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        delay.setOnFinished(e -> SceneManager.setScene(new ProfilePage().getScene()));
        delay.play();
    }
}
