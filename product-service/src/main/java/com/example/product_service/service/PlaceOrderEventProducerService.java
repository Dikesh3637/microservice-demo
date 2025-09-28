package com.example.product_service.service;

import org.example.events.PlaceOrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlaceOrderEventProducerService {

    private final KafkaTemplate<String, PlaceOrderEvent> kafkaTemplate;

    public PlaceOrderEventProducerService(KafkaTemplate<String, PlaceOrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductCheckEventResponse(PlaceOrderEvent productEvent) {
        kafkaTemplate.send("place-order-events", productEvent);
    }
}
