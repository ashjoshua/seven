package com.seven.userse.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PhoneLoginRequest {

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    private String phoneNumber;

    // Constructor
    public PhoneLoginRequest() {}

    public PhoneLoginRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
