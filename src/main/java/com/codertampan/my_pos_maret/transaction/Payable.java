package com.codertampan.my_pos_maret.transaction;

interface Payable {
    double calculateTotal();  // Menghitung total transaksi
    void processTransaction(); // Memproses transaksi
    String serializeTransaction();  // Menyimpan atau menampilkan data transaksi
}