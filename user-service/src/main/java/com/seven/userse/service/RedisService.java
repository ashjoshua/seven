package com.seven.userse.service;

import com.seven.userse.model.User;

import java.util.Map;

public interface RedisService {
   // void saveUserProfile(User user);

    //void saveLocation(Long userId, String location);

    // New method to save user contact information
    void saveUserContact(String key, Map<String, String> contactInfo, long ttl);
}
