package com.codertampan.my_pos_maret.service;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    // Method to update a product
    public Product updateProduct(Product product) {
        if (product == null || product.getId() == null) {
            logger.error("Product or product ID is null. Cannot update.");
            throw new IllegalArgumentException("Product or product ID is null.");
        }

        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if (existingProduct.isPresent()) {
            Product updatedProduct = productRepository.save(product);
            logger.info("Product with ID '{}' updated successfully.", product.getId());
            return updatedProduct;
        } else {
            logger.warn("Product with ID '{}' not found for update.", product.getId());
            throw new IllegalArgumentException("Product not found for update.");
        }
    }

    // Method to fetch all products
    public List<Product> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                logger.warn("No products found.");
            }
            return products;
        } catch (Exception e) {
            logger.error("Error fetching all products: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch products.", e);  // Rethrow exception or handle differently
        }
    }

    // Method to delete a product
    @Transactional
    public void deleteProduct(Product product) {
        if (product == null || product.getId() == null) {
            logger.error("Product or product ID is null. Cannot delete.");
            throw new IllegalArgumentException("Product or product ID is null.");
        }
        try {
            if (productRepository.existsById(product.getId())) {
                productRepository.delete(product);
                logger.info("Product with ID '{}' deleted successfully.", product.getId());
            } else {
                logger.warn("Product with ID '{}' not found for deletion.", product.getId());
                throw new IllegalArgumentException("Product not found for deletion.");
            }
        } catch (Exception e) {
            logger.error("Failed to delete product with ID '{}'. Error: {}", product.getId(), e.getMessage());
            throw new RuntimeException("Failed to delete product.", e);  // Rethrow exception or handle differently
        }
    }

    // Method to save a product (either new or updated)
    @Transactional
    public Product saveProduct(Product product) {
        if (product == null) {
            logger.warn("Product is null. Cannot save.");
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (StringUtils.isEmpty(product.getCode()) || StringUtils.isEmpty(product.getName())) {
            logger.warn("Product code or name is empty. Cannot save product.");
            throw new IllegalArgumentException("Product code or name cannot be empty.");
        }
        try {
            Product savedProduct = productRepository.save(product);
            logger.info("Product with code '{}' saved successfully.", product.getCode());
            return savedProduct;
        } catch (Exception e) {
            logger.error("Failed to save product with code '{}'. Error: {}", product.getCode(), e.getMessage());
            throw new RuntimeException("Failed to save product.", e);
        }
    }

    // Method to get a product by ID
    public Product getProductById(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            logger.warn("Product with ID '{}' not found.", id);
            throw new IllegalArgumentException("Product not found.");
        }
        return optionalProduct.get();  // Return the product if found
    }
}
