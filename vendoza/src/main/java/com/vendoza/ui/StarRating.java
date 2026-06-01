package com.vendoza.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class StarRating {

    public static HBox createStarRating(double rating) {
        HBox starBox = new HBox(2);
        starBox.setAlignment(Pos.CENTER_LEFT);

        int fullStars = (int) rating;
        double fractional = rating - fullStars;
        boolean hasHalfStar = fractional >= 0.25 && fractional < 0.75;

        for (int i = 0; i < fullStars; i++) {
            Text star = new Text("★");
            star.setStyle("-fx-fill: #D4A853; -fx-font-size: 12px;");
            starBox.getChildren().add(star);
        }

        if (hasHalfStar && fullStars < 5) {
            TextFlow halfStarContainer = new TextFlow();
            Text leftHalf = new Text("★");
            Text rightHalf = new Text("☆");

            leftHalf.setStyle("-fx-fill: #D4A853; -fx-font-size: 12px;");
            rightHalf.setStyle("-fx-fill: #E0E0E0; -fx-font-size: 12px;");

            leftHalf.setTranslateX(0);
            leftHalf.setTranslateY(0);
            rightHalf.setTranslateX(-8);
            rightHalf.setTranslateY(0);

            halfStarContainer.getChildren().addAll(leftHalf, rightHalf);
            halfStarContainer.setPrefWidth(8);
            starBox.getChildren().add(halfStarContainer);
        }
        else if (fractional >= 0.75 && fullStars < 5) {
            Text star = new Text("★");
            star.setStyle("-fx-fill: #D4A853; -fx-font-size: 12px;");
            starBox.getChildren().add(star);
            fullStars++;
        }

        int emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
        for (int i = 0; i < emptyStars; i++) {
            Text star = new Text("☆");
            star.setStyle("-fx-fill: #E0E0E0; -fx-font-size: 12px;");
            starBox.getChildren().add(star);
        }

        return starBox;
    }
}