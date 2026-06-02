package com.vendoza.ui;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

public class CameraCapturePage {

    private volatile boolean running = false;
    private volatile boolean captured = false;
    private BufferedImage capturedImage;
    private ImageView previewView;
    private Label statusLabel;
    private Button captureBtn;
    private Button useBtn;
    private Button retakeBtn;
    private Stage stage;
    private Webcam webcam;

    public void show(Consumer<String> onPhotoCaptured) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Take Profile Photo");

        // Preview
        previewView = new ImageView();
        previewView.setFitWidth(640);
        previewView.setFitHeight(480);
        previewView.setPreserveRatio(true);
        previewView.setStyle("-fx-background-color: black;");

        StackPane previewPane = new StackPane(previewView);
        previewPane.setPrefSize(640, 480);
        previewPane.setStyle("-fx-background-color: #1a1a1a; -fx-background-radius: 10;");

        // Status
        statusLabel = new Label("Initializing camera...");
        statusLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #8D6E63;");

        // Buttons
        captureBtn = styledBtn("📷  Capture", "#4CAF50", "white");
        captureBtn.setDisable(true);
        captureBtn.setOnAction(e -> capture());

        useBtn = styledBtn("✓  Use Photo", "#3E2723", "#D4A853");
        useBtn.setDisable(true);
        useBtn.setOnAction(e -> saveAndClose(onPhotoCaptured));

        retakeBtn = styledBtn("⟳  Retake", "#FF9800", "white");
        retakeBtn.setVisible(false);
        retakeBtn.setOnAction(e -> startCamera());

        Button cancelBtn = styledBtn("✕  Cancel", "#E53935", "white");
        cancelBtn.setOnAction(e -> { stopCamera(); stage.close(); });

        HBox btnRow = new HBox(12, captureBtn, useBtn, retakeBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER);

        VBox root = new VBox(16, previewPane, statusLabel, btnRow);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F0E8DF;");

        stage.setScene(new Scene(root, 700, 600));
        stage.setOnCloseRequest(e -> stopCamera());
        stage.show();

        startCamera();
    }

    private void startCamera() {
        captured = false;
        captureBtn.setDisable(true);
        useBtn.setDisable(true);
        retakeBtn.setVisible(false);
        statusLabel.setText("Opening camera...");

        new Thread(() -> {
            try {
                if (webcam == null) {
                    webcam = Webcam.getDefault();
                }
                if (webcam == null) {
                    Platform.runLater(() -> statusLabel.setText("No camera found on this device."));
                    return;
                }
                if (!webcam.isOpen()) {
                    webcam.open();
                }

                running = true;
                Platform.runLater(() -> {
                    statusLabel.setText("Camera ready. Click Capture.");
                    captureBtn.setDisable(false);
                });

                // Live preview loop
                while (running && !captured) {
                    BufferedImage frame = webcam.getImage();
                    if (frame != null) {
                        WritableImage fxImg = SwingFXUtils.toFXImage(frame, null);
                        Platform.runLater(() -> previewView.setImage(fxImg));
                    }
                    Thread.sleep(50); // ~20 FPS
                }
            } catch (Exception e) {
                Platform.runLater(() -> statusLabel.setText("Camera error: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    private void capture() {
        if (webcam == null || !webcam.isOpen()) return;
        capturedImage = webcam.getImage();
        if (capturedImage != null) {
            captured = true;
            running  = false;
            WritableImage fxImg = SwingFXUtils.toFXImage(capturedImage, null);
            Platform.runLater(() -> {
                previewView.setImage(fxImg);
                captureBtn.setDisable(true);
                useBtn.setDisable(false);
                retakeBtn.setVisible(true);
                statusLabel.setText("Photo captured! Click Use to apply.");
            });
            stopCamera();
        }
    }

    private void saveAndClose(Consumer<String> callback) {
        if (capturedImage == null) return;
        try {
            File tmp = File.createTempFile("vendoza_photo_", ".png");
            ImageIO.write(capturedImage, "png", tmp);
            String path = tmp.getAbsolutePath();
            stopCamera();
            stage.close();
            Platform.runLater(() -> callback.accept(path));
        } catch (IOException e) {
            statusLabel.setText("Failed to save: " + e.getMessage());
        }
    }

    private void stopCamera() {
        running = false;
        new Thread(() -> {
            try {
                if (webcam != null && webcam.isOpen()) {
                    webcam.close();
                    webcam = null;
                }
            } catch (Exception ignored) {}
        }).start();
    }

    private Button styledBtn(String text, String bg, String fg) {
        Button btn = new Button(text);
        String base  = "-fx-background-color: " + bg + "; -fx-text-fill: " + fg + "; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 25; -fx-padding: 10 24; -fx-cursor: hand;";
        btn.setStyle(base);
        return btn;
    }
}
