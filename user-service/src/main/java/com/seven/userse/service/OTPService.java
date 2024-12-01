package com.seven.userse.service;

import com.seven.userse.request.OtpRequest;
import com.seven.userse.request.OtpValidationRequest;

public interface OtpService {
    void generateAndSendOtp(OtpRequest otpRequest);
    boolean validateOtp(OtpValidationRequest otpValidationRequest);
}
