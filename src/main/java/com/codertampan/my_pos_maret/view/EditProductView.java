package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.service.ProductService;
import com.codertampan.my_pos_maret.service.ProductLogUpdate;
import com.codertampan.my_pos_maret.service.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "edit-product/:id", layout = MainLayout.class)
@PageTitle("Edit Produk")
@PermitAll
public class EditProductView extends VerticalLayout implements BeforeEnterObserver {

    private final ProductService productService;
    private final ProductLogUpdate productLogUpdate;
    private final UserSession userSession;

    private TextField nameField = new TextField("Nama");
    private TextField codeField = new TextField("Kode");
    private NumberField priceField = new NumberField("Harga");
    private NumberField stockField = new NumberField("Stok");

    private Button saveButton = new Button("Simpan");

    private Long productId;

    @Autowired
    public EditProductView(ProductService productService, ProductLogUpdate productLogUpdate, UserSession userSession) {
        this.productService = productService;
        this.productLogUpdate = productLogUpdate;
        this.userSession = userSession;

        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Edit Produk");
        add(title);

        FormLayout formLayout = new FormLayout();
        formLayout.add(nameField, codeField, priceField, stockField, saveButton);
        add(formLayout);

        saveButton.addClickListener(e -> saveProduct());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idStr = event.getRouteParameters().get("id").orElse(null);
        if (idStr != null) {
            try {
                productId = Long.parseLong(idStr);
                Product product = productService.getProductById(productId);
                if (product != null) {
                    nameField.setValue(product.getName());
                    codeField.setValue(product.getCode());
                    priceField.setValue(product.getPrice());
                    stockField.setValue((double) product.getStock());
                } else {
                    Notification.show("Produk tidak ditemukan");
                    event.forwardTo("dashboard");
                }
            } catch (NumberFormatException e) {
                Notification.show("ID tidak valid");
                event.forwardTo("dashboard");
            }
        }
    }

    private void saveProduct() {
        if (productId != null) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                String oldName = product.getName();
                String oldCode = product.getCode();
                double oldPrice = product.getPrice();
                int oldStock = product.getStock();

                // Update produk
                product.setName(nameField.getValue());
                product.setCode(codeField.getValue());
                product.setPrice(priceField.getValue());
                product.setStock(stockField.getValue().intValue());

                // Get the seller's username from the session
                String sellerUsername = userSession.getUser().getUsername();

                // Log perubahan
                if (!oldName.equals(nameField.getValue())) {
                    productLogUpdate.logProductUpdated(sellerUsername, oldCode, "name", oldName, nameField.getValue());
                }
                if (oldPrice != priceField.getValue()) {
                    productLogUpdate.logProductUpdated(sellerUsername, oldCode, "price", String.valueOf(oldPrice), String.valueOf(priceField.getValue()));
                }
                if (oldStock != stockField.getValue().intValue()) {
                    productLogUpdate.logProductUpdated(sellerUsername, oldCode, "stock", String.valueOf(oldStock), String.valueOf(stockField.getValue().intValue()));
                }

                productService.saveProduct(product);
                Notification.show("Produk berhasil diperbarui");
            }
        }
    }
}
