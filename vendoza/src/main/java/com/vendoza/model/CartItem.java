package com.vendoza.model;

public class CartItem {
    private Long id;           // Tambahan untuk ID dari database
    private Product product;
    private int quantity;

    // Constructor tanpa id (untuk pembuatan baru)
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Constructor dengan id (untuk hasil dari backend)
    public CartItem(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    // Getter & Setter untuk id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter & Setter untuk product
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Getter & Setter untuk quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Hitung subtotal
    public double getSubtotal() {
        return product.getCurrentPrice() * quantity;
    }
}
