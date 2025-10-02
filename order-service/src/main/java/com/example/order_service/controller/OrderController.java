package com.example.order_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.service.ProductCheckEventProducerService;

@RestController
public class OrderController {

    private ProductCheckEventProducerService productCheckEventProducer;

    public OrderController(ProductCheckEventProducerService productCheckEventProducer) {
        this.productCheckEventProducer = productCheckEventProducer;
    }

    @PostMapping("/")
    public void placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        productCheckEventProducer.sendProductCheckEvent(orderRequestDTO.getProductId(), orderRequestDTO.getQuantity());
    }

}
