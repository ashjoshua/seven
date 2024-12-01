package com.seven.userse.service;

import com.seven.userse.model.User;

public interface RedisService {
    void saveUser(User user);
}
