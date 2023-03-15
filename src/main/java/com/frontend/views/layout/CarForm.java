package com.frontend.views.layout;

import com.frontend.client.CarClient;
import com.frontend.domainDto.request.CarCreateDto;
import com.frontend.service.CarApiService;
import com.frontend.service.CarService;
import com.frontend.views.CarView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CarForm extends FormLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarClient.class);
    private final String currentUsername = "testuser6";
    private CarApiService carApiService;
    private CarService carService;
    private CarView carView;
    private ComboBox<Integer> year = new ComboBox<>("Year");
    private ComboBox<String> make = new ComboBox<>("Make");
    private ComboBox<String> type = new ComboBox<>("Type");
    private ComboBox<String> model = new ComboBox<>("Model");
    private TextField engine = new TextField("Engine");
    private Button save = new Button("Save");
    private Button edit = new Button("Edit");
    private Button delete = new Button("Delete");
    private Button refresh = new Button("Click to refresh car models.");
    private Button cancel = new Button("Cancel");
    private Binder<CarCreateDto> binder = new BeanValidationBinder<>(CarCreateDto.class);
    private CarCreateDto temporaryDto;
    public CarForm(CarApiService carApiService, CarService carService, CarView carView) {
        this.carApiService = carApiService;
        this.carService = carService;
        this.carView = carView;

        binder.bindInstanceFields(this);

        year.setItems(carApiService.getCarYears());
        make.setItems(carApiService.getCarMakes());
        type.setItems(carApiService.getCarTypes());

        HorizontalLayout buttons = new HorizontalLayout(save, edit, delete, cancel);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        refresh.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        refresh.addClassName(LumoUtility.Margin.Top.MEDIUM);
        add(year, make, type, model, engine);
        add(refresh);
        add(buttons);

        save.addClickListener(event -> save());
        delete.addClickListener(event -> delete());
        refresh.addClickListener(event -> refreshModels());

        binder.addValueChangeListener( event -> {
            CarCreateDto carCreateDto = new CarCreateDto(binder.getBean());
            if (temporaryDto != null) {
                if (temporaryDto.getYear() != carCreateDto.getYear() || !temporaryDto.getMake().equals(carCreateDto.getMake()) || !temporaryDto.getType().equals(carCreateDto.getType())) {
                    LOGGER.info("Model items set to new ArrayList");
                    model.setItems(new ArrayList<>());
                }
            }
        });
    }

    public void setCarCreateDto(CarCreateDto carCreateDto) {
    if (carCreateDto == null) {
        setVisible(false);
    } else if (carCreateDto.equals(new CarCreateDto())) {
        model.setItems(new ArrayList<>());
        binder.setBean(new CarCreateDto());
        save.setVisible(true);
        cancel.setVisible(true);
        delete.setVisible(false);
        edit.setVisible(false);
        refresh.setVisible(true);
        setVisible(true);
        year.focus();
    } else {
        binder.setBean(new CarCreateDto());
        List<String> modelList = carApiService.getCarModels(carCreateDto.getMake(), carCreateDto.getType(), carCreateDto.getYear());
        model.setItems(modelList);
        binder.setBean(carCreateDto);
        save.setVisible(false);
        cancel.setVisible(false);
        delete.setVisible(true);
        edit.setVisible(true);
        refresh.setVisible(false);
        setVisible(true);
        year.focus();
    }
}

    private void save() {
        CarCreateDto carCreateDto = binder.getBean();
        LOGGER.info("Car: " + carCreateDto);
//        carService.saveCar(carCreateDto, currentUsername);
        LOGGER.info("Button save clicked");
        carView.refresh();
        setCarCreateDto(null);
    }

    private void delete() {
        CarCreateDto carCreateDto = binder.getBean();
        LOGGER.info("Car: " + carCreateDto);
//        carService.deleteCar(carCreateDto.getId());
        LOGGER.info("Button delete clicked");
        carView.refresh();
        setCarCreateDto(null);
    }

    private void refreshModels() {
        CarCreateDto carCreateDto = new CarCreateDto(binder.getBean());
        LOGGER.info("Clicked refresh button with values: " + carCreateDto);
        if (carCreateDto.getYear() > 1950 && carCreateDto.getMake() != null && carCreateDto.getType() != null ){
            List<String> modelList = carApiService.getCarModels(carCreateDto.getMake(), carCreateDto.getType(), carCreateDto.getYear());
            model.setItems(modelList);
            Notification.show("You can pick your car model now.");
            this.temporaryDto = new CarCreateDto(carCreateDto);
        } else {
            Notification.show("You need to pick year, make and type in order to refresh model.");
        }
    }
}
