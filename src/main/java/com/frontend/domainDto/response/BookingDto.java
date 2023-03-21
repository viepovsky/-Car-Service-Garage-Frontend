package com.frontend.domainDto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("startHour")
    private LocalTime startHour;
    @JsonProperty("endHour")
    private LocalTime endHour;
    @JsonProperty("created")
    private LocalDateTime created;
    @JsonProperty("totalCost")
    private BigDecimal totalCost;
    @JsonProperty("carServiceDtoIdList")
    private List<Long> carServiceDtoIdList;
    @JsonProperty("garageDto")
    private GarageDto garageDto;
}
