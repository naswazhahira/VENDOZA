package com.vendoza.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password wajib diisi")
    private String password;

    @NotBlank(message = "Nama wajib diisi")
    private String name;

    private String role = "USER";

    // Constructor kosong (wajib untuk JPA)
    public User() {}

    // Getter dan Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}