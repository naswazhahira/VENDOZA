package com.vendoza.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // Getters
    public int getId()                { return id; }
    public String getOrderId()        { return orderId; }
    public User getUser()             { return user; }
    public List<CartItem> getItems()  { return items; }
    public double getSubtotal()       { return subtotal; }
    public double getShippingCost()   { return shippingCost; }
    public double getTotal()          { return total; }
    public double getTotalAmount()    { return total; }
    public String getStatus()         { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getShippingAddress(){ return shippingAddress; }

    public String getCustomerName() {
        return user != null ? user.getUsername() : "Unknown";
    }

    public void setStatus(String status) { this.status = status; }
}
