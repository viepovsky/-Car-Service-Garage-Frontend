package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.response.ForecastDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class WeatherApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public ForecastDto getWeatherForCityAndDate(String city, LocalDate date) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getWeatherApiEndpoint())
                    .queryParam("city", city)
                    .queryParam("date", date.toString())
                    .build()
                    .encode()
                    .toUri();
            return restTemplate.getForObject(url, ForecastDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ForecastDto();
        }
    }
}
