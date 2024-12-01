package com.seven.userse.service.impl;

import com.seven.userse.service.OtpService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements OtpService {

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    private final ConcurrentHashMap<String, String> otpCache = new ConcurrentHashMap<>();

    @Override
    public String generateOtp(String phoneNumber) {
        String otp = String.format("%04d", new Random().nextInt(10000));
        otpCache.put(phoneNumber, otp);
        sendOtp(phoneNumber, otp);
        return otp;
    }

    @Override
    public boolean validateOtp(String phoneNumber, String otp) {
        return otp.equals(otpCache.get(phoneNumber));
    }

    private void sendOtp(String phoneNumber, String otp) {
        Message.creator(
                new PhoneNumber("whatsapp:" + phoneNumber),
                new PhoneNumber("whatsapp:" + twilioPhoneNumber),
                "Your OTP is: " + otp
        ).create();
    }
}
