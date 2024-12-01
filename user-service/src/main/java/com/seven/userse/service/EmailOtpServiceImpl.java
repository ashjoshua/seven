package com.seven.userservice.service.impl;

import com.seven.userservice.service.EmailOtpService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailOtpServiceImpl implements EmailOtpService {

    private final RedisTemplate<String, String> redisTemplate;

    public EmailOtpServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void generateOtp(String email) {
        String otp = generateNumericOtp(); // Using numeric OTPs for simplicity
        String key = "otp:" + email;
        redisTemplate.opsForValue().set(key, otp, 60, TimeUnit.SECONDS); // Valid for 60 seconds

        sendEmail(email, otp);
    }

    private String generateNumericOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // 6-digit numeric OTP
    }

    private void sendEmail(String email, String otp) {
        String subject = "7 Registration OTP";
        String body = "Your OTP is: " + otp + ". It is valid for 60 seconds.";
        // Logic to send email (e.g., Amazon SES or SMTP)
    }

    @Override
    public boolean validateOtp(String email, String enteredOtp) {
        String key = "otp:" + email;
        String cachedOtp = redisTemplate.opsForValue().get(key);
        return enteredOtp.equals(cachedOtp);
    }
}
