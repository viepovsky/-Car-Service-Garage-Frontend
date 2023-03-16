package com.frontend.views;

import com.frontend.domainDto.request.CarCreateDto;
import com.frontend.domainDto.response.AvailableCarServiceDto;
import com.frontend.domainDto.response.CarDto;
import com.frontend.domainDto.response.GarageDto;
import com.frontend.service.AvailableServiceCarService;
import com.frontend.service.CarService;
import com.frontend.service.GarageService;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AnonymousAllowed
@Route(value = "/book", layout = MainLayout.class)
@PageTitle("Book services | Garage Booking Service")
public class BookView extends VerticalLayout {
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private static final Logger LOGGER = LoggerFactory.getLogger(BookView.class);
    private GarageService garageService;
    private CarService carService;
    private AvailableServiceCarService availableServiceCarService;
    private GarageDto selectedGarage;
    private CarDto selectedCar;
    private Set<AvailableCarServiceDto> selectedServices;
    private Paragraph garageText = new Paragraph("Here you can book your services, first select garage by clicking it.");

    private Grid<GarageDto> garageGrid = new Grid<>(GarageDto.class, false);
    private VerticalLayout garageLayout = new VerticalLayout(garageText, garageGrid);
    private Paragraph carText = new Paragraph("If you have already selected garage, now pick your car below:");
    private ComboBox<CarDto> carComboBox = new ComboBox<>("Select your car:");
    private VerticalLayout carLayout = new VerticalLayout(carText, carComboBox);
    private Paragraph serviceText = new Paragraph("Below select services you wish to have:");
    private Grid<AvailableCarServiceDto> serviceGrid = new Grid<>(AvailableCarServiceDto.class, false);
    private VerticalLayout serviceLayout = new VerticalLayout(serviceText, serviceGrid);

    public BookView(GarageService garageService, CarService carService, AvailableServiceCarService availableServiceCarService) {
        this.garageService = garageService;
        this.carService = carService;
        this.availableServiceCarService = availableServiceCarService;

        carLayout.setVisible(false);
        serviceLayout.setVisible(false);
        setSpacing(false);

        garageGrid.addColumn(GarageDto::getName).setHeader("Garage name").setSortable(true);
        garageGrid.addColumn(GarageDto::getAddress).setHeader("Address").setSortable(true);
        List<GarageDto> garageDtoList = garageService.getGarages();
        garageGrid.setItems(garageDtoList);
        garageGrid.addSelectionListener(selection -> {
            Optional<GarageDto> optionalGarage = selection.getFirstSelectedItem();
            if (optionalGarage.isPresent()) {
                selectedGarage = new GarageDto(optionalGarage.get());
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
                selectedGarage = null;
                selectedCar = null;
                carComboBox.setValue(null);
            }
        });
        garageGrid.setMaxHeight("250px");
        garageLayout.setMaxWidth("1000px");
        garageLayout.setWidthFull();
        add(garageLayout);

        carText.addClassName(LumoUtility.Margin.Top.NONE);
        carText.addClassName(LumoUtility.Margin.Bottom.NONE);
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

        serviceText.addClassName(LumoUtility.Margin.Top.NONE);
        serviceText.addClassName(LumoUtility.Margin.Bottom.NONE);
        serviceGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        serviceGrid.addColumn(AvailableCarServiceDto::getName).setHeader("Service").setSortable(true);
        serviceGrid.addColumn(AvailableCarServiceDto::getDescription).setHeader("Description");
        serviceGrid.addColumn(AvailableCarServiceDto::getCost).setHeader("Cost").setSortable(true);
        serviceGrid.addColumn(AvailableCarServiceDto::getRepairTimeInMinutes).setHeader("Repair time [minutes]");
        serviceGrid.addSelectionListener(selection -> {
            selectedServices = selection.getAllSelectedItems();
            LOGGER.info("Selected services id: " + selectedServices.stream().map(AvailableCarServiceDto::getId).toList());
        });
        serviceGrid.setMaxHeight("250px");
        serviceLayout.setMaxWidth("1000px");
        serviceLayout.setWidthFull();
        add(serviceLayout);

    }
}
