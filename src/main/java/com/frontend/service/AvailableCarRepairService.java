package com.frontend.service;

import com.frontend.client.AvailableCarRepairClient;
import com.frontend.domainDto.response.AvailableCarRepairDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailableCarRepairService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailableCarRepairService.class);
    private final AvailableCarRepairClient availableCarRepairClient;

    public List<AvailableCarRepairDto> getAllAvailableServices(Long garageId) {
        List<AvailableCarRepairDto> availableCarRepairDtoList = availableCarRepairClient.getALlAvailableServices(garageId);
        LOGGER.info("Retrieved available services list with size of: " + availableCarRepairDtoList.size());
        return availableCarRepairDtoList;
    }
}
