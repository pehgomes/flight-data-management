package com.flightdatamanagement.infra.api.adapter;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrazySupplierResponse {

    private String carrier;
    private BigDecimal basePrice;
    private BigDecimal tax;
    private String departureAirportName;
    private String arrivalAirportName;
    private OffsetDateTime outboundDateTime;
    private OffsetDateTime inboundDateTime;

}
