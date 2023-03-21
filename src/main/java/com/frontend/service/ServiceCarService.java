package com.frontend.service;

import com.frontend.client.CarServiceClient;
import com.frontend.domainDto.response.CarServiceDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCarService.class);
    private final CarServiceClient carServiceClient;

    public void addService(List<Long> selectedServicesIdList, Long carId) {
        carServiceClient.addService(selectedServicesIdList, carId);
        LOGGER.info("Added services of given id: " + selectedServicesIdList + " to car of given id: " + carId);
    }

    public List<CarServiceDto> getCarServices(String username) {
        List<CarServiceDto> carServiceDtoList = carServiceClient.getCarServices(username);
        LOGGER.info("Retrieved car services list with size of: " + carServiceDtoList.size());
        return carServiceDtoList;
    }

    public void cancelService(Long serviceId) {
        LOGGER.info("Deleting CarService with id: " + serviceId);
        carServiceClient.deleteService(serviceId);
    }
}
