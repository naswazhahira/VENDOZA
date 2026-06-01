package com.vendoza.ui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class CameraCapturePage {

    private Webcam webcam;
    private WebcamPanel webcamPanel;
    private ImageView previewView;
    private BufferedImage capturedImage;
    private boolean captured = false;

    public void show(Consumer<String> onPhotoCaptured) {
        Stage cameraStage = new Stage();
        cameraStage.setTitle("Take Profile Photo");
        cameraStage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #E8DCD0;");

        Label titleLabel = new Label("Take Profile Photo");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Styles.BROWN_DARK + ";");

        // SwingNode untuk embed WebcamPanel ke JavaFX
        SwingNode swingNode = new SwingNode();

        StackPane cameraPane = new StackPane(swingNode);
        cameraPane.setStyle("-fx-background-color: black; -fx-background-radius: 10;");
        cameraPane.setPrefSize(480, 360);
        cameraPane.setMaxSize(480, 360);

        // Preview setelah capture
        previewView = new ImageView();
        previewView.setFitWidth(480);
        previewView.setFitHeight(360);
        previewView.setPreserveRatio(true);
        previewView.setVisible(false);

        StackPane displayPane = new StackPane(cameraPane, previewView);
        displayPane.setPrefSize(480, 360);
        displayPane.setMaxSize(480, 360);

        Label statusLabel = new Label("Opening camera...");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_LIGHT + ";");

        // Tombol Capture / Retake
        Button captureBtn = new Button("Capture");
        captureBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;");
        captureBtn.setOnMouseEntered(e -> captureBtn.setStyle(Styles.buttonHoverStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;"));
        captureBtn.setOnMouseExited(e -> captureBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;"));
        captureBtn.setDisable(true);
        captureBtn.setOnAction(e -> {
            if (!captured) {
                capturedImage = webcam.getImage();
                if (capturedImage != null) {
                    Image fxImage = SwingFXUtils.toFXImage(capturedImage, null);
                    previewView.setImage(fxImage);
                    previewView.setVisible(true);
                    cameraPane.setVisible(false);
                    captured = true;
                    captureBtn.setText("Retake");
                    statusLabel.setText("Photo captured! Click 'Use This Photo' to apply.");
                }
            } else {
                // Retake
                previewView.setVisible(false);
                cameraPane.setVisible(true);
                captured = false;
                capturedImage = null;
                captureBtn.setText("Capture");
                statusLabel.setText("Camera ready.");
            }
        });

        // Tombol Use This Photo
        Button useBtn = new Button("Use This Photo");
        useBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;");
        useBtn.setOnMouseEntered(e -> useBtn.setStyle(Styles.buttonHoverStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;"));
        useBtn.setOnMouseExited(e -> useBtn.setStyle(Styles.buttonStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;"));
        useBtn.setOnAction(e -> {
            if (capturedImage != null) {
                try {
                    File tempFile = File.createTempFile("vendoza_profile_", ".jpg");
                    ImageIO.write(capturedImage, "jpg", tempFile);
                    String savedPath = tempFile.getAbsolutePath();
                    closeCamera(cameraStage);
                    Platform.runLater(() -> onPhotoCaptured.accept(savedPath));
                } catch (IOException ex) {
                    statusLabel.setText("Failed to save photo. Try again.");
                    ex.printStackTrace();
                }
            } else {
                statusLabel.setText("Please capture a photo first!");
            }
        });

        // Tombol Cancel
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;");
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(
                "-fx-background-color: " + Styles.BROWN_LIGHT + "; -fx-text-fill: " + Styles.BROWN_DARK + ";" +
                        "-fx-border-color: " + Styles.BROWN_DARK + "; -fx-border-radius: 25; -fx-padding: 10 25; -fx-cursor: hand;"));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(Styles.outlineButtonStyle() + " -fx-font-size: 13px; -fx-padding: 10 25;"));
        cancelBtn.setOnAction(e -> closeCamera(cameraStage));

        HBox btnRow = new HBox(12);
        btnRow.setAlignment(Pos.CENTER);
        btnRow.getChildren().addAll(captureBtn, useBtn, cancelBtn);

        root.getChildren().addAll(titleLabel, displayPane, statusLabel, btnRow);

        Scene scene = new Scene(root, 560, 520);
        cameraStage.setScene(scene);
        cameraStage.setOnCloseRequest(e -> closeCamera(cameraStage));
        cameraStage.show();

        // Inisialisasi kamera di background thread
        new Thread(() -> {
            try {
                webcam = Webcam.getDefault();
                if (webcam != null) {
                    webcam.open();
                    webcamPanel = new WebcamPanel(webcam);
                    webcamPanel.setFPSDisplayed(false);
                    webcamPanel.setDisplayDebugInfo(false);
                    webcamPanel.setImageSizeDisplayed(false);
                    webcamPanel.setMirrored(true);

                    SwingUtilities.invokeLater(() -> swingNode.setContent(webcamPanel));

                    Platform.runLater(() -> {
                        statusLabel.setText("Camera ready. Click 'Capture' to take a photo.");
                        captureBtn.setDisable(false);
                    });
                } else {
                    Platform.runLater(() -> {
                        statusLabel.setText("No camera detected on this device.");
                        Label noCamLabel = new Label("No camera detected");
                        noCamLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                        cameraPane.getChildren().add(noCamLabel);
                    });
                }
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    statusLabel.setText("Failed to open camera: " + ex.getMessage());
                    ex.printStackTrace();
                });
            }
        }).start();
    }

    private void closeCamera(Stage stage) {
        new Thread(() -> {
            try {
                if (webcamPanel != null) webcamPanel.stop();
                if (webcam != null && webcam.isOpen()) webcam.close();
            } catch (Exception ignored) {}
            Platform.runLater(stage::close);
        }).start();
    }
}