package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("nonperishable")
public class NonPerishableProduct extends Product implements Stokable {

    @Column(nullable = false)  // Ensure stock is persisted
    private Integer stock;

    public NonPerishableProduct() {
        super(null, null, 0.0, 0);  // Default constructor for Hibernate
    }

    @Override
    public Integer getStock() {
        return stock;
    }

    @Override
    public void setStock(Integer newStock) {
        this.stock = newStock;
    }

    @Override
    public void reduceStock(int qty) {
        if (qty > stock) {
            throw new IllegalArgumentException("Stok tidak mencukupi untuk produk: " + getName());
        }
        this.stock -= qty;
    }

    @Override
    public double getDiscountedPrice() {
        return getPrice(); // No discount for non-perishable products in this example
    }
}
