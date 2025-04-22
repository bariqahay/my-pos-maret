package com.codertampan.my_pos_maret.transaction;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.entity.SalesLog;
import com.codertampan.my_pos_maret.entity.Stokable;
import com.codertampan.my_pos_maret.service.ProductService;
import com.codertampan.my_pos_maret.service.SalesLogService;

import java.time.LocalDateTime;
import java.util.List;

public class PurchaseTransaction extends Transaction implements Payable {

    private final List<CartItem> items;
    private final ProductService productService;
    private final SalesLogService salesLogService;
    private final String sellerUsername;

    public PurchaseTransaction(List<CartItem> items, ProductService productService, SalesLogService salesLogService, String sellerUsername) {
        this.items = items;
        this.productService = productService;
        this.salesLogService = salesLogService;
        this.sellerUsername = sellerUsername;
    }

    // Hitung total semua item di keranjang
    public double calculateTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    // Validasi stok sebelum transaksi
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

    // Proses transaksi: update stok dan catat log
    public void processTransaction() {
        try {
            validateStock(); // Cek stok dulu sebelum lanjut

            for (CartItem item : items) {
                Product product = item.getProduct();

                if (product instanceof Stokable stokableProduct) {
                    reduceStockAndSave(stokableProduct, item.getQuantity());
                }

                // Save ke sales log
                SalesLog log = SalesLog.builder()
                .productId(Math.toIntExact(product.getId())) 
                .productName(product.getName())
                .quantity(item.getQuantity())
                .totalAmount(item.getSubtotal())
                .sellerUsername(sellerUsername)
                .transactionDate(LocalDateTime.now()) // Pastikan ada tanggal yang valid
                .build();

            

                salesLogService.save(log);
            }

            System.out.println("Transaksi dengan ID " + getTransactionId() + " telah diproses");

        } catch (InsufficientStockException e) {
            System.err.println(e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error during transaction processing: " + e.getMessage());
            throw new RuntimeException("Transaction processing failed", e);
        }
    }

    // Kurangi stok dan simpan perubahan produk
    private void reduceStockAndSave(Stokable stokableProduct, int quantity) {
        stokableProduct.reduceStock(quantity);

        if (stokableProduct instanceof Product product) {
            try {
                productService.updateProduct(product);
            } catch (Exception e) {
                System.err.println("Gagal update produk: " + product.getName());
                throw new RuntimeException("Gagal update produk: " + product.getName(), e);
            }
        } else {
            System.err.println("Bukan instance dari Product.");
            throw new RuntimeException("Tipe produk ga valid.");
        }
    }

    // Output serialisasi transaksi
    public String serializeTransaction() {
        String output = "Purchase Transaction: " + getTransactionId() + ", Total: " + calculateTotal();
        System.out.println(output);
        return output;
    }

    // Custom Exception kalo stok ga cukup
    public static class InsufficientStockException extends RuntimeException {
        public InsufficientStockException(String message) {
            super(message);
        }
    }
}
