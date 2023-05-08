package com.frontend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BackendConfig {
    @Value("${availableCarService.api.endpoint}")
    private String availableCarServiceApiEndpoint;
    @Value("${authentication.api.endpoint}")
    private String authenticationApiEndpoint;
    @Value("${booking.api.endpoint}")
    private String bookingApiEndpoint;
    @Value("${car.api.endpoint}")
    private String carApiEndpoint;
    @Value("${carService.api.endpoint}")
    private String carServiceApiEndpoint;
    @Value("${user.api.endpoint}")
    private String userApiEndpoint;
    @Value("${garage.api.endpoint}")
    private String garageApiEndpoint;
    @Value("${garageWorkTime.api.endpoint}")
    private String garageWorkTimeApiEndpoint;
    @Value("${car-api.api.endpoint}")
    private String carApiMakesEndpoint;
    @Value("${weather-api.api.endpoint}")
    private String weatherApiEndpoint;







}
