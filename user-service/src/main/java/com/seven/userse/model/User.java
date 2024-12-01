package com.seven.userse.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String email;
    private String paymentType;

    @ElementCollection
    private List<String> photoUrls;

    // Getters and Setters
    //feedback what about the other attributed like gender height text..is it needed here
}
