package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.PerishableProduct;
import com.codertampan.my_pos_maret.entity.NonPerishableProduct;
import com.codertampan.my_pos_maret.entity.DigitalProduct;
import com.codertampan.my_pos_maret.entity.BundleProduct;
import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.service.ProductLogService;
import com.codertampan.my_pos_maret.service.ProductService;
import com.codertampan.my_pos_maret.service.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "add-product", layout = MainLayout.class)
@PageTitle("Tambah Produk")
@CssImport("./styles.css")
public class AddProductView extends VerticalLayout {

    private final ProductService productService;
    private final UserSession userSession;

    private final TextField nameField = new TextField("Nama Produk");
    private final TextField codeField = new TextField("Kode Produk");
    private final NumberField priceField = new NumberField("Harga");
    private final NumberField stockField = new NumberField("Stok");
    private final DatePicker expiryDatePicker = new DatePicker("Tanggal Kadaluarsa");
    private final TextField vendorField = new TextField("Vendor Produk");
    private final TextField urlField = new TextField("Url Produk");
    private final ComboBox<String> typeField = new ComboBox<>("Tipe Produk");

    @Autowired
    public AddProductView(ProductService productService, UserSession userSession) {
        this.productService = productService;
        this.userSession = userSession;

        addClassName("add-product-view");
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Tambah Produk Baru");
        title.addClassName("add-product-title");
        add(title);

        nameField.setPlaceholder("Misal: Indomie Goreng");
        codeField.setPlaceholder("Misal: P001");
        priceField.setPlaceholder("Harga per item");
        stockField.setPlaceholder("Jumlah stok");
        expiryDatePicker.setPlaceholder("yyyy-MM-dd");
        vendorField.setPlaceholder("Nama Vendor");
        urlField.setPlaceholder("Url Link");

        typeField.setItems("Perishable", "Non-Perishable", "Digital", "Bundle"); // Pilihan tipe produk manual
        typeField.setPlaceholder("Pilih tipe produk");

        HorizontalLayout formLayout = new HorizontalLayout(
                nameField, codeField, priceField, stockField, expiryDatePicker, typeField, vendorField, urlField
        );
        formLayout.setSpacing(true);
        add(formLayout);

        Button saveButton = new Button("Simpan", e -> simpanProduk());
        add(saveButton);
    }

    @Autowired
    private ProductLogService productLogService;

    private void simpanProduk() {
        String name = nameField.getValue();
        String code = codeField.getValue();
        Double price = priceField.getValue();
        Double stock = stockField.getValue();
        LocalDate expiryDate = expiryDatePicker.getValue();
        String tipe = typeField.getValue();
        String vendor = vendorField.getValue();
        String urls = urlField.getValue();
        
        if (name.isEmpty() || code.isEmpty() || price == null || stock == null || tipe == null) {
            Notification.show("Semua field harus diisi!");
            return;
        }
        
        Product product; // <- Ini deklarasi di luar blok if/else
        
        //"Perishable", "Non-Perishable", "Digital", "Bundle"
        if (tipe.equals("Perishable")) {
            if (expiryDate == null) {
                Notification.show("Tanggal kadaluarsa wajib diisi untuk produk perishable!");
                return;
            }
            PerishableProduct perishable = new PerishableProduct(code, name, price, stock.intValue(), expiryDate);
            product = perishable;
        } else if (tipe.equals("Non-Perishable")) {
            NonPerishableProduct nonPerishable = new NonPerishableProduct();
            nonPerishable.setName(name);
            nonPerishable.setCode(code);
            nonPerishable.setPrice(price);
            nonPerishable.setStock(stock.intValue());
            product = nonPerishable;
        } else if (tipe.equals("Digital")) {
            DigitalProduct digital = new DigitalProduct();
            digital.setName(name);
            digital.setCode(code);
            digital.setPrice(price);
            digital.setStock(stock.intValue());
            digital.setVendor(vendor);
            digital.setUrl(urls);
            product = digital;
        } else if (tipe.equals("Bundle")) {
            BundleProduct bundle = new BundleProduct();
            bundle.setName(name);
            bundle.setCode(code);
            bundle.setPrice(price);
            //bundle.setStock(stock.intValue());
            product = bundle;
        } else {
            Notification.show("Tipe produk tidak dikenal!");
            return;
        }
        
        try {
            // Simpan produk ke database
            productService.saveProduct(product);
            
            // Log produk yang baru ditambahkan
            String sellerUsername = userSession.getUser().getUsername();
            productLogService.logProductCreated(sellerUsername, code, name, price, stock.intValue());


            Notification.show("Produk berhasil disimpan!");
        
            // Reset fields
            nameField.clear();
            codeField.clear();
            priceField.clear();
            stockField.clear();
            expiryDatePicker.clear();
            typeField.clear();
        } catch (Exception e) {
            Notification.show("Gagal menyimpan produk: " + e.getMessage());
        }
    }

}
