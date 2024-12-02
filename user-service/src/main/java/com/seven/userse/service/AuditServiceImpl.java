package com.seven.userse.service.impl;

import com.seven.userse.model.AuditLog;
import com.seven.userse.repository.AuditLogRepository;
import com.seven.userse.service.AuditService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void logUserRegistration(Long userId, String action, String location) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);

        // Log location as S2 cell or lat-long
        if (location.matches("\\d+")) {
            log.setLocation("S2 Cell ID: " + location);
        } else {
            log.setLocation("Coordinates: " + location);
        }

        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
