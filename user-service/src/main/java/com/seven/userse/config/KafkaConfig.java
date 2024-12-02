package com.seven.userse.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.name:user-topic}")
    private String userTopicName;

    @Value("${kafka.topic.partitions:3}")
    private int userPartitions;

    @Value("${kafka.topic.replication-factor:1}")
    private short userReplicationFactor;

    @Value("${kafka.topic.otp.name:otp-topic}")
    private String otpTopicName;

    @Value("${kafka.topic.otp.partitions:3}")
    private int otpPartitions;

    @Value("${kafka.topic.otp.replication-factor:1}")
    private short otpReplicationFactor;

    @Bean
    public NewTopic userTopic() {
        return new NewTopic(userTopicName, userPartitions, userReplicationFactor);
    }

    @Bean
    public NewTopic otpTopic() {
        return new NewTopic(otpTopicName, otpPartitions, otpReplicationFactor);
    }
}
