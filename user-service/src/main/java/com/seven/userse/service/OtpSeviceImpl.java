package com.seven.userse.service;

import com.seven.userse.request.OtpRequest;
import com.seven.userse.request.OtpValidationRequest;
import com.seven.userse.service.RedisService;
import com.seven.userse.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

    private final RedisService redisService;

    public OtpServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void generateAndSendOtp(OtpRequest otpRequest) {
        // Generate OTP for phone and email
        String phoneOtp = generateNumericOtp();
        String emailOtp = generateNumericOtp();

        // Save the OTPs in Redis with expiration time of 60 seconds
        String key = "otp:" + otpRequest.getPhoneNumber() + ":" + otpRequest.getEmail();
        redisService.saveUserContact(key, phoneOtp + ":" + emailOtp, TimeUnit.SECONDS.toSeconds(60));  // 60 seconds TTL

        // Send OTP via SMS and Email
        sendSms(otpRequest.getPhoneNumber(), phoneOtp);
        sendEmail(otpRequest.getEmail(), emailOtp);
    }

    private String generateNumericOtp() {
        // Generate a 6-digit numeric OTP
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    private void sendSms(String phoneNumber, String otp) {
        // Logic to send SMS (You can integrate Twilio or another SMS service)
        System.out.println("Sending SMS to " + phoneNumber + " with OTP: " + otp);
    }

    private void sendEmail(String email, String otp) {
        // Logic to send email (You can integrate Amazon SES or another email service)
        String subject = "Your OTP Code";
        String body = "Your OTP is: " + otp + ". It is valid for 60 seconds.";
        System.out.println("Sending email to " + email + " with OTP: " + otp);
    }

    /*
    @Override
    public boolean validateOtp(OtpValidationRequest otpValidationRequest) {
        // Retrieve OTP from Redis for phone and email
        String key = "otp:" + otpValidationRequest.getPhone() + ":" + otpValidationRequest.getEmail();
        String cachedOtp = redisService.getOtpFromRedis(key);

        if (cachedOtp == null) {
            return false; // OTP expired or not found
        }

        // Split the cached OTP into phone and email OTPs
        String[] otpParts = cachedOtp.split(":");
        String phoneOtp = otpParts[0];
        String emailOtp = otpParts[1];

        // Validate both phone and email OTPs
        return otpValidationRequest.getPhoneOtp().equals(phoneOtp) && otpValidationRequest.getEmailOtp().equals(emailOtp);
    }

     */
}
