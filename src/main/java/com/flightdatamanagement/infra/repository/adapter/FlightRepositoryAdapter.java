package com.flightdatamanagement.infra.repository.adapter;

import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.domain.model.Flight;
import com.flightdatamanagement.domain.port.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.flightdatamanagement.infra.repository.specification.FlightSpecification.filterAttributes;

@Component
@RequiredArgsConstructor
public class FlightRepositoryAdapter implements FlightRepository {

    private final FlightJpaRepository repository;

    @Override
    public Page<Flight> getFlights(FlightFilter filter) {
        return repository.findAll(filterAttributes(filter), filter.getPageable());
    }

    @Override
    public Optional<Flight> getById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Flight createFlights(Flight flight) {
        return repository.save(flight);
    }

    @Override
    public void updateFlight(Flight flight) {
        repository.saveAndFlush(flight);
    }

    @Override
    public void deleteFlight(Flight flight) {
        repository.delete(flight);
    }
}
