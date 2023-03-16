package com.frontend.domainDto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("make")
    private String make;

    @JsonProperty("model")
    private String model;

    @JsonProperty("year")
    private int year;

    @JsonProperty("type")
    private String type;

    @JsonProperty("engine")
    private String engine;

    public CarDto(CarDto carDto) {
        this.id = carDto.getId();
        this.make = carDto.getMake();
        this.model = carDto.getModel();
        this.year = carDto.getYear();
        this.type = carDto.getType();
        this.engine = carDto.getEngine();
    }
}
