package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.entity.Stokable;
import com.codertampan.my_pos_maret.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class); // Logger for warning if product is not found

    @Autowired
    private ProductRepository productRepository;

    /**
     * Mencari produk berdasarkan kode
     * @param code Kode produk
     * @return Produk yang ditemukan atau null jika tidak ditemukan
     */
    public Product findByCode(String code) {
        // Memastikan kode tidak null atau kosong
        if (!StringUtils.hasText(code)) {
            logger.warn("Product code is empty or invalid.");
            return null;
        }

        // Mencari produk berdasarkan kode
        Product product = productRepository.findByCode(code.trim());
        if (product == null) {
            logger.warn("Product with code '{}' not found.", code);
        }
        return product;
    }

    /**
     * Mengupdate informasi produk di database
     * @param product Produk yang akan diupdate
     */
    public void updateProduct(Product product) {
        if (product == null || product.getId() == null) {
            logger.error("Product or product ID is null. Cannot update.");
            return;
        }

        try {
            if (product instanceof Stokable) {
                // Mungkin lakukan logika khusus untuk Stokable jika perlu
                logger.info("Product is stokable: " + product.getName());
            }

            productRepository.save(product);  // Save the product or stokable product
            logger.info("Product with ID '{}' updated successfully.", product.getId());
        } catch (Exception e) {
            logger.error("Failed to update product with ID '{}'. Error: {}", product.getId(), e.getMessage());
        }
    }

    /**
     * Mendapatkan semua produk yang ada di database
     * @return Daftar semua produk
     */
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();  // Mengambil semua produk dari database
        } catch (Exception e) {
            logger.error("Error fetching all products: {}", e.getMessage());
            return List.of();  // Return empty list in case of error
        }
    }

    /**
     * Menyaring produk berdasarkan jenis
     * @param type Kelas produk untuk filter (misalnya: NonPerishableProduct, DigitalProduct)
     * @return Daftar produk sesuai jenis
     */
    public List<Product> getProductsByType(Class<? extends Product> type) {
        try {
            return productRepository.findByType(type);
        } catch (Exception e) {
            logger.error("Error fetching products by type {}: {}", type.getSimpleName(), e.getMessage());
            return List.of();  // Return empty list in case of error
        }
    }

    /**
     * Menyaring produk berdasarkan kode yang valid
     * @param code Kode produk
     * @return Optional Produk (lebih aman jika produk tidak ditemukan)
     */
    public Optional<Product> findByCodeOptional(String code) {
        if (!StringUtils.hasText(code)) {
            logger.warn("Product code is empty or invalid.");
            return Optional.empty();
        }
        
        Product product = productRepository.findByCode(code.trim());
        if (product == null) {
            logger.warn("Product with code '{}' not found.", code);
        }
        return Optional.ofNullable(product);
    }
}
