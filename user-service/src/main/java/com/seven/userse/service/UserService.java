package com.seven.userse.service;

import com.seven.userse.model.User;
import com.seven.userse.request.UserRegistrationRequest;

import java.util.Optional;

// Interface for user operations
public interface UserService {
    User registerUser(UserRegistrationRequest request);

    Optional<User> findUserById(Long userId);

    void validateOtp(String phoneNumber, String otp);

    void validatePhotos(List<String> photoUrls);

    void saveUserToCache(User user);
}
