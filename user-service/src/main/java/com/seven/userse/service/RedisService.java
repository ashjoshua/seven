package com.seven.userse.service;

import com.seven.userse.model.User;

public interface RedisService {
    void saveUserProfile(User user);
    void saveLocation(Long userId, String location);
}
