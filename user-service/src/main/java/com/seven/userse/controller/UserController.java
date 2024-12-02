package com.seven.userse.controller;

import com.seven.userse.request.*;
import com.seven.userse.service.UserService;
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



    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequest otpRequest) {
        userService.generateAndSendOtp(otpRequest);
        return ResponseEntity.ok("OTP sent to phone and email.");
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        boolean isValid = userService.validateOtp(otpValidationRequest);
        return isValid ? ResponseEntity.ok("OTP validated successfully.") : ResponseEntity.badRequest().body("Invalid OTP.");
    }

    @PostMapping("/capture-user-details")
    public ResponseEntity<?> captureUserDetails(@RequestBody com.seven.userse.request.UserPersonalDetailsRequest userPersonalDetailsRequest) {
        userService.saveUserDetails(userPersonalDetailsRequest);
        return ResponseEntity.ok("User details captured.");
    }

    @PostMapping("/upload-photos")
    public ResponseEntity<?> uploadPhotos(@RequestParam("photos") List<MultipartFile> photos) {
        userService.saveAndValidatePhotos(photos);
        return ResponseEntity.ok("Photos uploaded successfully.");
    }

   /* @PostMapping("/capture-payment")
    public ResponseEntity<?> capturePayment(@RequestBody PaymentRequest paymentRequest) {
        userService.savePaymentDetails(paymentRequest);
        return ResponseEntity.ok("Payment details captured.");
    }
    */

}
