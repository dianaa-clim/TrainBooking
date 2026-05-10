package com.trainbooking.train_booking_backend.run.mapper;

import com.trainbooking.train_booking_backend.run.TrainRun;
import com.trainbooking.train_booking_backend.run.TrainRunStop;
import com.trainbooking.train_booking_backend.run.dto.TrainRunResponse;
import com.trainbooking.train_booking_backend.run.dto.TrainRunStopResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainRunMapper {

    public TrainRunResponse toResponse(TrainRun trainRun) {
        List<TrainRunStopResponse> stops = trainRun.getStops()
                .stream()
                .map(this::toStopResponse)
                .toList();

        return new TrainRunResponse(
                trainRun.getId(),
                trainRun.getTrain().getId(),
                trainRun.getTrain().getCode(),
                trainRun.getTrain().getName(),
                trainRun.getTrain().getCapacity(),
                trainRun.getRoute().getId(),
                trainRun.getRoute().getCode(),
                trainRun.getRoute().getName(),
                trainRun.getRunDate(),
                trainRun.getStatus().name(),
                trainRun.isActive(),
                stops
        );
    }

    public TrainRunStopResponse toStopResponse(TrainRunStop stop) {
        return new TrainRunStopResponse(
                stop.getId(),
                stop.getStation().getId(),
                stop.getStation().getCode(),
                stop.getStation().getName(),
                stop.getStation().getCity(),
                stop.getStopOrder(),
                stop.getPlannedArrivalTime(),
                stop.getPlannedDepartureTime(),
                stop.getActualArrivalTime(),
                stop.getActualDepartureTime()
        );
    }
}