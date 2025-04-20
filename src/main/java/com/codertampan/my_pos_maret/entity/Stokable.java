package com.codertampan.my_pos_maret.entity;

public interface Stokable {
    void reduceStock(int qty);
    int getStock();
    void setStock(int newStock);
}
