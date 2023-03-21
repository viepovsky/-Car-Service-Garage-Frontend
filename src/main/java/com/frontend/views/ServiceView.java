package com.frontend.views;

import com.frontend.domainDto.response.CarServiceDto;
import com.frontend.service.BookingService;
import com.frontend.service.ServiceCarService;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AnonymousAllowed
@Route(value = "/services", layout = MainLayout.class)
@PageTitle("My Services | Garage Booking Service")
public class ServiceView extends Div {
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceView.class);
    private ServiceCarService serviceCarService;
    private BookingService bookingService;
    private CarServiceDto selectedCarService;
    private LocalDate selectedNewDate;
    private LocalTime selectedNewStartTime;
    private List<CarServiceDto> carServiceDtoList;
    private Grid<CarServiceDto> serviceDtoGrid = new Grid<>(CarServiceDto.class, false);
    private List<CarServiceDto> incomingServiceList;
    private List<CarServiceDto> inProgressServiceList;
    private List<CarServiceDto> completedServiceList;
    private final Tab incomingServiceTab;
    private final Tab inProgressTab;
    private final Tab completedServiceTab;
    private final Tabs tabs;
    private VerticalLayout incomingLayout;
    private DatePicker datePicker = new DatePicker("Service date:");
    private ComboBox<LocalTime> timePicker = new ComboBox<>("Select available time:");
    private HorizontalLayout horizontalPickersLayout;
    private Button editButton = new Button("Edit service time");
    private Button cancelButton = new Button("Cancel service");
    private HorizontalLayout horizontalButtonsLayout;
    public ServiceView(ServiceCarService serviceCarService, BookingService bookingService) {
        this.serviceCarService = serviceCarService;
        this.bookingService = bookingService;

        incomingServiceTab = new Tab("Incoming services");
        inProgressTab = new Tab("In progress services");
        completedServiceTab = new Tab("Completed services");
        tabs = new Tabs(incomingServiceTab, inProgressTab, completedServiceTab);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        formIncomingGrid();

        fetchCarServicesFromDb();
        filterCarServiceLists();

        tabs.addSelectedChangeListener(event -> setElementsVisible(event.getSelectedTab()));

        addTabsAndGridLayoutToView();

        addDateAndTimePicker();
        addListenerToDatePicker();
        addListenerToTimePicker();

        createEditAndCancelServiceForm();
        addListenersToGrid();
        addListenersToButtons();
    }

    private void formIncomingGrid() {
        serviceDtoGrid.addColumn(CarServiceDto::getCarDto).setHeader("Serviced car").setAutoWidth(true);
        serviceDtoGrid.addColumn(CarServiceDto::getName).setHeader("Service").setAutoWidth(true);
        serviceDtoGrid.addColumn(CarServiceDto::getCost).setHeader("Cost [PLN]").setAutoWidth(true).setFlexGrow(0);
        serviceDtoGrid.addColumn(n -> n.getBookingDto().getDate().atTime(n.getBookingDto().getStartHour()).format(DateTimeFormatter.ofPattern("HH:mm, dd-MM-yyyy")))
                .setHeader("Start time, date").setAutoWidth(true).setSortable(true);
        serviceDtoGrid.addColumn(n -> n.getBookingDto().getEndHour()).setHeader("Estimated end time").setAutoWidth(true).setFlexGrow(0).setKey("End");
        serviceDtoGrid.addColumn(CarServiceDto::getRepairTimeInMinutes).setHeader("Repair time [min]").setAutoWidth(true).setFlexGrow(0);
        serviceDtoGrid.addColumn(n -> n.getBookingDto().getGarageDto().getName()).setHeader("Garage").setAutoWidth(true);
        serviceDtoGrid.addColumn(n -> n.getBookingDto().getGarageDto().getAddress()).setHeader("Garage address").setAutoWidth(true);
        serviceDtoGrid.addColumn(CarServiceDto::getStatus).setHeader("Status").setAutoWidth(true).setFlexGrow(0).setKey("Stat");

        serviceDtoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        serviceDtoGrid.setMaxHeight("300px");
        serviceDtoGrid.setWidthFull();
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

    private void setElementsVisible(Tab selectedTab) {
        if (selectedTab.equals(incomingServiceTab)) {
            serviceDtoGrid.setItems(incomingServiceList);

            Grid.Column<CarServiceDto> column = serviceDtoGrid.getColumnByKey("End");
            column.setHeader("Estimated end time");
            Grid.Column<CarServiceDto> column2 = serviceDtoGrid.getColumnByKey("Stat");
            column2.setFlexGrow(0);

            datePicker.setValue(null);
            timePicker.setItems(new ArrayList<>());
            horizontalPickersLayout.setVisible(true);
            horizontalPickersLayout.setEnabled(false);
            horizontalButtonsLayout.setVisible(true);
            horizontalButtonsLayout.setEnabled(false);
        } else if (selectedTab.equals(inProgressTab)) {
            serviceDtoGrid.setItems(inProgressServiceList);

            Grid.Column<CarServiceDto> column = serviceDtoGrid.getColumnByKey("End");
            column.setHeader("Estimated end time");
            Grid.Column<CarServiceDto> column2 = serviceDtoGrid.getColumnByKey("Stat");
            column2.setAutoWidth(true).setFlexGrow(1);

            horizontalPickersLayout.setVisible(false);
            horizontalButtonsLayout.setVisible(false);
        } else if (selectedTab.equals(completedServiceTab)) {
            serviceDtoGrid.setItems(completedServiceList);

            Grid.Column<CarServiceDto> column = serviceDtoGrid.getColumnByKey("End");
            column.setHeader("End time");
            Grid.Column<CarServiceDto> column2 = serviceDtoGrid.getColumnByKey("Stat");
            column2.setAutoWidth(true).setFlexGrow(1);

            horizontalPickersLayout.setVisible(false);
            horizontalButtonsLayout.setVisible(false);
        }
    }

    private void addDateAndTimePicker() {
        datePicker.setMin(LocalDate.now());
        datePicker.setMax(LocalDate.now().plusDays(60));
        datePicker.setHelperText("Service date must be within 60 days from today, remember we work Mondays - Saturdays only");

        timePicker.setMaxWidth("170px");
        timePicker.setWidthFull();
        horizontalPickersLayout = new HorizontalLayout(datePicker, timePicker);
        horizontalPickersLayout.setAlignItems(FlexComponent.Alignment.START);
        horizontalPickersLayout.addClassNames(LumoUtility.Margin.MEDIUM, LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.LARGE);
        horizontalPickersLayout.setSizeFull();
        horizontalPickersLayout.setEnabled(false);
        add(horizontalPickersLayout);
    }

    private void addListenerToDatePicker() {
        datePicker.addValueChangeListener(event -> {
            LocalDate tempDate = datePicker.getValue();
            String errorMessage = null;
            if (tempDate != null) {
                if (tempDate.isBefore(datePicker.getMin())) {
                    errorMessage = "Too early, choose another date.";
                } else if (tempDate.isAfter(datePicker.getMax())) {
                    errorMessage = "Too late, choose another date.";
                } else if (tempDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    datePicker.setValue(null);
                }
                datePicker.setErrorMessage(errorMessage);
            }
            if (!datePicker.isInvalid() && datePicker.getValue() != null) {
                selectedNewDate = datePicker.getValue();
                LOGGER.info("Selected book date: " + selectedNewDate);
                timePicker.setItems(bookingService.getAvailableBookingTimes(selectedNewDate, selectedCarService));
            }
        });
    }

    private void addListenerToTimePicker() {
        timePicker.addValueChangeListener(event -> {
            selectedNewStartTime = timePicker.getValue();
            LOGGER.info("Selected time: " + selectedNewStartTime);
        });
    }

    private void addTabsAndGridLayoutToView() {
        add(tabs);
        serviceDtoGrid.setItems(incomingServiceList);
        incomingLayout = new VerticalLayout(serviceDtoGrid);
        add(incomingLayout);
    }

    private void createEditAndCancelServiceForm() {
        horizontalButtonsLayout = new HorizontalLayout();
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        horizontalButtonsLayout.add(editButton, cancelButton);
        horizontalButtonsLayout.addClassNames(LumoUtility.Margin.MEDIUM);
        horizontalButtonsLayout.setEnabled(false);
        add(horizontalButtonsLayout);
    }

    private void addListenersToGrid() {
        serviceDtoGrid.addSelectionListener(selection -> {
            if (tabs.getSelectedTab().equals(incomingServiceTab)){
                Optional<CarServiceDto> optionalCarServiceDto = selection.getFirstSelectedItem();
                if (optionalCarServiceDto.isPresent()) {
                    selectedCarService = optionalCarServiceDto.get();
                    LOGGER.info("Selected CarService: " + selectedCarService.getName() + ", with car: " + selectedCarService.getCarDto().toString());
                    datePicker.setValue(null);
                    timePicker.setItems(new ArrayList<>());
                    horizontalPickersLayout.setEnabled(true);
                    horizontalButtonsLayout.setEnabled(true);
                } else {
                    datePicker.setValue(null);
                    timePicker.setItems(new ArrayList<>());
                    horizontalPickersLayout.setEnabled(false);
                    horizontalButtonsLayout.setEnabled(false);
                    selectedCarService = null;
                    LOGGER.info("Unselected CarService.");
                }
            } else {
                if (selectedCarService != null) {
                    selectedCarService = null;
                    LOGGER.info("Unselected CarService.");
                }
            }
        });
    }

    private void addListenersToButtons() {
        cancelButton.addClickListener(event -> {
            if (LocalDateTime.now().plusHours(2).isBefore(selectedCarService.getBookingDto().getDate().atTime(selectedCarService.getBookingDto().getStartHour()))) {
                serviceCarService.cancelService(selectedCarService.getId());
                Notification.show("Service canceled");
                fetchCarServicesFromDb();
                filterCarServiceLists();
                serviceDtoGrid.setItems(incomingServiceList);
                selectedCarService = null;
                serviceDtoGrid.deselectAll();
            } else {
                Notification.show("It is too late to cancel service, only possible 2hours before start time. Please contact directly with " + selectedCarService.getBookingDto().getGarageDto().getName());
                LOGGER.info("Button cancel clicked, too late to cancel service.");
            }
        });

        editButton.addClickListener(event -> {
            if (LocalDateTime.now().plusHours(2).isBefore(selectedCarService.getBookingDto().getDate().atTime(selectedCarService.getBookingDto().getStartHour()))) {
                if (selectedNewDate != null && selectedNewStartTime != null){
                    bookingService.updateBooking(selectedCarService.getBookingDto().getId(), selectedNewDate, selectedNewStartTime);
                    Notification.show("Service time changed");
                    fetchCarServicesFromDb();
                    filterCarServiceLists();
                    serviceDtoGrid.setItems(incomingServiceList);
                    selectedCarService = null;
                    serviceDtoGrid.deselectAll();
                }
            } else {
                Notification.show("It is too late to change service time, only possible 2hours before start time. Please contact directly with " + selectedCarService.getBookingDto().getGarageDto().getName());
                LOGGER.info("Button edit clicked, too late to edit service time.");
            }
        });
    }
}
