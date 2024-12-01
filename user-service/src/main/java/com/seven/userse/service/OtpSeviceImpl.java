package com.seven.userservice.service.impl;

import com.seven.userservice.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    private final RedisTemplate<String, String> redisTemplate;

    public OtpServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void generateOtp(String phoneNumber) {
        String otp = generateNumericOtp();
        String key = "otp:" + phoneNumber;
        redisTemplate.opsForValue().set(key, otp, 60, TimeUnit.SECONDS); // Valid for 60 seconds

        sendSms(phoneNumber, otp);
    }

    private String generateNumericOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    private void sendSms(String phoneNumber, String otp) {
        // Logic to send SMS (e.g., Twilio API)
    }

    @Override
    public boolean validateOtp(String phoneNumber, String enteredOtp) {
        String key = "otp:" + phoneNumber;
        String cachedOtp = redisTemplate.opsForValue().get(key);
        return enteredOtp.equals(cachedOtp);
    }
}
