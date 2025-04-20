package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.net.URL;

@Entity
@DiscriminatorValue("digital")
public class DigitalProduct extends Product {

    private URL url;
    private String vendorName;

    public DigitalProduct() {
        // Default constructor for Hibernate
    }

    public DigitalProduct(String code, String name, double price, URL url, String vendorName) {
        super(code, name, price);
        this.url = url;
        this.vendorName = vendorName;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}