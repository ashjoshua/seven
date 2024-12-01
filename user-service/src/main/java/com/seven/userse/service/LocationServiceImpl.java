package com.seven.userse.service.impl;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.seven.userse.service.LocationService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private final RedisTemplate<String, Object> redisTemplate;

    public LocationServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void storeUserLocation(Long userId, double latitude, double longitude) {
        S2LatLng latLng = S2LatLng.fromDegrees(latitude, longitude);
        S2CellId cellId = S2CellId.fromLatLng(latLng);
        String key = "s2:" + cellId.id();
        redisTemplate.opsForList().rightPush(key, userId);
        // feedback if key ecists what you doing...should have code to use exisitng and epnd..whats tiem complesity here..if we append at end of list
    }
}
