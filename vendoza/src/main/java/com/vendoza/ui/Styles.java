package com.vendoza.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Styles {

    // Warna Aesthetic Coklat
    public static final String BROWN_DARK = "#5D4037";
    public static final String BROWN_MEDIUM = "#8D6E63";
    public static final String BROWN_LIGHT = "#D7CCC8";
    public static final String BROWN_PALE = "#F5F0EB";  // Diubah dari #EFEBE9 ke #F5F0EB (cream)
    public static final String BROWN_BG = "#E8DCD0";   // TAMBAHKAN: warna background coklat yang lebih jelas
    public static final String GOLD = "#D4AF37";
    public static final String WHITE = "#FFFFFF";
    public static final String TEXT_DARK = "#3E2723";
    public static final String TEXT_LIGHT = "#8D6E63";
    public static final String ERROR_RED = "#E57373";
    public static final String SUCCESS_GREEN = "#81C784";

    // ===== NAVBAR STYLES =====
    public static String navBarStyle() {
        return "-fx-background-color: " + WHITE + ";" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);";
    }

    // ===== BACKGROUND STYLE =====
    public static String bgStyle() {
        return "-fx-background-color: " + BROWN_BG + ";";  // Gunakan warna yang lebih jelas
    }

    // ===== BUTTON STYLES =====
    public static String buttonStyle() {
        return "-fx-background-color: " + BROWN_DARK + ";" +
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 10 20;" +
                "-fx-cursor: hand;";
    }

    public static String buttonHoverStyle() {
        return "-fx-background-color: " + BROWN_MEDIUM + ";" +
                "-fx-text-fill: " + WHITE + ";" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 10 20;";
    }

    public static String outlineButtonStyle() {
        return "-fx-background-color: transparent; -fx-text-fill: " + BROWN_DARK + ";" +
                "-fx-border-color: " + BROWN_DARK + "; -fx-border-radius: 25;" +
                "-fx-padding: 8 20; -fx-cursor: hand;";
    }

    public static String smallButtonStyle(String type) {
        if (type.equals("cart")) {
            return "-fx-background-color: " + BROWN_LIGHT + "; -fx-text-fill: " + TEXT_DARK + ";" +
                    "-fx-font-size: 12px; -fx-background-radius: 20; -fx-padding: 5 15; -fx-cursor: hand;";
        } else {
            return "-fx-background-color: " + GOLD + "; -fx-text-fill: " + BROWN_DARK + ";" +
                    "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 5 15; -fx-cursor: hand;";
        }
    }

    // ===== TEXT FIELD STYLES =====
    public static String textFieldStyle() {
        return "-fx-background-color: " + BROWN_PALE + ";" +
                "-fx-text-fill: " + TEXT_DARK + ";" +
                "-fx-prompt-text-fill: " + TEXT_LIGHT + ";" +
                "-fx-background-radius: 15;" +
                "-fx-padding: 12 15;" +
                "-fx-border-color: " + BROWN_LIGHT + ";" +
                "-fx-border-radius: 15;";
    }

    // ===== CARD STYLES =====
    public static String cardStyle() {
        return "-fx-background-color: " + WHITE + ";" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);" +
                "-fx-padding: 15;";
    }

    public static String categoryCardStyle() {
        return "-fx-background-color: " + WHITE + ";" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 3);" +
                "-fx-padding: 20; -fx-cursor: hand;";
    }

    // ===== TITLE STYLES =====
    public static String pageTitleStyle() {
        return "-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: " + BROWN_DARK + ";";
    }

    public static String sectionTitleStyle() {
        return "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + BROWN_DARK + ";";
    }

    // ===== UTILITY =====
    public static String formatPrice(double price) {
        return String.format("%,.0f", price).replace(",", ".");
    }
}
