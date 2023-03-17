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
@Route(value = "/", layout = MainLayout.class)
@PageTitle("Main Page | Garage Booking Service")
public class MainView extends VerticalLayout {
    private Image img = new Image("images/car_workshop.jpg", "car workshop");

    public MainView() {
        setSpacing(false);

        H1 header = new H1("Welcome to our Garage Booking Service!");
        header.addClassNames(LumoUtility.Margin.Top.XSMALL, LumoUtility.Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("To get started, add your car details by selecting the \"Cars\" tab."));
        add(new Paragraph("Next, go to the \"Book a service\" tab to choose your preferred workshop and the services you would like to have performed."));
        add(new Paragraph("You can then choose a date and time for your appointment."));
        add(new Paragraph("You can always check your upcoming and past services in the \"My Services\" tab."));
        add(new Paragraph("It's that simple! Prepare to have your car serviced or repaired by our skilled professionals."));
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.START);
        setDefaultHorizontalComponentAlignment(Alignment.START);

        img.setWidth("500px");
        img.addClassNames(LumoUtility.Margin.Top.XLARGE);
        add(img);
    }
}
