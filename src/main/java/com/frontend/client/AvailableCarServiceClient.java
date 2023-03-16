package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.response.AvailableCarServiceDto;
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
public class AvailableCarServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableCarServiceClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public List<AvailableCarServiceDto> getALlAvailableServices(Long garageId) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getAvailableCarServiceApiEndpoint() + "/" + garageId)
                    .build()
                    .encode()
                    .toUri();
            AvailableCarServiceDto[] response = restTemplate.getForObject(url, AvailableCarServiceDto[].class);
            return Arrays.asList(ofNullable(response).orElse(new AvailableCarServiceDto[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

}
