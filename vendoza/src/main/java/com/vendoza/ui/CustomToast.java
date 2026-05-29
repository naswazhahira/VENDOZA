package com.vendoza.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class CustomToast {

    public static void showSuccessToast(Window owner, String message) {
        Popup popup = new Popup();

        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: rgba(92, 64, 51, 0.8);" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 15 25 15 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 15, 0, 0, 5);" +
                "-fx-border-color: rgba(218, 165, 32, 0.5);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 20;");

        Label iconLabel = new Label("✓");
        iconLabel.setStyle("-fx-text-fill: " + Styles.GOLD + ";" +
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: " + Styles.WHITE + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 500;");

        HBox contentBox = new HBox(12);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(iconLabel, messageLabel);

        container.getChildren().add(contentBox);
        container.setAlignment(Pos.CENTER);

        popup.getContent().add(container);

        container.setOpacity(0);
        container.setScaleX(0.9);
        container.setScaleY(0.9);

        Timeline entranceAnim = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    container.setOpacity(0.5);
                    container.setScaleX(0.95);
                    container.setScaleY(0.95);
                }),
                new KeyFrame(Duration.millis(200), e -> {
                    container.setOpacity(1);
                    container.setScaleX(1);
                    container.setScaleY(1);
                })
        );
        entranceAnim.play();

        popup.show(owner);

        Timeline closeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> {
                    Timeline exitAnim = new Timeline(
                            new KeyFrame(Duration.millis(150), ev -> {
                                container.setOpacity(0);
                                container.setScaleX(0.9);
                                container.setScaleY(0.9);
                            }),
                            new KeyFrame(Duration.millis(250), ev -> popup.hide())
                    );
                    exitAnim.play();
                })
        );
        closeTimeline.play();

        updatePopupPositionBottomRight(popup, owner);

        owner.xProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.yProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.widthProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.heightProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
    }

    public static void showSuccessCartToast(Window owner, String productName) {
        Popup popup = new Popup();

        VBox container = new VBox(8);
        container.setStyle("-fx-background-color: rgba(92, 64, 51, 0.8);" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 12 25 12 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 15, 0, 0, 5);" +
                "-fx-border-color: rgba(218, 165, 32, 0.5);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 20;");

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label cartIcon = new Label("🛒");
        cartIcon.setStyle("-fx-text-fill: " + Styles.GOLD + ";" +
                "-fx-font-size: 22px;");

        Label successLabel = new Label("Added to Cart!");
        successLabel.setStyle("-fx-text-fill: " + Styles.GOLD + ";" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;");

        headerBox.getChildren().addAll(cartIcon, successLabel);

        Label productLabel = new Label(productName);
        productLabel.setStyle("-fx-text-fill: " + Styles.WHITE + ";" +
                "-fx-font-size: 12px;" +
                "-fx-wrap-text: true;" +
                "-fx-max-width: 250px;" +
                "-fx-font-weight: 500;");
        productLabel.setWrapText(true);

        HBox footerBox = new HBox(6);
        footerBox.setAlignment(Pos.CENTER_LEFT);

        Label checkIcon = new Label("✓");
        checkIcon.setStyle("-fx-text-fill: " + Styles.GOLD + ";" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;");

        Label footerLabel = new Label("Item added to your cart");
        footerLabel.setStyle("-fx-text-fill: " + Styles.WHITE + ";" +
                "-fx-font-size: 11px;" +
                "-fx-opacity: 0.8;");

        footerBox.getChildren().addAll(checkIcon, footerLabel);

        container.getChildren().addAll(headerBox, productLabel, footerBox);
        container.setAlignment(Pos.CENTER_LEFT);

        popup.getContent().add(container);

        container.setOpacity(0);
        container.setTranslateX(50);

        Timeline entranceAnim = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    container.setOpacity(0.5);
                    container.setTranslateX(25);
                }),
                new KeyFrame(Duration.millis(250), e -> {
                    container.setOpacity(1);
                    container.setTranslateX(0);
                })
        );
        entranceAnim.play();

        popup.show(owner);

        Timeline closeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2.5), e -> {
                    Timeline exitAnim = new Timeline(
                            new KeyFrame(Duration.millis(150), ev -> {
                                container.setOpacity(0);
                                container.setTranslateX(50);
                            }),
                            new KeyFrame(Duration.millis(250), ev -> popup.hide())
                    );
                    exitAnim.play();
                })
        );
        closeTimeline.play();

        updatePopupPositionBottomRight(popup, owner);

        owner.xProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.yProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.widthProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.heightProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
    }

    // Warning Toast untuk empty search
    public static void showWarningToast(Window owner, String message) {
        Popup popup = new Popup();

        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: rgba(92, 64, 51, 0.85);" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 15 25 15 25;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 8);" +
                "-fx-border-color: rgba(218, 165, 32, 0.6);" +
                "-fx-border-width: 1.5;" +
                "-fx-border-radius: 20;");

        HBox contentBox = new HBox(12);
        contentBox.setAlignment(Pos.CENTER);

        Label warningIcon = new Label("⚠️");
        warningIcon.setStyle("-fx-font-size: 28px;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: " + Styles.WHITE + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: 500;");

        contentBox.getChildren().addAll(warningIcon, messageLabel);

        container.getChildren().add(contentBox);
        container.setAlignment(Pos.CENTER);

        popup.getContent().add(container);

        container.setOpacity(0);
        container.setScaleX(0.85);
        container.setScaleY(0.85);

        Timeline entranceAnim = new Timeline(
                new KeyFrame(Duration.millis(80), e -> container.setOpacity(0.3)),
                new KeyFrame(Duration.millis(160), e -> {
                    container.setOpacity(0.6);
                    container.setScaleX(0.95);
                    container.setScaleY(0.95);
                }),
                new KeyFrame(Duration.millis(250), e -> {
                    container.setOpacity(1);
                    container.setScaleX(1);
                    container.setScaleY(1);
                })
        );
        entranceAnim.play();

        popup.show(owner);

        Timeline closeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> {
                    Timeline exitAnim = new Timeline(
                            new KeyFrame(Duration.millis(150), ev -> {
                                container.setOpacity(0);
                                container.setScaleX(0.9);
                                container.setScaleY(0.9);
                            }),
                            new KeyFrame(Duration.millis(250), ev -> popup.hide())
                    );
                    exitAnim.play();
                })
        );
        closeTimeline.play();

        updatePopupPositionBottomRight(popup, owner);

        owner.xProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.yProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.widthProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
        owner.heightProperty().addListener((obs, old, newVal) -> updatePopupPositionBottomRight(popup, owner));
    }

    private static void updatePopupPositionBottomRight(Popup popup, Window owner) {
        if (popup.isShowing()) {
            double popupWidth = 320;
            double popupHeight = 100;
            popup.setX(owner.getX() + owner.getWidth() - popupWidth - 20);
            popup.setY(owner.getY() + owner.getHeight() - popupHeight - 80);
        }
    }
}