package com.seven.userse.service;

public interface EmailOtpService {
    String generateOtp(String email);
    boolean validateOtp(String email, String otp);
}
