package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLogRepository extends JpaRepository<ProductLog, Long> {
}