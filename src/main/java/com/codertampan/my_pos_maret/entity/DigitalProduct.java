package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("digital")
public class DigitalProduct extends Product {

    @Column(nullable = false)
    private String url = "-";

    @Column(nullable = false)
    private String vendor_name = "-";

    // Default constructor for Hibernate
    public DigitalProduct() {
        super.setStock(null); // enforce null stock
    }

    // Constructor with parameters
    public DigitalProduct(String code, String name, double price) {
        super(code, name, price, null); // force stock to null
        this.url = "-";
        this.vendor_name = "-";
    }

    // Optional: Constructor with full parameters
    public DigitalProduct(String code, String name, double price, String url, String vendor_name) {
        super(code, name, price, null); // force stock to null
        this.url = (url != null) ? url : "-";
        this.vendor_name = (vendor_name != null) ? vendor_name : "-";
    }

    @Override
    public Integer getStock() {
        return null; // digital product tidak punya stok
    }

    @Override
    public void setStock(Integer newStock) {
        super.setStock(null); // always enforce null
    }

    @Override
    public double getDiscountedPrice() {
        return getPrice();
    }

    // Getters and Setters for url and vendor
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = (url != null) ? url : "-";
    }

    public String getVendor() {
        return vendor_name;
    }

    public void setVendor(String vendor_name) {
        this.vendor_name = (vendor_name != null) ? vendor_name : "-";
    }
}
