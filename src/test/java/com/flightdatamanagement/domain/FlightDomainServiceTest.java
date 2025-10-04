package com.flightdatamanagement.domain;

import com.flightdatamanagement.application.dto.FlightRequest;
import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.domain.model.Flight;
import com.flightdatamanagement.domain.port.api.CrazySupplierApiPort;
import com.flightdatamanagement.domain.port.repository.FlightRepository;
import com.flightdatamanagement.infra.exception.NotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightDomainServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private CrazySupplierApiPort crazySupplierApiPort;

    @InjectMocks
    private FlightDomainService flightService;

    @Test
    void shouldCreateAFlight() {
        final var request = Instancio.create(FlightRequest.class);
        final var entity = Instancio.create(Flight.class);

        when(flightRepository.createFlights(any(Flight.class))).thenReturn(entity);

        flightService.createFlights(request);

        ArgumentCaptor<Flight> captor = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository).createFlights(captor.capture());
        assertThat(captor.getValue().getFare()).isEqualTo(request.getFare());
    }

    @Test
    void shouldGetFlightsData() {
        final var filter = Instancio.create(FlightFilter.class);
        final var entity = Instancio.create(Flight.class);
        final var mockPage = new PageImpl<>(List.of(entity), PageRequest.of(0, 10), 1);
        final var anotherFlight = Instancio.create(FlightResponse.class);

        when(flightRepository.getFlights(any(FlightFilter.class))).thenReturn(mockPage);
        when(crazySupplierApiPort.getFlights(any(FlightFilter.class))).thenReturn(List.of(anotherFlight));

        final var result = flightService.getFlights(filter);

        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldGetFlightById() {
        final var id = UUID.randomUUID();
        final var entity = Instancio.create(Flight.class);

        when(flightRepository.getById(any(UUID.class))).thenReturn(Optional.ofNullable(entity));

        final var result = flightService.getById(id);

        assertThat(result).isNotNull();
    }

    @Test
    void shouldUpdateFlightById() {
        final var id = UUID.randomUUID();
        final var request = Instancio.create(FlightRequest.class);
        final var entity = Instancio.create(Flight.class);

        doNothing().when(flightRepository).updateFlight(any(Flight.class));
        when(flightRepository.getById(any(UUID.class))).thenReturn(Optional.of(entity));

        flightService.updateFlight(id, request);
        ArgumentCaptor<Flight> captor = ArgumentCaptor.forClass(Flight.class);
        Mockito.verify(flightRepository).updateFlight(captor.capture());

        final var flight = captor.getValue();
        assertThat(flight.getFare()).isEqualTo(request.getFare());
    }

    @Test
    void shouldThrowNotFoundWhenUpdateFlightNotExistingById() {
        final var id = UUID.randomUUID();
        final var request = Instancio.create(FlightRequest.class);

        assertThatThrownBy(() -> flightService.updateFlight(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Flight not found");
    }

    @Test
    void shouldDeleteFlightById() {
        final var id = UUID.randomUUID();
        final var entity = Instancio.create(Flight.class);

        doNothing().when(flightRepository).deleteFlight(any(Flight.class));
        when(flightRepository.getById(any(UUID.class))).thenReturn(Optional.of(entity));

        flightService.deleteFlight(id);
        ArgumentCaptor<Flight> captor = ArgumentCaptor.forClass(Flight.class);
        Mockito.verify(flightRepository).deleteFlight(captor.capture());

        verify(flightRepository).deleteFlight(eq(entity));
    }

    @Test
    void shouldThrowExceptionWhenDeleteFlightNotExistingById() {
        final var id = UUID.randomUUID();

        assertThatThrownBy(() -> flightService.deleteFlight(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Flight not found");
    }

}
