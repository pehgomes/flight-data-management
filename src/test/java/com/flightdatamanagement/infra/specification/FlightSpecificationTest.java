package com.flightdatamanagement.infra.specification;

import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.domain.model.Flight;
import com.flightdatamanagement.infra.repository.specification.FlightSpecification;
import jakarta.persistence.criteria.*;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightSpecificationTest {

    @Mock
    private Root<Flight> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder builder;
    @Mock
    private Path<Object> path;
    @Mock
    private Expression expression;
    @Mock
    private Predicate predicate;

    @Test
    void shouldFilterByAirline() {

        final var filter = Instancio.create(FlightFilter.class);

        when(root.get(anyString())).thenReturn(path);
        when(builder.lower(any())).thenReturn(expression);
        when(builder.like(any(), anyString())).thenReturn(predicate);

        Specification<Flight> spec = FlightSpecification.hasAirline(filter.getAirline());
        Predicate result = spec.toPredicate(root, query, builder);

        assertNotNull(result);
    }

    @Test
    void shouldFilterBySupplier() {

        final var filter = Instancio.create(FlightFilter.class);

        when(root.get(anyString())).thenReturn(path);
        when(builder.lower(any())).thenReturn(expression);
        when(builder.like(any(), anyString())).thenReturn(predicate);

        Specification<Flight> spec = FlightSpecification.hasSupplier(filter.getSupplier());
        Predicate result = spec.toPredicate(root, query, builder);

        assertNotNull(result);
    }

    @Test
    void shouldFilterByFare() {

        final var filter = Instancio.create(FlightFilter.class);

        when(root.get(anyString())).thenReturn(path);
        when(builder.equal(any(), any(BigDecimal.class))).thenReturn(predicate);

        Specification<Flight> spec = FlightSpecification.hasFare(filter.getFare());
        Predicate result = spec.toPredicate(root, query, builder);

        assertNotNull(result);
    }

    @Test
    void shouldFilterBDepartureAirport() {

        final var filter = Instancio.create(FlightFilter.class);

        when(root.get(anyString())).thenReturn(path);
        when(builder.lower(any())).thenReturn(expression);
        when(builder.like(any(), anyString())).thenReturn(predicate);

        Specification<Flight> spec = FlightSpecification.hasDepartureAirport(filter.getDepartureAirport());
        Predicate result = spec.toPredicate(root, query, builder);

        assertNotNull(result);
    }

    @Test
    void shouldFilterDestinationAirport() {

        final var filter = Instancio.create(FlightFilter.class);

        when(root.get(anyString())).thenReturn(path);
        when(builder.lower(any())).thenReturn(expression);
        when(builder.like(any(), anyString())).thenReturn(predicate);

        Specification<Flight> spec = FlightSpecification.hasDestinationAirport(filter.getDestinationAirport());
        Predicate result = spec.toPredicate(root, query, builder);

        assertNotNull(result);
    }

    @Test
    void shouldFilterDepartureTime() {

        final var filter = Instancio.create(FlightFilter.class);

        when(root.get(anyString())).thenReturn(path);
        when(builder.equal(any(), any(OffsetDateTime.class))).thenReturn(predicate);

        Specification<Flight> spec = FlightSpecification.hasDepartureTime(filter.getDepartureTime());
        Predicate result = spec.toPredicate(root, query, builder);

        assertNotNull(result);
    }

    @Test
    void shouldFilterArrivalTime() {

        final var filter = Instancio.create(FlightFilter.class);

        when(root.get(anyString())).thenReturn(path);
        when(builder.equal(any(), any(OffsetDateTime.class))).thenReturn(predicate);

        Specification<Flight> spec = FlightSpecification.hasArrivalTime(filter.getArrivalTime());
        Predicate result = spec.toPredicate(root, query, builder);

        assertNotNull(result);
    }
}
