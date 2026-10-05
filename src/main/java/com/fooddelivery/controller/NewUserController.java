package com.fooddelivery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fooddelivery.dto.request.NewUserRequest;
import com.fooddelivery.service.NewUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
public class NewUserController {

  private NewUserService newUserService;

    @PostMapping ("/save")
    public ResponseEntity<String> saveNewUser(@RequestBody NewUserRequest newUserRequest) {
        return new ResponseEntity<>(newUserService.saveNewUser(newUserRequest), HttpStatus.OK);
    }
}
 
    
    

