package com.seven.userservice.service;

public interface GeolocationService {
    String getCountryFromCoordinates(double latitude, double longitude);

    boolean isCountryAllowed(String country);
}
