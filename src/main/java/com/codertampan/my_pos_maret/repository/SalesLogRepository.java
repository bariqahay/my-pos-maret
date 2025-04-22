package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.SalesLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesLogRepository extends JpaRepository<SalesLog, Long> {
}