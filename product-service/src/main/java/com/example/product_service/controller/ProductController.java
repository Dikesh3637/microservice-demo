package com.example.product_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.model.Product;
import com.example.product_service.service.ProductService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public List<Product> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products;
    }

    @GetMapping("/id/{id}")
    public Product getProductById(@PathVariable("id") String id) {
        return productService.getProductById(id);
    }

}
