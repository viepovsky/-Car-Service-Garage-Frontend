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
    @JsonProperty("vehicleId")
    private Long vehicleId;

    @JsonProperty("vehicleModel")
    private ModelDto vehicleModel;

    @JsonProperty("manufactured_year")
    private int year;

    @JsonProperty("engineType")
    private String engineType;

    public CarDto(CarDto carDto) {
        this.vehicleId = carDto.getVehicleId();
        this.vehicleModel = carDto.getVehicleModel();
        this.year = carDto.getYear();
        this.engineType = carDto.getEngineType();
    }

    @Override
    public String toString() {
        return vehicleModel.vehicleMake().makeName() + ", " + vehicleModel.modelName() + ", " + year + ", " + vehicleModel.type() + ", " + engineType;
    }
}
