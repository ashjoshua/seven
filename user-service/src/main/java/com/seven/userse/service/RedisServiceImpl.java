package com.seven.userse.service.impl;

import com.seven.userse.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveLocation(Long userId, String location) {
        String key = "location:" + userId;
        redisTemplate.opsForValue().set(key, location);
    }
}
