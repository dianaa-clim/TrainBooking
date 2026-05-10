package com.trainbooking.train_booking_backend.auth.dto;

public class AuthResponse {

    private String token;
    private CurrentUserResponse user;

    public AuthResponse() {
    }

    public AuthResponse(String token, CurrentUserResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public CurrentUserResponse getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(CurrentUserResponse user) {
        this.user = user;
    }
}