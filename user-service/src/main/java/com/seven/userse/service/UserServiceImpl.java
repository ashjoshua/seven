package com.seven.userservice.service.impl;

import com.seven.userservice.model.User;
import com.seven.userservice.repository.UserRepository;
import com.seven.userservice.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final AuditService auditService;

    public UserServiceImpl(UserRepository userRepository, RedisService redisService, AuditService auditService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.auditService = auditService;
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        User savedUser = userRepository.save(user);
        redisService.saveUserProfile(savedUser.getId(), savedUser);
        auditService.logUserRegistration(savedUser.getId(), "User registered", "location-details");

        return savedUser;
    }
}
