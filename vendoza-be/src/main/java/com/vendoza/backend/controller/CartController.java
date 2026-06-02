package com.vendoza.backend.controller;

import com.vendoza.backend.entity.CartItem;
import com.vendoza.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity());
    }

    @DeleteMapping("/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return "Item removed from cart";
    }

    @PutMapping("/{cartItemId}")
    public String updateQuantity(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
        cartService.updateQuantity(cartItemId, quantity);
        return "Quantity updated";
    }

    @DeleteMapping("/clear/{userId}")
    public String clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return "Cart cleared";
    }
}

class AddToCartRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}