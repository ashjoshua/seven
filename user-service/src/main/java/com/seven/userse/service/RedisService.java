package com.seven.userse.service;

import java.util.Map;

public interface RedisService {
    void saveUserContact(String key, String value, long ttl);
    String getOtpFromRedis(String key);
    void delete(String key);
}
