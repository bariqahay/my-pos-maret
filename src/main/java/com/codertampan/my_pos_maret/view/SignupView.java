package com.codertampan.my_pos_maret.view;

import com.codertampan.my_pos_maret.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("signup")
@CssImport("./styles.css")
@PageTitle("Signup")
public class SignupView extends VerticalLayout {

    private final UserService userService;

    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField confirmPassword = new PasswordField("Confirm Password");
    private final PasswordField adminPassword = new PasswordField("Admin Password (if you want to be admin)");
    private final Button signupButton = new Button("Sign Up");

    public SignupView(UserService userService) {
        this.userService = userService;

        setupLayout();
        setupSignupAction();
    }

    private void setupLayout() {
        addClassName("signup-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setPadding(false);
        setSpacing(false);

        Div card = new Div();
        card.addClassName("signup-card");

        H2 header = new H2("Create Account");
        header.addClassName("signup-header");

        username.setWidthFull();
        password.setWidthFull();
        confirmPassword.setWidthFull();
        adminPassword.setWidthFull();
        signupButton.addClassName("primary-button");

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("signup-form");
        formLayout.add(header, username, password, confirmPassword, adminPassword, signupButton);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        formLayout.setWidth("400px");

        card.add(formLayout);
        add(card);
    }

    private void setupSignupAction() {
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
                getUI().ifPresent(ui -> ui.navigate(""));
            } else {
                username.setInvalid(true);
                username.setErrorMessage("Username already taken");
                Notification.show("Signup failed. Username may already exist.", 3000, Notification.Position.MIDDLE);
            }
        });
    }
}
