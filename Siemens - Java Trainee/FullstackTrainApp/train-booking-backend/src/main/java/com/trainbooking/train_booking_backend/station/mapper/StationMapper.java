package com.trainbooking.train_booking_backend.station.mapper;

import com.trainbooking.train_booking_backend.station.Station;
import com.trainbooking.train_booking_backend.station.dto.StationRequest;
import com.trainbooking.train_booking_backend.station.dto.StationResponse;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public Station toEntity(StationRequest request) {
        Station station = new Station();
        station.setCode(request.getCode().trim().toUpperCase());
        station.setName(request.getName().trim());
        station.setCity(request.getCity().trim());
        station.setActive(true);
        return station;
    }

    public StationResponse toResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getCode(),
                station.getName(),
                station.getCity(),
                station.isActive()
        );
    }

    public void updateEntity(Station station, StationRequest request) {
        station.setCode(request.getCode().trim().toUpperCase());
        station.setName(request.getName().trim());
        station.setCity(request.getCity().trim());
    }
}