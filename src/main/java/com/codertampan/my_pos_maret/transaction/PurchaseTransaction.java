package com.codertampan.my_pos_maret.transaction;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.entity.Stokable;
import com.codertampan.my_pos_maret.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PurchaseTransaction extends Transaction implements Payable {

    private final List<CartItem> items;
    private final ProductService productService;

    @Autowired
    public PurchaseTransaction(List<CartItem> items, ProductService productService) {
        this.items = items;
        this.productService = productService;
    }

    // Calculate total transaction
    public double calculateTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    // Validate stock before transaction
    private void validateStock() {
        for (CartItem item : items) {
            Product product = item.getProduct();
            if (product instanceof Stokable stokableProduct) {
                if (stokableProduct.getStock() < item.getQuantity()) {
                    throw new InsufficientStockException("Stok tidak cukup untuk produk: " + product.getName());
                }
            }
        }
    }

    // Process transaction: update stock and save to DB
    public void processTransaction() {
        try {
            validateStock();  // Ensure stock is valid before proceeding

            // Update stock for each item
            for (CartItem item : items) {
                Product product = item.getProduct();
                if (product instanceof Stokable stokableProduct) {
                    // Reduce stock and save updated product
                    reduceStockAndSave(stokableProduct, item.getQuantity());
                }
            }

            // Log the transaction process
            System.out.println("Transaksi dengan ID " + getTransactionId() + " telah diproses");

        } catch (InsufficientStockException e) {
            // Handle insufficient stock error
            System.err.println(e.getMessage());
            throw e;  // Rethrow if you want to propagate the error
        } catch (Exception e) {
            // Handle any other unforeseen exceptions
            System.err.println("Error during transaction processing: " + e.getMessage());
            throw new RuntimeException("Transaction processing failed", e);
        }
    }

    // Reduces stock and saves the product to the database
    private void reduceStockAndSave(Stokable stokableProduct, int quantity) {
        stokableProduct.reduceStock(quantity);
    
        // If the product is an instance of Product, get its name using getName()
        if (stokableProduct instanceof Product product) {
            try {
                productService.updateProduct(product);  // Save the updated product
            } catch (Exception e) {
                System.err.println("Failed to update product: " + product.getName());
                throw new RuntimeException("Failed to update product: " + product.getName(), e);
            }
        } else {
            System.err.println("Product is not an instance of Product. Cannot access name.");
            throw new RuntimeException("Invalid product type. Cannot access product name.");
        }
    }

    // Serialize transaction for output
    public String serializeTransaction() {
        String output = "Purchase Transaction: " + getTransactionId() + ", Total: " + calculateTotal();
        System.out.println(output);
        return output;
    }

    // Custom exception for insufficient stock
    public static class InsufficientStockException extends RuntimeException {
        public InsufficientStockException(String message) {
            super(message);
        }
    }
}
