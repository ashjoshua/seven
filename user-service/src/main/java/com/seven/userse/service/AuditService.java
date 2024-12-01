package com.seven.userse.service;

import com.seven.userse.model.User;

public interface AuditService {
    void logUserRegistration(User user, String location);
}
