package com.seven.userservice.listener;

import com.seven.userse.service.PhotoValidationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaPhotoListener {

    private final PhotoValidationService photoValidationService;

    public KafkaPhotoListener(PhotoValidationService photoValidationService) {
        this.photoValidationService = photoValidationService;
    }

    @KafkaListener(topics = "photo-validation-topic", groupId = "photo-validation-group")
    public void validatePhoto(String photoUrl) {
        if (!photoValidationService.isValidPhoto(photoUrl)) {
            throw new IllegalArgumentException("Photo validation failed: Exactly one face is required.");
        }
    }
}
