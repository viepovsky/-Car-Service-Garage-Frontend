package com.frontend.views;

import com.frontend.domainDto.response.BookingDto;
import com.frontend.domainDto.response.CarDto;
import com.frontend.domainDto.response.CarServiceDto;
import com.frontend.domainDto.response.GarageDto;
import com.frontend.service.ServiceCarService;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@AnonymousAllowed
@Route(value = "/services", layout = MainLayout.class)
@PageTitle("My Services | Garage Booking Service")
public class ServiceView extends Div {
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceView.class);
    private ServiceCarService serviceCarService;
    private CarDto carDto;
    private BookingDto bookingDto;
    private GarageDto garageDto;
    private List<CarServiceDto> carServiceDtoList;
    private Grid<CarServiceDto> incomingGrid = new Grid<>(CarServiceDto.class, false);
    private List<CarServiceDto> incomingServiceList;
    private Grid<CarServiceDto> inProgressGrid = new Grid<>(CarServiceDto.class, false);
    private List<CarServiceDto> inProgressServiceList;
    private Grid<CarServiceDto> completedGrid = new Grid<>(CarServiceDto.class, false);
    private List<CarServiceDto> completedServiceList;
    private final Tab incomingServiceTab;
    private final Tab inProgressTab;
    private final Tab completedServiceTab;
    private VerticalLayout incomingLayout;
    public ServiceView(ServiceCarService serviceCarService) {
        this.serviceCarService = serviceCarService;

        incomingServiceTab = new Tab("Incoming services");
        inProgressTab = new Tab("In progress services");
        completedServiceTab = new Tab("Completed services");
        Tabs tabs = new Tabs(incomingServiceTab, inProgressTab, completedServiceTab);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        formIncomingGrid();

        fetchCarServicesFromDb();
        filterCarServiceLists();

        tabs.addSelectedChangeListener( event -> setGridsVisible(event.getSelectedTab()));

        addTabsAndLayoutsToView(tabs);
    }

    private void formIncomingGrid() {
        incomingGrid.addColumn(CarServiceDto::getCarDto).setHeader("Serviced car").setAutoWidth(true);
        incomingGrid.addColumn(CarServiceDto::getName).setHeader("Service").setAutoWidth(true);
        incomingGrid.addColumn(CarServiceDto::getCost).setHeader("Cost [PLN]").setAutoWidth(true).setFlexGrow(0);
        incomingGrid.addColumn(n -> n.getBookingDto().getDate().atTime(n.getBookingDto().getStartHour()).format(DateTimeFormatter.ofPattern("HH:mm, dd-MM-yyyy")))
                .setHeader("Start time, date").setAutoWidth(true).setSortable(true);
        incomingGrid.addColumn(n -> n.getBookingDto().getEndHour()).setHeader("Estimated end time").setAutoWidth(true).setFlexGrow(0).setKey("End");
        incomingGrid.addColumn(CarServiceDto::getRepairTimeInMinutes).setHeader("Repair time [min]").setAutoWidth(true).setFlexGrow(0);
        incomingGrid.addColumn(n -> n.getBookingDto().getGarageDto().getName()).setHeader("Garage").setAutoWidth(true);
        incomingGrid.addColumn(n -> n.getBookingDto().getGarageDto().getAddress()).setHeader("Garage address").setAutoWidth(true);
        incomingGrid.addColumn(CarServiceDto::getStatus).setHeader("Status").setAutoWidth(true).setFlexGrow(0).setKey("Status");

        incomingGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        incomingGrid.setMaxHeight("300px");
        incomingGrid.setWidthFull();
    }

    private void fetchCarServicesFromDb() {
        carServiceDtoList = serviceCarService.getCarServices(currentUsername);
    }

    private void filterCarServiceLists() {
        incomingServiceList = carServiceDtoList.stream()
                .peek(n -> {
                    BigDecimal cost = n.getCost().setScale(0, RoundingMode.HALF_DOWN);
                    n.setCost(cost);
                })
                .filter(n -> n.getBookingDto().getDate().atTime(n.getBookingDto().getStartHour()).isAfter(LocalDateTime.now()))
                .toList();

        inProgressServiceList = carServiceDtoList.stream()
                .peek(n -> {
                    BigDecimal cost = n.getCost().setScale(0, RoundingMode.HALF_DOWN);
                    n.setCost(cost);
                })
                .filter(n -> n.getBookingDto().getDate().atTime(n.getBookingDto().getStartHour()).isBefore(LocalDateTime.now())
                && n.getBookingDto().getDate().atTime(n.getBookingDto().getEndHour()).isAfter(LocalDateTime.now()))
                .toList();

        completedServiceList = carServiceDtoList.stream()
                .peek(n -> {
                    BigDecimal cost = n.getCost().setScale(0, RoundingMode.HALF_DOWN);
                    n.setCost(cost);
                })
                .filter(n -> n.getBookingDto().getDate().atTime(n.getBookingDto().getEndHour()).isBefore(LocalDateTime.now()))
                .toList();
    }

    private void addTabsAndLayoutsToView(Tabs tabs) {
        add(tabs);
        incomingGrid.setItems(incomingServiceList);
        incomingLayout = new VerticalLayout(incomingGrid);
        add(incomingLayout);
    }

    private void setGridsVisible(Tab selectedTab) {
        if (selectedTab.equals(incomingServiceTab)) {
            incomingGrid.setItems(incomingServiceList);
            Grid.Column<CarServiceDto> column = incomingGrid.getColumnByKey("End");
            column.setHeader("Estimated end time");
        } else if (selectedTab.equals(inProgressTab)) {
            incomingGrid.setItems(inProgressServiceList);
            Grid.Column<CarServiceDto> column = incomingGrid.getColumnByKey("End");
            column.setHeader("Estimated end time");
        } else if (selectedTab.equals(completedServiceTab)) {
            incomingGrid.setItems(completedServiceList);
            Grid.Column<CarServiceDto> column = incomingGrid.getColumnByKey("End");
            column.setHeader("End time");
            Grid.Column<CarServiceDto> column2 = incomingGrid.getColumnByKey("Status");
            column2.setFlexGrow(1);
        }
    }
}
