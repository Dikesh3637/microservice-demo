package com.example.order_service.service;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;

import com.example.order_service.enums.StockCheckEventResponseType;
import com.example.order_service.event.PlaceOrderEvent;
import com.example.order_service.model.Order;

public class PlaceOrderEventConsumerService {

    private OrderService orderService;
    
    public PlaceOrderEventConsumerService(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "place-order-events")
    public void consume(PlaceOrderEvent placeOrderEvent) {
        if(placeOrderEvent.getResponseType() == StockCheckEventResponseType.INSUFFICIENT_STOCK) {
            System.out.println("Insufficient stock, cannot place order");
        }
        
        Order order = new Order(UUID.fromString(placeOrderEvent.getProductId()), placeOrderEvent.getQuantity(), 0.0);
        orderService.placeOrder(order);
        System.out.println("Order placed successfully for product: " + placeOrderEvent.getProductId());
    }

}
