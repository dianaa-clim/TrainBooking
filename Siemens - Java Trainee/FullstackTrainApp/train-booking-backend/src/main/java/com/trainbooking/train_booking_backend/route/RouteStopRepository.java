package com.trainbooking.train_booking_backend.route;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    List<RouteStop> findByRouteIdOrderByStopOrderAsc(Long routeId);

    List<RouteStop> findByRouteIdAndActiveTrueOrderByStopOrderAsc(Long routeId);

    Optional<RouteStop> findByRouteIdAndStopOrder(Long routeId, int stopOrder);

    Optional<RouteStop> findByRouteIdAndStationId(Long routeId, Long stationId);

    boolean existsByRouteIdAndStationId(Long routeId, Long stationId);

    boolean existsByRouteIdAndStopOrder(Long routeId, int stopOrder);
}