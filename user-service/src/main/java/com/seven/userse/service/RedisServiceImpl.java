package com.seven.userse.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUserContact(String key, Map<String, String> contactInfo, long ttl) {
        redisTemplate.opsForValue().set(key, contactInfo, ttl, TimeUnit.SECONDS);
    }
}
