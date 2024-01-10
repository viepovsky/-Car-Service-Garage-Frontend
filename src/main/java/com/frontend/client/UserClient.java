package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.frontend.domainDto.request.RegisterUserDto;
import com.frontend.domainDto.request.UpdateUserDto;
import com.frontend.domainDto.response.PasswordDto;
import com.frontend.domainDto.response.UserDto;
import com.frontend.domainDto.response.UserLoginDto;
import com.vaadin.flow.server.VaadinSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    public UserLoginDto getUserForLogin(String username) {
        try {
            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint() + "/login")
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();

            return restTemplate.getForObject(url, UserLoginDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public UserDto getUser(String username) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint() + "/information")
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();

            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserDto.class).getBody();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage());
            return null;
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
            LOGGER.error(e.getMessage());
            return true;
        }
    }

    public PasswordDto getPassword(String username) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint() + "/pass")
                    .queryParam("username", username)
                    .build()
                    .encode()
                    .toUri();

            return restTemplate.exchange(url, HttpMethod.GET, requestEntity, PasswordDto.class).getBody();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage());
            return new PasswordDto();
        }
    }

    public void updateUser(UpdateUserDto updateUserDto) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<UpdateUserDto> requestEntity = new HttpEntity<>(updateUserDto, header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getUserApiEndpoint())
                    .build()
                    .encode()
                    .toUri();

            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class).getBody();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private HttpHeaders createJwtHeader() {
        String jwtToken = VaadinSession.getCurrent().getAttribute("jwt").toString();
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + jwtToken);
        return header;
    }
}
