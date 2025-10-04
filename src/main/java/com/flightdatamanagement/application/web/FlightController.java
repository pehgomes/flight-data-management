package com.flightdatamanagement.application.web;

import com.flightdatamanagement.application.dto.FlightRequest;
import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.application.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequiredArgsConstructor
public class FlightController implements FlightApi {

    private final FlightService flightService;

    @Override
    public Page<FlightResponse> getFlights(FlightFilter filter) {
        return flightService.getFlights(filter);
    }

    @Override
    public FlightResponse getById(UUID id) {
        return flightService.getById(id);
    }

    @Override
    public ResponseEntity<Void> createFlights(FlightRequest flightRequest) {
        final var flightId = flightService.createFlights(flightRequest);

        return created(fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(flightId).toUri())
                .build();
    }

    @Override
    public void updateFlight(UUID id, FlightRequest flightRequest) {
        flightService.updateFlight(id, flightRequest);
    }

    @Override
    public void deleteFlight(UUID id) {
        flightService.deleteFlight(id);
    }
}
