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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Button delete = new Button("Delete");
    private Binder<CarCreateDto> binder = new Binder<>(CarCreateDto.class);

    public CarForm(CarApiService carApiService, CarService carService, CarView carView) {
        this.carApiService = carApiService;
        this.carService = carService;
        this.carView = carView;


        binder.bindInstanceFields(this);

        year.setItems(carApiService.getCarYears());
        make.setItems(carApiService.getCarMakes());
        type.setItems(carApiService.getCarTypes());

        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(year, make, type, model, engine, buttons);

        save.addClickListener(event -> save());
        delete.addClickListener(event -> delete());
    }

public void setCarCreateDto(CarCreateDto carCreateDto) {
    if (carCreateDto == null) {
        setVisible(false);
    } else if (carCreateDto.equals(new CarCreateDto())) {
        binder.setBean(new CarCreateDto());
        save.setText("Save");
        setVisible(true);
        year.focus();
    } else {
        binder.setBean(new CarCreateDto());
        List<String> modelList = carApiService.getCarModels(carCreateDto.getMake(), carCreateDto.getType(), carCreateDto.getYear());
        model.setItems(modelList);
        binder.setBean(carCreateDto);
        save.setText("Edit");
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
}
