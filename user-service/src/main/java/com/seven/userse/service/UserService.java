package com.seven.userservice.service;

import com.seven.userservice.request.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    boolean isLocationValid(String location);

    void captureContactInfo(ContactInfoRequest contactInfoRequest);

    void generateAndSendOtp(ContactInfoRequest contactInfoRequest);

    boolean validateOtp(OtpValidationRequest otpValidationRequest);

    void saveUserDetails(UserDetailsRequest userDetailsRequest);

    void saveAndValidatePhotos(List<MultipartFile> photos);

    void savePaymentDetails(PaymentRequest paymentRequest);
}
