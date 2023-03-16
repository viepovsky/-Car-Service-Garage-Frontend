package com.frontend.views;

import com.frontend.domainDto.response.GarageDto;
import com.frontend.service.GarageService;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@AnonymousAllowed
@Route(value = "/book", layout = MainLayout.class)
@PageTitle("Book services | Garage Booking Service")
public class BookView extends VerticalLayout {
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(BookView.class);
    private GarageService garageService;
    private Grid<GarageDto> garageDtoGrid = new Grid<>(GarageDto.class);
    Button selectGarageButton = new Button("Select");
    public BookView(GarageService garageService) {
        this.garageService = garageService;

//        Tab garage = new Tab(VaadinIcon.SHOP.create(), new Span("Select garage"));
//        Tab services = new Tab(VaadinIcon.CLIPBOARD_CHECK.create(), new Span("Select services"));
//        Tab calendar = new Tab(VaadinIcon.CALENDAR.create(), new Span("Select date"));
//
//        for (Tab tab : new Tab[] { garage, services, calendar }) {
//            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
//        }
//
//        Tabs tabs = new Tabs(garage, services, calendar);
//        tabs.addClassName(LumoUtility.Margin.Horizontal.AUTO);
//        add(tabs);
        setSpacing(false);
        add(new Paragraph("Here you can book your services, first select garage by clicking it."));


        List<GarageDto> garageDtoList = garageService.getGarages();
        garageDtoGrid.setItems(garageDtoList);
        garageDtoGrid.addSelectionListener(selection -> {
            Optional<GarageDto> optionalGarage = selection.getFirstSelectedItem();
            if (optionalGarage.isPresent()) {
                LOGGER.info("Selected garage: " + optionalGarage.get());
                 System.out.printf("Selected garage: %s%n",
                 optionalGarage.get().getName());
            }
        });
        garageDtoGrid.setMaxHeight("250px");
        selectGarageButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout garageLayout = new HorizontalLayout(garageDtoGrid, selectGarageButton);
        garageLayout.setMaxWidth("1000px");
        garageLayout.setWidthFull();
        garageLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        add(garageLayout);
    }
}
