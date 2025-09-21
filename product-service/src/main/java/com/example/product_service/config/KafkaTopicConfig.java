package com.example.product_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    
    @Bean
    public NewTopic productCheckEventsTopic() {
        return  TopicBuilder.name("product-check-events")
            .partitions(1)
            .replicas(1)
            .build();
        }
    
    @Bean
    public NewTopic placeOrderEventsTopic() {
        return  TopicBuilder.name("place-order-events")
            .partitions(1)
            .replicas(1)
            .build();
        }
}   
