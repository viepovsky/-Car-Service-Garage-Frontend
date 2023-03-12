package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.UserLoginDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@AllArgsConstructor
public class UserClient {
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public UserLoginDto getUser(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint())
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            return restTemplate.getForObject(url, UserLoginDto.class);
        } catch (RestClientException e) {
            return null;
        }
    }
}
