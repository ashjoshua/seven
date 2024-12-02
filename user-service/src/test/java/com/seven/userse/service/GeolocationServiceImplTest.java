package com.seven.userse.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GeolocationServiceImplTest {

    @InjectMocks
    private GeolocationServiceImpl geolocationService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        geolocationService = new GeolocationServiceImpl(restTemplate, "http://mock-api/geo");
    }

    @Test
    public void testGetCountryFromCoordinatesValid() {
        double latitude = 12.9716;
        double longitude = 77.5946;

        // Mock API response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("countryName", "IN");
        when(restTemplate.getForObject(anyString(), eq(Map.class), anyMap())).thenReturn(mockResponse);

        String country = geolocationService.getCountryFromCoordinates(latitude, longitude);
        assertEquals("IN", country);
    }

    @Test
    public void testGetCountryFromCoordinatesInvalid() {
        double latitude = 999;
        double longitude = 999;

        // Mock API response
        when(restTemplate.getForObject(anyString(), eq(Map.class), anyMap())).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            geolocationService.getCountryFromCoordinates(latitude, longitude);
        });

        assertEquals("Unable to determine country from coordinates.", exception.getMessage());
    }

    @Test
    public void testIsCountryAllowedTrue() {
        geolocationService.init();
        assertTrue(geolocationService.isCountryAllowed("IN"));
    }

    @Test
    public void testIsCountryAllowedFalse() {
        geolocationService.init();
        assertFalse(geolocationService.isCountryAllowed("US"));
    }
}
