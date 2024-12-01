package com.seven.userservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeolocationService {

    private static final String GEOLOCATION_API_URL = "https://api.bigdatacloud.net/data/reverse-geocode-client?"
            + "latitude={latitude}&longitude={longitude}&localityLanguage=en";

    private final RestTemplate restTemplate;

    public GeolocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCountryFromCoordinates(double latitude, double longitude) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("latitude", String.valueOf(latitude));
        uriVariables.put("longitude", String.valueOf(longitude));

        Map<String, Object> response = restTemplate.getForObject(GEOLOCATION_API_URL, Map.class, uriVariables);

        if (response != null && response.containsKey("countryName")) {
            return (String) response.get("countryName");
        }

        throw new IllegalArgumentException("Unable to determine country from coordinates.");
    }
}
