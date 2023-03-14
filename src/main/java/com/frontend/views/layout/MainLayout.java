package com.frontend.views.layout;

import com.frontend.security.SecurityService;
import com.frontend.views.MainView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private SecurityService securityService;
    public MainLayout(SecurityService securityService) {
        this.securityService = new SecurityService();
        createHeader();
        createDrawer();
    }
    private void createHeader() {
        H1 logo = new H1("Garage Booking Service");
        logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.SMALL);

        Button logOut = new Button("Log out", e -> securityService.logout());
        logOut.addClassName(LumoUtility.Margin.Right.LARGE);
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logOut);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.expand(logo);
        header.addClassName(LumoUtility.Padding.Vertical.NONE);

        addToNavbar(header);
    }
    private void createDrawer() {
        RouterLink mainView = new RouterLink("Start", MainView.class);
        mainView.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                mainView
        ));
    }


}
