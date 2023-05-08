package com.frontend.client;

import com.frontend.config.BackendConfig;
import com.vaadin.flow.server.VaadinSession;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@Component
@AllArgsConstructor
public class BookingClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingClient.class);
    private final RestTemplate restTemplate;
    private final BackendConfig backendConfig;

    public List<LocalTime> getAvailableBookingTimes(LocalDate date, int repairDuration, Long garageId) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getBookingApiEndpoint() + "/available-times")
                    .queryParam("date", date.toString())
                    .queryParam("repair-duration", repairDuration)
                    .queryParam("garage-id", garageId)
                    .build()
                    .encode()
                    .toUri();

            ResponseEntity<LocalTime[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, LocalTime[].class);
            return Arrays.asList(ofNullable(response.getBody()).orElse(new LocalTime[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<LocalTime> getAvailableBookingTimes(LocalDate selectedNewDate, Long carServiceId) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getBookingApiEndpoint() + "/available-times")
                    .queryParam("date", selectedNewDate.toString())
                    .queryParam("car-service-id", carServiceId)
                    .build()
                    .encode()
                    .toUri();

            ResponseEntity<LocalTime[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, LocalTime[].class);
            return Arrays.asList(ofNullable(response.getBody()).orElse(new LocalTime[0]));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void saveBooking(List<Long> selectedServiceIdList, LocalDate date, LocalTime startHour, Long garageId, Long carId, int repairDuration) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getBookingApiEndpoint())
                    .queryParam("service-id", selectedServiceIdList)
                    .queryParam("date", date.toString())
                    .queryParam("start-hour", startHour)
                    .queryParam("garage-id", garageId)
                    .queryParam("car-id", carId)
                    .queryParam("repair-duration", repairDuration)
                    .build()
                    .encode()
                    .toUri();

            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void updateBooking(Long bookingId, LocalDate selectedNewDate, LocalTime selectedNewStartTime) {
        try {
            HttpHeaders header = createJwtHeader();
            HttpEntity<Void> requestEntity = new HttpEntity<>(header);

            URI url = UriComponentsBuilder.fromHttpUrl(backendConfig.getBookingApiEndpoint() + "/" + bookingId)
                    .queryParam("date", selectedNewDate.toString())
                    .queryParam("start-hour", selectedNewStartTime)
                    .build()
                    .encode()
                    .toUri();

            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
        } catch (RestClientException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    private HttpHeaders createJwtHeader() {
        String jwtToken = VaadinSession.getCurrent().getAttribute("jwt").toString();
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + jwtToken);
        return header;
    }
}
