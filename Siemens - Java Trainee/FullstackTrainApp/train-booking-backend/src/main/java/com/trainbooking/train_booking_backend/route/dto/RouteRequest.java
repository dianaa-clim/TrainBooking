package com.trainbooking.train_booking_backend.route.dto;

import jakarta.validation.constraints.NotBlank;

public class RouteRequest {

    @NotBlank(message = "Route code is required")
    private String code;

    @NotBlank(message = "Route name is required")
    private String name;

    public RouteRequest() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}