package com.flightdatamanagement.application.dto.filter;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightFilter extends Filter {

    private String airline;

    private String supplier;

    private BigDecimal fare;

    private String departureAirport;

    private String destinationAirport;

    private OffsetDateTime departureTime;

    private OffsetDateTime arrivalTime;

    private FlightSort field = FlightSort.DEPARTURE_TIME;

    @AllArgsConstructor
    @Getter
    public enum FlightSort {
        AIRLINE("airline"),
        SUPPLIER("supplier"),
        FARE("fare"),
        DEPARTURE_AIRPORT("departureAirport"),
        DESTINATION_AIRPORT("destinationAirport"),
        DEPARTURE_TIME("departureTime"),
        ARRIVAL_TIME("arrivalTime");

        private final String description;
    }

    public PageRequest getPageable() {

        final var defaultSort = new Sort.Order(getDirection(), getField().getDescription());

        final var sorts =
                Optional.ofNullable(getField())
                        .map(field -> List.of(new Sort.Order(getDirection(), field.getDescription())))
                        .orElse(List.of(defaultSort));

        return PageRequest.of(getPage(), getSize(), Sort.by(sorts));
    }


}
