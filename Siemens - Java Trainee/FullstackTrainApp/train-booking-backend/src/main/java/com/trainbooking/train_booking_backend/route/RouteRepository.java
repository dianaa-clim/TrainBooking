package com.trainbooking.train_booking_backend.route;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    Optional<Route> findByCode(String code);

    boolean existsByCode(String code);

    List<Route> findByActiveTrue();
}