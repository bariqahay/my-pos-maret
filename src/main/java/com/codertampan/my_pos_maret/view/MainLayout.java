package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.User;
import com.codertampan.my_pos_maret.service.UserSession;
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
        // Ambil user dari session
        User currentUser = userSession.getUser();

        // Kalau user belum login, pake info default (misalnya Guest)
        String usernameText = currentUser != null ? currentUser.getUsername() : "Guest";
        String roleText = currentUser != null ? currentUser.getRole().toString() : "Guest";

        // Avatar, username, dan role
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

        // Menu links
        RouterLink dashboardLink = new RouterLink("🏠 Dashboard", DashboardView.class);
        dashboardLink.getStyle()
            .set("text-decoration", "none")
            .set("color", "#333")
            .set("padding", "0.75rem 1rem")
            .set("border-radius", "8px");

        RouterLink productListLink = new RouterLink("🏠 Product List", ProductListView.class);
        productListLink.getStyle()
            .set("text-decoration", "none")
            .set("color", "#333")
            .set("padding", "0.75rem 1rem")
            .set("border-radius", "8px");

        // Add "User Management" link for admins
        RouterLink userManagementLink = new RouterLink("👥 User Management", AdminUserManagementView.class);
        userManagementLink.getStyle()
            .set("text-decoration", "none")
            .set("color", "#333")
            .set("padding", "0.75rem 1rem")
            .set("border-radius", "8px");

        // Tombol logout
        Button logoutButton = new Button("Logout", e -> logout());

        logoutButton.getStyle()
            .set("text-decoration", "none")
            .set("color", "red")
            .set("padding", "0.75rem 1rem")
            .set("border-radius", "8px");

        // Menambahkannya ke layout menu
        VerticalLayout menuLayout = new VerticalLayout(dashboardLink, productListLink, userManagementLink, logoutButton);
        menuLayout.setPadding(true);
        menuLayout.setSpacing(false);

        // Layout drawer
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
