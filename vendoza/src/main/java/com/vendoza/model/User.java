package com.vendoza.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private List<Order> orders;
    private List<CartItem> cartItems;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.orders = new ArrayList<>();
        this.cartItems = new ArrayList<>();
    }

    // Getters dan Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<Order> getOrders() { return orders; }
    public List<CartItem> getCartItems() { return cartItems; }
}