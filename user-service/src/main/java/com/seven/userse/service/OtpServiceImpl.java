package com.seven.userse.service;

import com.seven.userse.request.OtpRequest;
import com.seven.userse.request.OtpValidationRequest;
import com.seven.userse.service.RedisService;
import com.seven.userse.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {

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
        String key = "otp:" + otpRequest.getPhoneNumber() + ":" + otpRequest.getEmail();
        redisService.saveUserContact(key, phoneOtp + ":" + emailOtp, TimeUnit.MINUTES.toSeconds(5));  // 5-minute TTL



        // Send OTP via SMS and Email
        // Push to Kafka topic
        // Publish OTP details to Kafka
        // Create the message key and value
        String messageKey = otpRequest.getPhoneNumber(); // Message Key
        String messageValue = String.format("{\"phone\":\"%s\",\"email\":\"%s\",\"phoneOtp\":\"%s\",\"emailOtp\":\"%s\"}",
                otpRequest.getPhoneNumber(), otpRequest.getEmail(), phoneOtp, emailOtp);

        // Publish to Kafka with key and value
        kafkaTemplate.send(otpTopic, messageKey, messageValue);
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
