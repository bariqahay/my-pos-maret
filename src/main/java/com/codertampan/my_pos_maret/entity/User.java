package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")  // Nama tabel di DB
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO INCREMENT di DB
    private Long id;

    @Column(nullable = false, unique = true)  // username unique dan gak boleh null
    private String username;

    @Column(nullable = false)  // password_hash gak boleh null
    private String passwordHash;

    @Enumerated(EnumType.STRING)  // Mapping ENUM role ke Java
    private UserRole role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
