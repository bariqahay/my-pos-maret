package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.SalesLog;
import com.codertampan.my_pos_maret.repository.SalesLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesLogService {

    private final SalesLogRepository salesLogRepository;

    @Autowired
    public SalesLogService(SalesLogRepository salesLogRepository) {
        this.salesLogRepository = salesLogRepository;
    }

    public void save(SalesLog salesLog) {
        salesLogRepository.save(salesLog);
    }
}