package com.frontend.views.layout;

import com.frontend.security.SecurityService;
import com.frontend.views.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    public MainLayout() {
        this.securityService = new SecurityService();
        createHeader();
        createDrawer();
    }
    private void createHeader() {
        Image serviceIcon = new Image("icons/icon.png", "Garage Icon");
        serviceIcon.setWidth("40px");
        serviceIcon.setHeight("40px");
        serviceIcon.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
        H1 serviceName = new H1("Garage Booking Service");
        serviceName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.SMALL);

        Icon userIcon = VaadinIcon.USER.create();
        userIcon.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
        Button userButton = new Button(userIcon, e -> UI.getCurrent().getPage().setLocation("/user"));
        userButton.addThemeVariants(ButtonVariant.LUMO_ICON);

        Button logOut = new Button("Log out", e -> securityService.logout());
        logOut.addClassName(LumoUtility.Margin.Right.LARGE);
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), serviceIcon, serviceName, userButton, logOut);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.expand(serviceName);
        header.addClassName(LumoUtility.Padding.Vertical.NONE);

        addToNavbar(header);
    }
    private void createDrawer() {
        Icon mainIcon = VaadinIcon.HOME_O.create();
        mainIcon.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Right.MEDIUM);
        Span mainText = new Span("Dashboard");
        RouterLink mainView = new RouterLink(MainView.class);
        mainView.add(mainIcon, mainText);
        mainView.setHighlightCondition(HighlightConditions.sameLocation());

        Icon carIcon = VaadinIcon.CAR.create();
        carIcon.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Right.MEDIUM);
        Span carText = new Span("Cars");
        RouterLink carsView = new RouterLink(CarView.class);
        carsView.add(carIcon, carText);
        carsView.setHighlightCondition(HighlightConditions.sameLocation());

        Icon bookIcon = VaadinIcon.CALENDAR_BRIEFCASE.create();
        bookIcon.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Right.MEDIUM);
        Span bookText = new Span("Book a service");
        RouterLink bookView = new RouterLink(BookView.class);
        bookView.add(bookIcon, bookText);
        bookView.setHighlightCondition(HighlightConditions.sameLocation());

        Icon myServiceIcon = VaadinIcon.TOOLS.create();
        myServiceIcon.addClassNames(LumoUtility.Margin.Bottom.SMALL, LumoUtility.Margin.Right.MEDIUM);
        Span myServiceText = new Span("My Services");
        RouterLink myServiceView = new RouterLink(ServiceView.class);
        myServiceView.add(myServiceIcon, myServiceText);
        myServiceView.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                mainView,
                carsView,
                bookView,
                myServiceView
        ));
    }
}
