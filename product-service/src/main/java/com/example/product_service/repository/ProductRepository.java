package com.example.product_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.product_service.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

}
