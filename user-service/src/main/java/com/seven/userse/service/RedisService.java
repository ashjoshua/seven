package com.seven.userse.service;

import java.util.Map;

public interface RedisService {
    void saveUserContact(String key, Map<String, String> contactInfo, long ttl);
}
