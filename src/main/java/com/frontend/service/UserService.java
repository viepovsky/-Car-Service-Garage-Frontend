package com.frontend.service;

import com.frontend.client.UserClient;
import com.frontend.domainDto.response.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserClient userClient;
    public UserDto getUser(String username) {
        LOGGER.info("Retrieving data about user with username: " + username);
        return userClient.getUser(username);
    }
}
