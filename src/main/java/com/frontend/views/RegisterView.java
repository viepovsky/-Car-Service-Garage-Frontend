package com.frontend.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@PageTitle("Register | Garage Booking Service")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    public RegisterView() {
        addClassName("register-view");
        add(new H1("Its your register tab"));
    }
}
