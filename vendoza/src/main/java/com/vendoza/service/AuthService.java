package com.vendoza.service;

import com.vendoza.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class AuthService {
    private static final String BASE_URL = "http://localhost:9191/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static Preferences prefs = Preferences.userNodeForPackage(AuthService.class);

    private static User currentUser = null;
    private static List<User> usersCache = new ArrayList<>();

    // ========== REGISTER ==========
    public static boolean register(String username, String password, String email) {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("email", email);
            body.addProperty("password", password);
            body.addProperty("name", username);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Register Response Status: " + response.statusCode());
            System.out.println("Register Response Body: " + response.body());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== LOGIN ==========
    public static boolean login(String usernameOrEmail, String password) {
        try {
            // Coba cari user berdasarkan username atau email
            String email = usernameOrEmail;

            // Jika input mengandung @, berarti email
            if (!usernameOrEmail.contains("@")) {
                // Cari email dari username di cache
                syncUsersFromBackend();
                for (User u : usersCache) {
                    if (u.getUsername().equalsIgnoreCase(usernameOrEmail)) {
                        email = u.getEmail();
                        break;
                    }
                }
            }

            JsonObject body = new JsonObject();
            body.addProperty("email", email);
            body.addProperty("password", password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Login Response Status: " + response.statusCode());
            System.out.println("Login Response Body: " + response.body());

            if (response.statusCode() == 200) {
                JsonObject userJson = gson.fromJson(response.body(), JsonObject.class);

                Long userId = userJson.get("id").getAsLong();
                String name = userJson.get("name").getAsString();
                String userEmail = userJson.get("email").getAsString();
                String role = userJson.get("role").getAsString();

                currentUser = new User(name, password, userEmail, role);
                currentUser.setId(userId);

                // ⭐ LOAD DATA DARI PREFERENCES (FOTO, PHONE, ADDRESS)
                String savedPhotoPath = getUserProfilePhoto(userEmail);
                if (savedPhotoPath != null && !savedPhotoPath.isEmpty()) {
                    currentUser.setProfilePhotoPath(savedPhotoPath);
                    System.out.println("Loaded profile photo from preferences: " + savedPhotoPath);
                }

                String savedPhone = getUserPhone(userEmail);
                if (savedPhone != null && !savedPhone.isEmpty()) {
                    currentUser.setPhoneNumber(savedPhone);
                    System.out.println("Loaded phone from preferences: " + savedPhone);
                }

                String savedAddress = getUserAddress(userEmail);
                if (savedAddress != null && !savedAddress.isEmpty()) {
                    currentUser.setAddress(savedAddress);
                    System.out.println("Loaded address from preferences: " + savedAddress);
                }

                syncUsersFromBackend();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== LOGOUT ==========
    public static void logout() {
        currentUser = null;
    }

    // ========== GET CURRENT USER ==========
    public static User getCurrentUser() {
        return currentUser;
    }

    // ========== CHECK LOGIN STATUS ==========
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // ========== CHECK ADMIN ==========
    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    // ========== UPDATE FULL PROFILE (nama, phone, address, photo) ==========
    public static boolean updateFullProfile(String username, String phone, String address, String photoPath) {
        if (currentUser == null) return false;
        try {
            currentUser.setUsername(username);

            if (phone != null) {
                currentUser.setPhoneNumber(phone.isEmpty() ? null : phone);
                saveUserPhone(currentUser.getEmail(), phone);
            }

            if (address != null) {
                currentUser.setAddress(address.isEmpty() ? null : address);
                saveUserAddress(currentUser.getEmail(), address);
            }

            if (photoPath != null) {
                if (photoPath.isEmpty()) {
                    currentUser.setProfilePhotoPath(null);
                    deleteUserProfilePhoto(currentUser.getEmail());
                } else {
                    currentUser.setProfilePhotoPath(photoPath);
                    updateUserProfilePhoto(currentUser.getEmail(), photoPath);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== UPDATE PROFILE (tanpa foto) ==========
    public static boolean updateProfile(String username, String phone, String address) {
        if (currentUser == null) return false;
        try {
            currentUser.setUsername(username);

            if (phone != null) {
                currentUser.setPhoneNumber(phone.isEmpty() ? null : phone);
                saveUserPhone(currentUser.getEmail(), phone);
            }

            if (address != null) {
                currentUser.setAddress(address.isEmpty() ? null : address);
                saveUserAddress(currentUser.getEmail(), address);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== UPDATE PROFILE PHOTO ==========
    public static void updateUserProfilePhoto(String email, String photoPath) {
        prefs.put("photo_" + email, photoPath);
        System.out.println("Profile photo saved to preferences for: " + email + " -> " + photoPath);

        if (currentUser != null && currentUser.getEmail().equals(email)) {
            currentUser.setProfilePhotoPath(photoPath);
        }
    }

    // ========== GET USER PROFILE PHOTO ==========
    public static String getUserProfilePhoto(String email) {
        return prefs.get("photo_" + email, null);
    }

    // ========== DELETE USER PROFILE PHOTO ==========
    public static void deleteUserProfilePhoto(String email) {
        prefs.remove("photo_" + email);
        if (currentUser != null && currentUser.getEmail().equals(email)) {
            currentUser.setProfilePhotoPath(null);
        }
        System.out.println("Profile photo deleted from preferences for: " + email);
    }

    // ========== SAVE USER PHONE NUMBER ==========
    public static void saveUserPhone(String email, String phone) {
        if (phone == null || phone.isEmpty()) {
            prefs.remove("phone_" + email);
        } else {
            prefs.put("phone_" + email, phone);
        }
        System.out.println("Phone saved to preferences for: " + email + " -> " + phone);
    }

    // ========== GET USER PHONE NUMBER ==========
    public static String getUserPhone(String email) {
        return prefs.get("phone_" + email, null);
    }

    // ========== SAVE USER ADDRESS ==========
    public static void saveUserAddress(String email, String address) {
        if (address == null || address.isEmpty()) {
            prefs.remove("address_" + email);
        } else {
            prefs.put("address_" + email, address);
        }
        System.out.println("Address saved to preferences for: " + email + " -> " + address);
    }

    // ========== GET USER ADDRESS ==========
    public static String getUserAddress(String email) {
        return prefs.get("address_" + email, null);
    }

    // ========== DELETE ALL USER DATA FROM PREFERENCES ==========
    public static void deleteAllUserData(String email) {
        prefs.remove("photo_" + email);
        prefs.remove("phone_" + email);
        prefs.remove("address_" + email);
        System.out.println("All user data deleted from preferences for: " + email);
    }

    // ========== GET ALL USERS ==========
    public static List<User> getAllUsers() {
        syncUsersFromBackend();
        return new ArrayList<>(usersCache);
    }

    // ========== SYNC USERS FROM BACKEND ==========
    private static void syncUsersFromBackend() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/users"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<UserFromBackend> backendUsers = gson.fromJson(response.body(),
                        new TypeToken<List<UserFromBackend>>(){}.getType());

                usersCache.clear();
                for (UserFromBackend bu : backendUsers) {
                    User u = new User(bu.name, "", bu.email, bu.role);
                    u.setId(bu.id);

                    // Load data dari preferences untuk setiap user
                    String photoPath = getUserProfilePhoto(bu.email);
                    if (photoPath != null) {
                        u.setProfilePhotoPath(photoPath);
                    }

                    String phone = getUserPhone(bu.email);
                    if (phone != null) {
                        u.setPhoneNumber(phone);
                    }

                    String address = getUserAddress(bu.email);
                    if (address != null) {
                        u.setAddress(address);
                    }

                    usersCache.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== DELETE USER ==========
    public static boolean deleteUser(String username) {
        if ("admin".equals(username)) return false;

        try {
            for (User u : usersCache) {
                if (u.getUsername().equals(username)) {
                    // Hapus semua data user dari preferences
                    deleteAllUserData(u.getEmail());

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/auth/users/" + u.getId()))
                            .DELETE()
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    return response.statusCode() == 200;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ========== HELPER CLASS ==========
    private static class UserFromBackend {
        Long id;
        String name;
        String email;
        String role;
    }
}
