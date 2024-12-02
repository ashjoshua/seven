package com.seven.userse.service;

import com.seven.userse.request.*;

import com.seven.userse.request.UserPersonalDetailsRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {




    void generateAndSendOtp(OtpRequest otpRequest);

    boolean validateOtp(OtpValidationRequest otpValidationRequest);

    void saveUserDetails(UserPersonalDetailsRequest userDetailsRequest);

    void saveAndValidatePhotos(List<MultipartFile> photos);

    //void savePaymentDetails(PaymentRequest paymentRequest);
}
