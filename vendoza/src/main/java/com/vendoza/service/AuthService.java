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

public class AuthService {
    private static final String BASE_URL = "http://localhost:9191/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    private static User currentUser = null;
    private static List<User> usersCache = new ArrayList<>();

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

            if (response.statusCode() == 200) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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

                syncUsersFromBackend();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        if (currentUser == null) return false;
        try {
            currentUser.setUsername(username);
            currentUser.setPhoneNumber(phone);
            currentUser.setAddress(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> getAllUsers() {
        syncUsersFromBackend();
        return new ArrayList<>(usersCache);
    }

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
                    usersCache.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteUser(String username) {
        if ("admin".equals(username)) return false;

        try {
            for (User u : usersCache) {
                if (u.getUsername().equals(username)) {
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

    private static class UserFromBackend {
        Long id;
        String name;
        String email;
        String role;
    }
}
