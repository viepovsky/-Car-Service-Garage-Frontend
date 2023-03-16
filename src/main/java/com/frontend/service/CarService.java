package com.frontend.service;

import com.frontend.client.CarClient;
import com.frontend.domainDto.request.CarCreateDto;
import com.frontend.domainDto.response.CarDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);
    private final CarClient carClient;

    public List<CarCreateDto> getCarsForGivenUsername(String username) {
        if (username == null) {
            LOGGER.error("Given username is invalid.");
            return new ArrayList<>();
        }
        List<CarDto> carDtoList = carClient.getCarsForGivenUsername(username);
        return carDtoList.stream()
                .map(n -> new CarCreateDto(
                        n.getId(),
                        n.getMake(),
                        n.getModel(),
                        n.getYear(),
                        n.getType(),
                        n.getEngine()
                ))
                .toList();
    }

    public void updateCar(CarCreateDto carCreateDto) {
        if (carCreateDto == null) {
            LOGGER.error("Given object for update is null.");
            return;
        }{
            carClient.updateCar(carCreateDto);
        }
    }

    public void saveCar(CarCreateDto carCreateDto, String username) {
        if (carCreateDto == null) {
            LOGGER.error("Given object for save is null.");
            return;
        }{
            carClient.saveCar(carCreateDto, username);
        }
    }

    public void deleteCar(Long carId) {
        if (carId == null || carId < 0) {
            LOGGER.error("Given car id is invalid.");
            return;
        }{
            carClient.deleteCar(carId);
        }
    }

}
