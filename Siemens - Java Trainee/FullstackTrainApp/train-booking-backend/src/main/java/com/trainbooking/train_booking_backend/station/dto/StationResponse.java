package com.trainbooking.train_booking_backend.station.dto;

public class StationResponse {

    private Long id;
    private String code;
    private String name;
    private String city;
    private boolean active;

    public StationResponse() {
    }

    public StationResponse(Long id, String code, String name, String city, boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.city = city;
        this.active = active;
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

    public String getCity() {
        return city;
    }

    public boolean isActive() {
        return active;
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

    public void setCity(String city) {
        this.city = city;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}