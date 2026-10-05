package com.fooddelivery.dto.request;

import lombok.Data;

@Data
public class NewUserRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String role;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private String createdBy;
    private String updatedBy;
    private String deletedBy;
}
