package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("bundle")
public class BundleProduct extends Product implements Stokable {

    public BundleProduct() {
    }

    public BundleProduct(String code, String name, double price) {
        super(code, name, price, null); // stock dipaksa null
    }

    @Override
    public Integer getStock() {
        return null; // Bundle tidak punya stock
    }

    @Override
    public void setStock(Integer newStock) {
        throw new UnsupportedOperationException("BundleProduct tidak mendukung stock.");
    }

    @Override
    public void reduceStock(int qty) {
        throw new UnsupportedOperationException("BundleProduct tidak punya stock untuk dikurangi.");
    }

    @Override
    public double getDiscountedPrice() {
        return getPrice() * 0.85;
    }
}
