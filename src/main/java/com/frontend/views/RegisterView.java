package com.frontend.views;

import com.frontend.domainDto.request.RegisterUserDto;
import com.frontend.service.AuthenticationService;
import com.frontend.service.LoginRegisterService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route("register")
@PageTitle("Register | Garage Booking Service")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
    private final LoginRegisterService loginRegisterService;
    private final AuthenticationService authenticationService;
    private final Binder<RegisterUserDto> binder = new BeanValidationBinder<>(RegisterUserDto.class);
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final EmailField email = new EmailField("Email address");
    private final TextField phoneNumber = new TextField("Phone number");
    private final Button createUserButton = new Button("Create account.");
    private final Button loginButton = new Button("Log in here if you already have account.");
    private final VerticalLayout centerFormLayout = new VerticalLayout();
    private final FormLayout formLayout = new FormLayout();

    public RegisterView(LoginRegisterService loginRegisterService, AuthenticationService authenticationService) {
        this.loginRegisterService = loginRegisterService;
        this.authenticationService = authenticationService;

        binder.bindInstanceFields(this);

        addRegisterHeader();
        addFormLayout();
        addCenterFormLayoutSettings();

        addButtonLayout();

        setAlignMode();

        addButtonClickListeners();
    }

    private void addRegisterHeader() {
        H1 h1 = new H1("Welcome in Garage Booking Service!");
        h1.addClassName(LumoUtility.Margin.Horizontal.AUTO);
        add(h1);
        add(new H3("Create new account or log in."));
    }

    private void addFormLayout() {
        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(firstName, lastName, email, phoneNumber, username, password);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2),
                new FormLayout.ResponsiveStep("1000px", 3));
        formLayout.setMaxWidth("1500px");
        centerFormLayout.add(formLayout);
    }

    private void addCenterFormLayoutSettings() {
        centerFormLayout.setAlignItems(Alignment.CENTER);
        centerFormLayout.setHorizontalComponentAlignment(Alignment.CENTER, formLayout);
        centerFormLayout.setSizeFull();
        add(centerFormLayout);
    }

    private void addButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        createUserButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(createUserButton);
        buttonLayout.add(loginButton);
        add(buttonLayout);
    }

    private void setAlignMode() {
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void addButtonClickListeners() {
        createUserButton.addClickListener(e -> {
            RegisterUserDto userToRegister = new RegisterUserDto();
            if (binder.writeBeanIfValid(userToRegister)) {
                if (loginRegisterService.isRegistered(userToRegister.getUsername())) {
                    Notification.show("Account with given username: " + userToRegister.getUsername() + " already exist. Change the username in order to register new account.");
                } else {
                    authenticationService.registerUser(userToRegister);
                    Notification.show("Account created, you can log in now.");
                    clearForm();
                }
            } else {
                Notification.show("All fields must be properly filled.");
            }
        });

        loginButton.addClickListener(e -> UI.getCurrent().getPage().setLocation("/login"));
    }

    private void clearForm() {
        binder.setBean(new RegisterUserDto());
    }
}
