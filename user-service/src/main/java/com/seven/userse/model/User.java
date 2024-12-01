package com.seven.userse.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    private String name;
    private String sex;
    private String orientation;
    private Integer age;
    private Float height;
    private String pitch;

    private Boolean isPremium;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
