package com.seven.userse.service.impl;

import com.seven.userse.model.User;
import com.seven.userse.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUserProfile(User user) {
        String key = "user:" + user.getId();
        redisTemplate.opsForValue().set(key, user);
        // feedback whats stored in redis chase ke us suer id and vaoue is what...
    }

    @Override
    public void saveLocation(Long userId, String location) {
        String key = "location:" + userId;
        redisTemplate.opsForValue().set(key, location);
    }
}
