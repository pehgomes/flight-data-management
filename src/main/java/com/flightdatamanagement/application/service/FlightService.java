package com.flightdatamanagement.application.service;

import com.flightdatamanagement.application.dto.FlightRequest;
import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface FlightService {

    Page<FlightResponse> getFlights(FlightFilter filter);

    FlightResponse getById(UUID id);

    UUID createFlights(FlightRequest flightRequest);

    void updateFlight(UUID id, FlightRequest flightRequest);

    void deleteFlight(UUID id);
}
