package com.fooddelivery.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.common.ApiResponse;
import com.fooddelivery.common.MessageConstants;
import com.fooddelivery.dto.request.LoginRequest;
import com.fooddelivery.dto.request.RegisterRequest;
import com.fooddelivery.dto.response.AuthResponse;
import com.fooddelivery.dto.response.UserResponse;
import com.fooddelivery.service.AuthService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody RegisterRequest request) {
        String result = authService.register(request);
        return new ApiResponse<>(
                true,
                200,
                MessageConstants.USER_REGISTERED_SUCCESSFULLY,
                result
        );
    }


    // @GetMapping("users")
    // public String getAllUsers() {
    //    return new ApiResponse<>(
    //             true,
    //             200,
    //             MessageConstants.USER_FIND_SUCESSFULLY,
    //             authService.getAllUsers()
    //     ).toString();
    // }
@GetMapping("/users")
public ApiResponse <List<UserResponse>> getAllUsers() {
    return new ApiResponse<>(
            true,
            200,
            MessageConstants.USER_FIND_SUCESSFULLY,
            authService.getAllUsers()
    );
}
    

    @PostMapping("/create-admin")
public ApiResponse<String> createAdmin(@RequestBody RegisterRequest request) {
    String result = authService.createAdmin(request);
    return new ApiResponse<>(
            true,
            200,
            MessageConstants.ADMIN_CREATED_SUCCESSFULLY,
            result
    );
}


// @PostMapping("/login")
//     public AuthResponse login(@RequestBody LoginRequest request) {
//         return authService.login(request);
//     }
@PostMapping("/login")
public ApiResponse<AuthResponse> login(@RequestBody LoginRequest request) {
    System.out.println("Login request received: " + request);

    AuthResponse response = authService.login(request);
     String message = MessageConstants.LOGIN_SUCCESSFUL;

    return new ApiResponse<>(
            true,
            200,
            message,
            response
    );
}

}