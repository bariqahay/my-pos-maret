package com.codertampan.my_pos_maret.transaction;

import com.codertampan.my_pos_maret.entity.Product;

public class CartItem extends TransactionItem {
    private final Product product;
    private final int quantity;

    // Constructor untuk inisialisasi CartItem dengan produk dan quantity
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getter untuk produk
    public Product getProduct() {
        return product;
    }

    // Getter untuk quantity
    public int getQuantity() {
        return quantity;
    }

    // Menghitung subtotal berdasarkan harga produk dan quantity
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}
