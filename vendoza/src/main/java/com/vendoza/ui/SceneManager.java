package com.vendoza.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static Scene currentScene;

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Vendoza - Fashion Aesthetic");
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(700);
    }

    public static void setScene(Scene scene) {
        currentScene = scene;
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
