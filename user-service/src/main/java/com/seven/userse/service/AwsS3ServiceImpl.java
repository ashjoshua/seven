package com.seven.userse.service.impl;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.seven.userse.config.ServiceConfig;
import com.seven.userse.service.AwsS3Service;
import org.springframework.stereotype.Service;

@Service
public class AwsS3ServiceImpl implements AwsS3Service {

    private final AmazonRekognition rekognitionClient;
    private final ServiceConfig serviceConfig;

    public AwsS3ServiceImpl(AmazonRekognition rekognitionClient, ServiceConfig serviceConfig) {
        this.rekognitionClient = rekognitionClient;
        this.serviceConfig = serviceConfig;
    }

    @Override
    public void validatePhoto(String photoUrl) {
        String bucketName = serviceConfig.getS3BucketName();
        DetectFacesRequest request = new DetectFacesRequest()
                .withImage(new Image().withS3Object(new S3Object().withBucket(bucketName).withName(photoUrl)))
                .withAttributes(Attribute.ALL);

        DetectFacesResult result = rekognitionClient.detectFaces(request);
        if (result.getFaceDetails().isEmpty() || result.getFaceDetails().size() > 1) {
            throw new IllegalArgumentException("Photo validation failed for URL: " + photoUrl);
        }
        //feedback there should only be one face per ohoto
    }
}
