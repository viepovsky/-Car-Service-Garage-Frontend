package com.frontend.views;

import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route(value = "/user", layout = MainLayout.class)
@PageTitle("User settings | Garage Booking Service")
public class UserView extends VerticalLayout {
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");
    private EmailField email = new EmailField("Email address");
    private TextField phoneNumber = new TextField("Phone number");
    public UserView() {
        H2 h2 = new H2("Personal information:");
        add(h2);


    }
}
