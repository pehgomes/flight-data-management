package com.flightdatamanagement.infra.repository.adapter;

import com.flightdatamanagement.domain.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlightJpaRepository extends JpaRepository<Flight, UUID>, JpaSpecificationExecutor<Flight> {
}
