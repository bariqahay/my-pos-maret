package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.entity.User;
import com.codertampan.my_pos_maret.entity.UserRole;
import com.codertampan.my_pos_maret.service.UserService;
import com.codertampan.my_pos_maret.service.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;

@Route(value = "admin/users", layout = MainLayout.class)
@PageTitle("Manajemen User")
public class AdminUserManagementView extends VerticalLayout {

    private final UserService userService;
    private final UserSession userSession;

    private Grid<User> userGrid = new Grid<>(User.class);
    private TextField usernameField = new TextField("Username");
    private PasswordField passwordField = new PasswordField("Password");
    private ComboBox<UserRole> roleField = new ComboBox<>("Role");
    private Button saveButton = new Button("Simpan");
    private Button newButton = new Button("User Baru");
    private Button deleteButton = new Button("Hapus");

    private User selectedUser;

    @Autowired
    public AdminUserManagementView(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;

        // Redirect jika bukan admin
        if (!currentUserIsAdmin()) {
            Notification.show("❌ Akses ditolak. Halaman ini hanya untuk admin.", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
            return;
        }

        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Manajemen Kasir & Admin");
        add(title);

        setupGrid();
        setupForm();
        refreshGrid();
    }

    private void setupGrid() {
        userGrid.setColumns("id", "username", "role", "createdAt");
        userGrid.setItems(userService.getAllUsers());
        userGrid.asSingleSelect().addValueChangeListener(e -> populateForm(e.getValue()));
        add(userGrid);
    }

    private void setupForm() {
        roleField.setItems(UserRole.values());

        saveButton.addClickListener(e -> saveUser());
        newButton.addClickListener(e -> clearForm());
        deleteButton.addClickListener(e -> {
            if (selectedUser != null) {
                userService.deleteUser(selectedUser.getId());
                Notification.show("User dihapus");
                refreshGrid();
                clearForm();
            }
        });

        HorizontalLayout buttons = new HorizontalLayout(saveButton, newButton, deleteButton);
        add(usernameField, passwordField, roleField, buttons);
    }

    private void saveUser() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();
        UserRole role = roleField.getValue();

        if (username.isEmpty() || role == null) {
            Notification.show("Username dan role wajib diisi");
            return;
        }

        if (selectedUser == null) {
            boolean created = userService.createUser(username, password, role == UserRole.ADMIN ? "haloadmin" : "");
            if (!created) {
                Notification.show("Gagal membuat user. Username mungkin sudah digunakan.");
                return;
            }
        } else {
            selectedUser.setUsername(username);
            if (!password.isEmpty()) {
                selectedUser.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
            }
            selectedUser.setRole(role);
            userService.saveUser(selectedUser);
        }

        Notification.show("User disimpan");
        refreshGrid();
        clearForm();
    }

    private void clearForm() {
        selectedUser = null;
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
    }

    private void populateForm(User user) {
        selectedUser = user;
        usernameField.setValue(user.getUsername());
        passwordField.clear();
        roleField.setValue(user.getRole());
    }

    private void refreshGrid() {
        List<User> users = userService.getAllUsers();
        userGrid.setItems(users);
    }

    private boolean currentUserIsAdmin() {
        User currentUser = userSession.getUser();
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN;
    }
}
