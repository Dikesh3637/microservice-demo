package com.example.product_service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;

public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public  List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Boolean canPlaceOrder(String productId, int quantity) {
        UUID productUuid = UUID.fromString(productId);
        Optional<Product> product = productRepository.findById(productUuid);
        if (product.isEmpty()){
            return false;
        }
        if (product.get().getQuantity() >= quantity) {
            return true;
        }
        return false;
    }
}
