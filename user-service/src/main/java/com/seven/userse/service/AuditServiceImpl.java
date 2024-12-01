package com.seven.userse.service.impl;

import com.seven.userse.model.User;
import com.seven.userse.service.AuditService;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {

    @Override
    public void logUserRegistration(User user) {
        // Log the audit information for user registration
        System.out.println("Audit Log: User Registered - ID: " + user.getId() +
                ", Phone: " + user.getPhoneNumber() +
                ", Email: " + user.getEmail());
        // In a real implementation, this would save to an audit table
    }
}
