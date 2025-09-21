package com.example.order_service.service;

import org.springframework.kafka.core.KafkaTemplate;

import com.example.order_service.event.ProductCheckEvent;

public class ProductCheckEventProducer {
    final KafkaTemplate<String, ProductCheckEvent> kafkaTemplate;
    
    public ProductCheckEventProducer(KafkaTemplate<String, ProductCheckEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductCheckEvent(String productId, int quantity) {
        kafkaTemplate.send("product-check-event", new ProductCheckEvent(productId, quantity));
    }

}
