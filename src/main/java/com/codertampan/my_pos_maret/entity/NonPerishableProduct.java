package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("nonperishable")

public class NonPerishableProduct extends Product implements Stokable {

    // Default constructor for Hibernate
    public NonPerishableProduct() {
    }

    // Constructor with parameters
    public NonPerishableProduct(String code, String name, double price, Integer stock) {
        super(code, name, price, stock);
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
        // Non-perishable products don't get expiry-based discounts
        return getPrice();
    }
}
