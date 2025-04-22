package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.ProductUpdateLog;
import com.codertampan.my_pos_maret.repository.ProductUpdateLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductLogUpdate {

    @Autowired
    private ProductUpdateLogRepository productUpdateLogRepository;

    public void logProductUpdated(String username, String productCode, String fieldChanged, String oldValue, String newValue) {
        ProductUpdateLog log = new ProductUpdateLog();
        log.setTimestamp(LocalDateTime.now());
        log.setAction("PRODUCT_UPDATED");
        log.setUserUsername(username);
        log.setProductCode(productCode);
        log.setFieldChanged(fieldChanged);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setUpdateBatchId(UUID.randomUUID()); // Generate unique batch ID for each update log

        productUpdateLogRepository.save(log);
    }
}
