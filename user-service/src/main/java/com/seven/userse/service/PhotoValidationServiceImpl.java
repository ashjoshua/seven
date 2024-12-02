package com.seven.userse.service.impl;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.seven.userse.service.PhotoValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoValidationServiceImpl implements PhotoValidationService {

    private final AmazonRekognition rekognitionClient;

    public PhotoValidationServiceImpl(AmazonRekognition rekognitionClient) {
        this.rekognitionClient = rekognitionClient;
    }

    @Override
    public boolean isValidPhoto(String photoUrl) {
        DetectFacesRequest request = new DetectFacesRequest()
                .withImage(new Image().withS3Object(new S3Object().withName(photoUrl)))
                .withAttributes(Attribute.ALL);

        DetectFacesResult result = rekognitionClient.detectFaces(request);
        List<FaceDetail> faces = result.getFaceDetails();

        if (faces.size() != 1) {
            throw new IllegalArgumentException("Photo validation failed: Exactly one human face is required.");
        }
        return true;
    }
}
