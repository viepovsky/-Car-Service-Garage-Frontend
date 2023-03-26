package com.frontend.views;

import com.frontend.domainDto.request.CarCreateDto;
import com.frontend.service.CarApiService;
import com.frontend.service.CarService;
import com.frontend.views.layout.CarForm;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;

@PermitAll
@Route(value = "/cars", layout = MainLayout.class)
@PageTitle("Cars | Garage Booking Service")
public class CarView extends VerticalLayout {
    private final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    private final CarService carService;
    private final CarForm form;
    private final Grid<CarCreateDto> carGrid = new Grid<>(CarCreateDto.class);
    private final Button addNewCar = new Button("Add new car");
    public CarView(CarService carService, CarApiService carApiService) {
        this.carService = carService;

        form = new CarForm(carApiService, carService, this);
        form.setCarCreateDto(null);

        addAndSetHeader();

        formCarGrid();
        formCarContent();

        addButtonNewCarListener();

        setSizeFull();
        refresh();

        carGrid.asSingleSelect().addValueChangeListener(event -> form.setCarCreateDto(carGrid.asSingleSelect().getValue()));
    }

    private void addAndSetHeader() {
        setSpacing(false);
        Paragraph information = new Paragraph("This page when it comes to getting car models works slower than the others, and that's intentional because the car API it uses only accepts one request per second in the free version.");
        information.addClassNames(LumoUtility.FontWeight.BOLD, LumoUtility.FontSize.LARGE);
        add(information);
        add(new Paragraph("Here you have got access to all your cars, you can add, edit or delete them."));
        add(new Paragraph("To book services, you must have at least one car added."));
    }

    private void formCarGrid(){
        carGrid.setColumns("make", "model", "year", "type", "engine");
        carGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        carGrid.setSizeFull();
    }

    private void formCarContent() {
        HorizontalLayout toolbar = new HorizontalLayout(addNewCar);
        HorizontalLayout carContent = new HorizontalLayout(carGrid, form);
        carContent.setSizeFull();
        add(toolbar, carContent);
    }

    private void addButtonNewCarListener() {
        addNewCar.addClickListener(e -> {
            carGrid.asSingleSelect().clear();
            form.setCarCreateDto(new CarCreateDto());
        });
    }
    public void refresh() {
        carGrid.setItems(carService.getMappedCarsForGivenUsername(currentUsername));
    }
}
