package com.codertampan.my_pos_maret.repository;

import com.codertampan.my_pos_maret.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Query untuk mencari berdasarkan kode produk
    Product findByCode(String code);

    // Query untuk menyaring berdasarkan class entity (subclass)
    @Query("SELECT p FROM Product p WHERE TYPE(p) = :type")
    List<Product> findByType(@Param("type") Class<? extends Product> type);
}
