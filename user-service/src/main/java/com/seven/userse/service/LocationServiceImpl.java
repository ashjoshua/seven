package com.seven.userse.service;

import com.seven.userse.service.LocationService;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private final RedisTemplate<String, List<Long>> redisTemplate;

    public LocationServiceImpl(RedisTemplate<String, List<Long>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void updateLocation(String s2Cell, Long userId) {
        String key = "location:" + s2Cell;
        List<Long> userIds = redisTemplate.opsForValue().get(key);

        if (userIds == null) {
            userIds = new ArrayList<>();
        }

        if (!userIds.contains(userId)) {
            userIds.add(userId);
            redisTemplate.opsForValue().set(key, userIds);
        }
    }

    @Override
    public void storeUserLocation(Long userId, double latitude, double longitude) {

    }
}
