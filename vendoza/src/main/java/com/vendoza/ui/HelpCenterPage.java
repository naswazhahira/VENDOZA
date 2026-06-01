package com.vendoza.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomDialog {

    private static final String BORDER_COLOR = "#C4A484";

    public static void showSuccess(String title, String message) {
        showDialog(title, message, "✓", "#4CAF50");
    }

    public static void showError(String title, String message) {
        showDialog(title, message, "⚠", "#E53935");
    }

    public static void showInfo(String title, String message) {
        showDialog(title, message, "ℹ", "#D4A853");
    }

    private static void showDialog(String title, String message, String icon, String iconColor) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(16);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-color: " + BORDER_COLOR + ";" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 20;" +
                        "-fx-padding: 30 40 30 40;"
        );
        root.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.15)));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle(
                "-fx-font-size: 48px;" +
                        "-fx-text-fill: " + iconColor + ";"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-family: 'Georgia';"
        );

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #5D4037;" +
                        "-fx-wrap-text: true;"
        );
        messageLabel.setMaxWidth(300);
        messageLabel.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK");
        okButton.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 35;" +
                        "-fx-cursor: hand;"
        );
        okButton.setOnMouseEntered(e -> okButton.setStyle(
                "-fx-background-color: #D4A853;" +
                        "-fx-text-fill: #2C1810;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 35;" +
                        "-fx-cursor: hand;"
        ));
        okButton.setOnMouseExited(e -> okButton.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 35;" +
                        "-fx-cursor: hand;"
        ));
        okButton.setOnAction(e -> dialogStage.close());

        root.getChildren().addAll(iconLabel, titleLabel, messageLabel, okButton);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);

        dialogStage.sizeToScene();
        dialogStage.centerOnScreen();

        dialogStage.showAndWait();
    }
}
