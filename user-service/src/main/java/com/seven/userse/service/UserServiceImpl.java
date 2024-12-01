package com.seven.userse.service.impl;

import com.seven.userse.model.User;
import com.seven.userse.repository.UserRepository;
import com.seven.userse.request.UserRegistrationRequest;
import com.seven.userse.service.AwsS3Service;
import com.seven.userse.service.OtpService;
import com.seven.userse.service.RedisService;
import com.seven.userse.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final OtpService otpService;
    private final AwsS3Service awsS3Service;

    public UserServiceImpl(UserRepository userRepository, RedisService redisService,
                           OtpService otpService, AwsS3Service awsS3Service) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.otpService = otpService;
        this.awsS3Service = awsS3Service;
    }

    @Override
    public User registerUser(UserRegistrationRequest request) {
        // Validate OTP
        validateOtp(request.getPhoneNumber(), request.getOtp());

        // Validate photo count
        validatePhotos(request.getPhotoUrls());

        // Create and save user
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPaymentType(request.getPaymentType());
        user.setPhotoUrls(request.getPhotoUrls());

        // Save user to cache and DB
        saveUserToCache(user);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void validateOtp(String phoneNumber, String otp) {
        if (!otpService.isValid(phoneNumber, otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }
    }

    @Override
    public void validatePhotos(List<String> photoUrls) {
        if (photoUrls.size() != 7) {
            throw new IllegalArgumentException("Exactly 7 photos are required");
        }
        photoUrls.forEach(awsS3Service::validatePhoto);
    }

    @Override
    public void saveUserToCache(User user) {
        redisService.saveUser(user);
    }
}
