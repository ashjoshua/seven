package com.seven.userse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seven.userse.model.User;
import com.seven.userse.repository.UserRepository;
import com.seven.userse.request.UserPersonalDetailsRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserKafkaConsumer {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public UserKafkaConsumer(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Value("${kafka.topic.name:user-topic}")
    @KafkaListener(topics = "${kafka.topic.name:user-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {
            // Deserialize the message
            UserPersonalDetailsRequest userDetails = objectMapper.readValue(message, UserPersonalDetailsRequest.class);

            // Map to User entity
            User user = new User();
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setEmail(userDetails.getEmail());
            user.setName(userDetails.getName());
            user.setAge(userDetails.getAge());
            user.setGender(userDetails.getGender());
            user.setOrientation(userDetails.getOrientation());
            user.setPitch(userDetails.getPitch());
            user.setHeight(userDetails.getHeightInCm());
            user.setSubscriptionType("free"); // Set subscription type

            // Save to PostgreSQL
            userRepository.save(user);

            System.out.println("User details persisted for: " + user.getPhoneNumber());
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
        }
    }
}
