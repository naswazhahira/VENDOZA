package com.vendoza.backend.service;

import com.vendoza.backend.entity.CartItem;
import com.vendoza.backend.entity.User;
import com.vendoza.backend.entity.Product;
import com.vendoza.backend.repository.CartItemRepository;
import com.vendoza.backend.repository.UserRepository;
import com.vendoza.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        System.out.println("🔍 Finding user ID: " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan: " + userId));

        System.out.println("🔍 Finding product ID: " + productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product tidak ditemukan: " + productId));

        CartItem existingItem = cartItemRepository.findByUserAndProduct(user, product).orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            return cartItemRepository.save(newItem);
        }
    }

    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public void updateQuantity(Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Item tidak ditemukan"));
        if (quantity <= 0) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        cartItemRepository.deleteByUser(user);
    }
}