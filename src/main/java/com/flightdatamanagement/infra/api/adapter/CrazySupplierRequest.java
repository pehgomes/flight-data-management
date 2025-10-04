package com.flightdatamanagement.infra.api.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CrazySupplierRequest {
    private String from;
    private String to;
    private String outboundDate;
    private String inboundDate;
}
