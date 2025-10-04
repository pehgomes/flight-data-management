package com.flightdatamanagement.infra.repository.specification;

import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.domain.model.Flight;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

public class FlightSpecification {

    public static Specification<Flight> filterAttributes(FlightFilter filter) {
        return hasAirline(filter.getAirline())
                .and(hasSupplier(filter.getSupplier()))
                .and(hasFare(filter.getFare()))
                .and(hasDepartureAirport(filter.getDepartureAirport()))
                .and(hasDestinationAirport(filter.getDestinationAirport()))
                .and(hasDepartureTime(filter.getDepartureTime()))
                .and(hasArrivalTime(filter.getArrivalTime()));
    }

    public static Specification<Flight> hasAirline(String airline) {
        return (root, query, cb) -> Optional.ofNullable(airline)
                .map(filter -> cb.like(cb.lower(root.get(Flight.Fields.airline)), airline.toLowerCase())).orElse(cb.conjunction());
    }

    public static Specification<Flight> hasSupplier(String supplier) {
        return (root, query, cb) -> Optional.ofNullable(supplier)
                .map(filter -> cb.like(cb.lower(root.get(Flight.Fields.supplier)), supplier.toLowerCase())).orElse(cb.conjunction());
    }

    public static Specification<Flight> hasFare(BigDecimal fare) {
        return (root, query, cb) -> Optional.ofNullable(fare)
                .map(filter -> cb.equal(root.get(Flight.Fields.fare), fare)).orElse(cb.conjunction());
    }

    public static Specification<Flight> hasDepartureAirport(String departureAirport) {
        return (root, query, cb) -> Optional.ofNullable(departureAirport)
                .map(filter -> cb.like(cb.lower(root.get(Flight.Fields.departureAirport)), departureAirport.toLowerCase()))
                .orElse(cb.conjunction());
    }

    public static Specification<Flight> hasDestinationAirport(String destinationAirport) {
        return (root, query, cb) -> Optional.ofNullable(destinationAirport)
                .map(filter -> cb.like(cb.lower(root.get(Flight.Fields.destinationAirport)), destinationAirport.toLowerCase()))
                .orElse(cb.conjunction());
    }

    public static Specification<Flight> hasDepartureTime(OffsetDateTime departureTime) {
        return (root, query, cb) -> Optional.ofNullable(departureTime)
                .map(filter -> cb.equal(root.get(Flight.Fields.departureTime), departureTime))
                .orElse(cb.conjunction());
    }

    public static Specification<Flight> hasArrivalTime(OffsetDateTime arrivalTime) {
        return (root, query, cb) -> Optional.ofNullable(arrivalTime)
                .map(filter -> cb.equal(root.get(Flight.Fields.arrivalTime), arrivalTime))
                .orElse(cb.conjunction());
    }
}
