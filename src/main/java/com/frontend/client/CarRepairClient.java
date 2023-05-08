package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.response.CarRepairDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
@AllArgsConstructor
public class CarRepairClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarRepairClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public List<CarRepairDto> getCarServices(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarServiceApiEndpoint())
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            CarRepairDto[] response = restTemplate.getForObject(url, CarRepairDto[].class);
            return Arrays.asList(ofNullable(response).orElse(new CarRepairDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void deleteService(Long serviceId) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarServiceApiEndpoint() + "/" + serviceId)
                    .build()
                    .encode()
                    .toUri();
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
