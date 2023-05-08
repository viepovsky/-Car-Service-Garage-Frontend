package com.frontend.domainDto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarRepairDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("cost")
    private BigDecimal cost;

    @JsonProperty("repairTimeInMinutes")
    private int repairTimeInMinutes;

    @JsonProperty("carDto")
    private CarDto carDto;

    @JsonProperty("bookingDto")
    private BookingDto bookingDto;

    @JsonProperty("status")
    private String status;
}
