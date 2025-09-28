package com.example.product_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.product_service.enums.StockCheckEventResponseType;
import com.example.product_service.event.ProductCheckEvent;
import com.example.product_service.event.PlaceOrderEvent;

@Service
public class ProductCheckEventConsumerService {

    private ProductService productService;

    private PlaceOrderEventProducerService productEventProducerService;

    private final Logger logger = LoggerFactory.getLogger(ProductCheckEventConsumerService.class);

    public ProductCheckEventConsumerService(ProductService productService,
            PlaceOrderEventProducerService productEventProducerService) {
        this.productEventProducerService = productEventProducerService;
        this.productService = productService;
    }

    @KafkaListener(topics = "product-check-events", groupId = "product-check-consumer-group")
    public void consumeProductCheckEvent(ProductCheckEvent productEvent) {
        logger.debug("recieved product event" + productEvent);
        Boolean canPlaceOrder = productService.canPlaceOrder(productEvent.getProductId(), productEvent.getQuantity());

        PlaceOrderEvent productCheckResponseEvent = new PlaceOrderEvent(productEvent.getProductId(),
                productEvent.getQuantity(),
                canPlaceOrder ? StockCheckEventResponseType.SUCCESS : StockCheckEventResponseType.INSUFFICIENT_STOCK);
        productEventProducerService.sendProductCheckEventResponse(productCheckResponseEvent);
    }
}
