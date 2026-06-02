package com.vendoza.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private static int counter = 1;

    private int id;
    private String orderId;
    private User user;
    private List<CartItem> items;
    private double subtotal;
    private double shippingCost;
    private double total;
    private String status;
    private LocalDateTime orderDate;
    private String shippingAddress;
    private String paymentMethod;
    private String shippingMethod;

    // Konstruktor lama — tetap ada agar kode lain tidak rusak
    public Order(String orderId, User user, List<CartItem> items,
                 double shippingCost, String shippingAddress) {
        this.id = counter++;
        this.orderId = orderId;
        this.user = user;
        this.items = items;
        this.subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        this.shippingCost = shippingCost;
        this.total = this.subtotal + shippingCost;
        this.status = "Pending";
        this.orderDate = LocalDateTime.now();
        this.shippingAddress = shippingAddress;
    }

    // ✅ Tambah no-arg constructor untuk parsing dari API
    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    // Getters (tetap sama)
    public int getId()                  { return id; }
    public String getOrderId()          { return orderId; }
    public User getUser()               { return user; }
    public List<CartItem> getItems()    { return items; }
    public double getSubtotal()         { return subtotal; }
    public double getShippingCost()     { return shippingCost; }
    public double getTotal()            { return total; }
    public double getTotalAmount()      { return total; }
    public String getStatus()           { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getShippingAddress()  { return shippingAddress; }
    public String getPaymentMethod()    { return paymentMethod; }
    public String getShippingMethod()   { return shippingMethod; }
    public String getCustomerName() {
        return user != null ? user.getUsername() : "Unknown";
    }
    
    public void setOrderId(String orderId)           { this.orderId = orderId; }
    public void setStatus(String status)             { this.status = status; }
    public void setTotal(double total)               { this.total = total; }
    public void setSubtotal(double subtotal)         { this.subtotal = subtotal; }
    public void setShippingCost(double shippingCost) { this.shippingCost = shippingCost; }
    public void setShippingAddress(String addr)      { this.shippingAddress = addr; }
    public void setPaymentMethod(String method)      { this.paymentMethod = method; }
    public void setShippingMethod(String method)     { this.shippingMethod = method; }
    public void setOrderDate(LocalDateTime date)     { this.orderDate = date; }
    public void setItems(List<CartItem> items)       { this.items = items; }
    public void setUser(User user)                   { this.user = user; }
}
