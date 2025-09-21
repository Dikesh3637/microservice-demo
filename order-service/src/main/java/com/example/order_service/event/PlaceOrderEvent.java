package com.example.order_service.event;

import com.example.order_service.enums.StockCheckEventResponseType;

public class PlaceOrderEvent {

    private String productId;
    private int quantity;
    private StockCheckEventResponseType responseType;

    public PlaceOrderEvent(String productId, int quantity, StockCheckEventResponseType responseType) {
        this.productId = productId;
        this.quantity = quantity;
        this.responseType = responseType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StockCheckEventResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(StockCheckEventResponseType responseType) {
        this.responseType = responseType;
    }
     

    
}
