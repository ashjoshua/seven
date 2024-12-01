package com.seven.userservice.controller;

import com.seven.userservice.request.*;
import com.seven.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/validate-location")
    public ResponseEntity<?> validateLocation(@RequestBody LocationRequest locationRequest) {
        boolean isValid = userService.isLocationValid(locationRequest.getLocation());
        return isValid ? ResponseEntity.ok("Location is valid.") : ResponseEntity.badRequest().body("Location not supported.");
    }

    @PostMapping("/capture-contact-info")
    public ResponseEntity<?> captureContactInfo(@RequestBody ContactInfoRequest contactInfoRequest) {
        userService.captureContactInfo(contactInfoRequest);
        return ResponseEntity.ok("Contact information captured.");
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody ContactInfoRequest contactInfoRequest) {
        userService.generateAndSendOtp(contactInfoRequest);
        return ResponseEntity.ok("OTP sent to phone and email.");
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        boolean isValid = userService.validateOtp(otpValidationRequest);
        return isValid ? ResponseEntity.ok("OTP validated successfully.") : ResponseEntity.badRequest().body("Invalid OTP.");
    }

    @PostMapping("/capture-user-details")
    public ResponseEntity<?> captureUserDetails(@RequestBody UserDetailsRequest userDetailsRequest) {
        userService.saveUserDetails(userDetailsRequest);
        return ResponseEntity.ok("User details captured.");
    }

    @PostMapping("/upload-photos")
    public ResponseEntity<?> uploadPhotos(@RequestParam("photos") List<MultipartFile> photos) {
        userService.saveAndValidatePhotos(photos);
        return ResponseEntity.ok("Photos uploaded successfully.");
    }

    @PostMapping("/capture-payment")
    public ResponseEntity<?> capturePayment(@RequestBody PaymentRequest paymentRequest) {
        userService.savePaymentDetails(paymentRequest);
        return ResponseEntity.ok("Payment details captured.");
    }
}
