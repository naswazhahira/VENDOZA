package com.vendoza.ui;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoginRequiredDialog {

    public static void show(String message) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        // Icon circle dengan background gold
        Circle iconCircle = new Circle(40);
        iconCircle.setFill(Color.web(Styles.GOLD));
        iconCircle.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

        Label iconLabel = new Label("🔐");
        iconLabel.setStyle("-fx-font-size: 40px;");
        iconLabel.setTranslateY(-40);

        // Stack icon
        BorderPane iconPane = new BorderPane();
        iconPane.setCenter(iconCircle);
        iconPane.getChildren().add(iconLabel);

        // Title
        Label titleLabel = new Label("Login Required");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        // Message
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(320);
        messageLabel.setAlignment(Pos.CENTER);

        // Buttons
        Button loginBtn = new Button("Login Now");
        loginBtn.setStyle(Styles.buttonStyle());
        loginBtn.setPrefWidth(140);
        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setStyle(Styles.buttonHoverStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(200), loginBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        loginBtn.setOnMouseExited(e -> {
            loginBtn.setStyle(Styles.buttonStyle());
            ScaleTransition st = new ScaleTransition(Duration.millis(200), loginBtn);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });
        loginBtn.setOnAction(e -> {
            dialogStage.close();
            SceneManager.setScene(new LoginPage().getScene());
        });

        Button cancelBtn = new Button("Maybe Later");
        cancelBtn.setStyle(Styles.outlineButtonStyle());
        cancelBtn.setPrefWidth(140);
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle("-fx-background-color: " + Styles.BROWN_LIGHT + ";" +
                "-fx-text-fill: " + Styles.BROWN_DARK + ";-fx-border-color: " + Styles.BROWN_DARK + ";" +
                "-fx-border-radius: 25;-fx-padding: 8 20;-fx-cursor: hand;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(Styles.outlineButtonStyle()));
        cancelBtn.setOnAction(e -> dialogStage.close());

        HBox buttonBox = new HBox(15, loginBtn, cancelBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Content
        VBox contentBox = new VBox(15, iconPane, titleLabel, messageLabel, buttonBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30, 40, 35, 40));
        contentBox.setStyle("-fx-background-color: " + Styles.WHITE + ";" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 25, 0, 0, 10);");

        // Animasi masuk
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), contentBox);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        scaleIn.play();

        BorderPane root = new BorderPane();
        root.setCenter(contentBox);
        root.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root);
        scene.setFill(null);

        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public static void show() {
        show("You need to login to access this feature.");
    }
}