package com.frontend.views;

import com.frontend.domainDto.request.CarCreateDto;
import com.frontend.service.CarApiService;
import com.frontend.service.CarService;
import com.frontend.views.layout.CarForm;
import com.frontend.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route(value = "/cars", layout = MainLayout.class)
@PageTitle("Cars | Garage Booking Service")
public class CarView extends VerticalLayout {
    private final String currentUsername = "testuser6"; //SecurityContextHolder.getContext().getAuthentication().getName();
    private final CarService carService;
    private final CarApiService carApiService;
    private final CarForm form;
    private Grid<CarCreateDto> grid = new Grid<>(CarCreateDto.class);
    private Button addNewCar = new Button("Add new car");
    public CarView(CarService carService, CarApiService carApiService) {
        this.carService = carService;
        this.carApiService = carApiService;
        form = new CarForm(carApiService, carService, this);

        grid.setColumns("make", "model", "year", "type", "engine");

        addNewCar.addClickListener(e -> {
            grid.asSingleSelect().clear();
            form.setCarCreateDto(new CarCreateDto());
        });
        HorizontalLayout toolbar = new HorizontalLayout(addNewCar);

        HorizontalLayout carContent = new HorizontalLayout(grid, form);
        carContent.setSizeFull();
        grid.setSizeFull();

        setSpacing(false);
        add(new Paragraph("Here you have got access to all your cars, you can add, edit or delete them."));
        add(new Paragraph("To book services, you must have at least one car added."));

        add(toolbar, carContent);
        form.setCarCreateDto(null);
        setSizeFull();
        refresh();

        grid.asSingleSelect().addValueChangeListener(event -> form.setCarCreateDto(grid.asSingleSelect().getValue()));
    }
    public void refresh() {
        grid.setItems(carService.getMappedCarsForGivenUsername(currentUsername));
    }
}
