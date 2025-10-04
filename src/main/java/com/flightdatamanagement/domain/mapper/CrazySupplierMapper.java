package com.flightdatamanagement.domain.mapper;

import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.infra.api.adapter.CrazySupplierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Mapper
public interface CrazySupplierMapper {

    @Mapping(source = "carrier", target = "airline")
    @Mapping(source = "crazySupplyResponse", target = "fare", qualifiedByName = "toCalculatedFare")
    @Mapping(constant = "Crazy Supplier", target = "supplier")
    @Mapping(source = "outboundDateTime", target = "departureTime", qualifiedByName = "toCetZone")
    @Mapping(source = "inboundDateTime", target = "arrivalTime", qualifiedByName = "toCetZone")
    @Mapping(source = "departureAirportName", target = "departureAirport")
    @Mapping(source = "arrivalAirportName", target = "destinationAirport")
    FlightResponse toFlightResponsePattern(CrazySupplierResponse crazySupplyResponse);


    @Named("toCetZone")
    default OffsetDateTime toCetZone(OffsetDateTime dateTime) {
        return Optional.ofNullable(dateTime).map(time -> time.withOffsetSameInstant(ZoneOffset.UTC))
                .orElse(null);
    }

    @Named("toCalculatedFare")
    default BigDecimal toCalculatedFare(CrazySupplierResponse crazySupplierResponse) {
        return Optional.ofNullable(crazySupplierResponse.getBasePrice())
                .map(price -> price.add(Optional.ofNullable(crazySupplierResponse.getTax()).orElse(BigDecimal.ZERO)))
                .orElse(BigDecimal.ZERO);
    }
}
