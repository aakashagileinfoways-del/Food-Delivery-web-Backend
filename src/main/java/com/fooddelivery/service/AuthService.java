package com.fooddelivery.service;

import java.util.List;

import com.fooddelivery.dto.request.LoginRequest;
import com.fooddelivery.dto.request.RegisterRequest;
import com.fooddelivery.dto.response.AuthResponse;
import com.fooddelivery.dto.response.UserResponse;

public interface AuthService {

    String register(RegisterRequest request);

    String createAdmin(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    List<UserResponse> getAllUsers();
}