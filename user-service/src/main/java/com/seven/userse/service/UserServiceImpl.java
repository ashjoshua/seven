package com.seven.userse.service;

import com.seven.userse.model.User;
import com.seven.userse.repository.UserRepository;
import com.seven.userse.request.*;
import com.seven.userse.service.*;
import com.seven.userse.request.UserPersonalDetailsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final AuditService auditService;
    private final OtpService otpService;
    private final LocationService locationService;

    public UserServiceImpl(UserRepository userRepository, RedisService redisService, AuditService auditService, OtpService otpService, LocationService locationService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.auditService = auditService;
        this.otpService = otpService;
        this.locationService = locationService;
    }


    @Override
    public void generateAndSendOtp(OtpRequest request) {
       otpService.generateAndSendOtp(request);


    }

    @Override
    public boolean validateOtp(OtpValidationRequest request) {
        return otpService.validateOtp(request.getPhone(), request.getPhoneOtp()) &&
                otpService.validateOtp(request.getEmail(), request.getEmailOtp());
    }

    @Transactional
    @Override
    public void saveUserDetails(UserPersonalDetailsRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setPreferences(request.getPreferences());
        userRepository.save(user);
        redisService.saveUserProfile(user);
        auditService.logUserRegistration(user.getId(), "User registered", "location-details");
    }

    @Override
    public void saveAndValidatePhotos(List<MultipartFile> photos) {
        // Validate and save photos
    }

    @Override
    public void savePaymentDetails(PaymentRequest request) {
        // Save payment details
    }
}
