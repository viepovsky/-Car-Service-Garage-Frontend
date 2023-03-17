package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.request.RegisterUserDto;
import com.frontend.domainDto.response.UserDto;
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

    public UserLoginDto getUserToLogin(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint())
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            return restTemplate.getForObject(url, UserLoginDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public UserDto getUser(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint() + "/information")
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            return restTemplate.getForObject(url, UserDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
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
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Boolean isRegistered(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint() + "/is-registered")
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();
            return restTemplate.getForObject(url, Boolean.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return true;
        }
    }
}
