package com.flightdatamanagement.application.web;


import com.flightdatamanagement.application.dto.FlightRequest;
import com.flightdatamanagement.application.dto.FlightResponse;
import com.flightdatamanagement.application.dto.filter.FlightFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Flights", description = "Operations over flights data")
@RequestMapping(value = "/api/v1/flights", produces = MediaType.APPLICATION_JSON_VALUE)
public interface FlightApi {

    @Operation(
            summary = "Get the flights data",
            description = "Returns a paginated list of flights matching the filter criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of flights",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FlightResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid filter parameters")
            }
    )
    @GetMapping
    Page<FlightResponse> getFlights(@ParameterObject FlightFilter filter);

    @Operation(
            summary = "Get a flight by ID",
            description = "Returns the flight data for the given flight ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Flight found",
                            content = @Content(mediaType = "com/flightdatamanagement/application/json",
                                    schema = @Schema(implementation = FlightRequest.class))),
                    @ApiResponse(responseCode = "404", description = "Flight not found")
            }
    )
    @GetMapping("/{id}")
    FlightResponse getById(
            @Parameter(description = "UUID of the flight to retrieve", required = true)
            @PathVariable UUID id);

    @Operation(
            summary = "Create a new flight",
            description = "Creates a new flight record in the system",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Flight created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid flight data"),
                    @ApiResponse(responseCode = "422", description = "Error while flight registration")
            }
    )
    @PostMapping
    ResponseEntity<Void> createFlights(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Flight data to create",
                    required = true)
            @RequestBody @Valid FlightRequest flightRequest);

    @Operation(
            summary = "Update an existing flight",
            description = "Updates the flight data for the specified flight ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Flight updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid flight data"),
                    @ApiResponse(responseCode = "404", description = "Flight not found")
            }
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateFlight(@Parameter(description = "UUID of the flight to update", required = true)
                      @PathVariable UUID id,
                      @RequestBody FlightRequest flightRequest);

    @Operation(
            summary = "Delete a flight",
            description = "Deletes the flight with the specified ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Flight deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Flight not found")
            }
    )
    @DeleteMapping("/{id}")
    void deleteFlight(@Parameter(description = "UUID of the flight to delete", required = true)
                      @PathVariable UUID id);

}
