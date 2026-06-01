package com.vendoza.service;

import com.vendoza.model.User;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    static {
        users.add(new User("fashionista", "password123", "fashionista@email.com", "USER"));
        users.add(new User("stylish", "abc123", "stylish@email.com", "USER"));
        users.add(new User("admin", "admin123", "admin@vendoza.com", "ADMIN"));
        users.add(new User("superadmin", "super123", "super@vendoza.com", "ADMIN"));
    }

    public static boolean register(String username, String password, String email) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        User newUser = new User(username, password, email, "USER"); // default role USER
        users.add(newUser);
        return true;
    }

    public static boolean login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public static boolean updateProfile(String username, String phone, String address) {
        if (currentUser != null) {
            currentUser.setUsername(username);
            currentUser.setPhoneNumber(phone);
            currentUser.setAddress(address);
            return true;
        }
        return false;
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public static boolean deleteUser(String username) {
        if ("admin".equals(username)) {
            return false;
        }
        return users.removeIf(u -> u.getUsername().equals(username));
    }
}
