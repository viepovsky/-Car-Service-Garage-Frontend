package com.frontend.domainDto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GarageWorkTimeDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("day")
    private String day;

    @JsonProperty("startHour")
    private LocalTime startHour;

    @JsonProperty("endHour")
    private LocalTime endHour;
}
