package com.example.order_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.service.OrderService;
import com.example.order_service.service.ProductCheckEventProducerService;

@RestController
public class OrderController {

    private ProductCheckEventProducerService productCheckEventProducer;
    private OrderService orderService;

    public OrderController(ProductCheckEventProducerService productCheckEventProducer, OrderService orderService) {
        this.productCheckEventProducer = productCheckEventProducer;
        this.orderService = orderService;
    }

    @PostMapping("/")
    public void placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        productCheckEventProducer.sendProductCheckEvent(orderRequestDTO.getProductId(), orderRequestDTO.getQuantity());
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> DeleteOrder(@PathVariable("id") String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
