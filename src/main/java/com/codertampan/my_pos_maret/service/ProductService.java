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

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    // Method to fetch all products
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();  // Returns all products
        } catch (Exception e) {
            logger.error("Error fetching all products: {}", e.getMessage());
            return List.of();  // Return an empty list in case of error
        }
    }

    public void deleteProduct(Product product) {
        if (product == null || product.getId() == null) {
            logger.error("Product or product ID is null. Cannot delete.");
            return;
        }
        try {
            productRepository.delete(product);
            logger.info("Product with ID '{}' deleted successfully.", product.getId());
        } catch (Exception e) {
            logger.error("Failed to delete product with ID '{}'. Error: {}", product.getId(), e.getMessage());
        }
    }

    public void saveProduct(Product product) {
        if (product == null) {
            logger.warn("Product is null. Cannot save.");
            return;
        }
        try {
            productRepository.save(product);
            logger.info("Product with code '{}' saved successfully.", product.getCode());
        } catch (Exception e) {
            logger.error("Failed to save product with code '{}'. Error: {}", product.getCode(), e.getMessage());
        }
    }

    public Product getProductById(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }
    
    // Other methods like updateProduct(), findByCode(), etc.
}
