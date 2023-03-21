package com.frontend.views;

import com.frontend.domainDto.response.BookingDto;
import com.frontend.domainDto.response.CarDto;
import com.frontend.domainDto.response.CarServiceDto;
import com.frontend.domainDto.response.GarageDto;
import com.frontend.service.ServiceCarService;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
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
    private Grid<CarServiceDto> incomingServiceGrid = new Grid<>(CarServiceDto.class, false);
    private CarDto carDto;
    private BookingDto bookingDto;
    private GarageDto garageDto;
    private List<CarServiceDto> carServiceDtoList;
    private List<CarServiceDto> incomingServiceList;
    private List<CarServiceDto> inProgressServiceList;
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
        Tabs tabs = new Tabs(incomingServiceTab, completedServiceTab);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        createColumnsIncomingServicesGrid();

        fetchCarServicesFromDb();
        filterCarServiceLists();

        tabs.addSelectedChangeListener( event -> setGridsVisible(event.getSelectedTab()));

        addTabsAndLayoutsToView(tabs);
    }

    private void createColumnsIncomingServicesGrid() {
        incomingServiceGrid.addColumn(CarServiceDto::getCarDto).setHeader("Car").setWidth("200px");
        incomingServiceGrid.addColumn(CarServiceDto::getName).setHeader("Service name");
        incomingServiceGrid.addColumn(n -> n.getBookingDto().getDate().atTime(n.getBookingDto().getStartHour()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss")))
                .setHeader("Date, time").setWidth("120px").setSortable(true);
        incomingServiceGrid.addColumn(CarServiceDto::getCost).setHeader("Cost [PLN]").setWidth("40px");
        incomingServiceGrid.addColumn(CarServiceDto::getRepairTimeInMinutes).setHeader("Repair time [min]").setWidth("60px");
        incomingServiceGrid.addColumn(n -> n.getBookingDto().getGarageDto().getName()).setHeader("Garage name");
        incomingServiceGrid.addColumn(n -> n.getBookingDto().getGarageDto().getAddress()).setHeader("Garage address").setWidth("160px");
        incomingServiceGrid.addColumn(CarServiceDto::getStatus).setHeader("Status");

        incomingServiceGrid.setMaxHeight("300px");
        incomingServiceGrid.setWidthFull();
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
        incomingServiceGrid.setItems(incomingServiceList);
        incomingLayout = new VerticalLayout(incomingServiceGrid);
        add(incomingLayout);
    }

    private void setGridsVisible(Tab selectedTab) {
        if (selectedTab == null) {
            return;
        }
        if (selectedTab.equals(incomingServiceTab)) {
            incomingLayout.setVisible(true);
        } else if (selectedTab.equals(inProgressTab)) {
            incomingLayout.setVisible(false);
        } else if (selectedTab.equals(completedServiceTab)) {
            incomingLayout.setVisible(false);
        }
    }
}
