package com.trainbooking.train_booking_backend.train.dto;

public class TrainResponse {

    private Long id;
    private String code;
    private String name;
    private int capacity;
    private boolean active;

    public TrainResponse() {
    }

    public TrainResponse(Long id, String code, String name, int capacity, boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.capacity = capacity;
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

    public int getCapacity() {
        return capacity;
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

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}