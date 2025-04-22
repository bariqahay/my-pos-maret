package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.TransactionModificationLog;
import com.codertampan.my_pos_maret.repository.TransactionModificationLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionModificationLogService {

    private final TransactionModificationLogRepository logRepository;

    public TransactionModificationLogService(TransactionModificationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // Method untuk mencatat perubahan transaksi
    public void log(String transactionId, String username, String actionType, String detail) {
        TransactionModificationLog log = new TransactionModificationLog(
                transactionId,
                username,
                actionType,
                detail,
                LocalDateTime.now() // Timestamp saat log dibuat
        );
        logRepository.save(log);  // Menyimpan log ke database
    }
}

