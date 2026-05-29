package com.vendoza.service;

import com.vendoza.model.User;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    static {
        // Dummy user
        users.add(new User("fashionista", "password123", "fashionista@email.com"));
        users.add(new User("stylish", "abc123", "stylish@email.com"));
    }

    public static boolean register(String username, String password, String email) {
        // Cek apakah username sudah ada
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false;
            }
        }

        User newUser = new User(username, password, email);
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

    public static boolean updateProfile(String username, String phone, String address) {
        if (currentUser != null) {
            currentUser.setUsername(username);
            currentUser.setPhoneNumber(phone);
            currentUser.setAddress(address);
            return true;
        }
        return false;
    }
}
