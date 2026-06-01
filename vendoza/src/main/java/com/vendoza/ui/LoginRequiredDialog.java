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

public class LoginRequiredDialog {

    private static final String BORDER_COLOR = "#C4A484";

    public static void show(String message) {
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

        Label titleLabel = new Label("Login Required");
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

        Button loginBtn = new Button("Login");
        loginBtn.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 30;" +
                        "-fx-cursor: hand;"
        );
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(
                "-fx-background-color: #D4A853;" +
                        "-fx-text-fill: #2C1810;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 30;" +
                        "-fx-cursor: hand;"
        ));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(
                "-fx-background-color: #3E2723;" +
                        "-fx-text-fill: #D4A853;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 30;" +
                        "-fx-cursor: hand;"
        ));
        loginBtn.setOnAction(e -> {
            dialogStage.close();
            SceneManager.setScene(new LoginPage().getScene());
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #8D6E63;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #C4A484;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 25;"
        );
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(
                "-fx-background-color: #EFEBE9;" +
                        "-fx-text-fill: #3E2723;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #C4A484;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 25;"
        ));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #8D6E63;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25;" +
                        "-fx-padding: 10 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-border-color: #C4A484;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 25;"
        ));
        cancelBtn.setOnAction(e -> dialogStage.close());

        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, cancelBtn);

        root.getChildren().addAll(titleLabel, messageLabel, buttonBox);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);

        dialogStage.sizeToScene();   
        dialogStage.centerOnScreen();

        dialogStage.showAndWait();
    }
}
