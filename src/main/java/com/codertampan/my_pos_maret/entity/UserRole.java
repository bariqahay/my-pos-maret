package com.codertampan.my_pos_maret.entity;

public enum UserRole {
    ADMIN,
    KASIR;

    @Override
    public String toString() {
        return name().toLowerCase(); // Tetap akan return "admin" atau "kasir"
    }
}
