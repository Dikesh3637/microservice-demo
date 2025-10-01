package com.example.order_service.service;

import java.util.UUID;

import org.example.enums.StockCheckEventResponseType;
import org.example.events.PlaceOrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.order_service.model.Order;

@Service
public class PlaceOrderEventConsumerService {

    private OrderService orderService;

    public PlaceOrderEventConsumerService(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "place-order-events", groupId = "place-order-consumer-group")
    public void consume(PlaceOrderEvent placeOrderEvent) {
        if (placeOrderEvent.getResponseType() == StockCheckEventResponseType.PRODUCT_NOT_FOUND) {
            System.out.println("product not found");
            return;
        }
        if (placeOrderEvent.getResponseType() == StockCheckEventResponseType.INSUFFICIENT_STOCK) {
            System.out.println("Insufficient stock, cannot place order");
            return;
        }

        Order order = new Order(UUID.fromString(placeOrderEvent.getProductId()), placeOrderEvent.getQuantity(), 1.0);
        orderService.placeOrder(order);
        System.out.println("Order placed successfully for product: " + placeOrderEvent.getProductId());
    }

}
