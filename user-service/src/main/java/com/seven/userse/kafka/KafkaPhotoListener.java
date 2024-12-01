package com.seven.userse.kafka;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaPhotoListener {

    private final AmazonRekognition rekognitionClient;

    public KafkaPhotoListener(AmazonRekognition rekognitionClient) {
        this.rekognitionClient = rekognitionClient;
    }

    @KafkaListener(topics = "photo-validation", groupId = "user-service")
    public void handlePhotoValidation(String photoUrl) {
        DetectFacesRequest request = new DetectFacesRequest()
                .withImage(new Image().withS3Object(new S3Object().withName(photoUrl)))
                .withAttributes(Attribute.ALL);

        DetectFacesResult result = rekognitionClient.detectFaces(request);
        if (result.getFaceDetails().isEmpty() || result.getFaceDetails().size() > 1) {
            throw new IllegalArgumentException("Photo validation failed for URL: " + photoUrl);
        }
        System.out.println("Photo validated successfully for URL: " + photoUrl);
    }
}
