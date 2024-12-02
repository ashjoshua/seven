package com.seven.userse.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seven.userse.request.OtpValidationRequest;
import com.seven.userse.request.UserPersonalDetailsRequest;
import com.seven.userse.util.JwtUtils;
import com.seven.userse.util.RetryUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seven.userse.model.User;
import com.seven.userse.repository.UserRepository;
import com.seven.userse.request.*;
import com.seven.userse.service.*;
import com.seven.userse.request.UserPersonalDetailsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;

    private final OtpService otpService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final LocationService locationService;
    @Value("${kafka.topic.name:user-topic}")
    private String userTopic;

    public UserServiceImpl(KafkaTemplate<String, String> kafkaTemplate, UserRepository userRepository, RedisService redisService, OtpService otpService, LocationService locationService) {
        this.userRepository = userRepository;
        this.redisService = redisService;

        this.otpService = otpService;
        this.locationService = locationService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void generateAndSendOtp(OtpRequest request) {
       otpService.generateAndSendOtp(request);


    }
    @Override
    public void sendLoginOtp(String phoneNumber) {
        otpService.generateAndSendOtpForPhone(phoneNumber);
    }

    @Override
    public String validateOtpAndGenerateSession(OtpValidationRequest otpValidationRequest) {
        String redisKey = String.format("otp:%s:%s", otpValidationRequest.getPhone(), otpValidationRequest.getEmail());
        String cachedOtp = redisService.getOtpFromRedis(redisKey);

        if (cachedOtp == null) {
            return null; // OTP expired or not found
        }

        String[] otpParts = cachedOtp.split(":");
        if (!otpValidationRequest.getPhoneOtp().equals(otpParts[0]) ||
                !otpValidationRequest.getEmailOtp().equals(otpParts[1])) {
            return null; // Invalid OTP
        }

        // Generate session token and store in Redis
        String sessionToken = UUID.randomUUID().toString();
        String sessionKey = String.format("session:%s:%s", otpValidationRequest.getPhone(), otpValidationRequest.getEmail());
        redisService.saveUserContact(sessionKey, sessionToken, TimeUnit.MINUTES.toSeconds(15)); // 15-minute TTL

        return sessionToken;
    }

    @Override
    public boolean validateSessionToken(String phone, String email, String sessionToken) {
        String sessionKey = String.format("session:%s:%s", phone, email);
        String storedToken = redisService.getOtpFromRedis(sessionKey);
        return sessionToken.equals(storedToken);
    }

    @Override
    public String loginWithPhoneNumber(String phoneNumber, String otp) {
        String redisKey = String.format("otp:%s", phoneNumber);
        String cachedOtp = redisService.getOtpFromRedis(redisKey);

        if (cachedOtp == null || !cachedOtp.equals(otp)) {
            throw new IllegalArgumentException("Invalid or expired OTP.");
        }

        // Remove OTP from Redis (if required)
        redisService.delete(redisKey);

        // Generate JWT
        return JwtUtils.generateToken(phoneNumber);
    }





    @Override
    public void captureUserDetails(String sessionToken, UserPersonalDetailsRequest userDetailsRequest) {
        // Validate session token
        String sessionKey = String.format("session:%s:%s", userDetailsRequest.getPhoneNumber(), userDetailsRequest.getEmail());
        String storedToken = redisService.getOtpFromRedis(sessionKey);
        if (!sessionToken.equals(storedToken)) {
            throw new IllegalArgumentException("Invalid or expired session token.");
        }

        // Publish user details to Kafka
        publishUserDetailsToKafka(userDetailsRequest);

        // Remove session token after successful capture
//redisService.delete(sessionKey);
    }
    private void publishUserDetailsToKafka(UserPersonalDetailsRequest userDetailsRequest) {
        try {
            String messageKey = userDetailsRequest.getPhoneNumber() + ":" + userDetailsRequest.getEmail();
            String messageValue = createUserDetailsJson(userDetailsRequest);

            // Retry Kafka publishing with exponential backoff
            RetryUtils.executeWithRetry(() -> {
                kafkaTemplate.send(userTopic, messageKey, messageValue);
                return null;
            }, "kafka-publish-retry");

            System.out.println("Published user details to Kafka for user: " + userDetailsRequest.getPhoneNumber());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize user details message", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish user details to Kafka after retries", e);
        }
    }
    private String createUserDetailsJson(UserPersonalDetailsRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(request);
    }

}
