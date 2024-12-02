package com.seven.userse    .controller;

import com.seven.userservice.service.GeolocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeolocationService geolocationService;

    @Test
    public void testVerifyLocationAllowed() throws Exception {
        double latitude = 12.9716;
        double longitude = 77.5946;

        // Mocking GeolocationService behavior
        when(geolocationService.getCountryFromCoordinates(latitude, longitude)).thenReturn("IN");
        when(geolocationService.isCountryAllowed("IN")).thenReturn(true);

        mockMvc.perform(post("/api/location/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\": 12.9716, \"longitude\": 77.5946}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAllowed").value(true));
    }

    @Test
    public void testVerifyLocationNotAllowed() throws Exception {
        double latitude = 40.7128;
        double longitude = -74.0060;

        // Mocking GeolocationService behavior
        when(geolocationService.getCountryFromCoordinates(latitude, longitude)).thenReturn("US");
        when(geolocationService.isCountryAllowed("US")).thenReturn(false);

        mockMvc.perform(post("/api/location/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\": 40.7128, \"longitude\": -74.0060}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAllowed").value(false));
    }

    @Test
    public void testVerifyLocationInvalidInput() throws Exception {
        mockMvc.perform(post("/api/location/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"latitude\": 999, \"longitude\": 999}"))
                .andExpect(status().isBadRequest());
    }
}
