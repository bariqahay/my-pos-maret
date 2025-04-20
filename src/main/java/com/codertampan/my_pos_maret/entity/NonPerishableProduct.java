package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("nonperishable")
public class NonPerishableProduct extends Product implements Stokable {

    @Column(nullable = false)  // Pastikan stock ada di DB
    private int stock;

    // Getter untuk stock
    @Override
    public int getStock() {
        return stock;
    }

    // Setter untuk stock
    @Override
    public void setStock(int newStock) {
        this.stock = newStock;
    }

    // Implementasi reduceStock dari Stokable interface
    @Override
    public void reduceStock(int qty) {
        if (qty > stock) {
            throw new IllegalArgumentException("Stok tidak mencukupi untuk produk: " + getName());  // Pastikan getName() ada di class Product
        }
        this.stock -= qty;
    }
}
