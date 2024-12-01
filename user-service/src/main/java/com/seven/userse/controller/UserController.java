package com.seven.userservice.controller;

import com.seven.userservice.request.*;
import com.seven.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Page 1: OTP Generation
    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestBody OtpRequest request) {
        userService.generateOtp(request);
        return ResponseEntity.ok("OTP sent successfully.");
    }

    // Page 2: Validate Personal Details
    @PostMapping("/validate-details")
    public ResponseEntity<String> validateDetails(@RequestBody UserPersonalDetailsRequest request) {
        userService.savePersonalDetails(request);
        return ResponseEntity.ok("Details validated and saved successfully.");
    }

    // Page 3: Validate Photos
    @PostMapping("/validate-photos")
    public ResponseEntity<String> validatePhotos(@RequestBody UserPhotoRequest request) {
        userService.saveAndValidatePhotos(request);
        return ResponseEntity.ok("Photos validated and saved successfully.");
    }

    // Page 4: Handle Payment
    @PostMapping("/payment")
    public ResponseEntity<String> handlePayment(@RequestBody UserPaymentRequest request) {
        userService.savePaymentType(request);
        return ResponseEntity.ok("Payment type saved successfully.");
    }
}
