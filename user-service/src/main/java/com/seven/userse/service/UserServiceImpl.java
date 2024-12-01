package com.seven.userse.service.impl;

import com.seven.userse.model.User;
import com.seven.userse.repository.UserRepository;
import com.seven.userse.request.UserRegistrationRequest;
import com.seven.userse.service.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final OtpService otpService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AuditService auditService;

    public UserServiceImpl(UserRepository userRepository, RedisService redisService, OtpService otpService,
                           KafkaTemplate<String, String> kafkaTemplate, AuditService auditService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.otpService = otpService;
        this.kafkaTemplate = kafkaTemplate;
        this.auditService = auditService;
    }

    @Transactional
    @Override
    public User registerUser(UserRegistrationRequest request) {
        otpService.validateOtp(request.getPhoneNumber(), request.getOtp());
        request.getPhotoUrls().forEach(photo -> kafkaTemplate.send("photo-validation", photo));

        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPaymentType(request.getPaymentType());
        user.setPhotoUrls(request.getPhotoUrls());

        User savedUser = userRepository.save(user);
        redisService.saveUserProfile(savedUser);
        auditService.logUserRegistration(savedUser, request.getLatitude() + "," + request.getLongitude());

        return savedUser;
    }
}
