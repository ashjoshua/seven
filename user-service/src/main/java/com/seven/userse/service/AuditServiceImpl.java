package com.seven.userse.service.impl;

import com.seven.userse.model.User;
import com.seven.userse.service.AuditService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImpl implements AuditService {

    @Override
    public void logUserRegistration(User user, String location) {
        System.out.println("Audit Log: User Registered - ID: " + user.getId() +
                ", Phone: " + user.getPhoneNumber() +
                ", Email: " + user.getEmail() +
                ", Location: " + location +
                ", Timestamp: " + LocalDateTime.now());
    }
    // feedback location os what lat n long or s2 cell
}
