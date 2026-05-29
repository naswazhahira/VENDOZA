package com.vendoza;

import com.vendoza.ui.LoginPage;
import com.vendoza.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.init(primaryStage);
        SceneManager.setScene(new LoginPage().getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}