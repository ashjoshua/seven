package com.seven.userse.service;

public interface RedisService {
    void saveLocation(Long userId, String location);
}
