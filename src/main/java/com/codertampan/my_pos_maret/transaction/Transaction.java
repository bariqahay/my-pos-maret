package com.codertampan.my_pos_maret.transaction;

import java.time.LocalDateTime;

public abstract class Transaction {
    private String transactionId;
    private LocalDateTime date;

    public Transaction() {
        this.transactionId = generateTransactionId();
        this.date = LocalDateTime.now();
    }

    private String generateTransactionId() {
        return "TRX-" + System.currentTimeMillis();  // ID Transaksi berbasis waktu
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
