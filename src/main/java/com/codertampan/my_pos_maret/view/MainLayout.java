package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.User;
import com.codertampan.my_pos_maret.service.UserSession;
import com.codertampan.my_pos_maret.view.AdminUserManagementView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles.css")
public class MainLayout extends AppLayout {

    private final UserSession userSession;
    
    public MainLayout(UserSession userSession) {
        this.userSession = userSession;
    
        User currentUser = userSession.getUser();
        String username = currentUser != null ? currentUser.getUsername() : "Guest";
        String role = currentUser != null ? currentUser.getRole().toString() : "-";
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        Span pos = new Span("Pos-");
        pos.getStyle()
            .set("color", "white")
            .set("font-weight", "bold");
    
        Span maret = new Span("Maret");
        maret.getStyle()
            .set("color", "red")
            .set("font-weight", "bold");
    
        Div logo = new Div(pos, maret);
        logo.getStyle()
            .set("font-size", "1.5rem")
            .set("display", "flex")
            .set("gap", "0.25rem")
            .set("align-items", "center");
    
        Header header = new Header(logo);
        header.getStyle()
            .set("background-color", "#4CAF50")
            .set("color", "white")
            .set("padding", "1rem");
    
        addToNavbar(header);
    }

    private void createDrawer() {
        User currentUser = userSession.getUser();
    
        String usernameText = currentUser != null ? currentUser.getUsername() : "Guest";
        String roleText = currentUser != null ? currentUser.getRole().toString() : "Guest";
    
        Avatar avatar = new Avatar(usernameText);
        H4 username = new H4(usernameText);
        Span role = new Span(roleText);
    
        username.getStyle().set("margin", "0");
        role.getStyle().set("font-size", "0.85rem").set("color", "gray");
    
        VerticalLayout userInfo = new VerticalLayout(avatar, username, role);
        userInfo.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        userInfo.setPadding(false);
        userInfo.setSpacing(false);
        userInfo.getStyle().set("padding", "1rem 0");
    
        // Menu Links
        RouterLink dashboardLink = new RouterLink("🏠 Dashboard", DashboardView.class);
        dashboardLink.getStyle().set("padding", "0.75rem 1rem");
    
        // Bikin layout menu
        VerticalLayout menuLayout = new VerticalLayout(dashboardLink);
    
        // Kondisional: Tambah Manajemen User kalau role ADMIN
        if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole().toString())) {
            RouterLink userManagementLink = new RouterLink("👥 Manajemen User", AdminUserManagementView.class);
            RouterLink productListLink = new RouterLink("📦 Product List", ProductListView.class);
            userManagementLink.getStyle().set("padding", "0.75rem 1rem");
            productListLink.getStyle().set("padding", "0.75rem 1rem");
            menuLayout.add(productListLink,userManagementLink);
        }
    
        // Logout button
        Button logoutButton = new Button("Logout", e -> logout());
        logoutButton.getStyle()
            .set("color", "red")
            .set("padding", "0.75rem 1rem");
    
        menuLayout.add(logoutButton);
    
        VerticalLayout drawerContent = new VerticalLayout(userInfo, menuLayout);
        drawerContent.setPadding(false);
        drawerContent.setSpacing(false);
        drawerContent.getStyle().set("background-color", "#f7f7f7");
    
        addToDrawer(drawerContent);
    }

    // Logika untuk logout
    private void logout() {
        // Clear session
        userSession.clear();

        // Redirect ke halaman login
        getUI().ifPresent(ui -> ui.navigate(""));
    }

}
