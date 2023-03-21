package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.response.CarServiceDto;
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
public class CarServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public void addService(List<Long> selectedServicesIdList, Long carId) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarServiceApiEndpoint())
                    .queryParam("service-id", selectedServicesIdList)
                    .queryParam("car-id", carId)
                    .build()
                    .encode()
                    .toUri();
            restTemplate.postForObject(url, null, Void.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<CarServiceDto> getCarServices(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarServiceApiEndpoint())
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            CarServiceDto[] response = restTemplate.getForObject(url, CarServiceDto[].class);
            return Arrays.asList(ofNullable(response).orElse(new CarServiceDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

}
