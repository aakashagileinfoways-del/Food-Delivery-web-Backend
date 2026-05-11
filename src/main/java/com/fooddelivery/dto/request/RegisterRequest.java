package com.fooddelivery.dto.request;

import lombok.Data;
// import lombok.Getter;
// import lombok.Setter;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Long roleId;
}