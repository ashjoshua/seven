package com.seven.userse.service;

public interface OtpService {
    String generateOtp(String phoneNumber);
    boolean validateOtp(String phoneNumber, String otp);
}
