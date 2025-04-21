package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.PerishableProduct;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

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
        // Set columns for displaying product data
        grid.setColumns("name", "productType", "code", "price", "stock");

        // Dynamically add expiryDate column for PerishableProduct
        grid.addColumn(product -> {
            if (product instanceof PerishableProduct) {
                return ((PerishableProduct) product).getExpiryDate();
            }
            return null;  // No expiryDate for non-perishable products
        }).setHeader("Expiry Date");

        // Set auto-width for all columns
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Add Edit and Delete buttons for each product row
        grid.addComponentColumn(product -> {
            Button editButton = new Button("Edit", e -> editProduct(product));
            Button deleteButton = new Button("Delete", e -> deleteProduct(product));

            HorizontalLayout actionLayout = new HorizontalLayout(editButton, deleteButton);
            return actionLayout;
        }).setHeader("Actions");

        // Add Lumo row stripes variant for visual style
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void updateList() {
        // Update the grid items by fetching the product list from the service
        grid.setItems(productService.getAllProducts());
    }

    private void editProduct(Product product) {
        // Navigate to the product edit page
        getUI().ifPresent(ui -> ui.navigate("edit-product/" + product.getId()));
    }

    private void deleteProduct(Product product) {
        // Delete the product and refresh the grid after deletion
        productService.deleteProduct(product);
        updateList();
        Notification.show("Produk berhasil dihapus");
    }
}
