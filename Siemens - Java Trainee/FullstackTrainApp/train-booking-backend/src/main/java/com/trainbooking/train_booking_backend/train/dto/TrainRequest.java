package com.trainbooking.train_booking_backend.train.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TrainRequest {

    @NotBlank(message = "Train code is required")
    private String code;

    @NotBlank(message = "Train name is required")
    private String name;

    @Min(value = 1, message = "Capacity must be greater than 0")
    private int capacity;

    public TrainRequest() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}