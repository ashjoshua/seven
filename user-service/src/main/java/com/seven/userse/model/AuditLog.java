package com.seven.userse.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String userId;
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(String action, String userId, LocalDateTime timestamp) {
        this.action = action;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    // Getters and Setters
}
