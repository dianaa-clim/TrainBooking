package com.trainbooking.train_booking_backend.delay.mapper;

import com.trainbooking.train_booking_backend.delay.TrainDelayEvent;
import com.trainbooking.train_booking_backend.delay.dto.DelayResponse;
import org.springframework.stereotype.Component;

@Component
public class DelayMapper {

    public DelayResponse toResponse(TrainDelayEvent delayEvent) {
        DelayResponse response = new DelayResponse();

        response.setId(delayEvent.getId());
        response.setTrainRunId(delayEvent.getTrainRun().getId());
        response.setTrainCode(delayEvent.getTrainRun().getTrain().getCode());
        response.setDelayMinutes(delayEvent.getDelayMinutes());
        response.setReason(delayEvent.getReason());
        response.setCreatedByEmail(delayEvent.getCreatedBy().getEmail());
        response.setCreatedAt(delayEvent.getCreatedAt());

        return response;
    }
}