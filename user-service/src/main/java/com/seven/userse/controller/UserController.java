package com.seven.userse.controller;

import com.seven.userse.request.*;
import com.seven.userse.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            userService.generateAndSendOtp(otpRequest);
            return ResponseEntity.ok("OTP sent to phone and email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP: " + e.getMessage());
        }
    }


    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestBody OtpValidationRequest otpValidationRequest) {
        String sessionToken = userService.validateOtpAndGenerateSession(otpValidationRequest);
        if (sessionToken == null) {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }

        return ResponseEntity.ok().body(Map.of("sessionToken", sessionToken));


    }

    @PostMapping("/capture-user-details")
    public ResponseEntity<?> captureUserDetails(
            @RequestHeader("Session-Token") String sessionToken,
            @RequestBody UserPersonalDetailsRequest userDetailsRequest) {
        try {
            // Delegate session validation and Kafka publishing to the service
            userService.captureUserDetails(sessionToken, userDetailsRequest);
            return ResponseEntity.ok("User details captured and sent for processing.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to capture user details.");
        }
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
