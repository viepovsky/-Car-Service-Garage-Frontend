package com.frontend.service;

import com.frontend.client.UserClient;
import com.frontend.domainDto.request.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

    private final UserClient userClient;
    public void createUser(RegisterUserDto registerUserDto) {
        if (registerUserDto == null) {
            LOGGER.error("Given object for registration is null.");
            return;
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        registerUserDto.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userClient.createUser(registerUserDto);
    }

    public boolean isRegistered(String username) {
        return userClient.isRegistered(username);
    }
}
