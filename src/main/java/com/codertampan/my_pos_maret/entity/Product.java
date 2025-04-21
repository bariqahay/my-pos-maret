package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private double price;
    private Integer stock;
    private String expiryDate;

    // Tidak perlu field type lagi karena ini akan selalu "perishable"
    @Column(name = "type", insertable = false, updatable = false)
    private String type;

    public Product() {
    }

    public Product(String code, String name, double price, Integer stock, String expiryDate) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.type = "perishable"; // Tetapkan "perishable" di sini
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getexpiryDate() { return expiryDate; }
    public void setexpiryDate(String expiry_date) { this.expiryDate = expiryDate; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getType() { return type; }  // Optional getter, bisa dihapus jika tidak digunakan lagi
}
