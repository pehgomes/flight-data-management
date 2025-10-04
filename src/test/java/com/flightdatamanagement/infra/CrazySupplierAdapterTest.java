package com.flightdatamanagement.infra;

import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import com.flightdatamanagement.infra.api.adapter.CrazySupplierAdapter;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CrazySupplierAdapterTest {

    private static WireMockServer wireMockServer;
    private CrazySupplierAdapter adapter;


    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setUp() {
        WebClient.Builder builder = WebClient.builder();
        adapter = new CrazySupplierAdapter(builder);
    }

    @Test
    void shouldCallExternalApiAndMapResponse() {

        wireMockServer.stubFor(post(urlEqualTo("/flights"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(getCrazyResponseJson())
                        .withFixedDelay(200)));

        FlightFilter filter = new FlightFilter();
        filter.setDepartureAirport("FRA");
        filter.setDestinationAirport("LHR");

        List<FlightResponse> flights = adapter.getFlights(filter);

        assertThat(flights).hasSize(1);
        assertThat(flights.get(0).airline()).isEqualTo("Lufthansa");
        assertThat(flights.get(0).departureAirport()).isEqualTo("FRA");

        wireMockServer.verify(postRequestedFor(urlEqualTo("/flights")));
    }

    @Test
    void shouldReturnEmptyListWhenServerIsDown() {
        WebClient.Builder builder = WebClient.builder();
        CrazySupplierAdapter adapter = new CrazySupplierAdapter(builder);

        FlightFilter filter = new FlightFilter();
        filter.setDepartureAirport("FRA");
        filter.setDestinationAirport("LHR");

        List<FlightResponse> result = adapter.getFlights(filter);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenTimeout() {

        wireMockServer.stubFor(post(urlEqualTo("/flights"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(getCrazyResponseJson())
                        .withFixedDelay(10000)));

        FlightFilter filter = new FlightFilter();
        filter.setDepartureAirport("FRA");
        filter.setDestinationAirport("LHR");

        List<FlightResponse> flights = adapter.getFlights(filter);

        assertThat(flights).isEmpty();

        wireMockServer.verify(postRequestedFor(urlEqualTo("/flights")));
    }

    private static String getCrazyResponseJson() {
        return """
                    [
                      {
                        "carrier": "Lufthansa",
                        "basePrice": 200.00,
                        "tax": 50.00,
                        "departureAirportName": "FRA",
                        "arrivalAirportName": "LHR",
                        "outboundDateTime": "2025-10-10T10:00:00+02:00",
                        "inboundDateTime": "2025-10-15T18:00:00+02:00"
                      }
                    ]
                """;
    }
}
