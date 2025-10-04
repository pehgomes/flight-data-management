package com.flightdatamanagement.infra.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiError {
    private int status;
    private List<String> errors;
}
