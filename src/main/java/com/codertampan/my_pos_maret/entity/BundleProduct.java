package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue("bundle")
public class BundleProduct extends Product {

    @OneToMany
    private List<Product> items;

    // Konstruktor default untuk Hibernate
    public BundleProduct() {
    }

    // Konstruktor dengan parameter
    public BundleProduct(String code, String name, double price, List<Product> items) {
        super(code, name, price);  // Panggil konstruktor dari Product
        this.items = items;
    }

    public double getDiscountedPrice() {
        double total = 0;
        for (Product p : items) {
            total += p.getPrice();
        }
        return total * 0.9; // Contoh diskon 10%
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }
}
