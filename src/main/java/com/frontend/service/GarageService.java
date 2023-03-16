package com.frontend.service;

import com.frontend.client.GarageClient;
import com.frontend.domainDto.response.GarageDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GarageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GarageService.class);
    private final GarageClient garageClient;

    public List<GarageDto> getGarages() {
        List<GarageDto> garageDtoList = garageClient.getGarages();
        LOGGER.info("Retrieved garage list with size of: " + garageDtoList.size());
        return garageDtoList;
    }
}
