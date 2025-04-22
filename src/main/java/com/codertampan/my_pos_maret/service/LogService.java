package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.AuthLog;
import com.codertampan.my_pos_maret.entity.ProductLog;
import com.codertampan.my_pos_maret.entity.ProductUpdateLog;
import com.codertampan.my_pos_maret.entity.SalesLog;
import com.codertampan.my_pos_maret.entity.TransactionItemLog;
import com.codertampan.my_pos_maret.entity.TransactionModificationLog;
import com.codertampan.my_pos_maret.repository.AuthLogRepository;
import com.codertampan.my_pos_maret.repository.ProductLogRepository;
import com.codertampan.my_pos_maret.repository.ProductUpdateLogRepository;
import com.codertampan.my_pos_maret.repository.SalesLogRepository;
import com.codertampan.my_pos_maret.repository.TransactionItemLogRepository;
import com.codertampan.my_pos_maret.repository.TransactionModificationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private AuthLogRepository authLogRepository;

    @Autowired
    private ProductLogRepository productLogRepository;

    @Autowired
    private ProductUpdateLogRepository productUpdateLogRepository;

    @Autowired
    private SalesLogRepository salesLogRepository;

    @Autowired
    private TransactionItemLogRepository transactionItemLogRepository;

    @Autowired
    private TransactionModificationLogRepository transactionModificationLogRepository; // Tambah repository untuk TransactionModificationLog

    // Method untuk ambil semua log dari auth log, product log, product update log, sales log, transaction item log, dan transaction modification log
    public List<Object> getAllLogs() {
        List<Object> allLogs = new ArrayList<>();

        // Ambil semua data log dari authLog, productLog, productUpdateLog, salesLog, transactionItemLog, dan transactionModificationLog
        allLogs.addAll(authLogRepository.findAll());
        allLogs.addAll(productLogRepository.findAll());
        allLogs.addAll(productUpdateLogRepository.findAll());
        allLogs.addAll(salesLogRepository.findAll());
        allLogs.addAll(transactionItemLogRepository.findAll());
        allLogs.addAll(transactionModificationLogRepository.findAll()); // Ambil TransactionModificationLog juga

        return allLogs;
    }
}
