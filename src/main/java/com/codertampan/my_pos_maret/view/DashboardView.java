package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.entity.Stokable;
import com.codertampan.my_pos_maret.service.ProductService;
import com.codertampan.my_pos_maret.transaction.CartItem;
import com.codertampan.my_pos_maret.transaction.PurchaseTransaction;
import com.codertampan.my_pos_maret.transaction.PurchaseTransaction.InsufficientStockException;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard")
@CssImport("./styles.css")
public class DashboardView extends VerticalLayout {

    private final ProductService productService;
    private final List<CartItem> cart = new ArrayList<>();
    private double total = 0.0;

    private final ComboBox<Product> kodeField = new ComboBox<>("Kode Barang");
    private final NumberField qtyField = new NumberField("Jumlah");
    private final NumberField bayarField = new NumberField("Pembayaran");

    private final Paragraph totalText = new Paragraph("Total: Rp 0");
    private final Paragraph kembalianText = new Paragraph("Kembalian: Rp 0");

    private final Grid<CartItem> cartGrid = new Grid<>(CartItem.class);

    @Autowired
    public DashboardView(ProductService productService) {
        this.productService = productService;

        addClassName("dashboard-view");
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Transaksi Penjualan Barang");
        title.addClassName("dashboard-title");
        add(title);

        // Setup ComboBox for product code
        setupKodeField();

        HorizontalLayout inputLayout = new HorizontalLayout();
        inputLayout.addClassName("input-section");
        qtyField.setValue(1.0);
        Button cariButton = new Button("Cari", e -> cariBarang());
        inputLayout.add(kodeField, qtyField, cariButton);
        add(inputLayout);

        setupCartGrid();

        totalText.addClassName("total-text");
        add(totalText);

        HorizontalLayout bayarLayout = new HorizontalLayout();
        bayarLayout.addClassName("bayar-section");
        Button prosesButton = new Button("Proses Transaksi", e -> prosesTransaksi());
        bayarLayout.add(bayarField, prosesButton);
        add(bayarLayout);

        kembalianText.addClassName("kembalian-text");
        add(kembalianText);
    }

    private void setupKodeField() {
        kodeField.setLabel("Kode Barang");
        kodeField.setItemLabelGenerator(Product::getCode);
        List<Product> products = productService.getAllProducts();
        
        if (products == null || products.isEmpty()) {
            Notification.show("No products available.");
            kodeField.setEnabled(false);  // Disable ComboBox if no products
        } else {
            kodeField.setItems(products);
        }
        kodeField.setAllowCustomValue(true);
    }

    private void setupCartGrid() {
        cartGrid.setColumns("product.name", "product.price", "quantity");
        cartGrid.setClassName("cart-grid");
        add(cartGrid);
    }

    private void cariBarang() {
        Product p = kodeField.getValue();
        if (p != null) {
            int qty = qtyField.getValue().intValue();
            if (qty <= 0) {
                Notification.show("Jumlah produk tidak valid.");
                return;
            }
            CartItem item = new CartItem(p, qty);
            cart.add(item);
            refreshCart();
        } else {
            Notification.show("Barang tidak ditemukan");
        }
    }

    private void refreshCart() {
        cartGrid.setItems(cart);
        total = cart.stream().mapToDouble(CartItem::getSubtotal).sum();
        totalText.setText("Total: Rp " + total);
    }

    private void prosesTransaksi() {
        double bayar = bayarField.getValue();

        if (bayar < total) {
            Notification.show("Pembayaran kurang!");
            return;
        }

        double kembali = bayar - total;
        kembalianText.setText("Kembalian: Rp " + kembali);

        try {
            // Process the transaction using PurchaseTransaction
            PurchaseTransaction trx = new PurchaseTransaction(cart, productService);
            trx.processTransaction(); // Could throw custom exceptions like InsufficientStockException
            trx.serializeTransaction();

            cart.clear();
            refreshCart();

            Notification.show("Transaksi berhasil dengan ID: " + trx.getTransactionId());

        } catch (InsufficientStockException e) {
            Notification.show(e.getMessage());
        } catch (Exception e) {
            Notification.show("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace(); // Optionally log the error for debugging
        }
    }

}
