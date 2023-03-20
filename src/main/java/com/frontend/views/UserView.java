package com.frontend.views;

import com.frontend.domainDto.request.UpdateUserDto;
import com.frontend.domainDto.response.UserDto;
import com.frontend.service.UserService;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
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
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private final UserService userService;
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField username = new TextField("Username");
    private PasswordField currentPassword = new PasswordField("Current password");
    private PasswordField newPassword = new PasswordField("New password");
    private PasswordField confirmPassword = new PasswordField("Confirm new password");
    private EmailField email = new EmailField("Email address");
    private TextField phoneNumber = new TextField("Phone number");
    private String pass ="";
    private FormLayout formLayout = new FormLayout(username, firstName, lastName,  email, phoneNumber, currentPassword, newPassword, confirmPassword);
    public UserView(UserService userService) {
        this.userService = userService;
        UserDto userDto = userService.getUser(currentUsername);

        bindFields();

        initForm(userDto);


    }

    private void bindFields() {
        binder.bindInstanceFields(this);

        Validator<String> currentPasswordValidator = (value, context) -> {
            if (newPassword.isEmpty() && confirmPassword.isEmpty()) {
                return ValidationResult.ok();
            } else if (currentPassword.isEmpty()) {
                return ValidationResult.error("Current password cannot be empty.");
            } else {
                return ValidationResult.ok();
            }
        };

        Validator<String> passwordValidator = (value, context) -> {
            if (newPassword.isEmpty() && confirmPassword.isEmpty()) {
                return ValidationResult.ok();
            } else if (!value.matches("(?=.*[A-Z])(?=.*[\\W])(?=\\S+$).{8,}")) {
                return ValidationResult.error("Password should contain at least 8 characters, one uppercase letter, and one special character.");
            } else if (!newPassword.getValue().equals(confirmPassword.getValue())) {
                return ValidationResult.error("Passwords do not match");
            } else {
                return ValidationResult.ok();
            }
        };

        Validator<String> confirmPasswordValidator = (value, context) -> {
            if (newPassword.isEmpty() && confirmPassword.isEmpty()) {
                return ValidationResult.ok();
            } else if (!newPassword.getValue().equals(confirmPassword.getValue())) {
                return ValidationResult.error("Passwords do not match");
            } else {
                return ValidationResult.ok();
            }
        };

        binder.forField(currentPassword)
                .withValidator(currentPasswordValidator)
                .bind(bean -> pass, (bean, value) -> pass = value);

        binder.forField(newPassword)
                .withValidator(passwordValidator)
                .bind(UpdateUserDto::getNewPassword, UpdateUserDto::setNewPassword);

        binder.forField(confirmPassword)
                .withValidator(confirmPasswordValidator)
                .bind(UpdateUserDto::getNewPassword, UpdateUserDto::setNewPassword);

        newPassword.addValueChangeListener(event -> binder.validate());
        confirmPassword.addValueChangeListener(event -> binder.validate());
    }

    private void initForm(UserDto userDto) {
        H2 h2 = new H2("Personal information:");
        add(h2);

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2),
                new FormLayout.ResponsiveStep("1000px", 3));
        firstName.setValue(userDto.getFirstName());
        lastName.setValue(userDto.getLastName());
        username.setValue(userDto.getUsername());
        username.setReadOnly(true);
        email.setValue(userDto.getEmail());
        phoneNumber.setValue(userDto.getPhoneNumber());

        username.setHelperText("Username cannot be changed.");
        currentPassword.setHelperText("You need to confirm your current password only if you want to change it.");
        newPassword.setHelperText("You need to enter new password only if you want to change it.");
        confirmPassword.setHelperText("You need to confirm password only if you want to change it.");

        add(formLayout);
    }
}
