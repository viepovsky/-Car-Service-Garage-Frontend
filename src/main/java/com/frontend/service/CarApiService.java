package com.frontend.service;

import com.frontend.client.CarApiClient;
import com.frontend.client.CarClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarApiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarClient.class);
    private final CarApiClient carApiClient;

    public List<String> getCarMakes() {
        LOGGER.info("Getting car makes.");
        return Arrays.asList("BMW", "Audi", "Volvo");
//        return carApiClient.getCarMakes();
    }

    public List<String> getCarTypes() {
        LOGGER.info("Getting car types.");
        return Arrays.asList("SEDAN", "COUPE", "SUV");
//        return carApiClient.getCarTypes();
    }

    public List<Integer> getCarYears() {
        LOGGER.info("Getting car years.");
        return Arrays.asList(2013, 2014, 2015);
//        return carApiClient.getCarYears();
    }

    public List<String> getCarModels(String make, String type, Integer year) {
        if (make == null){
            return new ArrayList<>();
        }
        LOGGER.info("Getting car models.");
//        List<String> carModelsList = carApiClient.getCarModels(make, type, year);
        List<String> carModelsList = Arrays.asList("M5", "5 Series", "7 Series", "3 Series");
        LOGGER.info("Car models received: " + carModelsList);
        return carModelsList;
    }


}
