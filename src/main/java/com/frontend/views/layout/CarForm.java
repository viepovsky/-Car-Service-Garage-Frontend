package com.frontend.views.layout;

import com.frontend.client.CarClient;
import com.frontend.domainDto.response.CarDto;
import com.frontend.service.CarApiService;
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

public class CarForm extends FormLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarClient.class);
    private CarApiService carApiService;
    private CarView carView;
    private ComboBox<Integer> year = new ComboBox<>("Year");
    private ComboBox<String> make = new ComboBox<>("Make");
    private ComboBox<String> type = new ComboBox<>("Type");
    private ComboBox<String> model = new ComboBox<>("Model");
    private TextField engine = new TextField("Engine");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Binder<CarDto> binder = new Binder<CarDto>(CarDto.class);

    public CarForm(CarApiService carApiService, CarView carView) {
        this.carApiService = carApiService;
        this.carView = carView;
        binder.bindInstanceFields(this);
        year.setItems(carApiService.getCarYears());
        make.setItems(carApiService.getCarMakes());
        type.setItems(carApiService.getCarTypes());
        model.setItems(carApiService.getCarModels(make.getValue(), type.getValue(), year.getValue()));
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(year, make, type, model, engine, buttons);
//        model.setVisible(false);
//        if (year.getOptionalValue().isPresent() && make.getOptionalValue().isPresent() && type.getOptionalValue().isPresent()) {
//            model.setItems(carApiService.getCarModels(make.getValue(), type.getValue(), year.getValue()));
//            model.setVisible(true);
//        }
        save.addClickListener(event -> save());
        delete.addClickListener(event -> delete());
    }

    public void setCarDto(CarDto carDto) {
        binder.setBean(carDto);
        if (carDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            make.focus();
        }
    }

    private void save() {
        LOGGER.info("Method save clicked");
    }

    private void delete() {
        LOGGER.info("Method delete clicked");
    }
}
