package com.frontend.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
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

    private final LoginOverlay loginOverlay = new LoginOverlay();

    public LoginView(){
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setAdditionalInformation("You can test the app by creating a new account using the registration form above. Additionally, you can explore the user account, which is preloaded with initial data to showcase the app's capabilities.");
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setForgotPassword("Create new account.");
        i18n.setForm(i18nForm);

        loginOverlay.setI18n(i18n);
        loginOverlay.setAction("login");
        loginOverlay.setTitle("Garage Booking Service");
        loginOverlay.setDescription("Testing app from user view, login as:\"testuser\" with \"testpassword\", or create new account.");
        loginOverlay.setOpened(true);
        loginOverlay.getElement().setAttribute("no-autofocus", "");
        loginOverlay.addForgotPasswordListener(click -> UI.getCurrent().getPage().setLocation("/register"));
        add(loginOverlay);
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
