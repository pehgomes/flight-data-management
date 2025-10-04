package com.flightdatamanagement.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flightdatamanagement.domain.mapper.CrazySupplierMapper;
import com.flightdatamanagement.infra.api.adapter.CrazySupplierResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CrazySupplierMapperTest {

    private final CrazySupplierMapper crazySupplierMapper = Mappers.getMapper(CrazySupplierMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldMappingCrazySupplierResponseToFlightResponse() throws JsonProcessingException {
        final var crazySupplierResponse = objectMapper
                .readValue(getCrazySupplierResponse(), CrazySupplierResponse.class);

        final var flightResponse = crazySupplierMapper.toFlightResponsePattern(crazySupplierResponse);
        final var calculatedFare = crazySupplierMapper.toCalculatedFare(crazySupplierResponse);
        final var arrivalTime = crazySupplierMapper.toCetZone(crazySupplierResponse.getInboundDateTime());
        final var departureTime = crazySupplierMapper.toCetZone(crazySupplierResponse.getOutboundDateTime());

        assertThat(flightResponse).isNotNull();
        assertThat(flightResponse.airline()).isEqualTo(crazySupplierResponse.getCarrier());
        assertThat(flightResponse.fare()).isEqualTo(calculatedFare);
        assertThat(flightResponse.departureAirport()).isEqualTo(crazySupplierResponse.getDepartureAirportName());
        assertThat(flightResponse.destinationAirport()).isEqualTo(crazySupplierResponse.getArrivalAirportName());
        assertThat(flightResponse.arrivalTime()).isEqualTo(arrivalTime);
        assertThat(flightResponse.departureTime()).isEqualTo(departureTime);
    }

    String getCrazySupplierResponse() {
        return """
                {
                        "carrier": "Lufthansa",
                        "basePrice": 200.00,
                        "tax": 50.00,
                        "departureAirportName": "FRA",
                        "arrivalAirportName": "LHR",
                        "outboundDateTime": "2025-10-10T10:00:00+02:00",
                        "inboundDateTime": "2025-10-15T18:00:00+02:00"
                }
                """;
    }
}
