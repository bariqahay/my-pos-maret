package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("bundle")
public class BundleProduct extends Product implements Stokable {

    // Default constructor for Hibernate
    public BundleProduct() {
    }

    // Constructor with parameters
    public BundleProduct(String code, String name, double price, Integer stock) {
        super(code, name, price, stock); // stock wajib diisi
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
            throw new IllegalArgumentException("Stok tidak mencukupi untuk produk bundle: " + getName());
        }
        setStock(getStock() - qty);
    }

    @Override
    public double getDiscountedPrice() {
        // Bundle product bisa dikasih diskon tetap, misalnya 15%
        return getPrice() * 0.85;
    }
}
