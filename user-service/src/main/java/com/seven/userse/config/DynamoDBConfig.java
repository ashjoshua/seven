package com.seven.userse.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "prod"})
public class DynamoDBConfig {
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion("ap-south-1")
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "https://dynamodb.ap-south-1.amazonaws.com", "ap-south-1"))
                .build();
    }

    @Bean
    public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDB(amazonDynamoDB);
    }
}
