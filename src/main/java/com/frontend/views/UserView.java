package com.frontend.views;

import com.frontend.domainDto.request.UpdateUserDto;
import com.frontend.domainDto.response.UserDto;
import com.frontend.service.UserService;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.LoggerFactory;

@AnonymousAllowed
@Route(value = "/user", layout = MainLayout.class)
@PageTitle("User settings | Garage Booking Service")
public class UserView extends VerticalLayout {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserView.class);
    private final Binder<UpdateUserDto> binder = new BeanValidationBinder<>(UpdateUserDto.class);
    private final Binder<Object> binder2 = new Binder<>();
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private final UserService userService;
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");
    private EmailField email = new EmailField("Email address");
    private TextField phoneNumber = new TextField("Phone number");
    private FormLayout formLayout = new FormLayout(firstName, lastName, username, password, confirmPassword, email, phoneNumber);
    public UserView(UserService userService) {
        this.userService = userService;
        UserDto userDto = userService.getUser(currentUsername);
        binder.bindInstanceFields(this);

        H2 h2 = new H2("Personal information:");
        add(h2);

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2),
                new FormLayout.ResponsiveStep("1000px", 3));
        formLayout.setMaxWidth("1500px");
        LOGGER.info("Retrieved user: " + userDto);
        firstName.setValue(userDto.getFirstName());
        lastName.setValue(userDto.getLastName());
        username.setValue(userDto.getUsername());
        username.setReadOnly(true);
        email.setValue(userDto.getEmail());
        phoneNumber.setValue(userDto.getPhoneNumber());
        confirmPassword.setRequired(true);
        confirmPassword.setErrorMessage("Passwords must match.");
        confirmPassword.addValueChangeListener(event -> {
            if (password.getValue() != null) {
                if (!password.getValue().equals(confirmPassword.getValue())) {
                    LOGGER.info("Password: " + password.getValue() + " confirm: " + confirmPassword.getValue());
                    confirmPassword.setInvalid(true);
                    Notification.show("Error");
                } else {
                    LOGGER.info("False");
                    confirmPassword.setInvalid(false);
                    Notification.show("Match");
                }
            }
        });
        add(formLayout);
    }
}
