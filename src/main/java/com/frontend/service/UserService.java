package com.frontend.service;

import com.frontend.client.UserClient;
import com.frontend.domainDto.request.UpdateUserDto;
import com.frontend.domainDto.response.PasswordDto;
import com.frontend.domainDto.response.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserClient userClient;
    public UserDto getUser(String username) {
        LOGGER.info("Retrieving data about user with username: " + username);
        UserDto userDto = userClient.getUser(username);
        LOGGER.info("Retrieved user: " + userDto);
        return userDto;
    }

    public boolean isPasswordMatched(String username, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        PasswordDto passwordDto = userClient.getPassword(username);
        String retrievedPassword = passwordDto.getPassword();
        LOGGER.info("Retrieved password: " + retrievedPassword + "current password raw to check: " + password);
        boolean matches = passwordEncoder.matches(password, retrievedPassword);
        LOGGER.info("Password match: " + matches);
        return matches;
    }

    public void updateUser(UpdateUserDto updateUserDto) {
        LOGGER.info("Updating user with username: " + updateUserDto.getUsername());
        if (updateUserDto.getNewPassword() != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            updateUserDto.setPassword(passwordEncoder.encode(updateUserDto.getNewPassword()));
            updateUserDto.setNewPassword(null);
        }
        userClient.updateUser(updateUserDto);
    }
}
