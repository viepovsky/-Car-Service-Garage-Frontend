package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.request.CarCreateDto;
import com.frontend.domainDto.request.RegisterUserDto;
import com.frontend.domainDto.response.CarDto;
import com.frontend.domainDto.response.UserLoginDto;
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
public class CarClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public List<CarDto> getCarsForGivenUsername(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarApiEndpoint())
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            CarDto[] response = restTemplate.getForObject(url, CarDto[].class);
            return Arrays.asList(ofNullable(response).orElse(new CarDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void saveCar(CarCreateDto carCreateDto, String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarApiEndpoint())
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            restTemplate.postForObject(url, carCreateDto, Void.class);
            LOGGER.info("Created car for given username: " + username);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void deleteCar(Long carId) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarApiEndpoint() + "/" + carId)
                    .build()
                    .encode()
                    .toUri();
            restTemplate.delete(url);
            LOGGER.info("Deleted with given id: " + carId);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
