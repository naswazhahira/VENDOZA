package com.vendoza.backend.controller;

import com.vendoza.backend.entity.User;
import com.vendoza.backend.repository.UserRepository;
import com.vendoza.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;  // Tambahan untuk getAllUsers & deleteUser

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return authService.getUserById(id);
    }

    // ========== TAMBAHAN ENDPOINT UNTUK JAVA FX ==========

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    // Endpoint sementara untuk generate hash — hapus setelah dapat hashnya
    @GetMapping("/generate-hash")
    public String generateHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode("admin123"); // ganti password sesuai keinginan
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    // Bagian updateUser, ganti jadi ini:
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = authService.getUserById(id);

        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        // Hash password baru sebelum disimpan
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(authService.encodePassword(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }
}

// Class untuk request login
class LoginRequest {
    private String email;
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}