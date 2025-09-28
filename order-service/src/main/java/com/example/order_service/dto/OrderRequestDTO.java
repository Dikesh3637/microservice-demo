package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderRequestDTO {
    @NotBlank(message = "the product id can't be null")
    private String productId;

    @NotBlank(message = "the product quantity can't be null")
    @Min(value = 1, message = "the quantity should be above or equal to 1")
    private int quantity;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
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
}
