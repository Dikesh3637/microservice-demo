package com.example.order_service.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.order_service.event.ProductCheckEvent;

@Service
public class ProductCheckEventProducerService {
    final KafkaTemplate<String, ProductCheckEvent> kafkaTemplate;

    public ProductCheckEventProducerService(KafkaTemplate<String, ProductCheckEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductCheckEvent(String productId, int quantity) {
        kafkaTemplate.send("product-check-events", new ProductCheckEvent(productId, quantity));
    }

}
