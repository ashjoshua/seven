package com.seven.userse.service.impl;

import com.seven.userse.service.RedisService;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveUserProfile(Long userId, Object userProfile) {
        String key = "user:" + userId;
        redisTemplate.opsForValue().set(key, userProfile);
    }

    @Override
    public void mapS2CellToUser(String s2Cell, Long userId) {
        String key = "s2:" + s2Cell;
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
    public void saveUserContact(String key, Map<String, String> contactInfo, long ttl) {
        redisTemplate.opsForHash().putAll(key, contactInfo);
        redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
    }
}
