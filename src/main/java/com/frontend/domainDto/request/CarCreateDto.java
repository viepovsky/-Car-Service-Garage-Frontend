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

    @NotNull
    private Long userId;

}
