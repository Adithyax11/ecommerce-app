package com.adithya.demo.dto;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data @AllArgsConstructor
public class AuthResponse {
    private String token;
}