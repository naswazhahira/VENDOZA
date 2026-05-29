package com.vendoza.model;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private double discountPrice;
    private String imageUrl;
    private String description;
    private boolean isOnSale;
    private int stock;

    public Product(int id, String name, String category, double price, double discountPrice,
                   String imageUrl, String description, boolean isOnSale, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.discountPrice = discountPrice;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isOnSale = isOnSale;
        this.stock = stock;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getDiscountPrice() { return discountPrice; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public boolean isOnSale() { return isOnSale; }
    public int getStock() { return stock; }

    // Method untuk mendapatkan harga yang ditampilkan
    public double getCurrentPrice() {
        return discountPrice > 0 ? discountPrice : price;
    }

    public double getDiscountPercent() {
        if (discountPrice > 0 && price > 0) {
            return Math.round(((price - discountPrice) / price) * 100);
        }
        return 0;
    }
}
