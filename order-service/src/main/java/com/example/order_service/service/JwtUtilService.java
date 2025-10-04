package com.example.order_service.service;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jwtUtilsService
 */
@Component
public class JwtUtilService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public Map<String, Object> extractPayload(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = parts[1];

            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String decodedPayload = new String(decodedBytes);

            return objectMapper.readValue(decodedPayload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode JWT", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesClaim(String token) {
        Map<String, Object> payload = extractPayload(token);
        return (List<String>) payload.get("roles");
    }
}
