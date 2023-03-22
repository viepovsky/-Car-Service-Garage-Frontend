package com.frontend.service;

import com.frontend.client.WeatherApiClient;
import com.frontend.domainDto.response.ForecastDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WeatherApiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiService.class);
    private final WeatherApiClient weatherApiClient;

    public ForecastDto getWeatherForCityAndDate(String city, LocalDate date) {
        ForecastDto forecastDto = weatherApiClient.getWeatherForCityAndDate(city, date);
        LOGGER.info("Retrieved forecast: " + forecastDto);
        return forecastDto;
    }
}
