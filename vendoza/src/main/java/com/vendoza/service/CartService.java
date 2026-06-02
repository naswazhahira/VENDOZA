package com.vendoza.service;

import com.vendoza.model.CartItem;
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

public class CartService {

    private static final String BASE_URL = "http://localhost:9191/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    private static List<CartItem> cachedCartItems = new ArrayList<>();

    private static void syncCartFromBackend() {
        if (!AuthService.isLoggedIn()) {
            cachedCartItems.clear();
            return;
        }

        try {
            User currentUser = AuthService.getCurrentUser();
            Long userId = currentUser.getId();

            System.out.println("🔄 Syncing cart for user ID: " + userId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/cart/" + userId))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("📦 Cart API Response Status: " + response.statusCode());
            System.out.println("📦 Cart API Response Body: " + response.body());

            if (response.statusCode() == 200) {
                List<CartItemFromBackend> backendItems = gson.fromJson(response.body(),
                        new TypeToken<List<CartItemFromBackend>>(){}.getType());

                cachedCartItems.clear();
                if (backendItems != null) {
                    for (CartItemFromBackend bi : backendItems) {
                        Product product = new Product(
                                bi.product.id,
                                bi.product.name,
                                bi.product.category != null ? bi.product.category : "Uncategorized",
                                bi.product.price != null ? bi.product.price : 0.0,
                                0,
                                bi.product.imageUrl,
                                bi.product.description != null ? bi.product.description : "",
                                false,
                                bi.product.stock != null ? bi.product.stock : 0,
                                4.0,
                                0
                        );
                        CartItem item = new CartItem(product, bi.quantity);
                        item.setId(bi.id);
                        cachedCartItems.add(item);
                    }
                }

                if (currentUser != null) {
                    currentUser.setCartItems(new ArrayList<>(cachedCartItems));
                }

                System.out.println("✅ Cart synced: " + cachedCartItems.size() + " items");
            } else {
                System.out.println("❌ Failed to sync cart. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("❌ Error syncing cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addToCart(Product product, int quantity) {
        if (!AuthService.isLoggedIn()) {
            System.out.println("❌ User not logged in, cannot add to cart");
            return;
        }

        try {
            User currentUser = AuthService.getCurrentUser();
            Long userId = currentUser.getId();
            Long productId = product.getId();

            System.out.println("➕ Adding to cart - UserID: " + userId + ", ProductID: " + productId + ", Quantity: " + quantity);

            JsonObject body = new JsonObject();
            body.addProperty("userId", userId);
            body.addProperty("productId", productId);
            body.addProperty("quantity", quantity);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/cart/add"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("📤 Add to cart response status: " + response.statusCode());
            System.out.println("📤 Add to cart response body: " + response.body());

            if (response.statusCode() == 200) {
                syncCartFromBackend();
                System.out.println("✅ Cart updated, now has " + cachedCartItems.size() + " items");
            } else {
                System.out.println("❌ Failed to add to cart");
            }
        } catch (Exception e) {
            System.err.println("❌ Error adding to cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void removeFromCart(Product product) {
        if (!AuthService.isLoggedIn()) return;

        try {
            for (CartItem item : getCartItems()) {
                if (item.getProduct().getId().equals(product.getId())) {
                    System.out.println("🗑 Removing from cart - CartItem ID: " + item.getId());

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/cart/" + item.getId()))
                            .DELETE()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("🗑 Remove response status: " + response.statusCode());

                    syncCartFromBackend();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateQuantity(Product product, int quantity) {
        if (!AuthService.isLoggedIn()) return;

        try {
            for (CartItem item : getCartItems()) {
                if (item.getProduct().getId().equals(product.getId())) {
                    if (quantity <= 0) {
                        removeFromCart(product);
                    } else {
                        System.out.println("✏️ Updating quantity - CartItem ID: " + item.getId() + ", New Quantity: " + quantity);

                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(BASE_URL + "/cart/" + item.getId() + "?quantity=" + quantity))
                                .PUT(HttpRequest.BodyPublishers.noBody())
                                .build();

                        client.send(request, HttpResponse.BodyHandlers.ofString());
                        syncCartFromBackend();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<CartItem> getCartItems() {
        if (!AuthService.isLoggedIn()) return new ArrayList<>();
        syncCartFromBackend();
        return new ArrayList<>(cachedCartItems);
    }

    public static double getCartTotal() {
        if (!AuthService.isLoggedIn()) return 0;
        syncCartFromBackend();
        return cachedCartItems.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public static int getCartItemCount() {
        if (!AuthService.isLoggedIn()) return 0;
        syncCartFromBackend();
        return cachedCartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public static void clearCart() {
        if (!AuthService.isLoggedIn()) return;

        try {
            User currentUser = AuthService.getCurrentUser();
            Long userId = currentUser.getId();

            System.out.println("🧹 Clearing cart for user ID: " + userId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/cart/clear/" + userId))
                    .DELETE()
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
            syncCartFromBackend();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshCart() {
        System.out.println("🔄 Manual refresh cart requested");
        syncCartFromBackend();
    }

    private static class CartItemFromBackend {
        Long id;
        Integer quantity;
        ProductFromBackend product;
    }

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
