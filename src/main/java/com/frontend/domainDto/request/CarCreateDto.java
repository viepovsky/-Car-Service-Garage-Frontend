package com.frontend.domainDto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarCreateDto {

    private Long id;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotNull
    private int year;

    @NotBlank
    private String type;

    @NotBlank
    private String engine;

    public CarCreateDto(CarCreateDto carCreateDto) {
        this.id = carCreateDto.getId();
        this.make = carCreateDto.getMake();
        this.model = carCreateDto.getModel();
        this.year = carCreateDto.getYear();
        this.type = carCreateDto.getType();
        this.engine = carCreateDto.getEngine();
    }
}
