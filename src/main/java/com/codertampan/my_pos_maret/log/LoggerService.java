package com.codertampan.my_pos_maret.log;

import com.codertampan.my_pos_maret.entity.User;
import com.codertampan.my_pos_maret.transaction.CartItem;
import com.codertampan.my_pos_maret.transaction.PurchaseTransaction;

import java.util.List;

public class LoggerService {

    // Method untuk mencatat data otentikasi
    public void logAuthentication(User user, boolean success) {
        String status = success ? "sukses" : "gagal";
        System.out.println("Otentikasi " + status + " untuk pengguna: " + user.getUsername());
    }

    // Method untuk mencatat data transaksi penjualan produk
    public void logTransaction(PurchaseTransaction transaction) {
        double total = transaction.calculateTotal();
        System.out.println("Transaksi ID: " + transaction.getTransactionId() + " berhasil diproses dengan total: Rp " + total);
    }

    // Method untuk mencatat aksi input produk
    public void logProductInput(String productCode, String productName) {
        System.out.println("Produk baru ditambahkan: Kode - " + productCode + ", Nama - " + productName);
    }

    // Method untuk mencatat aksi modifikasi data produk
    public void logProductModification(String productCode, String newName, double newPrice) {
        System.out.println("Produk dimodifikasi: Kode - " + productCode + ", Nama Baru - " + newName + ", Harga Baru - Rp " + newPrice);
    }

    // Method untuk mencatat aksi modifikasi penjualan produk
    public void logSalesModification(String transactionId, List<CartItem> updatedItems) {
        System.out.println("Transaksi ID: " + transactionId + " telah dimodifikasi. Item baru:");
        for (CartItem item : updatedItems) {
            System.out.println(" - " + item.getProduct().getName() + ": " + item.getQuantity() + " unit");
        }
    }
}