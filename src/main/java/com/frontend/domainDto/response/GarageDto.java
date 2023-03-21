package com.frontend.domainDto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GarageDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("garageWorkTimeDtoList")
    private List<GarageWorkTimeDto> garageWorkTimeDtoList;
    public GarageDto(GarageDto garageDto) {
        this.id = garageDto.getId();
        this.name = garageDto.getName();
        this.address = garageDto.getAddress();
        this.garageWorkTimeDtoList = garageDto.getGarageWorkTimeDtoList();
    }
}
