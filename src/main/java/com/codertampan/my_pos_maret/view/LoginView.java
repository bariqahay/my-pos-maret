package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport("./styles.css")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

    private final UserService userService;

    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button loginButton = new Button("Login");
    private Button signupButton = new Button("Don't have an account? Sign up!");

    public LoginView(UserService userService) {
        this.userService = userService;

        // Main container styling
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(false);
        setSpacing(false);

        // Create login card
        Div card = new Div();
        card.addClassName("login-card");

        // Header
        H2 header = new H2("Welcome Back");
        header.addClassName("login-header");

        // Form fields
        username.setWidthFull();
        password.setWidthFull();

        // Buttons
        loginButton.addClassName("primary-button");
        signupButton.addClassName("secondary-button");

        // Button actions
        loginButton.addClickListener(e -> {
            String enteredUsername = username.getValue().trim();
            String enteredPassword = password.getValue();
        
            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                username.setInvalid(true);
                password.setInvalid(true);
                username.setErrorMessage("Username and password are required");
                password.setErrorMessage("Username and password are required");
                return;
            }
        
            boolean isAuthenticated = userService.validateLogin(enteredUsername, enteredPassword);
            if (isAuthenticated) {
                getUI().ifPresent(ui -> ui.navigate("dashboard"));
            } else {
                username.setInvalid(true);
                password.setInvalid(true);
                username.setErrorMessage("Invalid username or password");
                password.setErrorMessage("Invalid username or password");
            }
        });

        signupButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("signup"));
        });

        // Form layout
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("login-form");
        formLayout.add(header, username, password, loginButton, signupButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidth("400px");

        card.add(formLayout);
        add(card);
    }
}
