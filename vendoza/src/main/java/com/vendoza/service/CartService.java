package com.vendoza.service;


import com.vendoza.model.CartItem;
import com.vendoza.model.Product;
import com.vendoza.model.User;
import java.util.ArrayList;
import java.util.List;

public class CartService {

    public static void addToCart(Product product, int quantity) {
        if (!AuthService.isLoggedIn()) return;

        User user = AuthService.getCurrentUser();
        List<CartItem> cart = user.getCartItems();

        // Cek apakah produk sudah ada di keranjang
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        cart.add(new CartItem(product, quantity));
    }

    public static void removeFromCart(Product product) {
        if (!AuthService.isLoggedIn()) return;

        User user = AuthService.getCurrentUser();
        user.getCartItems().removeIf(item -> item.getProduct().getId() == product.getId());
    }

    public static void updateQuantity(Product product, int quantity) {
        if (!AuthService.isLoggedIn()) return;

        User user = AuthService.getCurrentUser();
        for (CartItem item : user.getCartItems()) {
            if (item.getProduct().getId() == product.getId()) {
                if (quantity <= 0) {
                    user.getCartItems().remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    public static List<CartItem> getCartItems() {
        if (!AuthService.isLoggedIn()) return new ArrayList<>();
        return AuthService.getCurrentUser().getCartItems();
    }

    public static double getCartTotal() {
        if (!AuthService.isLoggedIn()) return 0;
        return AuthService.getCurrentUser().getCartItems().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public static int getCartItemCount() {
        if (!AuthService.isLoggedIn()) return 0;
        return AuthService.getCurrentUser().getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public static void clearCart() {
        if (!AuthService.isLoggedIn()) return;
        AuthService.getCurrentUser().getCartItems().clear();
    }
}