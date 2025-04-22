package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.service.ProductService;
import com.codertampan.my_pos_maret.service.SalesLogService;
import com.codertampan.my_pos_maret.service.TransactionModificationLogService;
import com.codertampan.my_pos_maret.service.TransactionItemLogService;
import com.codertampan.my_pos_maret.transaction.CartItem;
import com.codertampan.my_pos_maret.transaction.PurchaseTransaction;
import com.codertampan.my_pos_maret.transaction.PurchaseTransaction.InsufficientStockException;
import com.codertampan.my_pos_maret.service.UserSession;
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
    private final SalesLogService salesLogService;
    private final UserSession userSession;
    private final TransactionModificationLogService transactionModificationLogService;
    private final TransactionItemLogService transactionItemLogService;

    private final List<CartItem> cart = new ArrayList<>();
    private double total = 0.0;

    private final ComboBox<Product> kodeField = new ComboBox<>("Kode Barang");
    private final NumberField qtyField = new NumberField("Jumlah");
    private final NumberField bayarField = new NumberField("Pembayaran");

    private final Paragraph totalText = new Paragraph("Total: Rp 0");
    private final Paragraph kembalianText = new Paragraph("Kembalian: Rp 0");

    private final Grid<CartItem> cartGrid = new Grid<>(CartItem.class);

    @Autowired
    public DashboardView(ProductService productService, SalesLogService salesLogService, UserSession userSession, TransactionModificationLogService trxLogService, TransactionItemLogService itemLogService) {
        this.productService = productService;
        this.salesLogService = salesLogService;
        this.userSession = userSession;
        this.transactionModificationLogService = trxLogService;
        this.transactionItemLogService = itemLogService;

        addClassName("dashboard-view");
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Transaksi Penjualan Barang");
        title.addClassName("dashboard-title");
        add(title);

        setupKodeField();

        HorizontalLayout inputLayout = new HorizontalLayout();
        inputLayout.addClassName("input-section");
        qtyField.setValue(1.0);
        Button cariButton = new Button("Cari", e -> cariBarang());
        inputLayout.add(kodeField, qtyField, cariButton);
        add(inputLayout);

        setupCartGrid();

        Button hapusItemBtn = new Button("Hapus Item dari Cart", e -> {
            CartItem selected = cartGrid.asSingleSelect().getValue();
            if (selected != null) {
                cart.remove(selected);
                refreshCart();

                // Logging hapus item
                String username = userSession.getUser().getUsername();
                String dummyTrxId = "TRX-DRAFT-" + System.currentTimeMillis();
                String itemCode = selected.getProduct().getCode();
                transactionModificationLogService.log(dummyTrxId, username, "REMOVE_ITEM", itemCode);

                Notification.show("Item " + itemCode + " dihapus dan dicatat ke log.");
            } else {
                Notification.show("Pilih item dulu sebelum hapus.");
            }
        });
        add(hapusItemBtn);

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
            kodeField.setEnabled(false);
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
            String sellerUsername = userSession.getUser().getUsername();

            PurchaseTransaction trx = new PurchaseTransaction(cart, productService, salesLogService, sellerUsername);
            trx.processTransaction();
            trx.serializeTransaction();

            String trxId = trx.getTransactionId();

            for (CartItem item : cart) {
                String detail = item.getProduct().getName() + " x" + item.getQuantity()
                        + " @ Rp " + item.getProduct().getPrice()
                        + " = Rp " + item.getSubtotal();
                transactionItemLogService.logTransactionItem(trxId, sellerUsername, "ITEM_DETAIL", detail);
            }

            transactionModificationLogService.log(trxId, sellerUsername, "TRANSACTION_COMPLETED", "-");

            cart.clear();
            refreshCart();

            Notification.show("Transaksi berhasil dengan ID: " + trxId);

        } catch (InsufficientStockException e) {
            Notification.show(e.getMessage());
        } catch (Exception e) {
            Notification.show("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
