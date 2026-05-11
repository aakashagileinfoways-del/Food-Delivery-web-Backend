package com.fooddelivery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private String role;
    private String name;
    private String email;
    private String phone;
    
}