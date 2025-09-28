package com.example.product_service.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @NotBlank
    @Column(nullable = false)
    private String productName;

    @NotNull
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Column(nullable = false)
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Integer price;

    public Product() {
    }

    public Product(String productName, Integer quantity, Integer price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
