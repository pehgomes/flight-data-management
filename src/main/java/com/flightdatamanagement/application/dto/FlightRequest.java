package com.flightdatamanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
public class FlightRequest {

    @NotBlank(message = "Airline is required")
    private String airline;

    @NotBlank(message = "Supplier is required")
    private String supplier;

    @NotNull(message = "Fare is required")
    @Positive(message = "Fare must be positive")
    private BigDecimal fare;

    @NotBlank(message = "Departure airport is required")
    @Size(min = 3, max = 3, message = "Departure airport must be a 3-letter code")
    private String departureAirport;

    @NotBlank(message = "Destination airport is required")
    @Size(min = 3, max = 3, message = "Destination airport must be a 3-letter code")
    private String destinationAirport;

    @NotNull(message = "Departure time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    private OffsetDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    private OffsetDateTime arrivalTime;
}
