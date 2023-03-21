package com.frontend.service;

import com.frontend.client.BookingClient;
import com.frontend.domainDto.response.CarServiceDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);
    private final BookingClient bookingClient;

    public List<LocalTime> getAvailableBookingTimes(LocalDate date, int repairDuration, Long garageId) {
        LOGGER.info("Given parameters to get available times, date: " + date + ", total repair time: " + repairDuration + ", garage id: " + garageId);
        List<LocalTime> localTimeList = bookingClient.getAvailableBookingTimes(date, repairDuration, garageId);
        LOGGER.info("Retrieved LocalTime list with size of: " + localTimeList.size());
        return localTimeList;
    }

    public List<LocalTime> getAvailableBookingTimes(LocalDate selectedNewDate, CarServiceDto selectedCarService) {
        LOGGER.info("Given parameters to get available times, date: " + selectedNewDate + ", selected CarService: " + selectedCarService);
        List<LocalTime> localTimeList = bookingClient.getAvailableBookingTimes(selectedNewDate, selectedCarService.getId());
        LOGGER.info("Retrieved LocalTime list with size of: " + localTimeList.size());
        return localTimeList;
    }

    public void saveBooking(List<Long> selectedServiceIdList, LocalDate date, LocalTime startHour, Long garageId, Long carId, int repairDuration) {
        LOGGER.info("Saving booking and related carServices.");
        bookingClient.saveBooking(selectedServiceIdList, date, startHour, garageId, carId, repairDuration);
    }

    public void updateBooking(Long bookingId, LocalDate selectedNewDate, LocalTime selectedNewStartTime) {
        LOGGER.info("Updating booking with new date: " + selectedNewDate + " and time: " + selectedNewStartTime);
        bookingClient.updateBooking(bookingId, selectedNewDate, selectedNewStartTime);
    }
}
