package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {}
