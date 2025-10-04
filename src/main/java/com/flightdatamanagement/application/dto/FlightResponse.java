package com.flightdatamanagement.application.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record FlightResponse(
        UUID id,
        String airline,
        String supplier,
        BigDecimal fare,
        String departureAirport,
        String destinationAirport,
        OffsetDateTime departureTime,
        OffsetDateTime arrivalTime
) {
}
