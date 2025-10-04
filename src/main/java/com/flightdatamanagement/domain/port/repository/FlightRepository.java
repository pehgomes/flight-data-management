package com.flightdatamanagement.domain.port.repository;

import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.domain.model.Flight;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface FlightRepository {
    Page<Flight> getFlights(FlightFilter filter);

    Optional<Flight> getById(UUID id);

    Flight createFlights(Flight flight);

    void updateFlight(Flight flight);

    void deleteFlight(Flight id);
}
