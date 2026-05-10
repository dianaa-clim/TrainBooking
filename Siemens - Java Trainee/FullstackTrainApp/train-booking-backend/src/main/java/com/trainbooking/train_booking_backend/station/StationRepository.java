package com.trainbooking.train_booking_backend.station;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByCode(String code);

    boolean existsByCode(String code);

    List<Station> findByActiveTrue();

    Optional<Station> findByCodeAndActiveTrue(String code);
}