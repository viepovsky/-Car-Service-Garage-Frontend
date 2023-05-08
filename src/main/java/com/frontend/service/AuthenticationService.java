package com.frontend.service;

import com.frontend.client.AuthenticationClient;
import com.frontend.domainDto.request.AuthenticationUserRequest;
import com.frontend.domainDto.request.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationClient client;

    public void registerUser(RegisterUserDto request) {
        client.registerUser(request);
    }

    public String getAuthenticationToken(AuthenticationUserRequest request) {
        var response = client.getAuthenticationToken(request);
        return response.getJwtToken();
    }

}
