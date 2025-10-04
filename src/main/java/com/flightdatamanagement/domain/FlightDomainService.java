package com.flightdatamanagement.domain;

import com.flightdatamanagement.application.dto.FlightRequest;
import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.application.service.FlightService;
import com.flightdatamanagement.domain.mapper.FlightMapper;
import com.flightdatamanagement.domain.port.api.CrazySupplierApiPort;
import com.flightdatamanagement.domain.port.repository.FlightRepository;
import com.flightdatamanagement.infra.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class FlightDomainService implements FlightService {

    private final FlightRepository flightRepository;
    private final CrazySupplierApiPort crazySupplierApiPort;
    private final FlightMapper mapper = Mappers.getMapper(FlightMapper.class);

    @Override
    public Page<FlightResponse> getFlights(FlightFilter filter) {
        var localFlights = flightRepository.getFlights(filter).map(mapper::entityToResponse);

        var externalFlights = crazySupplierApiPort.getFlights(filter);

        final var combined = Stream.concat(
                localFlights.getContent().stream(),
                externalFlights.stream()
        ).toList();

        int page = localFlights.getNumber();
        int size = localFlights.getSize();
        int start = Math.min(page * size, combined.size());
        int end = Math.min(start + size, combined.size());
        final var pageContent = combined.subList(start, end);

        return new PageImpl<>(pageContent, localFlights.getPageable(), combined.size());
    }

    @Override
    public FlightResponse getById(UUID id) {
        return flightRepository.getById(id)
                .map(mapper::entityToResponse)
                .orElseThrow(() -> new NotFoundException("Flight not found"));
    }

    @Override
    public UUID createFlights(FlightRequest flightRequest) {
        final var flight = mapper.requestToEntity(flightRequest);

        return flightRepository.createFlights(flight).getId();
    }

    @Override
    public void updateFlight(UUID id, FlightRequest flightRequest) {
        final var flight = flightRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Flight not found"));

        mapper.updateFlight(flight, flightRequest);

        flightRepository.updateFlight(flight);
    }

    @Override
    public void deleteFlight(UUID id) {
        final var flight = flightRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Flight not found"));

        flightRepository.deleteFlight(flight);
    }
}
