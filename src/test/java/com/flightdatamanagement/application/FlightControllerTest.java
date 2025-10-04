package com.flightdatamanagement.application;

import com.flightdatamanagement.application.dto.FlightRequest;
import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.application.service.FlightService;
import com.flightdatamanagement.application.web.FlightController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @MockitoBean
    private FlightService flightService;

    @Autowired
    private MockMvc mockMvc;

    public static final String URI = ("/api/v1/flights");

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new FlightController(flightService)).build();
    }

    @Test
    void shouldGetFlightsWithFilter() throws Exception {

        Page<FlightResponse> mockPage = new PageImpl<>(List.of(
                new FlightResponse(UUID.randomUUID(), "LATAM", "Amadeus",
                        BigDecimal.valueOf(1000), "GRU", "JFK",
                        OffsetDateTime.parse("2025-10-03T14:30:00Z"),
                        OffsetDateTime.parse("2025-10-03T22:45:00Z"))
        ), PageRequest.of(0, 10), 1);

        when(flightService.getFlights(any(FlightFilter.class))).thenReturn(mockPage);

        mockMvc.perform(get(URI)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].airline").value("LATAM"))
                .andExpect(jsonPath("$.content[0].fare").value(1000));

        verify(flightService).getFlights(any(FlightFilter.class));
    }

    @Test
    void shouldGetFlightById() throws Exception {

        final var id = UUID.randomUUID();
        final var flight =
                new FlightResponse(UUID.randomUUID(), "LATAM", "Amadeus",
                        BigDecimal.valueOf(1000), "GRU", "JFK",
                        OffsetDateTime.parse("2025-10-03T14:30:00Z"),
                        OffsetDateTime.parse("2025-10-03T22:45:00Z"));

        when(flightService.getById(any(UUID.class))).thenReturn(flight);

        mockMvc.perform(get(URI.concat(String.format("/%s", id))))
                .andExpect(status().isOk());

        verify(flightService).getById(any(UUID.class));
    }

    @Test
    void shouldReturnCreatedWhenCreateAFlight() throws Exception {
        when(flightService.createFlights(any())).thenReturn(mock(UUID.class));

        mockMvc
                .perform(post(URI).contentType(APPLICATION_JSON).content(getValidFlightRequest()))
                .andExpect(status().isCreated());

        verify(flightService).createFlights(any(FlightRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        mockMvc
                .perform(post(URI).contentType(APPLICATION_JSON).content(getInvalidFLightRequest()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkWhenUpdateFlight() throws Exception {
        final var id = UUID.randomUUID();
        mockMvc
                .perform(patch(URI.concat(String.format("/%s", id))).contentType(APPLICATION_JSON).content(getValidFlightRequest()))
                .andExpect(status().isOk());

        verify(flightService).updateFlight(any(UUID.class), any(FlightRequest.class));
    }

    @Test
    void shouldReturnOkWhenDeleteFlight() throws Exception {
        final var id = UUID.randomUUID();
        mockMvc
                .perform(delete(URI.concat(String.format("/%s", id))).contentType(APPLICATION_JSON).content(getValidFlightRequest()))
                .andExpect(status().isOk());

        verify(flightService).deleteFlight(any(UUID.class));
    }

    String getValidFlightRequest() {
        return """
                {
                    "airline": "LATAM",
                    "supplier": "Amadeus",
                    "fare": 1250.75,
                    "departureAirport": "GRU",
                    "destinationAirport": "JFK",
                    "departureTime": "2025-10-03T14:30:00Z",
                    "arrivalTime": "2025-10-03T22:45:00Z"
                }
                """;
    }

    String getInvalidFLightRequest() {
        return """
                {
                    "airline": "",
                    "supplier": "Amadeus",
                    "fare": -100,
                    "departureAirport": "GR",
                    "destinationAirport": "JFK",
                    "departureTime": "2025-10-03T14:30:00Z",
                    "arrivalTime": "2025-10-03T22:45:00Z"
                }
                """;
    }


}
