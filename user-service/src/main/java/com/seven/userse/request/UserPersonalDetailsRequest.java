package com.seven.userservice.request;

public class UserPersonalDetailsRequest {

    private String phoneNumber;
    private String email;
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
