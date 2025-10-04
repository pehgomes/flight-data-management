package com.flightdatamanagement.domain.mapper;

import com.flightdatamanagement.application.dto.FlightRequest;
import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.domain.model.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        config = BaseMapper.class, componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FlightMapper {

    Flight requestToEntity(FlightRequest request);

    FlightResponse entityToResponse(Flight entity);

    void updateFlight(@MappingTarget Flight flight, FlightRequest flightRequest);
}
