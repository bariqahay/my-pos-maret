package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("signup")
@CssImport("./styles.css")
public class SignupView extends VerticalLayout {

    private final UserService userService; // Declare userService

    public SignupView(UserService userService) {
        this.userService = userService; // Initialize userService using constructor injection

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create signup card
        Div card = new Div();
        card.addClassName("signup-card");
        
        // Header
        H2 header = new H2("Create Account");
        header.addClassName("signup-header");

        // Form fields
        TextField username = new TextField("Username");
        username.setWidthFull();
        PasswordField password = new PasswordField("Password");
        password.setWidthFull();
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        confirmPassword.setWidthFull();
        PasswordField adminPassword = new PasswordField("Admin Password (optional)");
        adminPassword.setWidthFull();

        // Buttons
        Button signupButton = new Button("Sign Up");
        signupButton.addClassName("primary-button");

        // Add action listener
        signupButton.addClickListener(e -> {
            String enteredUsername = username.getValue().trim();
            String enteredPassword = password.getValue();
            String enteredConfirmPassword = confirmPassword.getValue();
            String enteredAdminPassword = adminPassword.getValue();

            if (!enteredPassword.equals(enteredConfirmPassword)) {
                confirmPassword.setInvalid(true);
                confirmPassword.setErrorMessage("Passwords do not match");
                return;
            }

            boolean success = userService.createUser(enteredUsername, enteredPassword, enteredAdminPassword);

            if (success) {
                Notification.show("Signup successful!", 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate("login"));
            } else {
                username.setInvalid(true);
                username.setErrorMessage("Username already taken");
                Notification.show("Signup failed. Username may already exist.", 3000, Notification.Position.MIDDLE);
            }
        });

        // Form layout
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("signup-form");
        formLayout.add(header, username, password, confirmPassword, adminPassword, signupButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidth("400px");

        card.add(formLayout);
        add(card);
    }
}
