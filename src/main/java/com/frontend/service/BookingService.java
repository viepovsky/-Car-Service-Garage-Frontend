package com.frontend.service;

import com.frontend.client.BookingClient;
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
        List<LocalTime> localTimeList = bookingClient.getAvailableBookingTimes(date, repairDuration, garageId);
        LOGGER.info("Retrieved LocalTime list with size of: " + localTimeList.size());
        return localTimeList;
    }

}
