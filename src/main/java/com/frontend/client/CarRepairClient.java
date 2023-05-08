package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.response.CarDto;
import com.frontend.domainDto.response.CarRepairDto;
import com.vaadin.flow.server.VaadinSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarServiceApiEndpoint())
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();

            ResponseEntity<CarRepairDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, CarRepairDto[].class);
            return Arrays.asList(ofNullable(response.getBody()).orElse(new CarRepairDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void deleteService(Long serviceId) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getCarServiceApiEndpoint() + "/" + serviceId)
                    .build()
                    .encode()
                    .toUri();

            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private HttpHeaders createJwtHeader() {
        String jwtToken = VaadinSession.getCurrent().getAttribute("jwt").toString();
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + jwtToken);
        return header;
    }
}
