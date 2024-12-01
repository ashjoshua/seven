package com.seven.userse.service;

public interface LocationService {
    void storeUserLocation(Long userId, double latitude, double longitude);
}
