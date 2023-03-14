package com.frontend.views;

import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@AnonymousAllowed
@Route(value = "/cars", layout = MainLayout.class)
@PageTitle("Car Page | Garage Booking Service")
public class CarView extends VerticalLayout {
    public CarView() {
        setSpacing(false);

        H1 header = new H1("Welcome to Garage Booking Service!");
        header.addClassNames(LumoUtility.Margin.Top.XSMALL, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("Here you can book a service in your favourite car workshop"));
        add(new Paragraph("To start you must add your car details, you can make it by choosing the tab named CAR."));
        add(new Paragraph("Next, in the ORDER tab, select the garage you would like your car to be repaired and services that should be done."));
        add(new Paragraph("Then in BOOK tab you can choose a day and time to book service."));
        add(new Paragraph("And that's it! It's just that simple, now prepare to have your car repaired"));
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setDefaultHorizontalComponentAlignment(Alignment.START);

    }
}
