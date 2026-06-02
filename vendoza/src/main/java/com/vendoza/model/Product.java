package com.vendoza.model;

public class Product {
    private Long id;           // Ubah dari int ke Long (biar match dengan backend)
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

    // ========== CONSTRUCTOR UNTUK BACKEND (yang akan dipanggil CartService) ==========
    public Product(Long id, String name, Double price, String description, String imageUrl, String category, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price != null ? price : 0.0;
        this.description = description != null ? description : "";
        this.imageUrl = imageUrl != null ? imageUrl : "";
        this.category = category != null ? category : "";
        this.stock = stock != null ? stock : 0;
        this.discountPrice = 0;
        this.isOnSale = false;
        this.rating = 4.0;
        this.reviewCount = 0;
    }

    // Constructor lengkap (dengan field baru)
    public Product(Long id, String name, String category, double price, double discountPrice,
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

    // Constructor lama tetap ada agar tidak breaking change di DataService (dengan int)
    public Product(int id, String name, String category, double price, double discountPrice,
                   String imageUrl, String description, boolean isOnSale, int stock) {
        this((long) id, name, category, price, discountPrice, imageUrl, description, isOnSale, stock,
                4.0, 0);
    }

    // Constructor untuk DataService (int version)
    public Product(int id, String name, String category, double price, double currentPrice,
                   String description, String imageUrl, boolean isOnSale, double rating) {
        this((long) id, name, category, price, currentPrice, imageUrl, description, isOnSale, 100, rating, 0);
    }

    // ========== GETTERS ==========
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getDiscountPrice() { return discountPrice; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public boolean isOnSale() { return isOnSale; }
    public int getStock() { return stock; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }

    // ========== SETTERS (untuk update dari backend) ==========
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; }
    public void setDiscountPrice(double discountPrice) { this.discountPrice = discountPrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setOnSale(boolean onSale) { isOnSale = onSale; }
    public void setStock(int stock) { this.stock = stock; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    // ========== HELPER METHODS ==========
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
}
