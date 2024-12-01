package com.seven.userse.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class UserRegistrationRequest {

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    private String phoneNumber;

    @Email
    private String email;

    @NotBlank
    private String paymentType;

    @NotBlank
    private List<String> photoUrls;

    @NotBlank
    private String location;

    @NotBlank
    private String otp;

    //feedback validation for phone should be 10 digit mobile number in india . Payment type should be gpay, razor pay enum, is location goin to contain s2 or lat long


    // Getters and Setters
}
