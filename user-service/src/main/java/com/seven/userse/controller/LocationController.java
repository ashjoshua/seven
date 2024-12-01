package com.seven.userservice.controller;

import com.seven.userservice.service.GeolocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final GeolocationService geolocationService;

    public LocationController(GeolocationService geolocationService) {
        this.geolocationService = geolocationService;
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Boolean>> verifyLocation(@RequestBody Map<String, Double> coordinates) {
        double latitude = coordinates.get("latitude");
        double longitude = coordinates.get("longitude");

        String country = geolocationService.getCountryFromCoordinates(latitude, longitude);
        boolean isAllowed = geolocationService.isCountryAllowed(country);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isAllowed", isAllowed);

        return ResponseEntity.ok(response);
    }
}
