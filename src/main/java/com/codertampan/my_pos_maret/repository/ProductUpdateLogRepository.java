package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.ProductUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductUpdateLogRepository extends JpaRepository<ProductUpdateLog, Long> {
}
