package com.example.product_service.service;

import org.springframework.kafka.annotation.KafkaListener;

import com.example.product_service.enums.StockCheckEventResponseType;
import com.example.product_service.event.ProductCheckEvent;
import com.example.product_service.event.PlaceOrderEvent;

public class ProductCheckEventConsumerService {

    private ProductService productService;

    private PlaceOrderEventProducerService productEventProducerService;

    public ProductCheckEventConsumerService(ProductService productService,
            PlaceOrderEventProducerService productEventProducerService) {
        this.productEventProducerService = productEventProducerService;
        this.productService = productService;
    }

    @KafkaListener(topics = "product-check-events")
    public void consumeProductCheckEvent(ProductCheckEvent productEvent) {
        Boolean canPlaceOrder = productService.canPlaceOrder(productEvent.getProductId(), productEvent.getQuantity());

        PlaceOrderEvent productCheckResponseEvent = new PlaceOrderEvent(productEvent.getProductId(),
                productEvent.getQuantity(),
                canPlaceOrder ? StockCheckEventResponseType.SUCCESS : StockCheckEventResponseType.INSUFFICIENT_STOCK);
        productEventProducerService.sendProductCheckEventResponse(productCheckResponseEvent);
    }
}
