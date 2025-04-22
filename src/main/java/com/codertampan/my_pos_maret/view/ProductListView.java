package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.PerishableProduct;
import com.codertampan.my_pos_maret.entity.Product;
import com.codertampan.my_pos_maret.entity.User;
import com.codertampan.my_pos_maret.entity.UserRole;
import com.codertampan.my_pos_maret.service.ProductService;
import com.codertampan.my_pos_maret.service.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "product-list", layout = MainLayout.class)
@PageTitle("Product List | My POS")
public class ProductListView extends VerticalLayout {

    private final ProductService productService;
    private final Grid<Product> grid = new Grid<>(Product.class);

    public ProductListView(ProductService productService, UserSession userSession) {
        this.productService = productService;

        // Cek apakah user admin
        User currentUser = userSession.getUser();
        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            Notification.show("❌ Akses ditolak. Halaman ini hanya untuk admin.", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
            return;
        }

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
        grid.setColumns("name", "productType", "code", "price", "stock");

        grid.addColumn(product -> {
            if (product instanceof PerishableProduct) {
                return ((PerishableProduct) product).getExpiryDate();
            }
            return null;
        }).setHeader("Expiry Date");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addComponentColumn(product -> {
            Button editButton = new Button("Edit", e -> editProduct(product));
            Button deleteButton = new Button("Delete", e -> deleteProduct(product));

            HorizontalLayout actionLayout = new HorizontalLayout(editButton, deleteButton);
            return actionLayout;
        }).setHeader("Actions");

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void updateList() {
        grid.setItems(productService.getAllProducts());
    }

    private void editProduct(Product product) {
        getUI().ifPresent(ui -> ui.navigate("edit-product/" + product.getId()));
    }

    private void deleteProduct(Product product) {
        productService.deleteProduct(product);
        updateList();
        Notification.show("Produk berhasil dihapus");
    }
}