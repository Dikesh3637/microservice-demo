package com.example.product_service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Boolean canPlaceOrder(String productId, int quantity) {
        UUID productUuid = UUID.fromString(productId);
        Optional<Product> productOptional = productRepository.findById(productUuid);
        if (productOptional.isEmpty()) {
            return false;
        }
        Product product = productOptional.get();
        if (product.getQuantity() >= quantity) {
            int currQuant = product.getQuantity();
            product.setQuantity(currQuant - quantity);
            productRepository.save(product);
            return true;
        }
        return false;
    }
}
