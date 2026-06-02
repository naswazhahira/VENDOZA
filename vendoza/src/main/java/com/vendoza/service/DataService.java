package com.vendoza.service;

import com.vendoza.model.Order;
import com.vendoza.model.Product;
import com.vendoza.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DataService {

    private static final String BASE_URL = "http://localhost:9191/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    // Cache untuk produk (gabungan statis + dinamis)
    private static Map<Long, Product> productMap = new ConcurrentHashMap<>();
    private static List<Product> cachedProducts = new ArrayList<>();
    private static boolean staticProductsLoaded = false;

    // ID untuk produk statis (mulai dari 1000 agar tidak bentrok dengan dinamis)
    private static long nextStaticId = 1000;

    // ========== LOAD 12 PRODUK STATIS (TETAP ADA) ==========
    private static void loadStaticProducts() {
        if (staticProductsLoaded) return;

        List<Product> staticProducts = new ArrayList<>();

        staticProducts.add(new Product(nextStaticId++, "Oversized Brown Blazer", "Women", 899000, 629000,
                "/images/oversized-brown-blazer.jpg",
                "Elegant oversized blazer in warm brown tone, perfect for chic office look",
                true, 25, 4.7, 312));

        staticProducts.add(new Product(nextStaticId++, "Cream Linen Shirt", "Men", 450000, 315000,
                "/images/cream-linen-shirt.png",
                "Lightweight linen shirt with premium quality, aesthetic loose fit",
                true, 30, 4.5, 210));

        staticProducts.add(new Product(nextStaticId++, "Cargo Pants Khaki", "Men", 599000, 419000,
                "/images/khaki-cargo-pants.png",
                "Trendy cargo pants with multiple pockets, streetwear style",
                true, 20, 4.3, 180));

        staticProducts.add(new Product(nextStaticId++, "Leather Tote Bag", "Accessories", 1250000, 875000,
                "/images/leather-tote-bag.png",
                "Premium leather tote bag in caramel color, spacious design",
                true, 10, 4.8, 95));

        staticProducts.add(new Product(nextStaticId++, "Wide Leg Pants", "Men", 399000, 0,
                "/images/wide-leg-pants.png",
                "High-waist wide leg pants with belt, elegant and comfortable",
                false, 40, 4.2, 150));

        staticProducts.add(new Product(nextStaticId++, "Vintage Denim Jacket", "Women", 799000, 0,
                "/images/vintage-denim-jacket.png",
                "Classic vintage washed denim jacket, timeless piece",
                false, 15, 4.6, 220));

        staticProducts.add(new Product(nextStaticId++, "Silk Scarf", "Accessories", 250000, 0,
                "/images/silk-scraf.png",
                "Luxury silk scarf with bohemian pattern",
                false, 50, 4.4, 88));

        staticProducts.add(new Product(nextStaticId++, "Cropped Knit Sweater", "Women", 499000, 0,
                "/images/cropped-knit-sweater.png",
                "Soft knit sweater cropped length, ribbed texture",
                false, 35, 4.5, 175));

        staticProducts.add(new Product(nextStaticId++, "Leather Sneakers", "Footwear", 1250000, 0,
                "/images/leather-sneakers.png",
                "Premium leather sneakers with cushioned sole",
                false, 20, 4.7, 300));

        staticProducts.add(new Product(nextStaticId++, "Straw Beach Hat", "Accessories", 199000, 0,
                "/images/straw-beach-hat.png",
                "Handwoven straw hat, perfect for summer vibes",
                false, 30, 4.3, 60));

        staticProducts.add(new Product(nextStaticId++, "Butterfly Necklace", "Jewelry", 289000, 0,
                "/images/butterfly-necklace.png",
                "An elegant butterfly necklace symbolizing grace and freedom.",
                false, 60, 4.8, 90));

        staticProducts.add(new Product(nextStaticId++, "Bracelet", "Jewelry", 199000, 0,
                "/images/bracelet.png",
                "A delicate bracelet that adds a touch of charm and elegance to your wrist.",
                false, 20, 4.2, 30));

        for (Product p : staticProducts) {
            productMap.put(p.getId(), p);
        }

        staticProductsLoaded = true;
    }

    // ========== SYNC PRODUK DINAMIS DARI BACKEND ==========
    private static void syncDynamicProductsFromBackend() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/data/products"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<ProductFromBackend> backendProducts = gson.fromJson(response.body(),
                        new TypeToken<List<ProductFromBackend>>(){}.getType());

                for (ProductFromBackend bp : backendProducts) {
                    // Hanya tambahkan jika belum ada di map (hindari duplikasi)
                    if (!productMap.containsKey(bp.id)) {
                        Product product = new Product(
                                bp.id,
                                bp.name,
                                bp.category != null ? bp.category : "Uncategorized",
                                bp.price != null ? bp.price : 0.0,
                                0,
                                bp.imageUrl != null ? bp.imageUrl : "/images/default.jpg",
                                bp.description != null ? bp.description : "",
                                false,
                                bp.stock != null ? bp.stock : 0,
                                4.0,
                                0
                        );
                        productMap.put(bp.id, product);
                    } else {
                        // Update produk yang sudah ada (misal stok berubah)
                        Product existing = productMap.get(bp.id);
                        if (existing != null && existing.getId() < 1000) {
                            existing.setName(bp.name);
                            existing.setPrice(bp.price != null ? bp.price : 0.0);
                            existing.setStock(bp.stock != null ? bp.stock : 0);
                            existing.setDescription(bp.description != null ? bp.description : "");
                            existing.setImageUrl(bp.imageUrl != null ? bp.imageUrl : "");
                            existing.setCategory(bp.category != null ? bp.category : "");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== REFRESH SEMUA PRODUK ==========
    private static void refreshAllProducts() {
        loadStaticProducts();           // Load 12 produk statis
        syncDynamicProductsFromBackend(); // Load produk dinamis dari backend
        cachedProducts = new ArrayList<>(productMap.values());
    }

    // ========== PUBLIC METHODS ==========

    public static List<Product> getAllProducts() {
        refreshAllProducts();
        return new ArrayList<>(cachedProducts);
    }

    public static List<Product> getOnSaleProducts() {
        refreshAllProducts();
        return cachedProducts.stream()
                .filter(Product::isOnSale)
                .collect(Collectors.toList());
    }

    public static List<Product> getRecommendedProducts() {
        refreshAllProducts();
        // Ambil 8 produk pertama atau semua jika kurang dari 8
        return cachedProducts.stream().limit(8).collect(Collectors.toList());
    }

    public static Product getProductById(Long id) {
        refreshAllProducts();
        return productMap.get(id);
    }

    // Overload untuk int (kompatibilitas dengan kode lama)
    public static Product getProductById(int id) {
        return getProductById((long) id);
    }

    public static List<Product> searchProducts(String keyword) {
        refreshAllProducts();
        String lower = keyword.toLowerCase();
        return cachedProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(lower) ||
                        p.getCategory().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public static List<Product> getProductsByCategory(String category) {
        refreshAllProducts();
        return cachedProducts.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // ========== ADMIN OPERATIONS (untuk produk dinamis) ==========

    public static void addProduct(String name, String category, double price, double discount,
                                  String image, String desc, boolean onSale, int stock,
                                  String brand, String material) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("name", name);
            body.addProperty("category", category);
            body.addProperty("price", price);
            body.addProperty("description", desc);
            body.addProperty("imageUrl", image != null && !image.isEmpty() ? image : "/images/default.jpg");
            body.addProperty("stock", stock);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/data/products"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
            refreshAllProducts(); // Refresh setelah tambah
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct(int id, String name, String category, double price,
                                     double discount, String image, String desc,
                                     boolean onSale, int stock) {
        // Produk statis (ID >= 1000) tidak bisa diupdate via backend
        if (id >= 1000) {
            // Update lokal saja
            Product p = productMap.get((long) id);
            if (p != null) {
                p.setName(name);
                p.setCategory(category);
                p.setPrice(price);
                p.setDescription(desc);
                p.setImageUrl(image);
                p.setStock(stock);
                p.setOnSale(onSale);
                if (discount > 0) {
                    p.setDiscountPrice(discount);
                }
            }
            return;
        }

        try {
            JsonObject body = new JsonObject();
            body.addProperty("name", name);
            body.addProperty("category", category);
            body.addProperty("price", price);
            body.addProperty("description", desc);
            body.addProperty("imageUrl", image);
            body.addProperty("stock", stock);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/data/products/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
            refreshAllProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(Long id) {
        // Produk statis (ID >= 1000) tidak bisa dihapus
        if (id >= 1000) {
            System.out.println("Produk statis tidak dapat dihapus!");
            return;
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/data/products/" + id))
                    .DELETE()
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
            productMap.remove(id); // Hapus dari map lokal
            refreshAllProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Overload untuk int
    public static void deleteProduct(int id) {
        deleteProduct((long) id);
    }

    // ========== ORDER METHODS ==========

    private static List<Order> orders = new ArrayList<>();

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

    // ========== HELPER CLASS ==========

    private static class ProductFromBackend {
        Long id;
        String name;
        Double price;
        String description;
        String imageUrl;
        String category;
        Integer stock;
    }
}
