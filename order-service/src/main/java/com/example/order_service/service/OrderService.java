package com.example.order_service.service;

import org.springframework.stereotype.Service;

import com.example.order_service.model.Order;
import com.example.order_service.repository.OrderRepository;

@Service
public class OrderService {

    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void placeOrder(Order order) {
        orderRepository.save(order);
        System.out.println("Order placed successfully");
    }
}
