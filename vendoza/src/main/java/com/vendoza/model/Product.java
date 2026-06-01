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
    private double rating;
    private int reviewCount;

    // Constructor lengkap (dengan field baru)
    public Product(int id, String name, String category, double price, double discountPrice,
                   String imageUrl, String description, boolean isOnSale, int stock,
                   double rating, int reviewCount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.discountPrice = discountPrice;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isOnSale = isOnSale;
        this.stock = stock;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    // Constructor lama tetap ada agar tidak breaking change di DataService
    public Product(int id, String name, String category, double price, double discountPrice,
                   String imageUrl, String description, boolean isOnSale, int stock) {
        this(id, name, category, price, discountPrice, imageUrl, description, isOnSale, stock,
                4.0, 0);
    }

    // ── Getters lama ──────────────────────────────────────────────────────────
    public int getId()             { return id; }
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public double getPrice()       { return price; }
    public double getDiscountPrice() { return discountPrice; }
    public String getImageUrl()    { return imageUrl; }
    public String getDescription() { return description; }
    public boolean isOnSale()      { return isOnSale; }
    public int getStock()          { return stock; }
    public double getRating()      { return rating; }
    public int getReviewCount()    { return reviewCount; }

    public double getCurrentPrice() {
        return discountPrice > 0 ? discountPrice : price;
    }

    public double getDiscountPercent() {
        if (discountPrice > 0 && price > 0) {
            return Math.round(((price - discountPrice) / price) * 100);
        }
        return 0;
    }

    public String getStockLabel() {
        if (stock <= 0)  return "Habis";
        if (stock <= 10) return "Stok Terbatas (" + stock + ")";
        return "Tersedia (" + stock + ")";
    }

    public String getStockColor() {
        if (stock <= 0)  return "#E53935";
        if (stock <= 10) return "#F57C00";
        return "#388E3C";
    }

   public String getStarString() {
        int full  = (int) rating;
        int half  = (rating - full >= 0.5) ? 1 : 0;
        int empty = 5 - full - half;
        return "★".repeat(full) + (half == 1 ? "⯨" : "") + "☆".repeat(empty);
    }

    public boolean isAvailable() {
        return stock > 0;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', category='" + category +
                "', price=" + price + ", rating=" + rating + ", stock=" + stock + "}";
    }

    public Product(int id, String name, String category, double price, double currentPrice, String description, String imageUrl, boolean isOnSale, double rating) {
    }
}
