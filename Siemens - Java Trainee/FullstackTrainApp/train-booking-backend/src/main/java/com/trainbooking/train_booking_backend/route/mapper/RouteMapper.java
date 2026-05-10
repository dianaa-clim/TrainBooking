package com.trainbooking.train_booking_backend.route.mapper;

import com.trainbooking.train_booking_backend.route.Route;
import com.trainbooking.train_booking_backend.route.RouteStop;
import com.trainbooking.train_booking_backend.route.dto.RouteRequest;
import com.trainbooking.train_booking_backend.route.dto.RouteResponse;
import com.trainbooking.train_booking_backend.route.dto.RouteStopResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteMapper {

    public Route toEntity(RouteRequest request) {
        Route route = new Route();
        route.setCode(request.getCode().trim().toUpperCase());
        route.setName(request.getName().trim());
        route.setActive(true);
        return route;
    }

    public RouteResponse toResponse(Route route) {
        List<RouteStopResponse> stops = route.getStops()
                .stream()
                .map(this::toStopResponse)
                .toList();

        return new RouteResponse(
                route.getId(),
                route.getCode(),
                route.getName(),
                route.isActive(),
                stops
        );
    }

    public RouteStopResponse toStopResponse(RouteStop stop) {
        return new RouteStopResponse(
                stop.getId(),
                stop.getStation().getId(),
                stop.getStation().getCode(),
                stop.getStation().getName(),
                stop.getStation().getCity(),
                stop.getStopOrder(),
                stop.getArrivalTime(),
                stop.getDepartureTime(),
                stop.isActive()
        );
    }

    public void updateEntity(Route route, RouteRequest request) {
        route.setCode(request.getCode().trim().toUpperCase());
        route.setName(request.getName().trim());
    }
}