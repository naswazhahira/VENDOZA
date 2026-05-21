package Model;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users = new ArrayList<>();

    // Daftarkan user ke sistem
    public void register(User user) {
        users.add(user);
        System.out.println("User " + user.getNama() + " berhasil didaftarkan.");
    }

    // Login: cocokkan email + password, return User kalau cocok
    public User login(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("Login berhasil! Selamat datang, " + user.getNama());
                return user;
            }
        }
        System.out.println("Login gagal. Email atau password salah.");
        return null;
    }
}
