package com.frontend.service;

import com.frontend.client.AvailableCarServiceClient;
import com.frontend.domainDto.response.AvailableCarServiceDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableServiceCarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableServiceCarService.class);
    private final AvailableCarServiceClient availableCarServiceClient;

    public List<AvailableCarServiceDto> getAllAvailableServices(Long garageId) {
        List<AvailableCarServiceDto> availableCarServiceDtoList = availableCarServiceClient.getALlAvailableServices(garageId);
        LOGGER.info("Retrieved available services list with size of: " + availableCarServiceDtoList.size());
        return availableCarServiceDtoList;
    }
}
