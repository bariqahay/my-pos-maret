package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.TransactionItemLog;
import com.codertampan.my_pos_maret.repository.TransactionItemLogRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionItemLogService {

    private final TransactionItemLogRepository logRepository;

    public TransactionItemLogService(TransactionItemLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // Method untuk menyimpan log transaksi item
    public void logTransactionItem(String transactionId, String username, String actionType, String logDetails) {
        TransactionItemLog log = new TransactionItemLog();
        log.setTransactionId(transactionId);
        log.setUsername(username);
        log.setActionType(actionType);
        log.setLogDetails(logDetails);

        logRepository.save(log);  // Menyimpan log ke database
    }
}
