package com.trainbooking.train_booking_backend.route.dto;

import java.util.ArrayList;
import java.util.List;

public class RouteResponse {

    private Long id;
    private String code;
    private String name;
    private boolean active;
    private List<RouteStopResponse> stops = new ArrayList<>();

    public RouteResponse() {
    }

    public RouteResponse(Long id, String code, String name, boolean active, List<RouteStopResponse> stops) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.active = active;
        this.stops = stops;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<RouteStopResponse> getStops() {
        return stops;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setStops(List<RouteStopResponse> stops) {
        this.stops = stops;
    }
}