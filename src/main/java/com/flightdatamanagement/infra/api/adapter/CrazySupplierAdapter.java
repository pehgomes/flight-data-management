package com.flightdatamanagement.infra.api.adapter;

import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.domain.mapper.CrazySupplierMapper;
import com.flightdatamanagement.domain.port.api.CrazySupplierApiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrazySupplierAdapter implements CrazySupplierApiPort {

    private final WebClient.Builder webClientBuilder;
    private final CrazySupplierMapper crazySupplierMapper = Mappers.getMapper(CrazySupplierMapper.class);

    @Override
    public List<FlightResponse> getFlights(FlightFilter filter) {

        WebClient webClient = webClientBuilder.baseUrl("http://localhost:8089").build();

        final var from = filter.getDepartureAirport();
        final var to = filter.getDestinationAirport();
        final var outboundDate = Optional.ofNullable(filter.getDepartureTime()).map(OffsetDateTime::toLocalDateTime).orElse(LocalDateTime.now());
        final var inboundDate = Optional.ofNullable(filter.getArrivalTime()).map(OffsetDateTime::toLocalDateTime).orElse(LocalDateTime.now());

        var flightsResponse = webClient.post()
                .uri("/flights")
                .bodyValue(
                        new CrazySupplierRequest(from, to, outboundDate.toString(), inboundDate.toString())
                )
                .retrieve()
                .bodyToFlux(CrazySupplierResponse.class)
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    log.error(" CrazySupplier returned error: {}", ex.getStatusCode());
                    return Mono.empty();
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("CrazySupplier unavailable: {}", ex.getMessage());
                    return Mono.empty();
                })
                .collectList()
                .blockOptional()
                .orElse(Collections.emptyList())
                .stream()
                .map(crazySupplierMapper::toFlightResponsePattern)
                .toList();

        return filterExternalFlights(flightsResponse, filter);
    }

    private List<FlightResponse> filterExternalFlights(List<FlightResponse> flights, FlightFilter filter) {
        if (flights == null || flights.isEmpty()) {
            return Collections.emptyList();
        }

        List<Predicate<FlightResponse>> predicates = new ArrayList<>();

        Optional.ofNullable(filter.getAirline())
                .ifPresent(a -> predicates.add(f -> f.airline() != null
                        && f.airline().toLowerCase().contains(a.toLowerCase())));

        Optional.ofNullable(filter.getDepartureAirport())
                .ifPresent(d -> predicates.add(f -> f.departureAirport() != null
                        && f.departureAirport().toLowerCase().contains(d.toLowerCase())));

        Optional.ofNullable(filter.getDestinationAirport())
                .ifPresent(d -> predicates.add(f -> f.destinationAirport() != null
                        && f.destinationAirport().toLowerCase().contains(d.toLowerCase())));

        Optional.ofNullable(filter.getDepartureTime())
                .ifPresent(d -> predicates.add(f -> !f.departureTime().isBefore(d)));

        Optional.ofNullable(filter.getArrivalTime())
                .ifPresent(a -> predicates.add(f -> !f.arrivalTime().isAfter(a)));

        Predicate<FlightResponse> combinedPredicate = predicates.stream()
                .reduce(f -> true, Predicate::and);

        return flights.stream()
                .filter(combinedPredicate)
                .toList();
    }

}
