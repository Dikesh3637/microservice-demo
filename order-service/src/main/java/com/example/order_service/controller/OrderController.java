package com.example.order_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.OrderRequestDTO;
import com.example.order_service.service.ProductCheckEventProducerService;

@RestController
public class OrderController {

    private ProductCheckEventProducerService productCheckEventProducer;

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(ProductCheckEventProducerService productCheckEventProducer) {
        this.productCheckEventProducer = productCheckEventProducer;
    }

    @PostMapping("/order")
    public void placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        productCheckEventProducer.sendProductCheckEvent(orderRequestDTO.getProductId(), orderRequestDTO.getQuantity());
    }

}
