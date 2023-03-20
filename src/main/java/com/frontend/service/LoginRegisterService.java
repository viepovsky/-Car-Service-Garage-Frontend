package com.frontend.service;

import com.frontend.client.UserClient;
import com.frontend.domainDto.request.RegisterUserDto;
import com.frontend.domainDto.response.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginRegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRegisterService.class);

    private final UserClient userClient;
    public UserLoginDto getUser(String username) {
        LOGGER.info("Retrieving data about user with username: " + username);
        return userClient.getUserForLogin(username);
    }

    public void createUser(RegisterUserDto registerUserDto) {
        if (registerUserDto == null) {
            LOGGER.error("Given object for registration is null.");
            return;
        }
        LOGGER.info("Creating user with username: " + registerUserDto.getUsername());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        registerUserDto.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userClient.createUser(registerUserDto);
    }

    public boolean isRegistered(String username) {
        return userClient.isRegistered(username);
    }
}
