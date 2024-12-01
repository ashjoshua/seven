package com.seven.userse.service.impl;

import com.seven.userse.model.User;
import com.seven.userse.repository.UserRepository;
import com.seven.userse.request.*;
import com.seven.userse.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final OtpService otpService;
    private final EmailOtpService emailOtpService;
    private final PhotoValidationService photoValidationService;
    private final AuditService auditService;

    public UserServiceImpl(UserRepository userRepository, RedisService redisService, OtpService otpService,
                           EmailOtpService emailOtpService, PhotoValidationService photoValidationService,
                           AuditService auditService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.otpService = otpService;
        this.emailOtpService = emailOtpService;
        this.photoValidationService = photoValidationService;
        this.auditService = auditService;
    }

    // Stage 1: Validate OTP
    @Override
    public void validateOtp(String phoneNumber, String otp) {
        if (!otpService.validateOtp(phoneNumber, otp)) {
            throw new IllegalArgumentException("Invalid phone OTP");
        }
    }

    @Override
    public void validateEmailOtp(String email, String otp) {
        if (!emailOtpService.validateOtp(email, otp)) {
            throw new IllegalArgumentException("Invalid email OTP");
        }
    }

    // Stage 2: Save Personal Details
    @Transactional
    @Override
    public User savePersonalDetails(UserPersonalDetailsRequest request) {
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setSex(request.getSex());
        user.setOrientation(request.getOrientation());
        user.setPitch(request.getPitch());
        user.setHeight(request.getHeight());

        User savedUser = userRepository.save(user);
        redisService.saveUserProfile(savedUser); // Cache user profile
        return savedUser;
    }

    // Stage 3: Save and Validate Photos
    @Transactional
    @Override
    public User saveAndValidatePhotos(UserPhotoRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        request.getPhotoUrls().forEach(photoValidationService::isValidPhoto);
        user.setPhotoUrls(request.getPhotoUrls());

        User updatedUser = userRepository.save(user);
        redisService.saveUserProfile(updatedUser); // Update cache
        return updatedUser;
    }

    // Stage 4: Save Payment Type
    @Transactional
    @Override
    public User savePaymentType(UserPaymentRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        user.setPaymentType(request.getPaymentType());

        User updatedUser = userRepository.save(user);
        redisService.saveUserProfile(updatedUser); // Update cache
        auditService.logUserRegistration(user, "Payment type updated"); // Log activity
        return updatedUser;
    }

    //feedback to have payment type enum

    // Utility: Retrieve User by ID
    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }
    // feedback  where is method to save .regsited user in redis , postgre db , audit db...
}
