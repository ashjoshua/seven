package com.seven.userse.service;

import com.seven.userse.request.*;

import com.seven.userse.request.UserPersonalDetailsRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {


    String validateOtpAndGenerateSession(OtpValidationRequest otpValidationRequest);

    void generateAndSendOtp(OtpRequest otpRequest);

    boolean validateSessionToken(String phone, String email, String sessionToken);

    void captureUserDetails(String sessionToken, UserPersonalDetailsRequest userDetailsRequest);

    void saveAndValidatePhotos(List<MultipartFile> photos);

    //void savePaymentDetails(PaymentRequest paymentRequest);
}
