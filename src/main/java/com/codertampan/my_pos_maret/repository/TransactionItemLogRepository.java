package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.TransactionItemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionItemLogRepository extends JpaRepository<TransactionItemLog, Long> {
    // Bisa nambahin custom query di sini kalau perlu
}
