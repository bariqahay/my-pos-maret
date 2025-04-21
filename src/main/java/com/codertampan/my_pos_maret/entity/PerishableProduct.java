package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("perishable")
public class PerishableProduct extends Product {
    public PerishableProduct() {
        // Default constructor for Hibernate
    }

    public PerishableProduct(String code, String name, double price) {
        super(code, name, price);
    }
}
