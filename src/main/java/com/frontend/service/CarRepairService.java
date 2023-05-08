package com.frontend.service;

import com.frontend.client.CarRepairClient;
import com.frontend.domainDto.response.CarRepairDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarRepairService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarRepairService.class);
    private final CarRepairClient carRepairClient;

    public List<CarRepairDto> getCarServices(String username) {
        List<CarRepairDto> carRepairDtoList = carRepairClient.getCarServices(username);
        LOGGER.info("Retrieved car services list with size of: " + carRepairDtoList.size());
        return carRepairDtoList;
    }

    public void cancelService(Long serviceId) {
        LOGGER.info("Deleting CarService with id: " + serviceId);
        carRepairClient.deleteService(serviceId);
    }
}
