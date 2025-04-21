package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route(value = "product-list", layout = MainLayout.class)
@PageTitle("Product List | My POS")
public class ProductListView extends VerticalLayout {

    private final ProductService productService;
    private final Grid<Product> grid = new Grid<>(Product.class);

    public ProductListView(ProductService productService) {
        this.productService = productService;

        addClassName("product-list-view");
        setSizeFull();
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Daftar Produk");
        title.addClassName("product-list-title");

        Button addProductButton = new Button("Tambah Produk", e ->
                getUI().ifPresent(ui -> ui.navigate("add-product"))
        );

        HorizontalLayout topBar = new HorizontalLayout(title, addProductButton);
        topBar.setWidthFull();
        topBar.setJustifyContentMode(JustifyContentMode.BETWEEN);

        configureGrid();

        add(topBar, grid);
        updateList();
    }

    private void configureGrid() {
        grid.setColumns("name", "type", "code", "price", "stock", "expiryDate");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Menambahkan tombol Edit dan Delete di setiap baris
        grid.addComponentColumn(product -> {
            Button editButton = new Button("Edit", e -> editProduct(product));
            Button deleteButton = new Button("Delete", e -> deleteProduct(product));

            // Membuat layout untuk tombol Edit dan Delete
            HorizontalLayout actionLayout = new HorizontalLayout(editButton, deleteButton);
            return actionLayout;
        }).setHeader("Actions");

        // Optional: Menambahkan varian untuk style grid
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void updateList() {
        grid.setItems(productService.getAllProducts());
    }

    private void editProduct(Product product) {
        // Mengarahkan ke halaman edit produk dengan ID produk yang dipilih
        getUI().ifPresent(ui -> ui.navigate("edit-product/" + product.getId()));
    }

    private void deleteProduct(Product product) {
        // Menghapus produk dan memperbarui daftar setelah dihapus
        productService.deleteProduct(product);
        updateList();
        Notification.show("Produk berhasil dihapus");
    }
}
