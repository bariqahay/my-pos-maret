package com.codertampan.my_pos_maret.entity;

public interface Stokable {
    void reduceStock(int qty);
    Integer getStock();  // Nullable return type
    void setStock(Integer newStock);  // Nullable parameter
}
