package com.seven.userse.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seven.userse.request.OtpRequest;
import com.seven.userse.request.OtpValidationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private final RedisService redisService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topic.otp.name}")
    private String otpTopic;

    @Autowired
    public OtpServiceImpl(RedisService redisService, KafkaTemplate<String, String> kafkaTemplate) {
        this.redisService = redisService;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void generateAndSendOtp(OtpRequest otpRequest) {
        // Generate OTP for phone and email
        String phoneOtp = generateNumericOtp();
        String emailOtp = generateNumericOtp();

        // Save the OTPs in Redis with expiration time of 60 seconds
        // Store OTPs in Redis with 5-minute TTL
        // Save the OTPs in Redis with a 5-minute TTL
        String redisKey = String.format("otp:%s:%s", otpRequest.getPhoneNumber(), otpRequest.getEmail());
        try {
            redisService.saveUserContact(redisKey, phoneOtp + ":" + emailOtp, TimeUnit.MINUTES.toSeconds(5));
            logger.info("Saved OTP to Redis with key: {}", redisKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save OTP to Redis", e);
        }


        // Publish OTP details to Kafka
        // Create the message key and value
        // Prepare message for Kafka
        try {
            String messageKey = otpRequest.getPhoneNumber();
            String messageValue = createMessageValue(otpRequest, phoneOtp, emailOtp);
            kafkaTemplate.send(otpTopic, messageKey, messageValue);
            logger.info("Published OTP message to Kafka with key: {}", messageKey);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize OTP message", e);
        }
    }
    // Added method for creating JSON message
    private String createMessageValue(OtpRequest otpRequest, String phoneOtp, String emailOtp) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> payload = new HashMap<>();
        payload.put("phone", otpRequest.getPhoneNumber());
        payload.put("email", otpRequest.getEmail());
        payload.put("phoneOtp", phoneOtp);
        payload.put("emailOtp", emailOtp);
        return objectMapper.writeValueAsString(payload);
    }

    private String generateNumericOtp() {
        // Generate a 6-digit numeric OTP
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
    @Override
    public boolean validateOtp(OtpValidationRequest otpValidationRequest) {
        String redisKey = String.format("otp:%s:%s", otpValidationRequest.getPhone(), otpValidationRequest.getEmail());
        String cachedOtp = redisService.getOtpFromRedis(redisKey);

        if (cachedOtp == null) {
            return false; // OTP expired or not found
        }

        String[] otpParts = cachedOtp.split(":");
        return otpValidationRequest.getPhoneOtp().equals(otpParts[0]) &&
                otpValidationRequest.getEmailOtp().equals(otpParts[1]);
    }




}
