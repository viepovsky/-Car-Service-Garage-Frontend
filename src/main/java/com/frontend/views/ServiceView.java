package com.frontend.views;

import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route(value = "/services", layout = MainLayout.class)
@PageTitle("My Services | Garage Booking Service")
public class ServiceView extends VerticalLayout {
    public ServiceView() {
        Tabs tabs = new Tabs();
        tabs.add(new Tab("Account information"), new Tab("Open"), new Tab("Completed"), new Tab("Cancelled"));
        add(tabs);
    }
}
