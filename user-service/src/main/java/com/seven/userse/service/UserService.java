package com.seven.userse.service;

import com.seven.userse.model.User;
import com.seven.userse.request.*;

import java.util.Optional;

public interface UserService {

    // Stage 1: Validate OTP
    void validateOtp(String phoneNumber, String otp);
    void validateEmailOtp(String email, String otp);

    // Stage 2: Save Personal Details
    User savePersonalDetails(UserPersonalDetailsRequest request);

    // Stage 3: Save and Validate Photos
    User saveAndValidatePhotos(UserPhotoRequest request);

    // Stage 4: Save Payment Type
    User savePaymentType(UserPaymentRequest request);

    // Utility: Retrieve User by ID
    Optional<User> findUserById(Long userId);
}
