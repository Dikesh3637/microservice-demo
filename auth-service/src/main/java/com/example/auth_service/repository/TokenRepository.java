package com.example.auth_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth_service.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

}
