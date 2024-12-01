package com.seven.userse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket-name}")
    private String s3BucketName;

    @Value("${photo.max-size}")
    private String photoMaxSize;

    public String getAwsRegion() {
        return awsRegion;
    }

    public String getS3BucketName() {
        return s3BucketName;
    }

    public String getPhotoMaxSize() {
        return photoMaxSize;
    }
}
