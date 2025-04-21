package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("perishable")
public class PerishableProduct extends Product implements Stokable {

    @Column(nullable = false)  // Ensure expiryDate is persisted
    private LocalDate expiryDate;

    // Default constructor for Hibernate
    public PerishableProduct() {
    }

    // Constructor with parameters
    public PerishableProduct(String code, String name, double price, Integer stock, LocalDate expiryDate) {
        super(code, name, price, stock);  // Initialize with stock
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public Integer getStock() {
        return super.getStock();
    }

    @Override
    public void setStock(Integer newStock) {
        super.setStock(newStock);
    }

    @Override
    public void reduceStock(int qty) {
        if (qty > getStock()) {
            throw new IllegalArgumentException("Stok tidak mencukupi untuk produk: " + getName());
        }
        setStock(getStock() - qty);
    }

    @Override
    public double getDiscountedPrice() {
        // Apply a discount for perishable products, for example 10% if it's close to expiry
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now().plusDays(3))) {
            return getPrice() * 0.9;  // 10% discount for expiring products in less than 3 days
        }
        return getPrice(); // No discount otherwise
    }
}
