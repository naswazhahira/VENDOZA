package com.vendoza.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;  // <-- TAMBAHKAN INI (untuk ID dari database)
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
    private List<Order> orders;
    private List<CartItem> cartItems;
    private List<Address> addresses;
    private String profilePhotoPath;

    // Constructor untuk registrasi/login (tanpa id)
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = "USER";
        this.orders = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        this.addresses = new ArrayList<>();
        this.profilePhotoPath = null;
    }

    // Constructor dengan role (tanpa id)
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.orders = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        this.addresses = new ArrayList<>();
        this.profilePhotoPath = null;
    }

    // Constructor lengkap dengan id (untuk dari backend)
    public User(Long id, String username, String password, String email, String role,
                String phoneNumber, String address, String profilePhotoPath) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profilePhotoPath = profilePhotoPath;
        this.orders = new ArrayList<>();
        this.cartItems = new ArrayList<>();
        this.addresses = new ArrayList<>();
    }

    // ========== GETTERS & SETTERS ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }

    public String getProfilePhotoPath() { return profilePhotoPath; }
    public void setProfilePhotoPath(String profilePhotoPath) { this.profilePhotoPath = profilePhotoPath; }

    // ========== HELPER METHODS ==========

    public Address getPrimaryAddress() {
        for (Address addr : addresses) {
            if (addr.isPrimary()) return addr;
        }
        return addresses.isEmpty() ? null : addresses.get(0);
    }

    public void setPrimaryAddress(Address newPrimary) {
        for (Address addr : addresses) {
            addr.setPrimary(addr == newPrimary);
        }
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public String getJoinDate() {
        return "—";
    }

    // ========== INNER CLASS ADDRESS ==========
    public static class Address {
        private String label;
        private String recipientName;
        private String phone;
        private String street;
        private String city;
        private String province;
        private String postalCode;
        private boolean isPrimary;

        public Address(String label, String recipientName, String phone,
                       String street, String city, String province,
                       String postalCode, boolean isPrimary) {
            this.label = label;
            this.recipientName = recipientName;
            this.phone = phone;
            this.street = street;
            this.city = city;
            this.province = province;
            this.postalCode = postalCode;
            this.isPrimary = isPrimary;
        }

        public String getLabel() { return label; }
        public String getRecipientName() { return recipientName; }
        public String getPhone() { return phone; }
        public String getStreet() { return street; }
        public String getCity() { return city; }
        public String getProvince() { return province; }
        public String getPostalCode() { return postalCode; }
        public boolean isPrimary() { return isPrimary; }

        public void setLabel(String label) { this.label = label; }
        public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setStreet(String street) { this.street = street; }
        public void setCity(String city) { this.city = city; }
        public void setProvince(String province) { this.province = province; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        public void setPrimary(boolean primary) { isPrimary = primary; }

        public String toDisplayString() {
            return recipientName + ", " + street + ", " + city + ", " + province + " " + postalCode;
        }
    }
}
