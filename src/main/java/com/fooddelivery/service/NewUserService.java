package com.fooddelivery.service;

import java.util.List;

import com.fooddelivery.dto.request.NewUserRequest;

public interface NewUserService {
    String saveNewUser(NewUserRequest newUserRequest);
    List<NewUserRequest> getAllNewUsers();
}
