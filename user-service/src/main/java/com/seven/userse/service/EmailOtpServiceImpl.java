package com.seven.userse.service.impl;

import com.seven.userse.service.EmailOtpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailOtpServiceImpl implements EmailOtpService {

    private final SesClient sesClient;

    @Value("${aws.ses.from-email}")
    private String fromEmail;

    private final ConcurrentHashMap<String, String> otpCache = new ConcurrentHashMap<>();

    public EmailOtpServiceImpl(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    @Override
    public String generateOtp(String email) {
        String otp = String.format("%04d", new Random().nextInt(10000));
        otpCache.put(email, otp);
        sendEmailOtp(email, otp);
        return otp;
        //feedbACK WHATS THE otp type number or alpha nume..also are you storing it in encrypted form in cache
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        return otp.equals(otpCache.get(email));
    }

    private void sendEmailOtp(String email, String otp) {
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(email).build())
                .message(Message.builder()
                        .subject(Content.builder().data("Your OTP").build())
                        .body(Body.builder().textPart(Content.builder()
                                .data("Your OTP is: " + otp)
                                .build()).build())
                        .build())
                .source(fromEmail)
                .build();

        sesClient.sendEmail(request);

        //feedback inluce subject like 7 registration otp.. and include text like otp ,how long its valid..lets keep it valid for 60 secs onlu
    }
}
