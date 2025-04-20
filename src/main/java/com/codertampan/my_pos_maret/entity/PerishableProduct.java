package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("perishable")
public class PerishableProduct extends Product {

    private LocalDate expiryDate;

    public PerishableProduct() {
        // Default constructor for Hibernate
    }

    public PerishableProduct(String code, String name, double price, LocalDate expiryDate) {
        super(code, name, price);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
