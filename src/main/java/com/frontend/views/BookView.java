package com.frontend.views;

import com.frontend.domainDto.response.AvailableCarServiceDto;
import com.frontend.domainDto.response.CarDto;
import com.frontend.domainDto.response.GarageDto;
import com.frontend.domainDto.response.GarageWorkTimeDto;
import com.frontend.service.*;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AnonymousAllowed
@Route(value = "/book", layout = MainLayout.class)
@PageTitle("Book a service | Garage Booking Service")
public class BookView extends VerticalLayout {
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(BookView.class);
    private GarageService garageService;
    private CarService carService;
    private AvailableServiceCarService availableServiceCarService;
    private ServiceCarService serviceCarService;
    private BookingService bookingService;
    private GarageDto selectedGarage;
    private CarDto selectedCar;
    private Set<AvailableCarServiceDto> selectedServices;
    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private int totalRepairTime;
    private Paragraph garageText = new Paragraph("Here you can book your services, first select garage by clicking it.");
    private Grid<GarageDto> garageGrid = new Grid<>(GarageDto.class, false);
    private VerticalLayout garageLayout = new VerticalLayout(garageText, garageGrid);
    private HorizontalLayout garageBigLayout = new HorizontalLayout();
    private Paragraph carText = new Paragraph("If you have already selected garage, now pick your car below:");
    private ComboBox<CarDto> carComboBox = new ComboBox<>("Select your car:");
    private VerticalLayout carLayout = new VerticalLayout(carText, carComboBox);
    private Paragraph serviceText = new Paragraph("Below select services you wish to have:");
    private Grid<AvailableCarServiceDto> serviceGrid = new Grid<>(AvailableCarServiceDto.class, false);
    private Button confirmServiceButton = new Button("Confirm chosen services");
    private VerticalLayout serviceLayout = new VerticalLayout(serviceText, serviceGrid, confirmServiceButton);
    private Accordion preparedBookingDetails = new Accordion();
    private Button backButton = new Button("Click to go back and change services");
    private VerticalLayout detailsLayout = new VerticalLayout();
    private Paragraph dateText = new Paragraph("Now select the date you would like to have your car serviced.");
    private DatePicker datePicker = new DatePicker("Service date:");
    private VerticalLayout dateLayout = new VerticalLayout(dateText, datePicker);
    private Paragraph addBookText = new Paragraph("Finally, select service time:");
    private ComboBox<LocalTime> timePicker = new ComboBox<>("Select available time:");
    private Button addBookButton = new Button("Click to book the service.");
    private VerticalLayout bookLayout = new VerticalLayout(addBookText, timePicker, addBookButton);
    private H2 thankYouText = new H2("Your appointment has been successfully scheduled.");
    private Paragraph endText = new Paragraph("Thank you for using our car service booking system.");
    private Paragraph endText2 = new Paragraph("You can check your upcoming and previous appointments in the 'Booking' tab in the drawer.");
    public BookView(GarageService garageService, CarService carService, AvailableServiceCarService availableServiceCarService, ServiceCarService serviceCarService, BookingService bookingService) {
        this.garageService = garageService;
        this.carService = carService;
        this.availableServiceCarService = availableServiceCarService;
        this.serviceCarService = serviceCarService;
        this.bookingService = bookingService;

        carLayout.setVisible(false);
        serviceLayout.setVisible(false);
        detailsLayout.setVisible(false);
        dateLayout.setVisible(false);
        bookLayout.setVisible(false);
        setSpacing(false);

        VerticalLayout garageWorkTimes = new VerticalLayout();
        garageGrid.addColumn(GarageDto::getName).setHeader("Garage name").setSortable(true);
        garageGrid.addColumn(GarageDto::getAddress).setHeader("Address").setSortable(true);
        List<GarageDto> garageDtoList = garageService.getGarages();
        garageGrid.setItems(garageDtoList);
        garageGrid.addSelectionListener(selection -> {
            Optional<GarageDto> optionalGarage = selection.getFirstSelectedItem();
            if (optionalGarage.isPresent()) {
                selectedGarage = new GarageDto(optionalGarage.get());
                garageWorkTimes.setVisible(true);
                setGarageWorkTimes(garageWorkTimes, selectedGarage);
                LOGGER.info("Selected garage: " + selectedGarage);
                carLayout.setVisible(true);
                List<AvailableCarServiceDto> serviceList = availableServiceCarService.getAllAvailableServices(selectedGarage.getId());
                if (selectedCar != null) {
                    serviceList.forEach(service -> {
                        BigDecimal cost = service.getCost().setScale(0, RoundingMode.HALF_DOWN);
                        service.setCost(cost);
                        if(service.getPremiumMakes().toLowerCase().contains(selectedCar.getMake().toLowerCase())) {
                            cost = service.getCost().multiply(service.getMakeMultiplier());
                            service.setCost(cost.setScale(0, RoundingMode.HALF_DOWN));
                        }
                        serviceGrid.setItems(serviceList);
                    });
                }
            } else {
                carLayout.setVisible(false);
                serviceLayout.setVisible(false);
                garageWorkTimes.setVisible(false);
                selectedGarage = null;
                selectedCar = null;
                carComboBox.setValue(null);
            }
        });
        garageGrid.setMaxHeight("250px");
        garageGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        garageLayout.setMaxWidth("1000px");
        garageLayout.setWidthFull();
        garageBigLayout.setWidthFull();
        garageBigLayout.setAlignItems(Alignment.END);
        garageBigLayout.add(garageLayout, garageWorkTimes);
        add(garageBigLayout);

        carText.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
        carComboBox.setItems(carService.getCarsForGivenUsername(currentUsername));
        carComboBox.setItemLabelGenerator(carDto ->
                carDto.getMake() + " " +
                carDto.getModel() + " " +
                carDto.getType() + " " +
                carDto.getYear() + " " +
                carDto.getEngine()
        );
        carComboBox.setMaxWidth("500px");
        carComboBox.setWidthFull();
        carComboBox.addClientValidatedEventListener(event -> {
            selectedCar = new CarDto(carComboBox.getValue());
            LOGGER.info("Selected car: " + selectedCar);
            serviceLayout.setVisible(true);
            List<AvailableCarServiceDto> serviceList = availableServiceCarService.getAllAvailableServices(selectedGarage.getId());
            serviceList.forEach(service -> {
                BigDecimal cost = service.getCost().setScale(0, RoundingMode.HALF_DOWN);
                service.setCost(cost);
                if(service.getPremiumMakes().toLowerCase().contains(selectedCar.getMake().toLowerCase())) {
                    cost = service.getCost().multiply(service.getMakeMultiplier());
                    service.setCost(cost.setScale(0, RoundingMode.HALF_DOWN));
                }
            });
            serviceGrid.setItems(serviceList);
            LOGGER.info("Available services list for selected garage: " + serviceList);
        });
        add(carLayout);

        serviceText.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
        confirmServiceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmServiceButton.addClickListener(event -> {
            if (selectedServices != null && selectedServices.size() != 0) {
                LOGGER.info("Clicked button confirm with values:");
                LOGGER.info("selected garage: " + selectedGarage);
                LOGGER.info("selected car: " + selectedCar);
                LOGGER.info("selected services id: " + selectedServices.stream().map(AvailableCarServiceDto::getId).toList());
                garageBigLayout.setVisible(false);
                carLayout.setVisible(false);
                serviceLayout.setVisible(false);

                Span garageDetails = new Span(selectedGarage.getName() + ", " + selectedGarage.getAddress());
                preparedBookingDetails.add("Selected garage", garageDetails);
                Span carDetails = new Span(selectedCar.getMake() + ", " + selectedCar.getModel() + ", " + selectedCar.getType() + ", " + selectedCar.getYear() + ", " + selectedCar.getEngine());
                preparedBookingDetails.add("Selected car", carDetails);
                VerticalLayout serviceDetailsLayout = new VerticalLayout();
                serviceDetailsLayout.setSpacing(false);
                serviceDetailsLayout.setPadding(false);
                BigDecimal totalCost = BigDecimal.ZERO;
                totalRepairTime = 0;
                for (AvailableCarServiceDto service : selectedServices) {
                    Span serviceSpan = new Span(service.getName() + ", cost: " + service.getCost() + ", estimated service time: " + service.getRepairTimeInMinutes() + " minutes.");
                    serviceDetailsLayout.add(serviceSpan);
                    totalCost = totalCost.add(service.getCost());
                    totalRepairTime +=service.getRepairTimeInMinutes();
                }
                Span serviceTotal = new Span("Total cost for selected services: " + totalCost + ", estimated services time: " + totalRepairTime / 60 + "h " + totalRepairTime % 60 + "min.");
                serviceTotal.addClassNames(LumoUtility.Margin.Top.MEDIUM, LumoUtility.FontWeight.BOLD);
                serviceDetailsLayout.add(serviceTotal);
                preparedBookingDetails.add("Selected services",serviceDetailsLayout);
                detailsLayout.removeAll();
                detailsLayout.add(preparedBookingDetails, backButton);
                add(detailsLayout);
                add(dateLayout);
                add(bookLayout);
                detailsLayout.setVisible(true);
                dateLayout.setVisible(true);
                bookLayout.setVisible(true);

            }
        });
        serviceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        serviceGrid.addColumn(AvailableCarServiceDto::getName).setHeader("Service").setSortable(true);
        serviceGrid.addColumn(AvailableCarServiceDto::getDescription).setHeader("Description");
        serviceGrid.addColumn(AvailableCarServiceDto::getCost).setHeader("Cost [PLN]").setSortable(true);
        serviceGrid.addColumn(AvailableCarServiceDto::getRepairTimeInMinutes).setHeader("Repair time [min]");
        serviceGrid.addSelectionListener(selection -> {
            selectedServices = selection.getAllSelectedItems();
            LOGGER.info("Selected services id: " + selectedServices.stream().map(AvailableCarServiceDto::getId).toList());
        });
        serviceGrid.setMaxHeight("250px");
        serviceGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        serviceLayout.setMaxWidth("1000px");
        serviceLayout.setWidthFull();
        add(serviceLayout);

        backButton.addClickListener(event -> {
            LOGGER.info("Back button clicked.");
            garageBigLayout.setVisible(true);
            carLayout.setVisible(true);
            serviceLayout.setVisible(true);
            detailsLayout.setVisible(false);
            preparedBookingDetails = new Accordion();
            selectedDate = null;
            selectedStartTime = null;
            datePicker.setValue(null);
            dateLayout.setVisible(false);
            timePicker.setValue(null);
            timePicker.setItems(new ArrayList<>());
            bookLayout.setVisible(false);

        });
        backButton.addClassName(LumoUtility.Margin.Top.LARGE);
        detailsLayout.setMaxWidth("600px");
        detailsLayout.setWidthFull();

        dateText.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.NONE);
        LocalDate now = LocalDate.now();
        datePicker.setMin(now);
        datePicker.setMax(now.plusDays(60));
        datePicker.setHelperText("Service date must be within 60 days from today, remember we work Mondays - Saturdays only");
        datePicker.addClassNames(LumoUtility.Margin.Top.NONE);
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
                selectedDate = datePicker.getValue();
                LOGGER.info("Selected book date: " + selectedDate);
                timePicker.setItems(bookingService.getAvailableBookingTimes(selectedDate, totalRepairTime, selectedGarage.getId()));
            }
        });
        addBookText.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.NONE);
        timePicker.setMaxWidth("300px");
        timePicker.setWidthFull();
        timePicker.addValueChangeListener(event -> {
            selectedStartTime = timePicker.getValue();
            LOGGER.info("Time picked, selected time: " + selectedStartTime);
        });
        addBookButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBookButton.addClickListener(event -> {
            LOGGER.info("Button save clicked.");
            if (selectedDate != null && selectedStartTime != null) {
                detailsLayout.setVisible(false);
                dateLayout.setVisible(false);
                bookLayout.setVisible(false);
                List<Long> selectedServicesIdList = selectedServices.stream().map(AvailableCarServiceDto::getId).toList();
                bookingService.saveBooking(selectedServicesIdList, selectedDate, selectedStartTime, selectedGarage.getId(), selectedCar.getId(), totalRepairTime);
                add(thankYouText);
                add(endText);
                add(endText2);
            } else {
                Notification.show("First select date and time.");
            }
        });
        endText.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.XSMALL);
        endText2.addClassName(LumoUtility.Margin.Top.XSMALL);
    }

    private void setGarageWorkTimes(VerticalLayout garageWorkTimes, GarageDto selectedGarage) {
        garageWorkTimes.removeAll();
        Span span = new Span("Garage: " + selectedGarage.getName() + " opens:");
        span.addClassNames(LumoUtility.FontWeight.BOLD);
        garageWorkTimes.add(span);
        for (GarageWorkTimeDto workTime : selectedGarage.getGarageWorkTimeDtoList()) {
            if (!Objects.equals(workTime.getStartHour(), workTime.getEndHour())){
                span = new Span(workTime.getDay().substring(0, 1).toUpperCase() + workTime.getDay().substring(1).toLowerCase() + "s: " +
                        workTime.getStartHour().format(DateTimeFormatter.ofPattern("HH:mm")) +
                        " till " +
                        workTime.getEndHour().format(DateTimeFormatter.ofPattern("HH:mm")));
                garageWorkTimes.add(span);
            } else {
                span = new Span(workTime.getDay().substring(0, 1).toUpperCase() + workTime.getDay().substring(1).toLowerCase() + "s: closed.");
                garageWorkTimes.add(span);
            }
        }
        garageWorkTimes.setSpacing(false);
    }
}
