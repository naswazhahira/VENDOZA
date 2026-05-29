package com.vendoza.service;

import com.vendoza.model.Product;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private static List<Product> products = new ArrayList<>();

    static {

        // On Sale Products (Diskon)
        products.add(new Product(1, "Oversized Brown Blazer", "Women", 899000, 629000,
                "🟤", "Elegant oversized blazer in warm brown tone, perfect for chic office look", true, 25));

        products.add(new Product(2, "Cream Linen Shirt", "Women", 450000, 315000,
                "⚪", "Lightweight linen shirt with premium quality, aesthetic loose fit", true, 30));

        products.add(new Product(3, "Cargo Pants Khaki", "Men", 599000, 419000,
                "🟤", "Trendy cargo pants with multiple pockets, streetwear style", true, 20));

        products.add(new Product(4, "Leather Tote Bag", "Accessories", 1250000, 875000,
                "👜", "Premium leather tote bag in caramel color, spacious design", true, 10));

        // Regular Products
        products.add(new Product(5, "Wide Leg Pants", "Women", 399000, 0,
                "👖", "High-waist wide leg pants with belt, elegant and comfortable", false, 40));

        products.add(new Product(6, "Vintage Denim Jacket", "Men", 799000, 0,
                "🧥", "Classic vintage washed denim jacket, timeless piece", false, 15));

        products.add(new Product(7, "Silk Scarf", "Accessories", 250000, 0,
                "🧣", "Luxury silk scarf with bohemian pattern", false, 50));

        products.add(new Product(8, "Cropped Knit Sweater", "Women", 499000, 0,
                "🧶", "Soft knit sweater cropped length, ribbed texture", false, 35));

        products.add(new Product(9, "Leather Sneakers", "Men", 1250000, 0,
                "👟", "Premium leather sneakers with cushioned sole", false, 20));

        products.add(new Product(10, "Straw Beach Hat", "Accessories", 199000, 0,
                "🧢", "Handwoven straw hat, perfect for summer vibes", false, 30));
    }

    public static List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public static List<Product> getOnSaleProducts() {
        List<Product> onSale = new ArrayList<>();
        for (Product p : products) {
            if (p.isOnSale()) onSale.add(p);
        }
        return onSale;
    }

    public static List<Product> getRecommendedProducts() {
        // Rekomendasi 6 produk random
        return new ArrayList<>(products.subList(0, Math.min(6, products.size())));
    }

    public static Product getProductById(int id) {
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public static List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(lowerKeyword) ||
                    p.getCategory().toLowerCase().contains(lowerKeyword)) {
                results.add(p);
            }
        }
        return results;
    }
}