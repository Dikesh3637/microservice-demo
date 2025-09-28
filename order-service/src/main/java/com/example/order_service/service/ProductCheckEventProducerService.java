package com.example.order_service.service;

import org.example.events.ProductCheckEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductCheckEventProducerService {

    final static Logger log = LoggerFactory.getLogger(ProductCheckEventProducerService.class);

    final KafkaTemplate<String, ProductCheckEvent> kafkaTemplate;

    public ProductCheckEventProducerService(KafkaTemplate<String, ProductCheckEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductCheckEvent(String productId, int quantity) {
        kafkaTemplate.send("product-check-events", new ProductCheckEvent(productId, quantity));
        log.debug("Sent ProductCheckEvent for productId={} quantity={}", productId, quantity);
    }

}
