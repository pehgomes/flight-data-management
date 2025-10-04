package com.flightdatamanagement.infra.repository;

import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.domain.model.Flight;
import com.flightdatamanagement.infra.repository.adapter.FlightJpaRepository;
import com.flightdatamanagement.infra.repository.adapter.FlightRepositoryAdapter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightRepositoryTest {

    @Mock
    private FlightJpaRepository repository;

    @InjectMocks
    private FlightRepositoryAdapter adapter;

    @Test
    void shouldGetFLight() {
        final var filter = Instancio.create(FlightFilter.class);
        adapter.getFlights(filter);
        verify(repository).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void shouldGetFLightById() {
        final var id = UUID.randomUUID();
        adapter.getById(id);
        verify(repository).findById(eq(id));
    }

    @Test
    void shouldCreateFLight() {
        final var flight = Instancio.create(Flight.class);
        adapter.createFlights(flight);
        verify(repository).save(eq(flight));
    }

    @Test
    void shouldUpdateFLight() {
        final var flight = Instancio.create(Flight.class);
        adapter.updateFlight(flight);
        verify(repository).saveAndFlush(eq(flight));
    }

    @Test
    void shouldDeleteFLight() {
        final var flight = Instancio.create(Flight.class);
        adapter.deleteFlight(flight);
        verify(repository).delete(eq(flight));
    }
}
