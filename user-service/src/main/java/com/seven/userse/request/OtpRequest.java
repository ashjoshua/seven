package com.seven.userse.request;

public class OtpRequest {

    private String phoneNumber;
    private String email;

    // Constructors
    public OtpRequest() {
    }

    public OtpRequest(String phoneNumber, String email) {
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
