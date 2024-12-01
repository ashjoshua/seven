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
        List<FaceDetail> faceDetails = result.getFaceDetails();

        return faceDetails.size() == 1; // feedbac Exactly one human face is required
    }
}
