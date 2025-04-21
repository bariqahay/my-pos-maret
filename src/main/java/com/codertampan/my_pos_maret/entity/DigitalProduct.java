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
        super(null, null, 0.0, 0);  // Call the Product constructor with default values
    }

    public DigitalProduct(String code, String name, double price, URL url, String vendorName) {
        super(code, name, price, 0);  // Default stock as 0
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

    @Override
    public double getDiscountedPrice() {
        return getPrice(); // No discount for digital products in this example
    }
}
