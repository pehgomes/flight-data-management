package com.flightdatamanagement.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@FieldNameConstants
@Entity
@Table(name = "tb_flight")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Flight {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String airline;

    @Column(nullable = false)
    private String supplier;

    @Positive(message = "Fare must be positive")
    @Column(nullable = false)
    private BigDecimal fare;

    @Column(length = 3, nullable = false)
    private String departureAirport;

    @Column(length = 3, nullable = false)
    private String destinationAirport;

    @Column(nullable = false)
    private OffsetDateTime departureTime;

    @Column(nullable = false)
    private OffsetDateTime arrivalTime;

}
