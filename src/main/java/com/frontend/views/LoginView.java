package com.frontend.views;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Garage Booking Service")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final Paragraph registerText = new Paragraph();
    private final LoginOverlay loginOverlay = new LoginOverlay();

    public LoginView(){
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setAdditionalInformation("You can always test app by creating new user in the registration form above.");
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setForgotPassword("Create new user, click to open registration form.");
        i18n.setForm(i18nForm);

        loginOverlay.setI18n(i18n);
        loginOverlay.setAction("login");
        loginOverlay.setTitle("Garage Booking Service");
        loginOverlay.setDescription("Testing app with USER privileges, login as:\"testuser\" with \"testpassword\", with ADMIN privileges, login as:\"testadmin\" with \"testpassword\"");
        add(loginOverlay);
        loginOverlay.setOpened(true);
        loginOverlay.getElement().setAttribute("no-autofocus", "");
        loginOverlay.addForgotPasswordListener(click -> Notification.show("Register clicked"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginOverlay.setError(true);
        }
    }
}
