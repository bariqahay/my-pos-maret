package com.codertampan.my_pos_maret.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_modification_logs")
public class TransactionModificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private String username;

    private String actionType; // Contoh: REMOVE_ITEM, EDIT_ITEM, VOID_TRANSACTION

    private String detail; // Misal: kode barang yang dihapus atau field yang diedit

    private LocalDateTime timestamp;

    public TransactionModificationLog(String transactionId, String username, String actionType, String detail, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.username = username;
        this.actionType = actionType;
        this.detail = detail;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
