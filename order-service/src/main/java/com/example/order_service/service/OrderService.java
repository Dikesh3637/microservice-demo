package com.example.order_service.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.order_service.exception.OrderNotFoundException;
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

    public void deleteOrder(String id) {
        Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(id));

        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }

        orderRepository.delete(orderOptional.get());
    }
}
