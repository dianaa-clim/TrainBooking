package com.trainbooking.train_booking_backend.train.mapper;

import com.trainbooking.train_booking_backend.train.Train;
import com.trainbooking.train_booking_backend.train.dto.TrainRequest;
import com.trainbooking.train_booking_backend.train.dto.TrainResponse;
import org.springframework.stereotype.Component;

@Component
public class TrainMapper {

    public Train toEntity(TrainRequest request) {
        Train train = new Train();
        train.setCode(request.getCode().trim().toUpperCase());
        train.setName(request.getName().trim());
        train.setCapacity(request.getCapacity());
        train.setActive(true);
        return train;
    }

    public TrainResponse toResponse(Train train) {
        return new TrainResponse(
                train.getId(),
                train.getCode(),
                train.getName(),
                train.getCapacity(),
                train.isActive()
        );
    }

    public void updateEntity(Train train, TrainRequest request) {
        train.setCode(request.getCode().trim().toUpperCase());
        train.setName(request.getName().trim());
        train.setCapacity(request.getCapacity());
    }
}