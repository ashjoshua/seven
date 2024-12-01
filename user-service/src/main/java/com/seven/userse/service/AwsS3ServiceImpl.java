package com.seven.userse.service.impl;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.seven.userse.service.AwsS3Service;
import org.springframework.stereotype.Service;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {

    private final AmazonRekognition rekognitionClient;

    public AwsS3ServiceImpl(AmazonRekognition rekognitionClient) {
        this.rekognitionClient = rekognitionClient;
    }

    @Override
    public void validatePhoto(String photoUrl) {
        DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
                .withImage(new Image().withS3Object(new S3Object().withName(photoUrl)))
                .withMinConfidence(70F);

        DetectModerationLabelsResult result = rekognitionClient.detectModerationLabels(request);

        if (!result.getModerationLabels().isEmpty()) {
            throw new IllegalArgumentException("Photo validation failed. Inappropriate content detected.");
        }
    }
}
