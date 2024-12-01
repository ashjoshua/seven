package com.seven.userse.service.impl;

import com.seven.userse.model.User;
import com.seven.userse.service.RedisService;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Override
    public void saveUser(User user) {
        // Implement Redis save logic here
    }
}
