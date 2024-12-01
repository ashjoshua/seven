package com.seven.userse.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.name:user-topic}")
    private String topicName;

    @Value("${kafka.topic.partitions:3}")
    private int partitions;

    @Value("${kafka.topic.replication-factor:1}")
    private short replicationFactor;

    @Bean
    public NewTopic userTopic() {
        return new NewTopic(topicName, partitions, replicationFactor);
    }
}
