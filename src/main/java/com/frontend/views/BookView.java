package com.frontend.views;

import com.frontend.domainDto.response.*;
import com.frontend.service.*;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@PermitAll
@Route(value = "/book", layout = MainLayout.class)
@PageTitle("Book a service | Garage Booking Service")
public class BookView extends VerticalLayout {
    private final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(BookView.class);
    private final GarageService garageService;
    private final CarService carService;
    private final AvailableServiceCarService availableServiceCarService;
    private final BookingService bookingService;
    private final WeatherApiService weatherApiService;
    private GarageDto selectedGarage;
    private CarDto selectedCar;
    private Set<AvailableCarServiceDto> selectedServices;
    private LocalDate selectedDate;
    private LocalTime selectedStartTime;
    private int totalRepairTime;
    private final Paragraph garageText = new Paragraph("Here you can book your services, first select garage by clicking it.");
    private final Grid<GarageDto> garageGrid = new Grid<>(GarageDto.class, false);
    private final VerticalLayout garageLayout = new VerticalLayout(garageText, garageGrid);
    private final VerticalLayout garageWorkTimes = new VerticalLayout();
    private final HorizontalLayout garageBigLayout = new HorizontalLayout();
    private final Paragraph carText = new Paragraph("If you have already selected garage, now pick your car below:");
    private final ComboBox<CarDto> carComboBox = new ComboBox<>("Select your car:");
    private final VerticalLayout carLayout = new VerticalLayout(carText, carComboBox);
    private final Paragraph serviceText = new Paragraph("Below select services you wish to have:");
    private final Grid<AvailableCarServiceDto> serviceGrid = new Grid<>(AvailableCarServiceDto.class, false);
    private final Button confirmServiceButton = new Button("Confirm chosen services");
    private final VerticalLayout serviceLayout = new VerticalLayout(serviceText, serviceGrid, confirmServiceButton);
    private Accordion preparedBookingDetails = new Accordion();
    private final Button backButton = new Button("Click to go back and change services");
    private final VerticalLayout detailsLayout = new VerticalLayout();
    private final Paragraph dateText = new Paragraph("Now select the date you would like to have your car serviced.");
    private final DatePicker datePicker = new DatePicker("Service date:");
    private final VerticalLayout forecastLayout = new VerticalLayout();
    private final HorizontalLayout dateWithForecast = new HorizontalLayout(datePicker, forecastLayout);
    private final VerticalLayout dateLayout = new VerticalLayout(dateText, dateWithForecast);
    private final Paragraph addBookText = new Paragraph("Finally, select service time:");
    private final ComboBox<LocalTime> timePicker = new ComboBox<>("Select available time:");
    private final Button addBookButton = new Button("Click to book the service.");
    private final VerticalLayout bookLayout = new VerticalLayout(addBookText, timePicker, addBookButton);
    private final H2 thankYouText = new H2("Your appointment has been successfully scheduled.");
    private final Paragraph endText = new Paragraph("Thank you for using our car service booking system.");
    private final Paragraph endText2 = new Paragraph("You can check your upcoming and previous appointments in the 'My Services' tab in the drawer or by clicking link below.");
    public BookView(GarageService garageService, CarService carService, AvailableServiceCarService availableServiceCarService, BookingService bookingService, WeatherApiService weatherApiService) {
        this.garageService = garageService;
        this.carService = carService;
        this.availableServiceCarService = availableServiceCarService;
        this.bookingService = bookingService;
        this.weatherApiService = weatherApiService;

        setStartingVisibilityForAllLayouts();
        setSpacing(false);

        formGarageGrid();
        formGarageLayouts();
        addGarageGridListener();

        formCarLayout();
        addCarComboBoxListener();

        formServiceGrid();
        formServiceLayout();
        addServiceLayoutListeners();

        formDetailsLayoutAndAddBackButtonListener();

        formForecastLayoutAndDatePicker();
        addDatePickerListener();

        formBookLayoutElements();
        addBookLayoutListeners();

        endText.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.XSMALL);
        endText2.addClassName(LumoUtility.Margin.Top.XSMALL);
    }

    private void setStartingVisibilityForAllLayouts() {
        carLayout.setVisible(false);
        serviceLayout.setVisible(false);
        detailsLayout.setVisible(false);
        dateLayout.setVisible(false);
        bookLayout.setVisible(false);
    }

    private void formGarageGrid() {
        garageGrid.addColumn(GarageDto::getName).setHeader("Garage name").setSortable(true);
        garageGrid.addColumn(GarageDto::getAddress).setHeader("Address").setSortable(true);
        garageGrid.setMaxHeight("250px");
        garageGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        List<GarageDto> garageDtoList = garageService.getGarages();
        garageGrid.setItems(garageDtoList);
    }

    private void formGarageLayouts() {
        garageLayout.setMaxWidth("1000px");
        garageLayout.setWidthFull();

        garageBigLayout.setWidthFull();
        garageBigLayout.setAlignItems(Alignment.END);
        garageBigLayout.add(garageLayout, garageWorkTimes);
        add(garageBigLayout);
    }

    private void addGarageGridListener() {
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
                        setMultipliedCostForPremiumMakes(service);
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
    }

    private void setMultipliedCostForPremiumMakes(AvailableCarServiceDto service) {
        BigDecimal cost = service.getCost().setScale(0, RoundingMode.HALF_DOWN);
        service.setCost(cost);
        if(service.getPremiumMakes().toLowerCase().contains(selectedCar.getMake().toLowerCase())) {
            cost = service.getCost().multiply(service.getMakeMultiplier());
            service.setCost(cost.setScale(0, RoundingMode.HALF_DOWN));
        }
    }

    private void formCarLayout(){
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

        add(carLayout);
    }

    private void addCarComboBoxListener() {
        carComboBox.addClientValidatedEventListener(event -> {
            if (!carComboBox.isEmpty()) {
                selectedCar = new CarDto(carComboBox.getValue());
                LOGGER.info("Selected car: " + selectedCar);
                serviceLayout.setVisible(true);
                List<AvailableCarServiceDto> serviceList = availableServiceCarService.getAllAvailableServices(selectedGarage.getId());
                serviceList.forEach(this::setMultipliedCostForPremiumMakes);
                serviceGrid.setItems(serviceList);
                LOGGER.info("Available services list for selected garage: " + serviceList);
            }
        });
    }

    private void formServiceGrid() {
        serviceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        serviceGrid.addColumn(AvailableCarServiceDto::getName).setHeader("Service").setSortable(true);
        serviceGrid.addColumn(AvailableCarServiceDto::getDescription).setHeader("Description");
        serviceGrid.addColumn(AvailableCarServiceDto::getCost).setHeader("Cost [PLN]").setSortable(true);
        serviceGrid.addColumn(AvailableCarServiceDto::getRepairTimeInMinutes).setHeader("Repair time [min]");
        serviceGrid.setMaxHeight("250px");
        serviceGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void formServiceLayout() {
        serviceText.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);

        confirmServiceButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        serviceLayout.setMaxWidth("1000px");
        serviceLayout.setWidthFull();
        add(serviceLayout);
    }

    private void addServiceLayoutListeners() {
        serviceGrid.addSelectionListener(selection -> {
            selectedServices = selection.getAllSelectedItems();
            LOGGER.info("Selected services id: " + selectedServices.stream().map(AvailableCarServiceDto::getId).toList());
        });

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
    }

    private void formDetailsLayoutAndAddBackButtonListener() {
        backButton.addClassName(LumoUtility.Margin.Top.LARGE);
        detailsLayout.setMaxWidth("600px");
        detailsLayout.setWidthFull();

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
    }

    private void formForecastLayoutAndDatePicker() {
        dateWithForecast.setAlignItems(Alignment.BASELINE);
        forecastLayout.setSpacing(false);
        dateText.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.NONE);

        LocalDate now = LocalDate.now();
        datePicker.setMin(now);
        datePicker.setMax(now.plusDays(60));
        datePicker.setHelperText("Service date must be within 60 days from today, remember we work Mondays - Saturdays only");
        datePicker.addClassNames(LumoUtility.Margin.Top.NONE);
    }

    private void addDatePickerListener() {
        datePicker.addValueChangeListener(event -> {
            forecastLayout.removeAll();
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
                forecastLayout.removeAll();
                selectedDate = datePicker.getValue();
                if (!selectedDate.isAfter(LocalDate.now().plusDays(13))){
                    setWeather();
                } else {
                    forecastLayout.add(new Span("Forecast is only available for 13 days ahead."));
                }
                LOGGER.info("Selected book date: " + selectedDate);
                timePicker.setItems(bookingService.getAvailableBookingTimes(selectedDate, totalRepairTime, selectedGarage.getId()));
            }
        });
    }

    private void formBookLayoutElements() {
        addBookText.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.NONE);
        timePicker.setMaxWidth("300px");
        timePicker.setWidthFull();
        addBookButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    }

    private void addBookLayoutListeners() {
        timePicker.addValueChangeListener(event -> {
            selectedStartTime = timePicker.getValue();
            LOGGER.info("Selected time: " + selectedStartTime);
        });

        addBookButton.addClickListener(event -> {
            LOGGER.info("Button save clicked.");
            if (selectedDate != null && selectedStartTime != null) {
                detailsLayout.setVisible(false);
                dateLayout.setVisible(false);
                bookLayout.setVisible(false);
                List<Long> selectedServicesIdList = selectedServices.stream().map(AvailableCarServiceDto::getId).toList();
                bookingService.saveBooking(selectedServicesIdList, selectedDate, selectedStartTime, selectedGarage.getId(), selectedCar.getId(), totalRepairTime);
                Anchor link = new Anchor("http://localhost:8081/services", "MyServices");
                add(thankYouText);
                add(endText);
                add(endText2);
                add(link);
            } else {
                Notification.show("First select date and time.");
            }
        });
    }


    private void setGarageWorkTimes(VerticalLayout garageWorkTimes, GarageDto selectedGarage) {
        garageWorkTimes.removeAll();
        Span span = new Span(selectedGarage.getName() + " opening hours:");
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

    private void setWeather() {
        ForecastDto forecastDto = weatherApiService.getWeatherForCityAndDate(selectedGarage.getAddress().substring(0, selectedGarage.getAddress().indexOf(" ")), selectedDate);
        Span span = new Span("Weather for city: " + selectedGarage.getAddress().substring(0, selectedGarage.getAddress().indexOf(" ")) + ", and date: " + selectedDate );
        span.addClassNames(LumoUtility.FontWeight.BOLD);
        Span span1 = new Span("Weather is: " + forecastDto.getSymbolPhrase().substring(0, 1).toUpperCase() + forecastDto.getSymbolPhrase().substring(1));
        Span span2 = new Span("Max temp. " + forecastDto.getMaxTemp() + "\u00B0C, min temp. " + forecastDto.getMinTemp() + "\u00B0C. Wind up to " + forecastDto.getMaxWindSpeed() + "km/h.");
        forecastLayout.add(span, span1, span2);
    }
}
