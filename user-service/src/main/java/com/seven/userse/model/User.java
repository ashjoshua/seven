package com.seven.userservice.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String email;
    private String name;
    private Integer age;
    private String gender; // Added gender
    private String orientation;
    private String pitch;
    private Integer height; // Stored in cm

    @ElementCollection
    private List<String> photoUrls;

    private String paymentType;

    // Getters and setters
}
