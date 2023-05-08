package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.request.AuthenticationUserRequest;
import com.frontend.domainDto.request.RegisterUserDto;
import com.frontend.domainDto.response.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class AuthenticationClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public JwtTokenResponse getAuthenticationToken(AuthenticationUserRequest request) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getAuthenticationApiEndpoint() + "/authenticate")
                    .build()
                    .encode()
                    .toUri();

            return restTemplate.postForObject(url, request, JwtTokenResponse.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void registerUser(RegisterUserDto request) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getAuthenticationApiEndpoint() + "/register")
                    .build()
                    .encode()
                    .toUri();

            restTemplate.postForObject(url, request, Void.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
