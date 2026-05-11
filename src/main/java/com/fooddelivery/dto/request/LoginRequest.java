package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String identifier; // email or phone
    private String password;

    // getters & setters
}