package com.seven.userse.request;

public class UserPersonalDetailsRequest {

    private String phoneNumber;
    private String email;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    private String name;
    private Integer age;
    private String gender;
    private String orientation;
    private String pitch;
    private String height; // Can be in feet or cm (captured as String)

    public Integer getHeightInCm() {
        if (height.toLowerCase().contains("ft")) {
            String[] parts = height.split("ft");
            int feet = Integer.parseInt(parts[0].trim());
            int inches = parts.length > 1 ? Integer.parseInt(parts[1].replaceAll("\\D+", "").trim()) : 0;
            return (feet * 30) + (inches * 2);
        }
        return Integer.parseInt(height.replaceAll("\\D+", "").trim());
    }

    // Getters and setters
}
