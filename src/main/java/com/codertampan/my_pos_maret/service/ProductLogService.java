package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.ProductLog;
import com.codertampan.my_pos_maret.repository.ProductLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductLogService {

    @Autowired
    private ProductLogRepository productLogRepository;

    public void logProductCreated(String username, String code, String name, Double price, Integer stock) {
        ProductLog log = new ProductLog();
        log.setTimestamp(LocalDateTime.now());
        log.setAction("PRODUCT_CREATED");
        log.setUserUsername(username);
        log.setProductCode(code);
        log.setProductName(name);
        log.setProductPrice(price);
        log.setProductStock(stock);
        log.setDescription("Produk ditambahkan ke sistem");

        productLogRepository.save(log);
    }
}