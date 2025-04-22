package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.TransactionModificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionModificationLogRepository extends JpaRepository<TransactionModificationLog, Long> {
    // Method tambahan bisa ditambahkan di sini kalau diperlukan, seperti mencari log berdasarkan transactionId
}
