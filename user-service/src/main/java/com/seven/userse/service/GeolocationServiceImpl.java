package com.seven.userservice.service.impl;

import com.seven.userservice.service.GeolocationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeolocationServiceImpl implements GeolocationService {

    private final RestTemplate restTemplate;
    private final String geolocationApiUrl;
    private final List<String> allowedCountries;

    public GeolocationServiceImpl(RestTemplate restTemplate,
                                  @Value("${geolocation.api.url}") String geolocationApiUrl,
                                  @Value("${registration.allowed.countries}") String allowedCountries) {
        this.restTemplate = restTemplate;
        this.geolocationApiUrl = geolocationApiUrl;
        this.allowedCountries = Arrays.asList(allowedCountries.split(","));
    }

    @Override
    public String getCountryFromCoordinates(double latitude, double longitude) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("latitude", String.valueOf(latitude));
        uriVariables.put("longitude", String.valueOf(longitude));

        Map<String, Object> response = restTemplate.getForObject(geolocationApiUrl, Map.class, uriVariables);

        if (response != null && response.containsKey("countryName")) {
            return (String) response.get("countryName");
        }

        throw new IllegalArgumentException("Unable to determine country from coordinates.");
    }

    @Override
    public boolean isCountryAllowed(String country) {
        return allowedCountries.contains(country);
    }
}
