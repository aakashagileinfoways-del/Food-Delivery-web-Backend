package com.fooddelivery.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.beans.BeanUtils;

import com.fooddelivery.dto.request.NewUserRequest;
import com.fooddelivery.repository.NewUserRepository;
import com.fooddelivery.service.NewUserService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class NewUserServiceImpl implements NewUserService {
    private final NewUserRepository newUserRepository;
    
    @Override
    public String saveNewUser(NewUserRequest newUserRequest) {
        return "New user saved successfully";
    }
    @Override
    public List<NewUserRequest> getAllNewUsers() {
        return newUserRepository.findAll().stream()
                .map(user -> {
                    NewUserRequest request = new NewUserRequest();
                    BeanUtils.copyProperties(user, request);
                    return request;
                })
                .toList();
    }
}
