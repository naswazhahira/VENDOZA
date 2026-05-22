package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthService {
    // ENCAPSULATION: private list of users
    private List<User> users;
    private User currentLoggedInUser;

    // Constructor
    public AuthService() {
        this.users = new ArrayList<>();
        this.currentLoggedInUser = null;
    }

    // Method untuk registrasi user baru
    public boolean register(User user) {
        // Validasi email sudah terdaftar atau belum
        if (isEmailExists(user.getEmail())) {
            System.out.println("Gagal registrasi! Email " + user.getEmail() + " sudah terdaftar.");
            return false;
        }

        users.add(user);
        System.out.println("Registrasi berhasil! Selamat datang, " + user.getNama());
        return true;
    }

    // Method untuk login
    public User login(String email, String password) {
        if (email == null || password == null) {
            System.out.println("Email dan password tidak boleh kosong!");
            return null;
        }

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
                currentLoggedInUser = user;
                System.out.println("Login berhasil! Selamat datang, " + user.getNama());
                user.tampilRole();
                return user;
            }
        }

        System.out.println("Login gagal! Email atau password salah.");
        return null;
    }

    // Method untuk logout
    public void logout() {
        if (currentLoggedInUser != null) {
            System.out.println("👋 " + currentLoggedInUser.getNama() + " telah logout.");
            currentLoggedInUser = null;
        } else {
            System.out.println("Tidak ada user yang sedang login.");
        }
    }

    // Method untuk cek email sudah terdaftar
    private boolean isEmailExists(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    // Method untuk mendapatkan user berdasarkan email
    public Optional<User> getUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    // Method untuk mendapatkan semua user (hanya untuk Admin)
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Method untuk mendapatkan current logged in user
    public User getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    // Method untuk cek apakah ada user yang sedang login
    public boolean isUserLoggedIn() {
        return currentLoggedInUser != null;
    }

    // Method untuk update data user
    public boolean updateUser(String email, User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equalsIgnoreCase(email)) {
                users.set(i, updatedUser);
                System.out.println("Data user berhasil diupdate!");
                return true;
            }
        }
        System.out.println("User dengan email " + email + " tidak ditemukan!");
        return false;
    }

    // Method untuk hapus user
    public boolean deleteUser(String email) {
        boolean removed = users.removeIf(user -> user.getEmail().equalsIgnoreCase(email));
        if (removed) {
            System.out.println("User dengan email " + email + " berhasil dihapus!");
        } else {
            System.out.println("User dengan email " + email + " tidak ditemukan!");
        }
        return removed;
    }

    // Method untuk menampilkan semua user (Admin only)
    public void tampilkanSemuaUser() {
        System.out.println("\n DAFTAR SEMUA USER ");
        if (users.isEmpty()) {
            System.out.println("Belum ada user yang terdaftar.");
        } else {
            for (int i = 0; i < users.size(); i++) {
                System.out.println((i + 1) + ". " + users.get(i).toString());
            }
        }
        System.out.println("Total user: " + users.size());
    }

    // Method untuk reset password (lupa password)
    public boolean resetPassword(String email, String newPassword) {
        Optional<User> userOpt = getUserByEmail(email);
        if (userOpt.isPresent()) {
            userOpt.get().setPassword(newPassword);
            System.out.println("Password untuk email " + email + " berhasil direset!");
            return true;
        }
        System.out.println("Email " + email + " tidak ditemukan!");
        return false;
    }
}
