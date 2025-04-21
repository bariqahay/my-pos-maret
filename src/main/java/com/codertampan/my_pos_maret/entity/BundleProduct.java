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

    // Default constructor for Hibernate
    public BundleProduct() {
        super(null, null, 0.0, 0);  // Default values
    }

    // Parameterized constructor
    public BundleProduct(String code, String name, double price, List<Product> items) {
        super(code, name, price, 0);  // Default stock as 0, since it's a bundle
        this.items = items;
    }

    // Method to calculate the discounted price for the bundle
    @Override
    public double getDiscountedPrice() {
        double total = 0;
        // Calculate the total price of all items in the bundle
        for (Product p : items) {
            total += p.getPrice();
        }
        return total * 0.9;  // Example discount of 10%
    }

    // Getter for the items in the bundle
    public List<Product> getItems() {
        return items;
    }

    // Setter for the items in the bundle
    public void setItems(List<Product> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "BundleProduct{" +
                "code='" + getCode() + '\'' +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", items=" + items +
                '}';
    }
}
