RouterLink dashboardLink = new RouterLink("🏠 Dashboard", DashboardView.class);
dashboardLink.getStyle()
    .set("text-decoration", "none")
    .set("color", "#333")
    .set("padding", "0.75rem 1rem")
    .set("border-radius", "8px");

RouterLink productListLink = new RouterLink("📦 Product List", ProductListView.class);
productListLink.getStyle()
    .set("text-decoration", "none")
    .set("color", "#333")
    .set("padding", "0.75rem 1rem")
    .set("border-radius", "8px");

RouterLink userManagementLink = new RouterLink("👥 User Management", AdminUserManagementView.class);
userManagementLink.getStyle()
    .set("text-decoration", "none")
    .set("color", "#333")
    .set("padding", "0.75rem 1rem")
    .set("border-radius", "8px");

Button logoutButton = new Button("Logout", e -> logout());
logoutButton.getStyle()
    .set("color", "red")
    .set("padding", "0.75rem 1rem")
    .set("border-radius", "8px");

VerticalLayout menuLayout = new VerticalLayout(
    dashboardLink,
    productListLink,
    userManagementLink,
    logoutButton
);
menuLayout.setPadding(true);
menuLayout.setSpacing(false);
