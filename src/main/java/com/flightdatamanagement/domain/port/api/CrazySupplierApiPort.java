package com.flightdatamanagement.domain.port.api;

import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;

import java.util.List;

public interface CrazySupplierApiPort {

    List<FlightResponse> getFlights(FlightFilter filter);
}
