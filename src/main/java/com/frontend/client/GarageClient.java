package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.response.GarageDto;
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
public class GarageClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GarageClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public List<GarageDto> getGarages() {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getGarageApiEndpoint())
                    .build()
                    .encode()
                    .toUri();

            ResponseEntity<GarageDto[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GarageDto[].class);
            return Arrays.asList(ofNullable(response.getBody()).orElse(new GarageDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private HttpHeaders createJwtHeader() {
        String jwtToken = VaadinSession.getCurrent().getAttribute("jwt").toString();
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + jwtToken);
        return header;
    }
}
