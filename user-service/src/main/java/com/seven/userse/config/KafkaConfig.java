package com.seven.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    private static final int PARTITIONS = 3; // Number of partitions for the topic

    @Bean
    public NewTopic userTopic() {
        return new NewTopic("user-topic", PARTITIONS, (short) 1);
    }
}
