package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
