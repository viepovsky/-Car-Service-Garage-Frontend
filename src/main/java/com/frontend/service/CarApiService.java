package com.frontend.service;

import com.frontend.client.CarApiClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarApiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarApiService.class);
    private final CarApiClient carApiClient;

    public List<String> getCarMakes() {
        LOGGER.info("Getting car makes.");
        return carApiClient.getCarMakes();
    }

    public List<String> getCarTypes() {
        LOGGER.info("Getting car types.");
        return carApiClient.getCarTypes();
    }

    public List<Integer> getCarYears() {
        LOGGER.info("Getting car years.");
        return carApiClient.getCarYears();
    }

    public List<String> getCarModels(String make, String type, Integer year) {
        LOGGER.info("Getting car models.");
        List<String> carModelsList = carApiClient.getCarModels(make, type, year);
        LOGGER.info("Car models received: " + carModelsList);
        return carModelsList;
    }
}
