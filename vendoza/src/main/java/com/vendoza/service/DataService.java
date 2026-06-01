package com.vendoza.service;

import com.vendoza.model.Order;
import com.vendoza.model.Product;
import com.vendoza.model.User;
import java.util.ArrayList;
import java.util.List;

public class DataService {

    private static List<Product> products = new ArrayList<>();
    private static List<Order> orders = new ArrayList<>();

    static {
        products.add(new Product(1, "Oversized Brown Blazer", "Women", 899000, 629000,
                "/images/oversized-brown-blazer.jpg",
                "Elegant oversized blazer in warm brown tone, perfect for chic office look",
                true, 25, 4.7, 312));

        products.add(new Product(2, "Cream Linen Shirt", "Men", 450000, 315000,
                "/images/cream-linen-shirt.png",
                "Lightweight linen shirt with premium quality, aesthetic loose fit",
                true, 30, 4.5, 210));

        products.add(new Product(3, "Cargo Pants Khaki", "Men", 599000, 419000,
                "/images/khaki-cargo-pants.png",
                "Trendy cargo pants with multiple pockets, streetwear style",
                true, 20, 4.3, 180));

        products.add(new Product(4, "Leather Tote Bag", "Accessories", 1250000, 875000,
                "/images/leather-tote-bag.png",
                "Premium leather tote bag in caramel color, spacious design",
                true, 10, 4.8, 95));

        products.add(new Product(5, "Wide Leg Pants", "Men", 399000, 0,
                "/images/wide-leg-pants.png",
                "High-waist wide leg pants with belt, elegant and comfortable",
                false, 40, 4.2, 150));

        products.add(new Product(6, "Vintage Denim Jacket", "Women", 799000, 0,
                "/images/vintage-denim-jacket.png",
                "Classic vintage washed denim jacket, timeless piece",
                false, 15, 4.6, 220));

        products.add(new Product(7, "Silk Scarf", "Accessories", 250000, 0,
                "/images/silk-scraf.png",
                "Luxury silk scarf with bohemian pattern",
                false, 50, 4.4, 88));

        products.add(new Product(8, "Cropped Knit Sweater", "Women", 499000, 0,
                "/images/cropped-knit-sweater.png",
                "Soft knit sweater cropped length, ribbed texture",
                false, 35, 4.5, 175));

        products.add(new Product(9, "Leather Sneakers", "Footwear", 1250000, 0,
                "/images/leather-sneakers.png",
                "Premium leather sneakers with cushioned sole",
                false, 20, 4.7, 300));

        products.add(new Product(10, "Straw Beach Hat", "Accessories", 199000, 0,
                "/images/straw-beach-hat.png",
                "Handwoven straw hat, perfect for summer vibes",
                false, 30, 4.3, 60));
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
        return new ArrayList<>(products);
    }

    public static Product getProductById(int id) {
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public static List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(lower) ||
                    p.getCategory().toLowerCase().contains(lower)) {
                results.add(p);
            }
        }
        return results;
    }

    public static List<Product> getProductsByCategory(String category) {
        return getAllProducts().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(java.util.stream.Collectors.toList());
    }

    public static void addProduct(String name, String category, double price, double discount,
                                  String image, String desc, boolean onSale, int stock,
                                  String brand, String material) {
        int newId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        products.add(new Product(newId, name, category, price, discount,
                image, desc, onSale, stock, 4.0, 0));
    }

    public static void updateProduct(int id, String name, String category, double price,
                                     double discount, String image, String desc,
                                     boolean onSale, int stock) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                Product old = products.get(i);
                products.set(i, new Product(id, name, category, price, discount,
                        image, desc, onSale, stock,
                        old.getRating(), old.getReviewCount()));
                break;
            }
        }
    }

    public static void deleteProduct(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    public static List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static void updateOrderStatus(int orderId, String status) {
        for (Order o : orders) {
            if (o.getId() == orderId) {
                o.setStatus(status);
                break;
            }
        }
    }
    
    public static List<User> getAllUsers() {
        return AuthService.getAllUsers();
    }
}
