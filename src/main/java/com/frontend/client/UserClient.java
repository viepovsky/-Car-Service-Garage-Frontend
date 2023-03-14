package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.request.RegisterUserDto;
import com.frontend.domainDto.response.UserLoginDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@AllArgsConstructor
public class UserClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserClient.class);
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

    public void createUser(RegisterUserDto registerUserDto) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint())
                    .build()
                    .encode()
                    .toUri();
            restTemplate.postForObject(url, registerUserDto, Void.class);
            LOGGER.info("Created user with given username: " + registerUserDto.getUsername() );
        } catch (RestClientException e) {
            LOGGER.error("Error while creating user.", e);
        }
    }

    public Boolean isRegistered(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint() + "/is-registered")
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            LOGGER.info("Checking if account with username: " + username + " exists.");
            return restTemplate.getForObject(url, Boolean.class);
        } catch (RestClientException e) {
            LOGGER.error("Error while checking if account with username: " + username + " exists.", e);
            return true;
        }
    }
}
